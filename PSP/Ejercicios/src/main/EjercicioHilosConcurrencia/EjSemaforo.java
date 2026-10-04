package main.EjercicioHilosConcurrencia;


import java.util.concurrent.Semaphore;

public class EjSemaforo implements Runnable {
    private Semaphore semaforo;
//    private int id;

//    public EjSemaforo(int id) {
//        this.id = id;
//    }

    public EjSemaforo(Semaphore semaforo) {
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        try {
            if (!semaforo.tryAcquire()){
                System.out.println("Coche esperando, no hay espacio");
                semaforo.acquire();
            }

            System.out.println("coche  entra");
            Thread.sleep((int) (Math.random() * 7000));

            System.out.println("coche  sale");
            semaforo.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    static void main() {
        Semaphore semaforo = new Semaphore(3);
        for (int i = 0; i < 10; i++) {
            Thread coche = new Thread(new EjSemaforo(semaforo));
            coche.start();
        }

    }
}


