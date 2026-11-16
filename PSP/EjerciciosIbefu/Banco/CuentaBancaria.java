package Banco;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class CuentaBancaria {
    private IBanco saldo;

    public IBanco getSaldo() {
        return saldo;
    }

    public void setContador(IBanco saldo) {
        this.saldo = saldo;
    }

    public CuentaBancaria(IBanco saldo) {
        this.saldo = saldo;
    }

    public void work()
    {
        Thread[] hilos = new Thread[50];
        Random rand = new Random();
        AtomicInteger contador = new AtomicInteger();



        for (int i = 0; i < 50; i++) {
            double cantidadIngr = rand.nextDouble(1,50);
            double cantidadRet = rand.nextDouble(1,100);
            Thread hilo = Thread.ofVirtual().start(() ->{


                // numero aleatorio entre 50 y 150
                try {
                    Thread.sleep((int)(Math.random() * 100) + 200);
                } catch (InterruptedException e) {
                    System.out.println("El hilo ha sido interrumpido.");
                }
                double prob = Math.random();
                if (prob < 0.4) {
                    saldo.retirar(cantidadRet);
                    if (!saldo.retirar(cantidadIngr)) {
                        contador.getAndIncrement();
                    }
                } else {
                    saldo.ingresar(cantidadIngr);
                }


            });
            hilos[i] = hilo;
            //hilo.start();
        }

        //join de hilos
        for (int i = 0; i < 50; i++) {
            try {
                hilos[i].join();
            } catch (InterruptedException e) {
                System.out.println("El hilo ha sido interrumpido.");
            }
        }

        System.out.println("Resultado: " + saldo.consultarSaldo());
        System.out.println("Transacciones correctas: " + (50-contador.get())+"/50");
        System.out.println("Transacciones fallidas: " + (contador.get())+"/50");
        System.out.println(saldo.consultarSaldo());

    }

}
