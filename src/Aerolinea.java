
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

class Aerolinea {

    public static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private final String nombre;
    private Semaphore puestoAtencion, ingresoPuesto, atencion, salida, atenderTurno;
    private Lock guardia, lockTurno, asignacion;
    private Condition esperaTurno;
    private int turnoPasajero;
    private int turnoAtencion;

    public Aerolinea(String nombre, int capacidad) {

        this.nombre = nombre;

        //Semáforo para ingreso hasta capacidad máxima y para recibir atención
        this.ingresoPuesto = new Semaphore(capacidad);
        this.atencion = new Semaphore(0);
        this.salida = new Semaphore(0);
        this.atenderTurno = new Semaphore(1);

        //Locks para variables compartidas y condiciones
        this.guardia = new ReentrantLock();
        this.lockTurno = new ReentrantLock();
        this.esperaTurno = this.guardia.newCondition();

        //Turno que se le asigna al pasajero y turno que se está atendiendo
        this.turnoPasajero = 1;
        this.turnoAtencion = 0;

    }

    public String getNombre() {
        return this.nombre;
    }

    public void obtenerTurno(Pasajero pasajero) {

        lockTurno.lock();

        pasajero.setTurno(this.turnoPasajero);
        System.out.println("[PASAJERO]: " + Thread.currentThread().getName() + " ingresó al puesto de atención, se le asignó el turno " + this.turnoPasajero + " en la aerolínea " + this.nombre);
        this.turnoPasajero++;

        lockTurno.unlock();

    }

    public void ingresarPuesto() {

        System.out.println("[PASAJERO]: " + Thread.currentThread().getName() + " está en el hall central, quiere ingresar al puesto de atención");

        try {
            this.ingresoPuesto.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void recibirAtencion(int turnoAtencionPasajero) {

        //Se obtiene el lock
        guardia.lock();
        try {

            while (turnoAtencionPasajero != this.turnoAtencion) {

                try {
                    this.esperaTurno.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            //Libera al hilo de atención
            atencion.release();

            System.out.println(ANSI_GREEN + "[PUESTO DE ATENCIÓN - " + this.nombre + "]: " + Thread.currentThread().getName() + " está haciendo el check-in" + ANSI_RESET);

        } finally {
            guardia.unlock();
        }

    }

    public void salirPuestoAtencion() {

        try {
            //El pasajero intenta salir
            this.salida.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(ANSI_GREEN + "[PUESTO DE ATENCIÓN - " + this.nombre + "]: " + Thread.currentThread().getName() + " terminó su atención en el puesto" + ANSI_RESET);

        //Se le indica al hilo de atención que ya terminó y puede atender otro pasajero
        this.atenderTurno.release();

        // libera un lugar para que entre otro pasajero
        this.ingresoPuesto.release();

    }

    public void esperarPasajero() {
        try {
            this.atenderTurno.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void llamarTurno() {

        this.guardia.lock();

        this.turnoAtencion++;
        System.out.println("[PUESTO DE ATENCIÓN - " + this.nombre + "]: espera al pasajero con turno: " + this.turnoAtencion);

        //Libera a los que estén esperando el turno (solo pasara el que tenga el turno correspondiente)
        this.esperaTurno.signalAll();

        this.guardia.unlock();

    }

    public void atenderTurno() {

        try {
            this.atencion.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void terminarAtencion() {

        //Se permite la salida al pasajero
        this.salida.release();

    }

}
