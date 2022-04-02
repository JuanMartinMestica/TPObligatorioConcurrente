
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aeropuerto {

    Map<Character, Terminal> terminales = new HashMap<>();
    private Semaphore puestosDeInforme, atencionAeropuerto;
    private ControlVuelos control;
    private int horaActual;
    private Aerolinea[] aerolineas;
    private String[] nombres = {"Aerolineas Argentinas", "Latam Airways", "Copa Airlines", "Fly Emirates", "Alitalia", "Lufthansa", "FlyBondi", "JetSmart", "AeroMexico", "American Airlines"};
    private Tren tren;

    //Constructor
    public Aeropuerto(int cantAerolineas, int cantPuestosInforme, int capacidadFreeshop, int cantCajasFreeshop, int capacidadTren, int horaInicio, int cantPuestosAtencion) {

        //Se crean las aerolineas cada una con su puesto de atención
        this.aerolineas = new Aerolinea[cantAerolineas];

        for (int i = 0; i < cantAerolineas; i++) {
            aerolineas[i] = new Aerolinea(nombres[i], cantPuestosAtencion);
        }

        //Generador de vuelos y asignador de vuelos a pasajeros
        this.control = new ControlVuelos(this.horaActual, cantAerolineas, aerolineas);

        //Se generan y muestran los vuelos
        this.control.generarVuelosDelDia();
        this.control.mostrarVuelosGenerados();

        //Se crea el tren y el controlador del tren
        this.tren = new Tren(capacidadTren);
        ControlTren control = new ControlTren(this.tren);
        Thread cont = new Thread(control, "[Control Tren]");
        cont.start();

        //Se crean las tres terminales
        terminales.put('A', new Terminal('A', capacidadFreeshop, cantCajasFreeshop));
        terminales.put('B', new Terminal('B', capacidadFreeshop, cantCajasFreeshop));
        terminales.put('C', new Terminal('C', capacidadFreeshop, cantCajasFreeshop));

        this.crearCajeros(cantCajasFreeshop);

        //Se crea un semáforo con la cantidad de puestos de informe
        this.puestosDeInforme = new Semaphore(cantPuestosInforme);

        this.horaActual = horaInicio;

        //Semáforo para permitir la atención o no en el aeropueto
        if (horaInicio > 6 && horaInicio < 23) {
            this.atencionAeropuerto = new Semaphore(1);
        } else {
            this.atencionAeropuerto = new Semaphore(0);
        }

    }

    public void crearCajeros(int cantCajas) {

        char[] letras = {'A', 'B', 'C'};

        for (int i = 0; i <= 2; i++) {

            for (int j = 1; j <= cantCajas; j++) {

                Terminal terminal = this.terminales.get(letras[i]);
                FreeShop freeshopCajero = terminal.getFreeshop();

                Cajero nuevoCajero = new Cajero(freeshopCajero);
                Thread caj = new Thread(nuevoCajero, "[CAJERO TERMINAL: " + letras[i] + " CAJA: " + j + " ]");
                caj.start();

            }

        }

    }

    //1- El pasajero llega e ingresa al aeropuerto.
    public void ingresoAeropuerto() {

        try {
            //Trata de tomar la atención
            this.atencionAeropuerto.acquire();

            //El pasajero llega y trata de acceder al puesto de informe
            System.out.println("[PUESTO DE INFORME]: " + Thread.currentThread().getName() + " pudo ingresar al aeropuerto y se formó en la fila del puesto");

            this.atencionAeropuerto.release();

        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //2- El pasajero llega al aeropuerto y se dirige al puesto de informe
    public void irPuestoInforme(Pasajero pasajero) {

        try {

            //Trata de tomar un lugar en el puesto para que lo atiendan
            this.puestosDeInforme.acquire();

            //Mensaje informativo
            System.out.println("[PUESTO DE INFORME]: " + Thread.currentThread().getName() + " ya está siendo atendido");

        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //3- El pasajero termina su atención en el puesto de informe
    public Vuelo salirPuestoInforme() {

        System.out.println("[PUESTO DE INFORME]: " + Thread.currentThread().getName() + " terminó su atención en el puesto");
        this.puestosDeInforme.release();

        return control.obtenerVuelo(this.horaActual);
    }

    //4-El pasajero se toma el tren hacia la estacion que le corresponde
    //Método para simular una hora de reloj
    public synchronized void pasarTiempo() {

        //Se adelanta una hora
        this.horaActual = (this.horaActual + 1) % 24;
        System.out.println(" ============== [RELOJ ⌚ ⌚ ⌚]: Hora actual " + this.horaActual + ":00 ==============");

        //Se verifica en que hora actual se encuentra para iniciar las acciones que corresponda
        if (!(this.horaActual > 6 && this.horaActual < 23)) {

            //Se verifica si es hora de abrir
            if (this.horaActual == 6) {

                //Si es hora de abrir se abren las puertas
                this.iniciarDia();
            } else {

                //Si es hora de cerrar atención
                if (this.horaActual == 21) {

                    //Se cierra la atención al público
                    this.terminarDia();

                } else {

                    //Cuando inicia un nuevo día se programan los vuelos nuevamente
                    if (this.horaActual == 0) {
                        this.control.generarVuelosDelDia();
                        this.control.mostrarVuelosGenerados();
                    }

                }

            }

        }
    }

    public synchronized int getHora() {
        return this.horaActual;
    }

    //Se abre la atención al público
    public void iniciarDia() {
        System.out.println(" ============== [ ⌚ ⌚ ⌚ AEROPUERTO ABRE] -- 06:00HS ==============");
        this.atencionAeropuerto.release();
    }

    //Se cierra la atención al público
    public void terminarDia() {
        this.atencionAeropuerto.drainPermits();
        System.out.println("============== [ ⌚ ⌚ ⌚ AEROPUERTO CIERRA] -- 23.00HS ==============");

    }

    public char obtenerTerminal(int puerta) {

        char terminal;

        //Se obtiene la terminal correspondiente dependiendo de la puerta de embarque
        if (puerta <= 7) {
            terminal = 'A';
        } else {
            if (puerta >= 8 && puerta <= 15) {
                terminal = 'B';
            } else {
                terminal = 'C';
            }
        }

        return terminal;
    }

    public void tomarTren(char terminal) {

        try {
            this.tren.subirTren(terminal);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void bajarTren(char terminal) {

        try {
            this.tren.bajarTren(terminal);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeropuerto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Terminal getTerminal(char terminalSalida) {

        return this.terminales.get(terminalSalida);

    }

}
