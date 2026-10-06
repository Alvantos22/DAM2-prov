package main.EjercicioHilosConcurrencia;


// aplicar log4j en vez de usar sout
public class ClaseThread extends Thread{
    private int numero;

    public ClaseThread(int numero){
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
            ClaseThread hilo = new ClaseThread(i);
            hilo.start();
            hilo.join();
        }
            }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }




    }
}
