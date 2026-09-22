package Ejercicios;

class ContadorThread extends Thread{
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
public class PrimerHilo {
    static void main() {
        ContadorThread ct =new ContadorThread();
        ContadorThread ct2=new ContadorThread();
        ct.start();
        ct2.start();
    }
}
