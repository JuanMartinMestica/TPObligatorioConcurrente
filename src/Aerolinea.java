
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

class Aerolinea {

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
            System.out.println("[PUESTO DE ATENCIÓN - " + this.nombre + "]: " + Thread.currentThread().getName() + " fue llamado por el guardia y está a la espera de un puesto");

            try {
                //Se busca un puesto de atención disponible
                puestoDeAtencion.acquire();

                System.out.println("[PUESTO DE ATENCIÓN - " + this.nombre + "]: " + Thread.currentThread().getName() + " está haciendo el check-in");

            } catch (InterruptedException ex) {
                Logger.getLogger(Aerolinea.class.getName()).log(Level.SEVERE, null, ex);
            }

        } finally {
            guardia.unlock();
        }

    }

    public void salirPuestoAtencion() {

       System.out.println("[PUESTO DE ATENCIÓN - " + this.nombre + "]: " + Thread.currentThread().getName() + " terminó su atención en el puesto");
       
       puestoDeAtencion.release();
        
    }

}
