package main.EjercicioHilosConcurrencia;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class EjReentrantLock implements Runnable {
    private final ReentrantLock lock = new ReentrantLock();
    private int contador = 0;

    @Override
    public void run() {
        while (true) {
            try {
                if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
                    try {
                        contador++;
                        System.out.println("Incrementado: " + Thread.currentThread().getName());
                        Thread.sleep(1000);
                        return;
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("Ocupado, lo intento más tarde");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    void main(){

        EjReentrantLock ejercicio = new EjReentrantLock();
        Thread[] hilos = new Thread[100];

        for (int i = 0; i < hilos.length; i++) {
            hilos[i] = new Thread(ejercicio);
            hilos[i].start();
        }
        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Contador final: " + ejercicio.contador);
    }
}