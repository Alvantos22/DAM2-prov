package Contador_Visitas;

public class ContadorVisitasSync implements IContadorVisitas{
    private int contador;


    @Override
    public synchronized void incrementarVisita() {
        contador++;
    }

    @Override
    public int getContador() {
        return contador;
    }
}
