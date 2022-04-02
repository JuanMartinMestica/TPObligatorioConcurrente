
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

class FreeShop {

    private Semaphore cajas;
    private Semaphore entrada;
    private Semaphore atencion;
    private Semaphore salida;

    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_RESET = "\u001B[0m";

    public FreeShop(int capFreeShop, int cantCajas) {

        //Se crea un semáforo con la capacidad máxima del freeshop
        this.entrada = new Semaphore(capFreeShop);

        //Se crean los permisos para las cajas
        this.cajas = new Semaphore(cantCajas);

        //Semaforos para atención de cajero
        this.atencion = new Semaphore(0, true);
        this.salida = new Semaphore(0, true);

    }

    public void ingresoPasajero() {

        try {
            //Ingreso con un semáforo
            this.entrada.acquire();

            System.out.println(ANSI_PURPLE + "[PASAJERO]: " + Thread.currentThread().getName() + " pudo ingresar al freeshop" + ANSI_RESET);

            this.cajas.acquire();

            System.out.println(ANSI_PURPLE + "[PASAJERO]: " + Thread.currentThread().getName() + " se encuentra en la caja" + ANSI_RESET);

            this.atencion.release();

        } catch (InterruptedException ex) {
            Logger.getLogger(FreeShop.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void salir() {

        try {
            this.salida.acquire();

            System.out.println(ANSI_PURPLE + "[PASAJERO]: " + Thread.currentThread().getName() + " saliendo del freeshop" + ANSI_RESET);
        } catch (InterruptedException ex) {
            Logger.getLogger(FreeShop.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void atender() {

        try {

            //Está pendiente de atender
            this.atencion.acquire();

            System.out.println(ANSI_PURPLE + Thread.currentThread().getName() + " Cobrando los productos " + ANSI_RESET);

        } catch (InterruptedException ex) {
            Logger.getLogger(FreeShop.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void terminarAtencion() {

        this.salida.release();

    }

}
