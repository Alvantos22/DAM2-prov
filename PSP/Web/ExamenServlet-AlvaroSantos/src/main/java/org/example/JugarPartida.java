package org.example;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Participante;
import org.example.model.Partida;

import java.io.IOException;
@WebServlet("/jugar")
public class JugarPartida extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Partida partida = (Partida) req.getSession().getAttribute("partida");
        resp.setContentType("text/plain;charset=UTF-8");

        if (partida == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Primero crea la partida");
            return;
        }
        if (partida.getParticipantes().isEmpty()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No hay participantes");
            return;
        }
        if (!partida.isJugable()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "La partida no es jugable todavia, comienzala");
            return;
        }

        int contadorTurnos= partida.getContadorTurnos();
        req.getSession().setAttribute("contadorTurnos", contadorTurnos);

        String nombreJugador=req.getParameter("nombreJugador");


        if (!partida.getParticipantes().containsKey(nombreJugador)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "jugador no registrado");
            return;
        }

        Participante jugadorActual= partida.getParticipantes().get(nombreJugador);
        if (jugadorActual.isTurnoCumplido()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "este jugador ya ha jugado este turno");
            return;
        }


        jugadorActual.sumarPosiciones(partida.tirarDado());
        if (jugadorActual.getPosicionesAvanzadas()>=partida.getNumCasillas()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "JUGADOR"+ jugadorActual.toString()+"ha ganado");
           return;
        }else{
            resp.getWriter().println("JUGADOR "+ jugadorActual.toString()+"Va por la casilla"+jugadorActual.getPosicionesAvanzadas());
            jugadorActual.setTurnoCumplido(true);
            for (Participante participante:partida.getParticipantes().values()){
                if (participante.isTurnoCumplido()){
                    resp.getWriter().println(participante.toString()+"ya ha jugado");
                }else {
                    resp.getWriter().println(participante.toString()+"todavia NO ha jugado");
                }
            }
        }




    }
}
