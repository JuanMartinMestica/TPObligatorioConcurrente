
import java.util.logging.Level;
import java.util.logging.Logger;

public class AtencionPuesto implements Runnable {

    private Aerolinea aerolinea;

    public AtencionPuesto(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    @Override
    public void run() {

        while (true) {

            aerolinea.esperarPasajero();
            aerolinea.llamarTurno();
            aerolinea.atenderTurno();
            this.simularAtencion(600);
            aerolinea.terminarAtencion();
        }

    }

    public void simularAtencion(int ms) {

        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(AtencionPuesto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
