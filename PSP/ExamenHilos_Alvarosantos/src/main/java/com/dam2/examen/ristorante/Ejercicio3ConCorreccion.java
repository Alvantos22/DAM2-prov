
package com.dam2.examen.ristorante;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;


public class Ejercicio3ConCorreccion {

    // CONSTANTES
    static final int COCINEROS = 3;
    static final int CAMAREROS = 5;
    static final int CLIENTES = 100;

    static final int CAPACIDAD_MESA = 10;

    static final long LLEGADA_CLIENTE_MS = 500;
    static final long TIEMPO_CAMARERO_MS = 1000;

    static final int NUM_GRUPOS = 25;
    static final int PERSONAS_POR_GRUPO = 4;

    static final long TIEMPO_COMER_MS = 3000;
    static final long ESPERA_MESA_MS = 5000;

    // RECURSOS COMPARTIDOS
    static final BlockingQueue<Pedido> colaCocina =
             new ArrayBlockingQueue<>(CAPACIDAD_MESA);

    static final BlockingQueue<Pedido> colaClientes =
            new ArrayBlockingQueue<>(CLIENTES);

    static final CountDownLatch clientesAtendidos =
            new CountDownLatch(CLIENTES);

    static final CountDownLatch gruposTerminados =
            new CountDownLatch(NUM_GRUPOS);

    static final BlockingQueue<Pedido> colaPlatosListos =
            new ArrayBlockingQueue<>(CLIENTES);
    
    static final Semaphore plazas =
            new Semaphore(CAPACIDAD_MESA, true);

    static final List<Thread> hilosCocineros = new ArrayList<>();
    static final List<Thread> hilosCamareros = new ArrayList<>();

    // ESTADISTICAS
    static final AtomicLong ingresosCentimos = new AtomicLong();
    static final AtomicInteger platosServidos = new AtomicInteger();
    static final AtomicLong esperaTotalMs = new AtomicLong();
    static final AtomicInteger mesaLlenaCount = new AtomicInteger();
    static final AtomicLong cocinerosEsperandoMs = new AtomicLong();

    // HORA ACTUAL
    static String ahora() {
        return LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss")
        );
    }

    //INICIAR COCINERO

    static void iniciarCocineros() {

        for (int i = 1; i <= COCINEROS; i++) {

            final int id = i;

            Thread cocinero = Thread.ofPlatform()
                    .name("cocinero-" + id)
                    .start(() -> {
                        try {
                            while (true) {

                                // 1. Recoger un pedido
                                Pedido p = colaCocina.take();

                                // 2. Cocinar
                                Thread.sleep(p.plato().getTiempoMs());

                                // 3. Actualizar estadísticas
                                esperaTotalMs.addAndGet(
                                        System.currentTimeMillis() - p.horaCreacionMs()
                                );

                                platosServidos.incrementAndGet();

                               //YA NO AVISA AL GRUPO, AHORA METE EL PEDIDO EN LA COLA
                                colaPlatosListos.put(p);

                                System.out.println(
                                        "[" + ahora() + "] Cocinero-" + id
                                                + " termina " + p.plato()
                                                + " para Cliente-" + p.cliente()
                                );
                            }

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();

                            System.out.println(
                                    "Cocinero-" + id + " termina su turno."
                            );
                        }
                    });

            hilosCocineros.add(cocinero);
        }
    }

