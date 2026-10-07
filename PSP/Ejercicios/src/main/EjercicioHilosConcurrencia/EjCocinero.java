package main.EjercicioHilosConcurrencia;

public class EjCocinero implements Runnable {
    @Override
    public void run() {
        System.out.println(" Estado del cocinero: "+Thread.currentThread().getState());
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Cocinando... ->"+i);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("El cocinero ha sido interrumpido");
            }

        }
    }

    void main(){
        Thread cocinero = new Thread(new EjCocinero());

        try {
            System.out.println(" \n Estado inicial del cocinero: "+cocinero.getState());
            cocinero.start();
            Thread.sleep(2000);
            System.out.println("Estado cocinero: "+cocinero.getState());
            Thread.sleep(2000);
            cocinero.interrupt();
            cocinero.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Estado cocinero: "+cocinero.getState());

        }

    }
}
