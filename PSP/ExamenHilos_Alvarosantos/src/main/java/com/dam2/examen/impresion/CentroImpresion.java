
package com.dam2.examen.impresion;


/*
 * MEJORAS A IMPLEMENTAR:
 *
 * 1. Identificar cada impresora (Impresora-1, Impresora-2...).
 *
 * 2. Mostrar qué documento está imprimiendo cada impresora.
 *
 * 3. Mostrar cuántos documentos ha impreso cada impresora.
 *
 * 4. Calcular cuánto tiempo espera cada documento
 *    desde que llega hasta que comienza a imprimirse.
 *
 * 5. Calcular el tiempo medio de espera de los documentos.
 *
 * 6. Dar prioridad a los documentos urgentes frente
 *    a los documentos normales.
 *
 * 7. Evitar que dos impresoras impriman el mismo documento.
 *
 * 8. Cerrar correctamente las impresoras cuando
 *    todos los documentos hayan terminado.
 */





import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CentroImpresion {

    private static final int NUM_DOCUMENTOS = 20;
    private static final int NUM_IMPRESORAS = 3;

    private static final List<Thread> impresoras = new ArrayList<>();
    private static final List<AtomicInteger> contadores = new ArrayList<>();

    private static final BlockingQueue<Documento> colaDocumentos =
            new PriorityBlockingQueue<>(
                    NUM_DOCUMENTOS,
                    Comparator.comparing(Documento::urgente).reversed());

    private static final CountDownLatch documentosTerminados=
            new CountDownLatch(NUM_DOCUMENTOS);

    private static final AtomicInteger documentosImpresos =
            new AtomicInteger(0);
    private static final AtomicLong tiemposEsperaMs =
            new AtomicLong(0);

    public static void main(String[] args) {

        System.out.println("=== CENTRO DE IMPRESIÓN ===");

        prepararImpresoras();
        prepararDocumentos();

        try {
            documentosTerminados.await();

                for (Thread impresora:impresoras){
                    impresora.interrupt();
                }

                for (Thread impresora:impresoras){
                    impresora.join();
                }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== RESULTADOS ===");

        System.out.println("Documentos impresos: "
                + documentosImpresos.get());

        for (int i = 0; i < contadores.size(); i++) {
            System.out.println("Impresora-" + (i + 1)
                    + " ha impreso " + contadores.get(i).get() + " documentos.");
        }

        double media = tiemposEsperaMs.get() / (double) NUM_DOCUMENTOS;

        System.out.printf("\nTiempo medio de espera: %.2f segundos%n", media / 1000);

        System.out.println("Centro de impresión cerrado.");
    }

    private static void imprimir(int idImpresora, AtomicInteger contador) {

        try {

            while (true) {

                Documento documento=colaDocumentos.take();
                long inicio = System.currentTimeMillis();
                int idDocumento = documento.id();
                // Aquí trabaja la impresora.

                System.out.println("Impresora-" + idImpresora
                        + " comienza a imprimir Documento-" + idDocumento);

                int tiempo = ThreadLocalRandom.current()
                        .nextInt(1000, 4001);

                Thread.sleep(tiempo);
                long espera = inicio - documento.horaLlegada();
                double esperaSegundos = espera / 1000.0;
                tiemposEsperaMs.addAndGet(espera);



                documentosImpresos.incrementAndGet();
                contador.incrementAndGet();
                documentosTerminados.countDown();

                System.out.println("Impresora-" + idImpresora
                        + " termina Documento-" + idDocumento );
                System.out.printf("Espera: %.2f segundos%n", esperaSegundos);


            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

        }
    }

    private static void generarDocumentos() {


        for (int i = 1; i <= NUM_DOCUMENTOS; i++) {

            System.out.println("Llega Documento-" + i);
            try {
                boolean urgente = i % 5 == 0;
                long llegada = System.currentTimeMillis();
                colaDocumentos.put(new Documento(i, llegada,urgente));


                Thread.sleep(500);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private static void prepararDocumentos() {

        Thread generador = Thread.ofPlatform()
                .name("Generador-Documentos")
                .start(() -> generarDocumentos());

        try {

            generador.join();


        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }

    private static void prepararImpresoras() {

        for (int i = 1; i <= NUM_IMPRESORAS; i++) {

            int id = i;
            AtomicInteger contador = new AtomicInteger(0);
            Thread impresora = Thread.ofPlatform()
                    .name("Impresora-" + id)
                    .start(() ->
                            imprimir(id,contador)
                    );


            contadores.add(contador);
            impresoras.add(impresora);
        }
    }




}
