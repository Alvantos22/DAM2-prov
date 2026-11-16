package Restaurante;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Cocinero implements Runnable {
    private final List<Pedido> pedidos;
    private final BlockingQueue<Pedido> colaPedidos;
    private volatile boolean running;
    private AtomicInteger numPedidosPreparados;

    public Cocinero(List<Pedido> pedidos, BlockingQueue<Pedido> colaPedidos, AtomicInteger numPedidosPreparados) {
        this.pedidos = pedidos;
        this.colaPedidos = colaPedidos;
        this.running = true;
        this.numPedidosPreparados = numPedidosPreparados;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Pedido pedido = colaPedidos.take();
                Thread.sleep(pedido.getTiempoPreparacion());
                numPedidosPreparados.incrementAndGet();
                pedidos.add(pedido);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            System.out.println("Todos los pedidos preparados");
        }
    }
}
