
public class AeropuertoMain {

    public static void main(String[] args) {

        //Variables con capacidades/cantidades
        int cantPasajerosIniciales = 20;

        int capacidadTren = 10;
        int cantAerolineas = 9; // Hasta ahora hay 10 nombres de aerolineas
        int capacidadFreeshop = 4;
        int cantCajasFreeshop = 2;
        int cantPuestosInformacion = 5;
        int cantPuestosAtencion = 1;

        //Variable que permite modificar cada cuandos segundos se simulará una hora
        int segundosPorHora = 5;
        int horaInicio = 4;

        Aeropuerto aeropuerto = new Aeropuerto(cantAerolineas, cantPuestosInformacion, capacidadFreeshop, cantCajasFreeshop, capacidadTren, horaInicio, cantPuestosAtencion);

        //Inicia el reloj
        Timer reloj = new Timer(aeropuerto, segundosPorHora);
        Thread r = new Thread(reloj, "RELOJ");
        r.start();

        //Cada hora se generará una cantidad de pasajeros entre el minimo y el máximo
        int cantPasajerosMaxima = 5;
        int cantPasajerosMinima = 1;

        //Hilo que generará los pasajeros constantemente
        GeneradorPasajeros generador = new GeneradorPasajeros(segundosPorHora, cantPasajerosMaxima, cantPasajerosMinima, aeropuerto);
        Thread gen = new Thread(generador, "GENERADOR");
        gen.start();
        

    }

}
