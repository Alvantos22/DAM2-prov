package main.EjercicioHilosConcurrencia;

public class Ejercicio1_1T extends Thread{
    @Override
     public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(200);
                System.out.println(Thread.currentThread().getName()+ " lleva "+i);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage()); // aplicar log4j
            }
        }
    }

    static void main() {
        Ejercicio1_1T t1 =new Ejercicio1_1T();
        Ejercicio1_1T t2 =new Ejercicio1_1T();
        Ejercicio1_1T t3 =new Ejercicio1_1T();
        Ejercicio1_1T t4 =new Ejercicio1_1T();
        t1.start();
        t2.start();
        t3.start();
        t4.start();



    }
}
