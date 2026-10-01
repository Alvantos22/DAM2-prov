package main.EjercicioHilosConcurrencia;

public class Ejercicio1_1Runnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(200);
                System.out.println(Thread.currentThread().getName()+ " lleva "+i);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static void main() {
        //sin bucle para que se vea que espera a que termine el primero
        Thread t = new Thread(new Ejercicio1_1Runnable());
        Thread t2 = new Thread(new Ejercicio1_1Runnable());
        Thread t3 = new Thread(new Ejercicio1_1Runnable());
        Thread t4 = new Thread(new Ejercicio1_1Runnable());

        t.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