//INICIAR CAMARERO

    private static void iniciarCamareros() {

        for (int i = 1; i <= CAMAREROS; i++) {

            int id = i;

            Thread camarero = Thread.ofPlatform()
                    .name("Camarero-" + id)
                    .start(() -> {

                        // Pedido que el camarero ya ha recogido,
                        // pero todavía no ha metido en cocina.
                        Pedido pendiente = null;

                        try {
                            while (true) {

                                // 1. Prioridad absoluta: entregar platos listos.
                                Pedido listo = colaPlatosListos.poll();

                                if (listo != null) {

                                    System.out.println(ahora()
                                            + " Camarero-" + id
                                            + " entrega " + listo.plato()
                                            + " al cliente " + listo.cliente());

                                    ingresosCentimos.addAndGet(
                                            Math.round(listo.plato().getPrecio() * 100)
                                    );

                                    listo.grupo().platoTerminado();

                                    continue;
                                }

                                // 2. Si tenemos un pedido pendiente,
                                // lo metemos en cocina.
                                if (pendiente != null) {

                                    if (colaCocina.remainingCapacity() == 0) {
                                        mesaLlenaCount.incrementAndGet();
                                    }

                                    colaCocina.put(pendiente);

                                    pendiente = null;

                                    continue;
                                }

                                // 3. Si no hay platos ni pedidos pendientes,
                                // recogemos un pedido nuevo.
                                Pedido nuevo = colaClientes.poll(
                                        100, TimeUnit.MILLISECONDS
                                );

                                if (nuevo != null) {

                                    Thread.sleep(TIEMPO_CAMARERO_MS);

                                    System.out.println(ahora()
                                            + " Camarero-" + id
                                            + " toma pedido del cliente "
                                            + nuevo.cliente());

                                    pendiente = nuevo;
                                }
                            }

                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.out.println("Camarero-" + id
                                    + " termina su turno.");
                        }
                    });

            hilosCamareros.add(camarero);
        }
    }




    static void iniciarGrupos() {

        for (int i = 1; i <= NUM_GRUPOS; i++) {

            final int id = i;

            Thread.ofVirtual().name("grupo-" + id).start(() -> {

                Grupo grupo = new Grupo(id, PERSONAS_POR_GRUPO);
                boolean tieneMesa = false;

                try {

                    // 1. Intentar conseguir mesa
                    tieneMesa = plazas.tryAcquire(
                            PERSONAS_POR_GRUPO,
                            ESPERA_MESA_MS,
                            TimeUnit.MILLISECONDS
                    );

                    if (!tieneMesa) {
                        System.out.println(
                                "Grupo-" + id + " se marcha: no hay mesa."
                        );
                        return;
                    }

                    System.out.println(
                            "[" + ahora() + "] Grupo-" + id
                                    + " consigue mesa para "
                                    + PERSONAS_POR_GRUPO + " personas."
                    );

                    // 2. Cada persona realiza un pedido
                    Random random = new Random();

                    for (int j = 1; j <= PERSONAS_POR_GRUPO; j++) {

                        int idCliente =
                                (id - 1) * PERSONAS_POR_GRUPO + j;

                        TipoPlato plato = TipoPlato.values()[
                                random.nextInt(TipoPlato.values().length)
                                ];

                        Pedido pedido = new Pedido(
                                idCliente,
                                plato,
                                System.currentTimeMillis(),
                                grupo
                        );

                        colaClientes.put(pedido);

                        System.out.println(
                                "Grupo-" + id + ": Cliente-" + idCliente
                                        + " pide " + plato
                        );
                    }

                    // 3. Esperar a que se entreguen todos los platos
                    grupo.esperarPlatos();

                    // 4. Comer
                    System.out.println(
                            "Grupo-" + id + " ya tiene todos sus platos. ¡A comer!"
                    );

                    Thread.sleep(TIEMPO_COMER_MS);

                    System.out.println(
                            "Grupo-" + id + " termina de comer."
                    );

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                } finally {

                    // 5. Liberar las plazas
                    if (tieneMesa) {
                        plazas.release(PERSONAS_POR_GRUPO);

                        System.out.println(
                                "Grupo-" + id + " libera su mesa."
                        );
                    }

                    gruposTerminados.countDown();
                }
            });

            try {
                Thread.sleep(LLEGADA_CLIENTE_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private static void cerrarTrabajadores()  {

        // Interrumpimos a todos los camareros
        for (Thread camarero : hilosCamareros) {
            camarero.interrupt();
        }

        // Interrumpimos a todos los cocineros
        for (Thread cocinero : hilosCocineros) {
            cocinero.interrupt();
        }

        // Esperamos a que terminen los camareros
        for (Thread camarero : hilosCamareros) {
            try {
                camarero.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Esperamos a que terminen los cocineros
        for (Thread cocinero : hilosCocineros) {
            try {
                cocinero.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Todos los trabajadores han terminado.");
    }




    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== RESTAURANTE CONCURRENTE ===");

        iniciarCocineros();
        iniciarCamareros();

        iniciarGrupos();

        // Esperar a que terminen todos los grupos
        gruposTerminados.await();

        System.out.println("Todos los grupos han terminado.");

        cerrarTrabajadores();

        System.out.println("\n--- INGRESOS DEL RESTAURANTE ---");

        System.out.println(
                "Dinero recaudado: "
                        + String.format("%.2f €", ingresosCentimos.get() / 100.0)
        );
    }
}
