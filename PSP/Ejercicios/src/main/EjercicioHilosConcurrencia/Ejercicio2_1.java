package main.EjercicioHilosConcurrencia;



public class Ejercicio2_1 implements Runnable{
    @Override
    public void run() {
        for (int i = 10; i > 0; i--) {
            try {
            System.out.println(i);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void main(){
        try {
        Thread cuenta = new Thread(new Ejercicio2_1());
        cuenta.start();
        cuenta.join();
        System.out.println("<==DESPEGADO==>");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
