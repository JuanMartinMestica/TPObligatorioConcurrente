
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneradorPasajeros implements Runnable {

    private int segundosPorHora;
    private Random r;
    private int cantidadPasajeros;

    private Aeropuerto ap;
    private int pasajeroActual;

    public GeneradorPasajeros(int segundosPorHora, int cant, Aeropuerto ap) {
        this.segundosPorHora = segundosPorHora;
        this.r = new Random();
        this.cantidadPasajeros = cant;
        this.ap = ap;
        this.pasajeroActual = 1;
    }

    @Override
    public void run() {

        while (true) {

            //Si los pasajeros podrán obtener un vuelo, se los genera
            if (this.ap.getHora() >= 5 && this.ap.getHora() <= 19) {
                //Se crean los hilos
                this.crearPasajeros(this.cantidadPasajeros);

            }

            //Espera una hora
            this.esperar(segundosPorHora);

        }

    }

    private void crearPasajeros(int cantidad) {

        for (int i = 1; i <= cantidad; i++) {
            Pasajero nuevoPasajero = new Pasajero(ap);
            Thread hiloPasajero = new Thread(nuevoPasajero, "PASAJERO " + this.pasajeroActual);
            hiloPasajero.start();
            pasajeroActual++;
        }

    }

    private void esperar(int segundos) {

        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GeneradorPasajeros.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
