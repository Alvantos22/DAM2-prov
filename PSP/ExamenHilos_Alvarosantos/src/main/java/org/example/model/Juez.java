package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Juez {
   private List<String> mensajesPosiciones;


    public Juez() {
       mensajesPosiciones=new ArrayList<>();
    }

    public void dice(String mensaje){
       System.out.println(mensaje);
   }
    public List<String> getMensajesPosiciones() {
        return mensajesPosiciones;
    }

    public void setMensajesPosiciones(List<String> mensajesPosiciones) {
        this.mensajesPosiciones = mensajesPosiciones;
    }
}
