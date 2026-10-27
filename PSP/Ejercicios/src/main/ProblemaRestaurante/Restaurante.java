package main.ProblemaRestaurante;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class Restaurante {

    static final int NUM_COCINEROS = 3;
    static final int NUM_CAMAREROS = 5;
    static final int NUM_CLIENTES = 100;
    static final int CAPACIDAD_MESA_PEDIDOS = 10;
    static final int ASIENTOS = 15;
    static final int LLEGADA_CLIENTES_MS = 500;
    static final int TOMAR_PEDIDO_MS = 1000;
    static final int COMER_MS = 1500;

    enum TipoPlato {
        ENSALADA(2000, 6),
        PASTA(3000, 10),
        PIZZA(4000, 12),
        CARNE(5000, 18);

        final int tiempoMs;
        final int precio;

        TipoPlato(int tiempoMs, int precio) {
            this.tiempoMs = tiempoMs;
            this.precio = precio;
        }
    }

    static class Pedido {
        final int clienteId;
        final TipoPlato tipo;
        final CountDownLatch servido = new CountDownLatch(1);

        Pedido(int clienteId, TipoPlato tipo) {
            this.clienteId = clienteId;
            this.tipo = tipo;
        }
    }

    static final BlockingQueue<Pedido> colaClientes = new LinkedBlockingQueue<>();
    static final BlockingQueue<Pedido> mesaPedidos = new ArrayBlockingQueue<>(CAPACIDAD_MESA_PEDIDOS);
    static final BlockingQueue<Pedido> platosListos = new LinkedBlockingQueue<>();
    static final Semaphore asientos = new Semaphore(ASIENTOS, true);
    static final CountDownLatch clientesTerminados = new CountDownLatch(NUM_CLIENTES);

    static final LongAdder ganancias = new LongAdder();
    static final LongAdder esperaClientesMs = new LongAdder();
    static final LongAdder esperaCocinerosNs = new LongAdder();
    static final LongAdder cocinaOcupadaMs = new LongAdder();
    static final AtomicInteger mesaLlenaVeces = new AtomicInteger();
    static final AtomicInteger platosServidos = new AtomicInteger();
    static final AtomicInteger clientesAtendidos = new AtomicInteger();

    static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    static synchronized void log(String msg) {
        System.out.println("[" + LocalTime.now().format(FMT) + "] " + msg);
    }

    static void camarero(int id) {
        String nombre = "Camarero-" + id;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Pedido listo = platosListos.poll();
                if (listo != null) {
                    log(nombre + " sirve " + listo.tipo + " a Cliente-" + String.format("%03d", listo.clienteId));
                    listo.servido.countDown();
                    platosServidos.incrementAndGet();
                    continue;
                }

                Pedido pedido = colaClientes.poll(100, TimeUnit.MILLISECONDS);
                if (pedido == null) continue;

                Thread.sleep(TOMAR_PEDIDO_MS);
                log(nombre + " toma pedido " + pedido.tipo);

                if (!mesaPedidos.offer(pedido)) {
                    mesaLlenaVeces.incrementAndGet();
                    log(nombre + " ESPERA: mesa de pedidos llena");
                    mesaPedidos.put(pedido);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void cocinero(int id) {
        String nombre = "Cocinero-" + id;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                long t0 = System.nanoTime();
                Pedido p = mesaPedidos.take();
                esperaCocinerosNs.add(System.nanoTime() - t0);

                log(nombre + " empieza " + p.tipo + " para Cliente-" + String.format("%03d", p.clienteId));
                Thread.sleep(p.tipo.tiempoMs);
                cocinaOcupadaMs.add(p.tipo.tiempoMs);

                log(nombre + " termina " + p.tipo + " para Cliente-" + String.format("%03d", p.clienteId));
                platosListos.put(p);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void cliente(int id, TipoPlato tipo) {
        String nombre = "Cliente-" + String.format("%03d", id);
        long llegada = System.currentTimeMillis();
        try {
            asientos.acquire();
            log(nombre + " se sienta y pide " + tipo);

            Pedido pedido = new Pedido(id, tipo);
            colaClientes.put(pedido);
            pedido.servido.await();

            esperaClientesMs.add(System.currentTimeMillis() - llegada);
            Thread.sleep(COMER_MS);
            ganancias.add(tipo.precio);
            clientesAtendidos.incrementAndGet();
            log(nombre + " paga " + tipo.precio + "€ y se va");

            asientos.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            clientesTerminados.countDown();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== RESTAURANTE CONCURRENTE ===");
        System.out.println("Iniciando servicio con " + NUM_COCINEROS + " cocineros y "
                + NUM_CAMAREROS + " camareros...\n");

        long inicio = System.currentTimeMillis();

        Thread[] personal = new Thread[NUM_COCINEROS + NUM_CAMAREROS];
        int k = 0;

        for (int i = 1; i <= NUM_COCINEROS; i++) {
            final int id = i;
            personal[k++] = Thread.ofPlatform().name("Cocinero-" + id).start(() -> cocinero(id));
        }

        for (int i = 1; i <= NUM_CAMAREROS; i++) {
            final int id = i;
            personal[k++] = Thread.ofPlatform().name("Camarero-" + id).start(() -> camarero(id));
        }

        ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "monitor");
            t.setDaemon(true);
            return t;
        });

        monitor.scheduleAtFixedRate(() -> log(String.format(
                        "--- EN VIVO: mesaPedidos=%d/%d | clientes esperando camarero=%d | platos listos=%d | "
                                + "atendidos=%d/%d | ganancias=%d€ ---",
                        mesaPedidos.size(), CAPACIDAD_MESA_PEDIDOS, colaClientes.size(), platosListos.size(),
                        clientesAtendidos.get(), NUM_CLIENTES, ganancias.sum())),
                5, 5, TimeUnit.SECONDS);

        Random rnd = new Random();
        TipoPlato[] tipos = TipoPlato.values();

        for (int i = 1; i <= NUM_CLIENTES; i++) {
            final int id = i;
            final TipoPlato tipo = tipos[rnd.nextInt(tipos.length)];
            Thread.ofVirtual().name("Cliente-" + id).start(() -> cliente(id, tipo));
            try {
                Thread.sleep(LLEGADA_CLIENTES_MS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            clientesTerminados.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (Thread t : personal) t.interrupt();
        for (Thread t : personal) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        monitor.shutdownNow();

        long totalMs = System.currentTimeMillis() - inicio;
        double espera = esperaClientesMs.sum() / 1000.0 / Math.max(1, clientesAtendidos.get());
        double eficiencia = 100.0 * cocinaOcupadaMs.sum() / ((double) totalMs * NUM_COCINEROS);

        System.out.println("\n--- ESTADÍSTICAS FINALES ---");
        System.out.printf("Clientes atendidos: %d/%d %s%n", clientesAtendidos.get(), NUM_CLIENTES,
                clientesAtendidos.get() == NUM_CLIENTES ? " Buen trabajo" : " Mierdon de servicio");
        System.out.println("Platos servidos: " + platosServidos.get());
        System.out.printf("Ganancias del día: %d€%n", ganancias.sum());
        System.out.printf("Tiempo promedio de espera: %.1fs%n", espera);
        System.out.println("Mesa llena (veces): " + mesaLlenaVeces.get());
        System.out.printf("Cocineros esperando (tiempo): %ds total%n",
                esperaCocinerosNs.sum() / 1_000_000_000L);
        System.out.printf("Eficiencia: %.0f%%%n", eficiencia);
    }
}
