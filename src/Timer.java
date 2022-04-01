
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Timer implements Runnable {
    
    private Aeropuerto aeropuerto;
    private int segActualizacion;

    public Timer(Aeropuerto aeropuerto, int segActualizacion) {
        this.aeropuerto = aeropuerto;
        //Determina cada cuantos segundos pasará una hora.
        this.segActualizacion = segActualizacion;
    }

    @Override
    public void run() {

        //Corre constantemente 
        while (true) {
            this.aeropuerto.pasarTiempo();
            try {
                Thread.sleep(this.segActualizacion * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
