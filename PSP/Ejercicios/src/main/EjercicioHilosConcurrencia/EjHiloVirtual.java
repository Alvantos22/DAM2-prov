package main.EjercicioHilosConcurrencia;

public class EjHiloVirtual {
     static void main() {

        for (int i = 1; i <= 10000; i++) {

            int numero = i;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Thread.startVirtualThread(() -> {
                System.out.println(
                        "hilo " + numero +
                                " - " + Thread.currentThread()
                );
            });
        }
    }
}
