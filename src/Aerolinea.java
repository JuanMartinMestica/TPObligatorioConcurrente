
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

class Aerolinea {

    public static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private String nombre;
    private Semaphore puestoDeAtencion;
    private Lock guardia;

    public Aerolinea(String nombre, int cantPuestosAtencion) {
        this.nombre = nombre;
        this.puestoDeAtencion = new Semaphore(cantPuestosAtencion);
        this.guardia = new ReentrantLock();
    }

    public String getNombre() {
        return this.nombre;
    }

    public void irAPuestoDeAtencion() {

        //Se obtiene el lock
        guardia.lock();
        try {
            System.out.println(ANSI_GREEN + "[PUESTO DE ATENCIÓN - " + this.nombre + "]: " + Thread.currentThread().getName() + " fue llamado por el guardia y está a la espera de un puesto" + ANSI_RESET);

            try {
                //Se busca un puesto de atención disponible
                puestoDeAtencion.acquire();

                System.out.println(ANSI_GREEN + "[PUESTO DE ATENCIÓN - " + this.nombre + "]: " + Thread.currentThread().getName() + " está haciendo el check-in" + ANSI_RESET);

            } catch (InterruptedException ex) {
                Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
            }

        } finally {
            guardia.unlock();
        }

    }

    public void salirPuestoAtencion() {

        System.out.println(ANSI_GREEN + "[PUESTO DE ATENCIÓN - " + this.nombre + "]: " + Thread.currentThread().getName() + " terminó su atención en el puesto" + ANSI_RESET);

        puestoDeAtencion.release();

    }

}
