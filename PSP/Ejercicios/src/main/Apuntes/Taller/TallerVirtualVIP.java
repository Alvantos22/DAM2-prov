package main.Apuntes.Taller;

import java.util.concurrent.atomic.AtomicInteger;

public class TallerVirtualVIP {

    private final int NUM_EQUIPOS = 10;

    private final AtomicInteger reparadosVIP =
            new AtomicInteger(0);

    private final AtomicInteger reparadosNormales =
            new AtomicInteger(0);

    public void iniciar() throws InterruptedException {

        Thread[] hilos = new Thread[NUM_EQUIPOS];

        for (int i = 1; i <= NUM_EQUIPOS; i++) {

            final int numero = i;
            final boolean vip = (i == 3 || i == 7);

            hilos[i - 1] = Thread.startVirtualThread(() -> {
                try {
                    if (vip) {
                        System.out.println(
                                "PC-" + numero + " es VIP");
                        Thread.sleep(1000);
                        reparadosVIP.incrementAndGet();
                    } else {
                        System.out.println(
                                "PC-" + numero + " es normal");
                        Thread.sleep(2000);
                        reparadosNormales.incrementAndGet();
                    }

                    System.out.println(
                            "PC-" + numero + " reparado");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Esperar a que terminen todos
        for (Thread hilo : hilos) {
            hilo.join();
        }

        System.out.println("\n--- RESULTADOS ---");
        System.out.println(
                "VIP reparados: " + reparadosVIP.get());
        System.out.println(
                "Normales reparados: " + reparadosNormales.get());
        System.out.println(
                "Total reparados: "
                        + (reparadosVIP.get()
                        + reparadosNormales.get()));
    }

    public static void main(String[] args)
            throws InterruptedException {
        new TallerVirtualVIP().iniciar();
    }
}