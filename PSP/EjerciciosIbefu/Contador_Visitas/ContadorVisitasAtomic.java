package Contador_Visitas;

import java.util.concurrent.atomic.AtomicInteger;

public class ContadorVisitasAtomic implements IContadorVisitas{
    AtomicInteger contadorAtomic = new AtomicInteger(0);

    @Override
    public void incrementarVisita() {
        contadorAtomic.incrementAndGet();
    }

    @Override
    public int getContador() {
        return contadorAtomic.get();
    }
}
