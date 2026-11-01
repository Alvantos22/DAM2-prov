package main.Apuntes.Taller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Taller {

    private final BlockingQueue<String> equipos =
            new ArrayBlockingQueue<>(4);
    private final Semaphore semaforo =new Semaphore(2);

    private final int NUM_TECNICOS = 3;
    private final int NUM_EQUIPOS = 10;


    public void iniciar() {

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

                // Un mensaje de fin para cada técnico
                for (int i = 0; i < NUM_TECNICOS; i++) {
                    equipos.put("FIN");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Creamos los técnicos
        for (int i = 1; i <= NUM_TECNICOS; i++) {
            final int numeroTecnico = i;


            Thread tecnico = new Thread(() -> {
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

                        System.out.println(
                                "Técnico " + numeroTecnico
                                        + " termina " + equipo
                        );
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            tecnico.start();
        }

        recepcionista.start();
    }

    public static void main(String[] args) {
        new Taller().iniciar();
    }
}
