package main.EjercicioHilosConcurrencia;

import java.util.concurrent.atomic.AtomicInteger;

public class EjVisitasWeb implements Runnable{
    AtomicInteger visitas;

    public EjVisitasWeb(AtomicInteger contador) {
        this.visitas = contador;
    }

    @Override
    public void run() {
        visitas.incrementAndGet();
    }


    static void main(String[] args){
        AtomicInteger visitas= new AtomicInteger(0);

        for (int i = 0; i < 1000; i++) {
            try {
            Thread hilo = new Thread(new EjVisitasWeb(visitas));
            hilo.start();
            hilo.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(visitas);
    }

}


