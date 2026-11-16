package Parking;

import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Coche implements Runnable {
    private final int id;
    private final TipoCoches tipoCoche;
    private final BlockingQueue<Coche> coches;
    private volatile boolean running;
    private final Semaphore semaforo;
    private final Semaphore semaforoVIP;
    private final int tiempo;
    private int usadosNormal;
    private int usadosVIP;
    private int enCola;
    private AtomicInteger ingresos;
    private final AtomicInteger cochesFallados;



    public Coche(int id, TipoCoches tipoCoche, BlockingQueue<Coche> coches, Semaphore semaforo, Semaphore semaforoVIP, int tiempo, AtomicInteger ingresos, AtomicInteger cochesFallados) {
        this.id = id;
        this.tipoCoche = tipoCoche;
        this.coches = coches;
        this.semaforo = semaforo;
        this.semaforoVIP = semaforoVIP;
        this.tiempo = tiempo;
        this.cochesFallados = cochesFallados;
        this.running = true;
        this.usadosNormal = 20 - semaforo.availablePermits();
        this.usadosVIP = 5 - semaforoVIP.availablePermits();
        this.enCola = coches.size();
        this.ingresos = ingresos;
    }

    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        boolean cocheEnCola = coches.offer(this);

        if (!cocheEnCola) {
            System.out.println(" [" + LocalTime.now().format(formatter) + "]" + id + " se va porque la cola está llena.");
            cochesFallados.addAndGet(1);

        } else {
            System.out.println(" [" + LocalTime.now().format(formatter) + "]" + id + " ha entrado en la cola");
        }

    }

    public void esperar(int tiempo) throws InterruptedException {
        Thread.sleep(2000);
        Thread.sleep(tiempo);
        Thread.sleep(1000);
    }
}