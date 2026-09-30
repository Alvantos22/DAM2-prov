package EjerciciosConcurrencia;

public class Ejercicio1_Thread extends Thread {
    public Thread t = new Thread();
    @Override
    public void run() {
        for (int cont = 1; cont < 4; cont++) {


            for (int i = 1; i < 101; i++) {

                if (i == 25 || i == 50 || i == 75 || i == 100) {

                    System.out.println("Hilo" + cont + " porcentaje= ");
                }
            }
        }
        }
    }

     void main() {
    }

