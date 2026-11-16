package Parking;

import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class AparcaCoches implements Runnable {
    private final BlockingQueue<Coche> colaCoches;

    private final Semaphore semaforo;
    private final Semaphore semaforoVIP;
    private volatile boolean running;
    private final AtomicInteger ingresos;
    private final AtomicInteger cantCoches;
    private final AtomicInteger cochesFallados;
    private final AtomicInteger cochesExito;

    public AparcaCoches(BlockingQueue<Coche> colaCoches,
                        Semaphore semaforo, Semaphore semaforoVIP, AtomicInteger cochesFallados, AtomicInteger cochesExito) {
        this.colaCoches = colaCoches;
        this.semaforo = semaforo;
        this.semaforoVIP = semaforoVIP;
        this.cochesFallados = cochesFallados;
        this.cochesExito = cochesExito;
        this.running = true;
        this.ingresos = new AtomicInteger(0);
        this.cantCoches = new AtomicInteger(0);
    }


    private String barraVisual(int usados, int total, int longitud) {
        int llenas = (int) Math.round((double) usados / total * longitud);
        int vacias = longitud - llenas;
        return "█".repeat(llenas) + "░".repeat(vacias);
    }

    private void procesarEntrada(Semaphore semaforoActual, DateTimeFormatter formatter, Coche coche) {
        cochesExito.addAndGet(1);
        System.out.println("[" + LocalTime.now().format(formatter) + "] " + coche.getId() +
                " (" + coche.getTipoCoche() + ") ha entrado al parking.");


        new Thread(() -> {
            try {
                cantCoches.incrementAndGet();
                coche.esperar(coche.getTiempo());
                int pago = (int) (coche.getTipoCoche().getTarifaPorMinuto() * coche.getTiempo() / 1000);
                ingresos.addAndGet(pago);

                System.out.println("[" + LocalTime.now().format(formatter) + "] " + coche.getId() +
                        " ha salido del parking y ha pagado " + pago + "€");

                semaforoActual.release();
                synchronized (AparcaCoches.this) {
                    AparcaCoches.this.notify();
                    System.out.println("ME HE DESPERTADO JEJEJEJEJEJEJE");
                }
                cantCoches.decrementAndGet();
                mostrarEstadisticas(ingresos.get());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        mostrarEstadisticas(ingresos.get());
    }

    public void mostrarEstadisticas(int ingresos) {
        int usadosNormal = 20 - semaforo.availablePermits();
        int usadosVIP = 5 - semaforoVIP.availablePermits();
        int enCola = colaCoches.size();

        String barraN = barraVisual(usadosNormal, 20, 20);
        String barraV = barraVisual(usadosVIP, 5, 20);
        String barraC = barraVisual(enCola, 10, 20);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        System.out.println("\n=== 🅿️  PARKING INTELIGENTE ===");
        System.out.println("Hora actual: [" + LocalTime.now().format(formatter) + "]");
        System.out.println("┌──────────────────────────────────────────┐");
        System.out.printf("│ NORMALES:      %s %2d/20│%n", barraN, usadosNormal);
        System.out.printf("│ VIP:           %s %2d/5 │%n", barraV, usadosVIP);
        System.out.printf("│ COLA DE ESPERA:%s %2d/10│%n", barraC, enCola);
        System.out.printf("│ INGRESOS HOY: %8.0f€                  │%n", (double) ingresos);
        System.out.println("└──────────────────────────────────────────┘\n");
    }
    public AtomicInteger getCantCoches() {
        return cantCoches;
    }

    @Override
    public void run() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        while (running) {
            try {
                Coche coche = colaCoches.take();
                TipoCoches tipoCoche = coche.getTipoCoche();

                boolean haEntrado = false;
                Semaphore semaforoActual = null;

                if (tipoCoche == TipoCoches.VIP) {
                    if (semaforoVIP.tryAcquire()) {
                        haEntrado = true;
                        semaforoActual = semaforoVIP;
                    } else if (semaforo.tryAcquire()) {
                        haEntrado = true;
                        semaforoActual = semaforo;
                    }
                } else {
                    if (semaforo.tryAcquire()) {
                        haEntrado = true;
                        semaforoActual = semaforo;
                    }
                }

                if (haEntrado && semaforoActual != null) {
                    procesarEntrada(semaforoActual, formatter, coche);
                } else {
                    if(!colaCoches.offer(coche)){
                        cochesFallados.addAndGet(1);
                    }
                }
                if(semaforo.availablePermits() == 0 && semaforoVIP.availablePermits() == 0 ){
                    synchronized (this) {
                        System.out.println("ME HE DORMIDO JEJJEJEJEJE");
                        this.wait();
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }
}