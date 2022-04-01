
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


    public Pasajero(Aeropuerto ap) {
        this.aeropuerto = ap;
    }

    @Override
    public void run() {

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

    }

    public void simularAtencion(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Pasajero.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
