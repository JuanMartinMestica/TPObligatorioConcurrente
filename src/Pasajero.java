
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pasajero implements Runnable {

    /*===================== Variables para print ================*/
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
    /*===========================================================*/

    private Aeropuerto aeropuerto;
    private char terminalSalida;
    private Vuelo vuelo;
    private Random r = new Random();
    private Terminal terminalEmbarque;

    public Pasajero(Aeropuerto ap) {
        this.aeropuerto = ap;
    }

    @Override
    public void run() {

        int deseaComprar = r.nextInt(3);

        //Ingreso al aeropuerto
        System.out.println(ANSI_GREEN + "[PASAJERO]: " + Thread.currentThread().getName() + " acaba de llegar al aeropuerto" + ANSI_RESET);
        this.aeropuerto.ingresoAeropuerto();

        //El pasajero llega y va al puesto de informe para ser atendido
        this.aeropuerto.irPuestoInforme(this);
        this.simularAtencion(1200);
        this.vuelo = this.aeropuerto.salirPuestoInforme();

        //Ya se tiene el vuelo, se dirige al puesto de atención que corresponda
        System.out.println(ANSI_GREEN + "[PASAJERO]: " + Thread.currentThread().getName() + " tiene el vuelo: " + this.vuelo + ANSI_RESET);
        this.vuelo.getAerolinea().irAPuestoDeAtencion();
        this.simularAtencion(2000);
        this.vuelo.getAerolinea().salirPuestoAtencion();

        /*Dependiendo de la puerta del embarque, se solicita al aeropuerto la terminal 
        correspondiente a la que debe ir el pasajero*/
        this.terminalSalida = this.aeropuerto.obtenerTerminal(this.vuelo.getPuerta());

        //Viaje en tren del pasajero
        System.out.println(ANSI_GREEN + " [PASAJERO]: " + Thread.currentThread().getName() + " se tomará el tren hacia la terminal: " + terminalSalida + ANSI_RESET);
        this.aeropuerto.tomarTren(terminalSalida);
        this.aeropuerto.bajarTren(terminalSalida);

        //Si se tiene al menos dos horas y sale según la probabilidad entonces entra a comprar
        if (this.vuelo.getHora() - this.aeropuerto.getHora() >= 2 && deseaComprar == 1) {

            System.out.println(ANSI_GREEN + "[PASAJERO]: " + Thread.currentThread().getName() + " va a comprar al freeshop " + terminalSalida + ANSI_RESET);

            //El cliente ingresa al freeshop
            this.terminalEmbarque = this.aeropuerto.getTerminal(terminalSalida);

            //Interacciones de compra
            this.terminalEmbarque.comprar();
            this.terminalEmbarque.salir();

        }

    }

    public void simularAtencion(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
