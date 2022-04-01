
class Vuelo {

    private Aerolinea aerolinea;
    private int hora;
    private int puestoEmbarque;
    private int pasajesVendidos = 0;

    public Vuelo(Aerolinea aerolinea, int hora, int puestoEmbarque) {
        this.aerolinea = aerolinea;
        this.hora = hora;
        this.puestoEmbarque = puestoEmbarque;
    }

    public Aerolinea getAerolinea() {
        return aerolinea;
    }

    public int getPuerta() {
        return puestoEmbarque;
    }
    
    public void pasajeVendido(){
        this.pasajesVendidos++;
    }

    @Override
    public String toString() {
        return "[VUELO] ---> [Aerolinea]: " + this.aerolinea.getNombre() + " [Hora Salida]: " + this.hora + " [Puesto de Embarque]: " + this.puestoEmbarque;
    }

}
