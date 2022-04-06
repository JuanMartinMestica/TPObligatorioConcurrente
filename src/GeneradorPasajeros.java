
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GeneradorPasajeros implements Runnable {

    private int segundosPorHora;
    private Random r;
    private int maximo;
    private int minimo;
    private Aeropuerto ap;
    private int pasajeroActual;

    public GeneradorPasajeros(int segundosPorHora, int maximo, int minimo, Aeropuerto ap) {
        this.segundosPorHora = segundosPorHora;
        this.r = new Random();
        this.maximo = maximo;
        this.minimo = minimo;
        this.ap = ap;
        this.pasajeroActual = 1;
    }

    @Override
    public void run() {

        while (true) {
            //Random entre minimo y máximo
            int cantPasajeros = r.nextInt(maximo - minimo) + minimo;
            this.crearPasajeros(cantPasajeros);
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
