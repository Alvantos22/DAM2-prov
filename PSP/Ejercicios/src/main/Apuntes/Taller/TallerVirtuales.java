package main.Apuntes.Taller;

import java.util.concurrent.atomic.AtomicInteger;

public class TallerVirtuales {

    private final int NUM_EQUIPOS = 10;
    private final AtomicInteger reparados = new AtomicInteger(0);

    public void iniciar() throws InterruptedException {

        Thread[] hilos = new Thread[NUM_EQUIPOS];

        for (int i = 0; i < NUM_EQUIPOS; i++) {
            final int numero = i + 1;

            hilos[i] = Thread.startVirtualThread(() -> {
                try {
                    System.out.println(
                            "Reparando PC-" + numero);

                    Thread.sleep(2000);

                    reparados.incrementAndGet();

                    System.out.println(
                            "Terminado PC-" + numero);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Esperamos a que terminen todos los hilos
        for (Thread hilo : hilos) {
            hilo.join();
        }

        System.out.println(
                "Total reparados: " + reparados.get());
    }

    public static void main(String[] args)
            throws InterruptedException {
        new TallerVirtuales().iniciar();
    }
}