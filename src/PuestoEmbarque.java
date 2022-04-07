
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

class PuestoEmbarque {

    private static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    private int numero;
    private Semaphore ingreso;
    private int pasajerosPresentes;

    public PuestoEmbarque(int numero) {
        //Se deja ingresar el inicio (se atiende de a un pasajero)
        this.ingreso = new Semaphore(1);
        this.numero = numero;
    }

    public void irPuestoEmbarque() {

        try {
            ingreso.acquire();

            //Se hace el ingreso y se cuenta la cantidad de pasajeros 
            System.out.println(ANSI_RED + "[PASAJERO]: " + Thread.currentThread().getName() + " embarcando en la terminal: " + this.numero + ANSI_RESET);

        } catch (InterruptedException ex) {
            Logger.getLogger(PuestoEmbarque.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return "Puesto " + this.numero;
    }

    public void terminarEmbarque() {

        System.out.println(ANSI_RED + "[PASAJERO]: " + Thread.currentThread().getName() + " embarcando en la terminal: " + this.numero + ANSI_RESET);
      
        this.ingreso.release();

    }

}
