package main.EjercicioHilosConcurrencia;

import java.util.ArrayList;
import java.util.List;

public class EjHiloVirtual {
     static void main() {

         List<Thread> hilos = new ArrayList<>();

         for (int i = 1; i <= 10000; i++) {

             int numero = i;

             Thread hilo = Thread.startVirtualThread(() -> {
                 try {
                     Thread.sleep(200);
                 } catch (InterruptedException e) {
                     Thread.currentThread().interrupt();
                 }

                 System.out.println("hilo " + numero);
             });

             hilos.add(hilo);
         }

         for (Thread hilo : hilos) {
             try {
                 hilo.join();
             } catch (InterruptedException e) {
                 throw new RuntimeException(e);
             }
         }
     }
}
