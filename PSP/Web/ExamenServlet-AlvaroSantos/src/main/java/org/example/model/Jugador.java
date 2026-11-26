package org.example.model;

public class Jugador {
    private String nombre;
    private int id;
    private boolean turnoCumplido;
    private int contadorVictorias;


    public Jugador(String nombre, int id) {
        this.nombre = nombre;
        this.id = id;
        turnoCumplido=false;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTurnoCumplido() {
        return turnoCumplido;
    }

    public void setTurnoCumplido(boolean turnoCumplido) {
        this.turnoCumplido = turnoCumplido;
    }

    public int getContadorVictorias() {
        return contadorVictorias;
    }

    public void setContadorVictorias(int contadorVictorias) {
        this.contadorVictorias = contadorVictorias;
    }

    @Override
    public String toString() {
        return
                "nombre='" + nombre + '\'' +
                ", id=" + id;
    }
}
