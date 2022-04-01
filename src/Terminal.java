
import java.util.HashMap;
import java.util.Map;

class Terminal {

    char letra;
    Map<Integer, PuestoEmbarque> puestosEmbarque = new HashMap<>();
    FreeShop freeshop;
    int desde, hasta;

    public Terminal(char letra, int capacidadFreeshop, int cantCajasFreeShop) {
        this.letra = letra;

        //Se determina cuales son los puestos de embarque contenidos dependiendo la letra
        switch (this.letra) {
            case 'A':
                desde = 1;
                hasta = 7;
                break;
            case 'B':
                desde = 8;
                hasta = 15;
                break;
            case 'C':
                desde = 16;
                hasta = 20;
                break;
        }

        //Se instancian los puestos de embarque
        for (int i = desde; i < hasta; i++) {
            PuestoEmbarque nuevoPuesto = new PuestoEmbarque();
            puestosEmbarque.put(i, nuevoPuesto);

        //Se crea el freeshop con la capacidad y cajas
         this.freeshop = new FreeShop(capacidadFreeshop, cantCajasFreeShop);

        }

    }

}
