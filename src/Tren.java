
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

class Tren {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private int espaciosRestantes;
    private Semaphore lugares, semBajar = new Semaphore(0);
    private int pasajerosA = 0, pasajerosB = 0, pasajerosC = 0;
    private Lock lockTren;
    private boolean viajeEnCurso = false;
    private boolean puedeBajar = false;
    private Condition esperaTren, esperaInicio, terminalA, terminalB, terminalC, bajandoPasajeros;

    public Tren(int capTren) {
        this.lockTren = new ReentrantLock();

        //Condición para que los pasajeros que se quieren subir esperen
        this.esperaTren = lockTren.newCondition();
        this.bajandoPasajeros = lockTren.newCondition();

        //Condición para que el tren sepa cuando arrancar
        this.esperaInicio = lockTren.newCondition();

        //Condiciones para avisar a cada pasajero cuando bajar
        this.terminalA = lockTren.newCondition();
        this.terminalB = lockTren.newCondition();
        this.terminalC = lockTren.newCondition();

        //Control de lugares
        this.lugares = new Semaphore(capTren);
        this.espaciosRestantes = capTren;

    }

    /*   =======================METODOS PARA PASAJERO =======================  */
    public void subirTren(char terminal) throws InterruptedException {

        lockTren.lock();

        try {

            System.out.println("[TREN]: " + Thread.currentThread().getName() + " esperando para subir al tren");

            //Mientras haya viaje en curso espera
            while (viajeEnCurso) {
                try {
                    esperaTren.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Tren.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            System.out.println(ANSI_GREEN + "[PASAJERO]: " + Thread.currentThread().getName() + " se subió al tren" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "[TREN]: ESPACIOS RESTANTES: " + this.espaciosRestantes + ANSI_RESET);

            //Si puede entrar, resto un espacio
            this.espaciosRestantes--;
            //Se cuenta un pasajero que se baja en la terminal que llega como parámetro
            this.contarPasajero(terminal);

            //Aviso al control de tren que puede iniciar el viaje
            if (this.espaciosRestantes == 0) {
                viajeEnCurso = true;
                this.esperaInicio.signalAll();
            }

        } finally {
            lockTren.unlock();
        }

    }

    public void bajarTren(char terminal) throws InterruptedException {

        lockTren.lock();

        System.out.println("[TREN]: " + Thread.currentThread().getName() + " esperando para bajar");

        while (!puedeBajar) {

            //Espera que se libere según donde se tiene que bajar
            switch (terminal) {
                case 'A':
                    this.terminalA.await();
                    break;
                case 'B':
                    this.terminalB.await();
                    break;
                case 'C':
                    this.terminalC.await();
                    break;
            }

        }

        //Libera un espacio
        System.out.println("[TREN]: " + Thread.currentThread().getName() + " se bajó del tren en la terminal " + terminal);
        this.espaciosRestantes++;
        this.restarPasajero(terminal);

        //Si es el último pasajero avisa al tren que puede reanudar
        if (obtenerPasajeros(terminal) == 0) {

            this.bajandoPasajeros.signalAll();

        }

        lockTren.unlock();

    }

    /*   =======================METODOS DEL CONTROL TREN =======================  */
    public void iniciarViaje() {

        try {
            lockTren.lock();

            //Si quedan lugares espera para arrancar
            this.esperaInicio.await(10, TimeUnit.SECONDS);

            //Si se llenó arranca el tren
            System.out.println(ANSI_BLUE + "==================== TREN INICIA VIAJE ====================" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "[TREN]: PASAJEROS ESTACIÓN A: " + this.pasajerosA + ANSI_RESET);
            System.out.println(ANSI_BLUE + "[TREN]: PASAJEROS ESTACIÓN B: " + this.pasajerosB + ANSI_RESET);
            System.out.println(ANSI_BLUE + "[TREN]: PASAJEROS ESTACIÓN C: " + this.pasajerosC + ANSI_RESET);
            System.out.println(ANSI_BLUE + "============================================================" + ANSI_RESET);

        } catch (InterruptedException ex) {
            Logger.getLogger(Tren.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lockTren.unlock();
        }
    }

    public void pararEstacion(char terminal) throws InterruptedException {

        lockTren.lock();

        puedeBajar = true;

        System.out.println(ANSI_BLUE + "[TREN]: Llegó a la terminal: " + terminal + ANSI_RESET);

        switch (terminal) {
            case 'A':
                this.terminalA.signalAll();
                break;
            case 'B':
                this.terminalB.signalAll();
                break;
            case 'C':
                this.terminalC.signalAll();
                break;
        }

        lockTren.unlock();

    }

    public void esperarParaSeguir(char terminal) {

        lockTren.lock();

        //Se fija cuantos pasajeros se bajan en la terminal
        int cantPasajerosAbajar = obtenerPasajeros(terminal);

        //Mientras que se estén bajando pasajeros se espera
        while (cantPasajerosAbajar > 0) {

            try {
                this.bajandoPasajeros.await();
                cantPasajerosAbajar = obtenerPasajeros(terminal);

            } catch (InterruptedException ex) {
                Logger.getLogger(Tren.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        System.out.println(ANSI_BLUE + "[TREN]: Reanuda viaje hacia la siguiente estación" + ANSI_RESET);
        puedeBajar = false;

        lockTren.unlock();

    }

    public void irAInicio() {

        lockTren.lock();

        System.out.println(ANSI_BLUE + "[TREN]: llegó a la estación principal, ya se pueden subir los pasajeros" + ANSI_RESET);

        this.viajeEnCurso = false;
        this.esperaTren.signalAll();

        lockTren.unlock();

    }

    /*   =======================METODOS PARA CONTAR PASAJEROS =======================  */
    public void contarPasajero(char terminal) {

        switch (terminal) {
            case 'A':
                this.pasajerosA++;
                break;
            case 'B':
                this.pasajerosB++;
                break;
            case 'C':
                this.pasajerosC++;
                break;
        }

    }

    public void restarPasajero(char terminal) {

        switch (terminal) {
            case 'A':
                this.pasajerosA--;
                break;
            case 'B':
                this.pasajerosB--;
                break;
            case 'C':
                this.pasajerosC--;
                break;
        }

    }

    public int obtenerPasajeros(char terminal) {

        int cant = 0;

        switch (terminal) {
            case 'A':
                cant = this.pasajerosA;
                break;
            case 'B':
                cant = this.pasajerosB;
                break;
            case 'C':
                cant = this.pasajerosC;
                break;
        }

        return cant;

    }

}
