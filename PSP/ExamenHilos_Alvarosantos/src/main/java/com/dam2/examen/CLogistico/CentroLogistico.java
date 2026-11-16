
package com.dam2.examen.CLogistico;

/*
1. Identificar a los 4 trabajadores.
2. Mostrar qué pedido está preparando cada uno.
3. Contabilizar los pedidos enviados por trabajador.
4. Calcular el tiempo de espera de cada pedido.
5. Calcular el tiempo medio de espera.
6. Dar prioridad a pedidos críticos, urgentes y normales.
7. Evitar que dos trabajadores procesen el mismo pedido.
8. Limitar a 2 el número de trabajadores que preparan pedidos simultáneamente.
9. Mostrar cuántos pedidos críticos, urgentes y normales se han enviado.
10. Cerrar correctamente todos los hilos.
*/

import com.dam2.examen.CLogistico.model.Pedido;
import com.dam2.examen.CLogistico.model.Prioridad;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CentroLogistico {

    private static final int NUM_PEDIDOS = 30;
    private static final int NUM_TRABAJADORES = 4;

    private static final List<Thread> trabajadores = new ArrayList<>();
    private static final List<AtomicInteger> contadores = new ArrayList<>();

    private static final Map<Prioridad, AtomicInteger> prioridades =
            new ConcurrentHashMap<>();

    private static final AtomicLong tiemposEsperaMs = new AtomicLong(0);

    private static final BlockingQueue<Pedido> colaPedidos =
            new PriorityBlockingQueue<>(
                    NUM_PEDIDOS,
                    Comparator.comparing(Pedido::prioridad).reversed()
            );

    private static final CountDownLatch pedidosTerminados =
            new CountDownLatch(NUM_PEDIDOS);

    // Como máximo 2 trabajadores pueden preparar pedidos.
    private static final Semaphore plazasPreparacion = new Semaphore(2);

    private static final AtomicInteger pedidosEnviados = new AtomicInteger(0);

    public static void main(String[] args) {

        System.out.println("=== CENTRO LOGÍSTICO ===");

        inicializarPrioridades();
        prepararTrabajadores();
        prepararPedidos();

        try {
            pedidosTerminados.await();

            for (Thread trabajador : trabajadores) {
                trabajador.interrupt();
            }

            for (Thread trabajador : trabajadores) {
                trabajador.join();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== RESULTADOS ===");
        System.out.println("Pedidos repartidos: " + pedidosEnviados.get());

        for (int i = 0; i < contadores.size(); i++) {
            System.out.println("Trabajador-" + (i + 1)
                    + " ha enviado " + contadores.get(i).get() + " pedidos.");
        }

        for (Prioridad prioridad : Prioridad.values()) {
            System.out.println(prioridad + ": "
                    + prioridades.get(prioridad).get() + " pedidos");
        }

        double media = tiemposEsperaMs.get() / (double) NUM_PEDIDOS;

        System.out.printf("\nTiempo medio de espera: %.2f segundos%n", media / 1000);

        double esperaSegundos = tiemposEsperaMs.get() / 1000.0;

        System.out.printf("\nTiempo de espera Total: %.2f segundos%n", esperaSegundos);

        System.out.println("Centro de Logistica cerrado.");
    }

    private static void inicializarPrioridades() {
        for (Prioridad prioridad : Prioridad.values()) {
            prioridades.put(prioridad, new AtomicInteger(0));
        }
    }


    private static void trabajar(int idTrabajador, AtomicInteger contador) {

        try {
            while (true) {

                plazasPreparacion.acquire();

                try {
                    Pedido pedido = colaPedidos.take();

                    long inicio = System.currentTimeMillis();
                    int idPedido = pedido.id();

                    System.out.println("Trabajador-" + idTrabajador
                            + " coge el pedido-" + idPedido);

                    int tiempo = ThreadLocalRandom.current()
                            .nextInt(1000, 4001);

                    Thread.sleep(tiempo);

                    long espera = inicio - pedido.tiempoLlegada();
                    double esperaSegundos = espera / 1000.0;

                    prioridades.get(pedido.prioridad()).incrementAndGet();

                    tiemposEsperaMs.addAndGet(espera);
                    pedidosEnviados.incrementAndGet();
                    contador.incrementAndGet();
                    pedidosTerminados.countDown();

                    System.out.println("Trabajador-" + idTrabajador
                            + " ha repartido el pedido-" + idPedido);

                    System.out.printf("Espera: %.2f segundos%n", esperaSegundos);

                } finally {
                    plazasPreparacion.release();
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private static void generarPedidos() {

        for (int i = 0; i < NUM_PEDIDOS; i++) {

            System.out.println("Llega Pedido-" + i);

            try {
                Prioridad priorityRandom = Prioridad.values()[
                        ThreadLocalRandom.current().nextInt(0, 3)
                        ];

                long llegada = System.currentTimeMillis();

                colaPedidos.put(new Pedido(i, llegada, priorityRandom));

                Thread.sleep(500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private static void prepararPedidos() {

        Thread generador = Thread.ofPlatform()
                .name("Generador-Pedidos")
                .start(() -> generarPedidos());

        try {
            generador.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void prepararTrabajadores() {

        for (int i = 1; i <= NUM_TRABAJADORES; i++) {

            int id = i;
            AtomicInteger contador = new AtomicInteger(0);

            Thread trabajador = Thread.ofPlatform()
                    .name("Trabajador-" + id)
                    .start(() -> trabajar(id, contador));

            contadores.add(contador);
            trabajadores.add(trabajador);
        }
    }
}
