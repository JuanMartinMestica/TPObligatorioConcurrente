
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

class Vuelo {

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private Aerolinea aerolinea;
    private int hora;
    private int puestoEmbarque;
    private int pasajesVendidos = 0;
    private Semaphore subidaAvion;
    private CountDownLatch despegue;

    public Vuelo(Aerolinea aerolinea, int hora, int puestoEmbarque) {
        this.aerolinea = aerolinea;
        this.hora = hora;
        this.puestoEmbarque = puestoEmbarque;
        this.subidaAvion = new Semaphore(0);

    }

    public Aerolinea getAerolinea() {
        return aerolinea;
    }

    public int getPuerta() {
        return puestoEmbarque;
    }

    public void pasajeVendido() {
        this.pasajesVendidos++;
    }

    public int getHora() {
        return this.hora;
    }

    @Override
    public String toString() {
        return "[VUELO] ---> [Aerolinea]: " + this.aerolinea.getNombre() + " [Hora Salida]: " + this.hora + " [Puesto de Embarque]: " + this.puestoEmbarque;
    }

    public int getPasajesVendidos() {
        return this.pasajesVendidos;
    }

    public void subir() {

        try {
            //Una vez se autoriza el despegue, se libera el semáforo para que suban los pasajeros
            this.subidaAvion.acquire();

            System.out.println(ANSI_RED + "[PASAJERO]: " + Thread.currentThread().getName() + " se subió al avión" + ANSI_RESET);

            //Libera para los pasajeros que vienen detrás
            this.subidaAvion.release();

        } catch (InterruptedException ex) {
            Logger.getLogger(Vuelo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void esperarDespegue() {

        this.despegue.countDown();
        System.out.println(ANSI_RED + " [PASAJERO]: " + Thread.currentThread().getName() + " EN VUELO" + ANSI_RESET); 

    }

    public void autorizarSubida() {

        //Reemplazar barrera ciclica
        this.despegue = new CountDownLatch(this.pasajesVendidos);

        this.subidaAvion.release();

    }

}
