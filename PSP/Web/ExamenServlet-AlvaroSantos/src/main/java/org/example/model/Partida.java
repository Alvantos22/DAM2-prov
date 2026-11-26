package org.example.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Partida {
    private String[] tablero;
    private Map<String, Jugador> jugadores;
    private String nombreBiblioteca;
    private String palabraOculta;
    private char[] progreso;

    public Partida(String nombreBiblioteca,String palabraOculta) {
        this.nombreBiblioteca=nombreBiblioteca;
        this.palabraOculta = palabraOculta;
        tablero=new String[palabraOculta.length()];
        jugadores=new HashMap<>(2);
        progreso=palabraOculta.toCharArray();

        Arrays.fill(progreso, '_');
    }



    public String[] getTablero() {
        return tablero;
    }

    public void setTablero(String[] tablero) {
        this.tablero = tablero;
    }

    public Map<String, Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(Map<String, Jugador> jugadores) {
        jugadores = jugadores;
    }

    public String getNombreBiblioteca() {
        return nombreBiblioteca;
    }

    public void setNombreBiblioteca(String nombreBiblioteca) {
        this.nombreBiblioteca = nombreBiblioteca;
    }

    public String getPalabraOculta() {
        return palabraOculta;
    }

    public void setPalabraOculta(String palabraOculta) {
        this.palabraOculta = palabraOculta;
    }

    public char[] getProgreso() {
        return progreso;
    }

    public void setProgreso(char[] progreso) {
        this.progreso = progreso;
    }
}
