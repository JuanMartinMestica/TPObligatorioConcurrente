
public class AeropuertoMain {

    public static void main(String[] args) {

        int capacidadTren = 5;
        int cantAerolineas = 3; // Hasta ahora hay 10 nombres de aerolineas
        int capacidadFreeshop = 4;
        int cantCajasFreeshop = 2;
        int cantPuestosInformacion = 5;
        int capacidadPuestoAtencion = 2;

        //Variable que permite modificar cada cuandos segundos se simulará una hora
        int segundosPorHora = 7;
        int horaInicio = 5;

        Aeropuerto aeropuerto = new Aeropuerto(cantAerolineas, cantPuestosInformacion, capacidadFreeshop, cantCajasFreeshop, capacidadTren, horaInicio, capacidadPuestoAtencion);

        //Inicia el reloj
        Timer reloj = new Timer(aeropuerto, segundosPorHora);
        Thread r = new Thread(reloj, "RELOJ");
        r.start();

        //Cada hora se generará una cantidad de pasajeros 
        int cantPasajeros = 5;

        //Hilo que generará los pasajeros constantemente
        GeneradorPasajeros generador = new GeneradorPasajeros(segundosPorHora, cantPasajeros, aeropuerto);
        Thread gen = new Thread(generador, "GENERADOR");
        gen.start();

    }

}
