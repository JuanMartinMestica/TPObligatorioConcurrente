
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

        for (int i = 1; i <= cantPasajerosIniciales; i++) {
            Pasajero pasajero = new Pasajero(aeropuerto);
            Thread p = new Thread(pasajero, "PASAJERO " + i);
            p.start();
        }

    }

}
