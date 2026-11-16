
package com.dam2.examen.ristorante;

import java.util.concurrent.CountDownLatch;

public class Grupo {

    private final int id;
    private final int personas;
    private final CountDownLatch platosListos;

    public Grupo(int id, int personas) {
        this.id = id;
        this.personas = personas;
        this.platosListos = new CountDownLatch(personas);
    }

    public int getId() {
        return id;
    }

    public int getPersonas() {
        return personas;
    }

    public void platoTerminado() {
        platosListos.countDown();
    }

    public void esperarPlatos() throws InterruptedException {
        platosListos.await();
    }
}
