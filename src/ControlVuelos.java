
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ControlVuelos {

    private int horaActual;
    private int cantVuelosPorDia = 20;
    private int cantAerolineas;
    private Aerolinea[] aerolineas;
    private Random r = new Random();
    Map<Integer, Vuelo> vuelos = new ConcurrentHashMap<>();
    private static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public ControlVuelos(int horaActual, int cantAerolineas, Aerolinea[] aerolineas) {
        this.horaActual = horaActual;
        this.cantAerolineas = cantAerolineas;
        this.aerolineas = aerolineas;
    }

    public void generarVuelosDelDia() {
        int horaVuelo;
        int puestoEmbarque = 1;
        vuelos.clear();//Se limpia los vuelos anteriores

        //Se crean vuelos desde las 9hs hasta las 22hs
        for (horaVuelo = 9; horaVuelo <= 22; horaVuelo++) {

            //Se selecciona una aerolinea aleatorio
            Aerolinea aerolineaVuelo = aerolineas[r.nextInt(cantAerolineas)];

            Vuelo nuevoVuelo = new Vuelo(aerolineaVuelo, horaVuelo, puestoEmbarque);

            puestoEmbarque++;
            vuelos.put(horaVuelo, nuevoVuelo);

        }
    }

    public void mostrarVuelosGenerados() {

        System.out.println("============== [SE GENERAN LOS VUELOS DEL DÍA] ==============");
        vuelos.forEach((hora, vuelo) -> System.out.println(vuelo));
    }

    public Vuelo obtenerVuelo(int horaPasajero) {

        int posVuelo;

        //Hora máxima del vuelo
        int horaMax = 22;

        //Como minimo se le asigna un vuelo con 3 horas de diferencia con la hora actual
        int horaMin = (horaPasajero + 3);

        if (horaMin == horaMax || horaMin > horaMax) {

            posVuelo = horaMax;

        } else {
            //Se selecciona un vuelo aleatorio entre la hora minima y máxima
            posVuelo = r.nextInt(horaMax - horaMin) + horaMin;

        }

        Vuelo vueloAsignado = vuelos.get(posVuelo);

        //Se marca un pasaje vendido
        vueloAsignado.pasajeVendido();

        return vueloAsignado;
    }

    public void autorizarDespegue(int hora) {

        Vuelo vueloDespegue = this.vuelos.get(hora);
        int pasajesVendidosVuelo = vueloDespegue.getPasajesVendidos();

        if (pasajesVendidosVuelo > 0) {

            //Si se vendieron pasajes, entonces se autoriza el despegue
            System.out.println(ANSI_RED + "======================= DESPEGUE AUTORIZADO ==================="
                    + "\n" + vueloDespegue + "\n"
                    + "PASAJES VENDIDOS: " + pasajesVendidosVuelo + "\n"
                    + "================================================================" + ANSI_RESET);

            vueloDespegue.autorizarSubida();

        } else {

            //Si nadie sacó pasaje para el vuelo, no despega y se cancela
            System.out.println(ANSI_RED + "=======================[VUELO CANCELADO]============================ \n"
                    + vueloDespegue
                    + "\n ============================================================" + ANSI_RESET);

        }

    }

}
