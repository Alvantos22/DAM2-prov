package org.example.model;

import lombok.Data;

@Data
public class Jugador {
    private int id;
    private int posicion;


    public Jugador(int id, int posicion) {
        this.id = id;
        this.posicion = posicion;
    }

    public int sumarPosicion(int newposicion){
        return posicion+=newposicion;

    }
}
