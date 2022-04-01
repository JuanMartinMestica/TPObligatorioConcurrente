
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ControlVuelos {

    private int horaActual;
    private int cantVuelosPorDia = 20;
    private int cantAerolineas;
    private Aerolinea[] aerolineas;
    private Random r = new Random();
    Map<Integer, Vuelo> vuelos = new HashMap<>();

    public ControlVuelos(int horaActual, int cantAerolineas, Aerolinea[] aerolineas) {
        this.horaActual = horaActual;
        this.cantAerolineas = cantAerolineas;
        this.aerolineas = aerolineas;
    }

    public void generarVuelosDelDia() {
        int horaVuelo;
        int puestoEmbarque = 1;
        vuelos.clear();//Se limpia los vuelos anteriores

        //Se crean vuelos desde las 7hs hasta las 22hs
        for (horaVuelo = 7; horaVuelo <= 22; horaVuelo++) {

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

        //Hora máxima del vuelo
        int horaMax = 22;
        
        //Como minimo se le asigna un vuelo con 3 horas de diferencia con la hora actual
        int horaMin = horaPasajero + 3;

        //Se selecciona un vuelo aleatorio entre la hora minima y máxima
        int posVuelo = r.nextInt(horaMax - horaMin) + horaMin;
        Vuelo vueloPasajero = vuelos.get(posVuelo);

        return vueloPasajero;
    }
}
