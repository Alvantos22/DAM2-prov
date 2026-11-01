package main.Apuntes.Taller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class TallerSemaphore {

    private final BlockingQueue<String> equipos =
            new ArrayBlockingQueue<>(4);

    private final Semaphore plazas = new Semaphore(2);
    private final int NUM_TECNICOS = 3;
    private final int NUM_EQUIPOS = 10;

    public void iniciar() throws InterruptedException {

        Thread recepcionista = new Thread(() -> {
            try {
                for (int i = 1; i <= NUM_EQUIPOS; i++) {
                    equipos.put("PC-" + i);
                }

                for (int i = 0; i < NUM_TECNICOS; i++) {
                    equipos.put("FIN");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread[] tecnicos = new Thread[NUM_TECNICOS];

        for (int i = 0; i < NUM_TECNICOS; i++) {
            final int numero = i + 1;

            tecnicos[i] = new Thread(() -> {
                try {
                    while (true) {
                        String equipo = equipos.take();

                        if (equipo.equals("FIN")) {
                            break;
                        }

                        plazas.acquire();
                        try {
                            System.out.println(
                                    "Técnico " + numero
                                            + " repara " + equipo);
                            Thread.sleep(2000);
                            System.out.println(
                                    "Técnico " + numero
                                            + " termina " + equipo);
                        } finally {
                            plazas.release();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        for (Thread tecnico : tecnicos) {
            tecnico.start();
        }

        recepcionista.start();
        recepcionista.join();

        for (Thread tecnico : tecnicos) {
            tecnico.join();
        }
    }

    public static void main(String[] args)
            throws InterruptedException {
        new TallerSemaphore().iniciar();
    }
}
