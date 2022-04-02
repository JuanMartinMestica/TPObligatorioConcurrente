
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cajero implements Runnable {

    private FreeShop freeshop;

    public Cajero(FreeShop freeshop) {
        this.freeshop = freeshop;
    }

    @Override
    public void run() {

        while (true) {

            this.freeshop.atender();
            this.simularAtencion(1200);
            this.freeshop.terminarAtencion();

        }

    }

    public void simularAtencion(int ms) {

        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cajero.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
