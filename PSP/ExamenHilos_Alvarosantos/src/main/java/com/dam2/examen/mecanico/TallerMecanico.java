package com.dam2.examen.mecanico;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * MEJORAS A IMPLEMENTAR:
 *
 * 1. Identificar a cada mecánico (Mecánico-1 y Mecánico-2).
 *
 * 2. Mostrar cuántos coches ha reparado cada mecánico.
 *
 * 3. Mostrar cuánto tiempo ha esperado cada coche
 *    antes de comenzar su reparación.
 *
 * 4. Mostrar el tiempo medio de espera de todos los coches.
 *
 * 5. Permitir que el taller cierre correctamente cuando
 *    todos los coches hayan sido reparados.
 */

public class TallerMecanico {

    private static final int NUM_COCHES = 15;
    private static final int NUM_MECANICOS = 2;
    private static final List<Thread> coches = new ArrayList<>();
    private static final List<Thread> mecanicos = new ArrayList<>();
    private static final int FIN = -1;


    private static final BlockingQueue<Integer> colaCoches =
            new ArrayBlockingQueue<>(NUM_COCHES);

    private static final CountDownLatch reparacionesTerminadas =
            new CountDownLatch(NUM_COCHES);

    private static final AtomicInteger reparaciones =
            new AtomicInteger(0);

    public static void main(String[] args){

        System.out.println("=== TALLER MECÁNICO ===");

        prepararMecanico();
        prepararCoches();

        try {
            reparacionesTerminadas.await();


            // Cerramos los mecánicos
            for (Thread mecanico : mecanicos) {
             mecanico.interrupt();
            }

            // Esperamos a que los mecánicos finalicen
            for (Thread mecanico : mecanicos) {
             mecanico.join();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\n=== RESULTADOS ===");
        System.out.println("Coches reparados: " + reparaciones.get());
        System.out.println("El taller ha terminado.");
    }

    private static void reparar(int idMeca) {

        try {

            while (true){

            int idCoche=colaCoches.take();

            if (idCoche == FIN) {
                    break;
            }

            System.out.println("Coche-" + idCoche
                    + " comienza su reparación y es atendido por Mecanico-"+idMeca);

            int tiempo = ThreadLocalRandom.current()
                    .nextInt(2000, 5001);

            Thread.sleep(tiempo);

                reparaciones.incrementAndGet();
                reparacionesTerminadas.countDown();

            System.out.println("Coche-" + idCoche
                    + " termina su reparación.");
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

        } finally {
            if (Thread.currentThread().isInterrupted()) {
                // No hacemos nada adicional.
            }
        }
    }

    private static void generarCoches() {

        for (int i = 1; i <= NUM_COCHES; i++) {

            System.out.println("Llega Coche-" + i);

            try {
                colaCoches.put(i);
                Thread.sleep(500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
    private static void prepararCoches()  {

        Thread generador = Thread.ofPlatform()
                .name("Generador-Coches")
                .start(() -> generarCoches());

        try {
            generador.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void prepararMecanico(){
        for (int i = 1; i <= NUM_MECANICOS; i++) {

            int id = i;

        Thread mecanico = Thread.ofPlatform()
                .name("mecanico-"+id)
                .start(() -> reparar(id));
            mecanicos.add(mecanico);
        }
    }

}
