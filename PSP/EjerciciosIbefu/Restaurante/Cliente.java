package Restaurante;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Collections;
import java.util.ArrayList;

public class Cliente implements Runnable {
    private final BlockingQueue<Pedido> colaPedidos;
    private final List<Pedido> ListaPedidos;
    private final String nombre;
    private final int id;
    private volatile boolean running;
    private AtomicInteger contPedidos;
    public static final List<Long> tiemposEspera = Collections.synchronizedList(new ArrayList<>());

    public Cliente(String nombre, int id, BlockingQueue<Pedido> colaPedidos, List<Pedido> listaPedidos, AtomicInteger contPedidos) {
        this.nombre = nombre;
        this.id = id;
        this.colaPedidos = colaPedidos;
        this.ListaPedidos = listaPedidos;
        this.contPedidos = contPedidos;
        this.running = true;
    }

    @Override
    public void run() {

        TipoPlato tipo = TipoPlato.values()[ThreadLocalRandom.current().nextInt(TipoPlato.values().length)];
        Pedido pedido = new Pedido(tipo, id);
        boolean pedidoEnviado = colaPedidos.offer(pedido);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        if (!pedidoEnviado) {
            System.out.println(" [" + LocalTime.now().format(formatter) + "]" + nombre + " se va porque la cola está llena.");
            return;
        }
        System.out.println(" [" + LocalTime.now().format(formatter) + "]" + nombre + " ha realizado un pedido: " + pedido.getNombre() + " id: " + pedido.getId());
        contPedidos.getAndIncrement();
        long tiempoPedido = System.currentTimeMillis();

        while (running) {
            Pedido recibido = null;
            recibido = ListaPedidos.stream().filter(p -> p.getId() == pedido.getId()).findFirst().orElse(null);
            if (recibido != null) {
                long tiempoRecibido = System.currentTimeMillis();
                long espera = tiempoRecibido - tiempoPedido;
                tiemposEspera.add(espera);
                System.out.println(" [" + LocalTime.now().format(formatter) + "]" + nombre + " ha recibido su pedido: " + recibido.getNombre() + " id: " +recibido.getId());
                ListaPedidos.remove(recibido);
                running = false;
            }
            if (running) {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }
    }
}
