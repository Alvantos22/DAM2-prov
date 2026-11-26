package org.example;

import org.example.model.Jugador;
import org.example.model.Partida;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

@WebServlet("/jugar")
public class JugarPartida extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        Partida partida = (Partida) req.getSession().getAttribute("partida");
        resp.setContentType("text/plain;charset=UTF-8");

        if (partida == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Primero crea la partida");
            return;
        }
        if (partida.getJugadores().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No hay participantes");
            return;
        }

        String userJugador=req.getParameter("userJugador");
        String letra=req.getParameter("letra");

        if (userJugador==null||letra==null||letra.length()>1){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan parametros o has puesto mas de una letra");
            return;
        }

        if (!partida.getJugadores().containsKey(userJugador)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "jugador no registrado");
            return;
        }

        Jugador jugadorActual= partida.getJugadores().get(userJugador);
        if (jugadorActual.isTurnoCumplido()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "este jugador ya ha jugado este turno");
            return;
        }

        char[] solucion=partida.getProgreso();
        char intento = letra.charAt(0);

        for (int i = 0; i < solucion.length; i++) {
            String cambiarLetra= String.valueOf(solucion[i]);
            if (cambiarLetra.equalsIgnoreCase(letra)){
                solucion[i]=intento;
            }
            resp.getWriter().print(solucion);
        }

        partida.setProgreso(solucion);


        boolean todosHanJugado = true;
        for (Jugador jugador :partida.getJugadores().values()){
            if (!jugador.isTurnoCumplido()){
                todosHanJugado = false;
                break;
            }
        }

        if (todosHanJugado) {
            for (Jugador jugador :partida.getJugadores().values()){
                jugador.setTurnoCumplido(false);
            }
        }


        if (Arrays.equals(partida.getProgreso(), solucion)){
            resp.getWriter().print(solucion);
            resp.getWriter().println(jugadorActual+"HA GANADO");
            jugadorActual.setContadorVictorias(jugadorActual.getContadorVictorias()+1);

        }
    }

}
