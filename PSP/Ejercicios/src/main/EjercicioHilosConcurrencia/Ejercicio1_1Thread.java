package main.EjercicioHilosConcurrencia;


// aplicar log4j en vez de usar sout
public class Ejercicio1_1Thread extends Thread{
    private int numero;

    public Ejercicio1_1Thread(int numero){
    this.numero=numero;
    }

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
        try {
        for (int i = 1; i <= 4; i++) {
            Ejercicio1_1Thread hilo = new Ejercicio1_1Thread(i);
            hilo.start();
            hilo.join();
        }
            }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }




    }
}
