
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlTren implements Runnable {

    private Tren tren;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public ControlTren(Tren tren) {
        this.tren = tren;
    }

    @Override
    public void run() {

        while (true) {

            try {

                /*TREN INTENTA INICIAR VIAJE*/
                tren.iniciarViaje();

                //Si hay pasajeros para la A, se para si no se sigue de largo
                if (tren.obtenerPasajeros('A') > 0) {
                    tren.pararEstacion('A');
                    tren.esperarParaSeguir('A');
                } else {

                    System.out.println(ANSI_BLUE + "[TREN]: No va a parar en la estación A, no hay pasajeros" + ANSI_RESET);

                }

                if (tren.obtenerPasajeros('B') > 0) {
                    tren.pararEstacion('B');
                    tren.esperarParaSeguir('B');
                } else {

                    System.out.println(ANSI_BLUE + "[TREN]: No va a parar en la estación B, no hay pasajeros" + ANSI_RESET);

                }

                if (tren.obtenerPasajeros('C') > 0) {
                    tren.pararEstacion('C');
                    tren.esperarParaSeguir('C');
                } else {

                    System.out.println(ANSI_BLUE + "[TREN]: No va a parar en la estación C, no hay pasajeros" + ANSI_RESET);

                }

                //Prepara el tren para una nueva vuelta
                tren.irAInicio();

            } catch (InterruptedException ex) {
                Logger.getLogger(ControlTren.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
