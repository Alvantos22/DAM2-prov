
package com.dam2.examen.ristorante;

public enum TipoPlato {

    ENSALADA(2000, 8.0),
    PASTA(3000, 12.0),
    PIZZA(4000, 15.0),
    CARNE(5000, 20.0);

    private final int tiempoMs;
    private final double precio;

    TipoPlato(int tiempoMs, double precio) {
        this.tiempoMs = tiempoMs;
        this.precio = precio;
    }

    public int getTiempoMs() {
        return tiempoMs;
    }

    public double getPrecio() {
        return precio;
    }
}
