package Contador_Visitas;

public class ContadorVisitasNoSync implements IContadorVisitas{
    private int contador;



    @Override
    public void incrementarVisita() {
       contador++;
    }

    @Override
    public int getContador() {
        return contador;
    }
}
