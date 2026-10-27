package main.EjercicioHilosConcurrencia;

public class EjCuentaBancaria implements Runnable {
    int saldo;

    public void depositarDinero(int cantidad) {
        synchronized (this) {
            this.saldo += cantidad;
        }
    /*

    public synchronized void depositarDinero(int cantidad){
            this.saldo += cantidad;

    }



     */
    }

    @Override
    public void run() {
        depositarDinero(1);
    }


    void main(){
        EjCuentaBancaria cuenta = new EjCuentaBancaria();

        for (int i = 0; i < 100; i++) {
            try {
            Thread loteria =new Thread(cuenta);
            loteria.start();
                loteria.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        System.out.println(cuenta.saldo);
    }
}
