
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Terminal {

    char letra;
    ConcurrentHashMap<Integer, PuestoEmbarque> puestosEmbarque;
    FreeShop freeshop;
    int desde, hasta, cant;

    public Terminal(char letra, int capacidadFreeshop, int cantCajasFreeShop) {
        this.letra = letra;
        this.cant = 7;

        //Se determina cuales son los puestos de embarque contenidos dependiendo la letra
        switch (this.letra) {
            case 'A':
                desde = 1;
                hasta = 7;
                break;
            case 'B':
                this.cant = 8;
                desde = 8;
                hasta = 15;
                break;
            case 'C':
                desde = 16;
                hasta = 20;
                this.cant = 4;
                break;
        }

        //Constructor contiene: cantidad de lugares, factor de carga y cantidad de hilos que podrán acceder concurrentemente
        this.puestosEmbarque = new ConcurrentHashMap(this.cant, 1, this.cant);

        //Se instancian los puestos de embarque
        for (int i = desde; i <= hasta; i++) {
            PuestoEmbarque nuevoPuesto = new PuestoEmbarque(i);
            puestosEmbarque.put(i, nuevoPuesto);
        }

        //Se crea el freeshop con la capacidad y cajas
        this.freeshop = new FreeShop(capacidadFreeshop, cantCajasFreeShop);

    }

    public void irPuestoEmbarque(int puerta) {

        PuestoEmbarque puesto = this.puestosEmbarque.get(puerta);

        puesto.irPuestoEmbarque();

    }

    public FreeShop getFreeshop() {
        return this.freeshop;
    }

    public void comprar() {
        this.freeshop.ingresoPasajero();
    }

    public void salir() {
        this.freeshop.salir();
    }

}
