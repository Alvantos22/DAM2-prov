package main;


import java.util.concurrent.Semaphore;
class AccesoVentanillas implements Runnable{
    private final Semaphore semaforo;
    private final int id;

    AccesoVentanillas(Semaphore semaforo, int id) {
        this.semaforo = semaforo;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            System.out.println("Hilo "+id+" esta ESPERANDO en ventanilla");
            semaforo.acquire();
            System.out.println("Hilo "+id+" esta EN ventanilla");
            Thread.sleep(2000);
            System.out.println("Hilo "+id+" esta FUERA ventanilla");
            semaforo.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
public class Main {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(1);

        for (int i = 1; i < 9; i++) {
            Thread t = new Thread(new AccesoVentanillas(semaphore,i));
            t.start();
        }
    }
}
