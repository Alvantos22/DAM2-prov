package main.Apuntes.Taller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class TallerCompletable {

    private final int NUM_EQUIPOS = 10;

    private final AtomicInteger reparadosVIP =
            new AtomicInteger(0);

    private final AtomicInteger reparadosNormales =
            new AtomicInteger(0);

    public void iniciar() {

        CompletableFuture<?>[] tareas =
                new CompletableFuture[NUM_EQUIPOS];

        for (int i = 1; i <= NUM_EQUIPOS; i++) {

            final int numero = i;
            final boolean vip = (i == 3 || i == 7);

            tareas[i - 1] = CompletableFuture.runAsync(() -> {
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

        // Esperar a que terminen todas las tareas
        CompletableFuture.allOf(tareas).join();

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

    public static void main(String[] args) {
        new TallerCompletable().iniciar();
    }
}