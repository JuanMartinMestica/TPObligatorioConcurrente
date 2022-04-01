
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

class FreeShop {

    private ArrayBlockingQueue<CajaFreeShop> cajas;
    private static double probCompra = 0.5;
    private Semaphore entrada;

    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";

    public FreeShop(int capFreeShop, int cantCajas) {

        //Se crea un semáforo con la capacidad máxima del freeshop
        this.entrada = new Semaphore(capFreeShop);
        //Se crea la Blocking Queue con la cantidad de cajas del main
        this.cajas = new ArrayBlockingQueue<CajaFreeShop>(cantCajas);

        for (int i = 0; i < cantCajas; i++) {
            this.cajas.add(new CajaFreeShop());
        }
    }

    

}
