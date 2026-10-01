package main.EjercicioHilosConcurrencia;


// aplicar log4j en vez de usar sout
public class Ejercicio1_1Thread extends Thread{
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
        Ejercicio1_1Thread t1 =new Ejercicio1_1Thread();
        Ejercicio1_1Thread t2 =new Ejercicio1_1Thread();
        Ejercicio1_1Thread t3 =new Ejercicio1_1Thread();
        Ejercicio1_1Thread t4 =new Ejercicio1_1Thread();

        try {
            t1.start();
            t1.join();
            t2.start();
            t2.join();
            t3.start();
            t3.join();
            t4.start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }




    }
}
