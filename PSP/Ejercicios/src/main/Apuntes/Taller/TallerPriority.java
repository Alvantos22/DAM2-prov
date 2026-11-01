
package main.Apuntes.Taller;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TallerPriority {

    private final BlockingQueue<Equipo> equipos =
            new PriorityBlockingQueue<>();

    private final int NUM_TECNICOS = 3;
    private final int NUM_EQUIPOS = 10;

    public void iniciar() throws InterruptedException {

        // Hilo que recibe los ordenadores
        Thread recepcionista = new Thread(() -> {
            try {
                for (int i = 1; i <= NUM_EQUIPOS; i++) {

                    boolean urgente = (i == 3 || i == 7);

                    Equipo equipo = new Equipo(
                            "PC-" + i,
                            urgente ? 1 : 0,
                            false
                    );

                    equipos.put(equipo);

                    System.out.println(
                            "Recepción registra "
                                    + equipo.getNombre()
                                    + (urgente ? " (URGENTE)" : "")
                    );
                }

                // Una señal FIN por técnico
                for (int i = 0; i < NUM_TECNICOS; i++) {
                    equipos.put(new Equipo("FIN", -1, true));
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
                        Equipo equipo = equipos.take();

                        if (equipo.isFin()) {
                            System.out.println(
                                    "Técnico " + numeroTecnico
                                            + " termina su turno"
                            );
                            break;
                        }

                        System.out.println(
                                "Técnico " + numeroTecnico
                                        + " repara " + equipo.getNombre()
                                        + (equipo.getPrioridad() == 1
                                        ? " (URGENTE)" : "")
                        );

                        Thread.sleep(2000);

                        System.out.println(
                                "Técnico " + numeroTecnico
                                        + " termina " + equipo.getNombre()
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

        // Esperamos a que termine la recepcionista
        recepcionista.join();

        // Esperamos a que terminen los técnicos
        for (Thread tecnico : tecnicos) {
            tecnico.join();
        }

        System.out.println("Todos los equipos procesados.");
    }

    public static void main(String[] args)
            throws InterruptedException {
        new Taller().iniciar();
    }
}

class Equipo implements Comparable<Equipo> {

    private final String nombre;
    private final int prioridad;
    private final boolean fin;

    public Equipo(String nombre, int prioridad, boolean fin) {
        this.nombre = nombre;
        this.prioridad = prioridad;
        this.fin = fin;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public boolean isFin() {
        return fin;
    }

    @Override
    public int compareTo(Equipo otro) {
        return Integer.compare(otro.prioridad, this.prioridad);
    }
}
