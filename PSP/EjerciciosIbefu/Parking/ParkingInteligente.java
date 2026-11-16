package Parking;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingInteligente {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Coche> colaCoches = new LinkedBlockingQueue<>(10);
        Semaphore semaforo = new Semaphore(20, true);
        Semaphore semaforoVIP = new Semaphore(5, true);
        int tiempo;
        long startTime = System.currentTimeMillis();
        List<Thread> coches = new ArrayList<>();
        AtomicInteger ingresos = new AtomicInteger(0);
        TipoCoches tipo;
        AtomicInteger cocheExito = new AtomicInteger(0);
        AtomicInteger cochesFallados = new AtomicInteger(0);

        AparcaCoches aparca = new AparcaCoches(colaCoches,semaforo, semaforoVIP,cochesFallados,cocheExito);
        Thread aparcaCoches = new Thread (aparca);
        aparcaCoches.start();


        for(int i = 0; i < 200; i++){
            Random rnd = new Random();
            tiempo = rnd.nextInt(10000,30000);
            int dado = rnd.nextInt(1,10);
            if(dado < 4){
                tipo = TipoCoches.NORMAL;
            }else{
                tipo = TipoCoches.VIP;
            }
            Thread coche = new Thread(new Coche(i, tipo,colaCoches,semaforo,semaforoVIP,tiempo,ingresos,cochesFallados));
            coche.start();

            coches.add(coche);
            try{
                Thread.sleep(rnd.nextInt(500));
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        for(Thread coche : coches){
            coche.join();
        }

        while (!colaCoches.isEmpty() || aparca.getCantCoches().get() > 0) {
            Thread.sleep(500);
        }



        System.out.println("\n=== RESUMEN DEL DÍA ===");
        System.out.println("Total ingresos: " + ingresos.get() + "€");
        System.out.println("Vehiculos totales: " + coches.size());
        System.out.println("Vehículos aparcados con éxito: " + cocheExito.get() + "/" + coches.size());
        System.out.println("Vehículos que se marcharon (cola llena): " + cochesFallados.get() + "/" + coches.size());
        System.out.printf("Tiempo promedio de estancia: %.2f s%n",
                (System.currentTimeMillis() - startTime) / (coches.size() * 1000.0));

    }
}