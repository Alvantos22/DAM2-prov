
package main.Apuntes.Taller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TallerConContadores {

    private final BlockingQueue<String> equipos =
            new ArrayBlockingQueue<>(4);

    private final int NUM_TECNICOS = 3;
    private final int NUM_EQUIPOS = 10;

    private final AtomicInteger totalReparados =
            new AtomicInteger(0);

    private final AtomicInteger[] reparacionesTecnicos =
            new AtomicInteger[NUM_TECNICOS];

    public void iniciar() throws InterruptedException {

        // Inicializamos los contadores de cada técnico
        for (int i = 0; i < NUM_TECNICOS; i++) {
            reparacionesTecnicos[i] = new AtomicInteger(0);
        }

        // Hilo que recibe los ordenadores
        Thread recepcionista = new Thread(() -> {
            try {
                for (int i = 1; i <= NUM_EQUIPOS; i++) {
                    String equipo = "PC-" + i;

                    equipos.put(equipo);

                    System.out.println(
                            "Recepción registra " + equipo
                    );
                }

                // Un FIN para cada técnico
                for (int i = 0; i < NUM_TECNICOS; i++) {
                    equipos.put("FIN");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Creamos los técnicos
        Thread[] tecnicos = new Thread[NUM_TECNICOS];

        for (int i = 0; i < NUM_TECNICOS; i++) {
            final int numeroTecnico = i + 1;

            tecnicos[i] = new Thread(() -> {
                try {
                    while (true) {
                        String equipo = equipos.take();

                        if (equipo.equals("FIN")) {
                            System.out.println(
                                    "Técnico " + numeroTecnico
                                            + " termina su turno"
                            );
                            break;
                        }
                        System.out.println(
                                "Técnico " + numeroTecnico
                                        + " repara " + equipo
                        );
                        Thread.sleep(2000);
                        // Contador individual
                        reparacionesTecnicos[numeroTecnico - 1]
                                .incrementAndGet();
                        // Contador compartido
                        totalReparados.incrementAndGet();

                        System.out.println(
                                "Técnico " + numeroTecnico
                                        + " termina " + equipo
                        );
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Iniciamos los técnicos
        for (Thread tecnico : tecnicos) {
            tecnico.start();
        }

        recepcionista.start();

        // Esperamos a la recepcionista
        recepcionista.join();

        // Esperamos a todos los técnicos
        for (Thread tecnico : tecnicos) {
            tecnico.join();
        }

        // Mostramos las estadísticas
        System.out.println("\n--- ESTADÍSTICAS ---");

        for (int i = 0; i < NUM_TECNICOS; i++) {
            System.out.println(
                    "Técnico " + (i + 1) + ": "
                            + reparacionesTecnicos[i].get()
                            + " reparaciones"
            );
        }

        System.out.println(
                "Total reparados: " + totalReparados.get()
        );
    }

    public static void main(String[] args)
            throws InterruptedException {
        new Taller().iniciar();
    }
}
