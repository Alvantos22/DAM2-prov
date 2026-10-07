package main.EjercicioHilosConcurrencia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EjSuma10M {

    void main() throws Exception {
        int[] numeros = new int[10_000_000];

        for (int i = 0; i < numeros.length; i++) {
            numeros[i] = 1;
        }
        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
            List<Future<Long>> resultados = new ArrayList<>();
            int tamanoTrozo = numeros.length / 4;
            for (int i = 0; i < 4; i++) {
                int inicio = i * tamanoTrozo;
                int fin = (i + 1) * tamanoTrozo;

                Callable<Long> tarea=()->{
                    long suma = 0;
                    for (int j = inicio; j < fin; j++) {
                        suma += numeros[j];
                    }
                    return suma;
                };

                resultados.add(executor.submit(tarea));
            }
            long sumaTotal = 0;
            for (Future<Long> resultado : resultados) {
                sumaTotal += resultado.get();
            }
            System.out.println("Suma total: " + sumaTotal);
            executor.shutdown();
        }
    }
}