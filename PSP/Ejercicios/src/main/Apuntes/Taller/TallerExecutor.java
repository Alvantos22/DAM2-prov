package main.Apuntes.Taller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TallerExecutor {

    private final int NUM_EQUIPOS = 10;
    private final int NUM_TECNICOS = 3;

    private final AtomicInteger reparadosVIP =
            new AtomicInteger(0);

    private final AtomicInteger reparadosNormales =
            new AtomicInteger(0);

    public void iniciar() throws InterruptedException {

        ExecutorService executor =
                Executors.newFixedThreadPool(NUM_TECNICOS);

        for (int i = 1; i <= NUM_EQUIPOS; i++) {

            final int numero = i;
            final boolean vip = (i == 3 || i == 7);

            executor.submit(() -> {
                try {
                    if (vip) {
                        System.out.println(
                                "PC-" + numero + " VIP en reparación");

                        Thread.sleep(1000);

                        reparadosVIP.incrementAndGet();

                    } else {
                        System.out.println(
                                "PC-" + numero + " normal en reparación");

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

        // No se aceptan nuevas tareas
        executor.shutdown();

        // Esperar a que terminen todas las tareas
        if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
            executor.shutdownNow();
        }

        System.out.println("\n--- ESTADÍSTICAS ---");
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
        new TallerExecutor().iniciar();
    }
}
