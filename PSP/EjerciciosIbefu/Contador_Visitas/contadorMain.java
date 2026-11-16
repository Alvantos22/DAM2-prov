package Contador_Visitas;

public class contadorMain {
    public static void main(String[] args) {
        IContadorVisitas contador = new ContadorVisitasNoSync();
        ContadorVisitas servicios = new ContadorVisitas(contador);
        servicios.work();

        contador = new ContadorVisitasSync();
        servicios = new ContadorVisitas(contador);
        servicios.work();


        contador = new ContadorVisitasAtomic();
        servicios = new ContadorVisitas(contador);
        servicios.work();
    }
}
