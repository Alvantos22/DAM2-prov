package Ejercicios;

class ContadorThreadRunnable implements Runnable{
    @Override
    public void run(){
        for (int i = 1; i <11 ; i++) {
            System.out.println("hilo Nº " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
public class PrimerHiloRunnable {
    static void main() {
        Thread ct =new Thread(new ContadorThreadRunnable());
        Thread ct2 =new Thread(new ContadorThreadRunnable());
        ct.start();
        ct2.start();
    }
}
