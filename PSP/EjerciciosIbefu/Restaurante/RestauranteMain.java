package Restaurante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class RestauranteMain {
    public static void main(String[] args) throws InterruptedException {
        Random rnd = new Random();
        System.out.println("=== RESTAURANTE CONCURRENTE ===");
        System.out.println("Iniciando servicio con 3 cocineros y 5 camareros...\n");
        BlockingQueue<Pedido> colaPedidos = new LinkedBlockingQueue<>(10);
        List<Pedido> listaPedidos = Collections.synchronizedList(new ArrayList<>());
        List<Thread> cocineros = new ArrayList<>();
        List<Thread> clientes = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        AtomicInteger numPedidosPreparados = new AtomicInteger(0);
        AtomicInteger vecesMesaLLena = new AtomicInteger(0);


        for (int i = 0; i < 3; i++) {
            Thread cocinero = new Thread(new Cocinero(listaPedidos,colaPedidos,numPedidosPreparados));
            cocinero.start();
            cocineros.add(cocinero);
        }


        for (int i = 0; i < 100; i++) {
            Thread cliente = new Thread(new Cliente("Cliente " + (i + 1), i + 1, colaPedidos, listaPedidos, vecesMesaLLena));
            cliente.start();
            clientes.add(cliente);
            try{
                Thread.sleep(rnd.nextInt(500));
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        for (Thread cliente: clientes){
            cliente.join();
        }

        for (Thread cocinero : cocineros){
            cocinero.interrupt();
        }
        for (Thread cocinero : cocineros){
            cocinero.join();
        }

        System.out.println();
        System.out.println();
        System.out.println("=== ESTADISTICAS FINALES ===");
        System.out.println();
        System.out.println("Cantidad de clientes atendidos/potenciales: " + clientes.size());
        System.out.println("Cantidad de pedidos preparados: " + numPedidosPreparados.get()+ "/100");
        System.out.println("Efectividad: " + (numPedidosPreparados.get()*100/100) + "%");
        System.out.println("Tiempo total de ejecucion: " + (System.currentTimeMillis() - startTime)/1000 + "s");
        System.out.println("Promedio de ejecucion por cliente: " + (System.currentTimeMillis() - startTime)/numPedidosPreparados.get()/1000 + " s");
        System.out.println("Cantidad de veces que la mesa estuvo llena: " + vecesMesaLLena.get());



    }
}