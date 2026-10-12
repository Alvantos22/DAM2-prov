package main.EjercicioHilosConcurrencia;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public class EjLlamadaAPIs{

    void main() {
try(ExecutorService executor = Executors.newFixedThreadPool(5)){
    Callable<String> api1=()->{
        Thread.sleep(2000);
        return "Llamada a api1";
    };

    Callable<String> api2=()->{
        Thread.sleep(3000);
        return "Llamada a api2";
    };

    Callable<String> api3=()->{
        Thread.sleep(1000);
        return "Llamada a api3";
    };

    Callable<String> api4=()->{
        Thread.sleep(2000);
        return "Llamada a api4";
    };

    Callable<String> api5=()->{
        Thread.sleep(3000);
        return "Llamada a api5";
    };
    List<Callable<String>> tareas= new ArrayList<>();

    tareas.add(api1);
    tareas.add(api2);
    tareas.add(api3);
    tareas.add(api4);
    tareas.add(api5);

    try {
        List<Future<String>> resultados = executor.invokeAll(tareas);
        for (int i = 0; i <= 4; i++) {
            System.out.println(resultados.get(i).get());
        }
    } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
    }
}
    }
}
