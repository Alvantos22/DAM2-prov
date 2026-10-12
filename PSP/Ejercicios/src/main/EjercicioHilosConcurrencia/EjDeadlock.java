package main.EjercicioHilosConcurrencia;

public class EjDeadlock implements Runnable{
    private int saldo;
    //private int id; id comentado para demostrar deadlock



    public EjDeadlock(int saldo) {
       // this.id = id; colocar en el constructor id para quitar deadlock
        this.saldo = saldo;
    }


public void transferir(EjDeadlock destino, int cantidad){

        synchronized (this){
            try {
                Thread.sleep(200);
                synchronized (destino){
                    this.saldo-=cantidad;
                    destino.saldo+=cantidad;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
}

    static void main() {

        EjDeadlock cuentaA = new EjDeadlock(100);
        EjDeadlock cuentaB = new EjDeadlock( 100);

        Thread t1 = new Thread(() -> {
            cuentaA.transferir(cuentaB, 20);
        });

        Thread t2 = new Thread(() -> {
            cuentaB.transferir(cuentaA, 20);
        });

        t1.start();
        t2.start();
    }


    @Override
    public void run() {

    }

}
