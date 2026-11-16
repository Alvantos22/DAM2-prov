package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;
import org.example.model.Participante;
import org.example.model.Partida;

import java.io.IOException;

@WebServlet("/unirse")
public class UnirsePartida extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Partida partida = (Partida) req.getSession().getAttribute("partida");
        resp.setContentType("text/plain;charset=UTF-8");

        if (partida == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Primero crea la partida");
            return;
        }

        String jugador= req.getParameter("jugador");
        String id = req.getParameter("id");


        if (jugador==null||id==null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametros requeridos");
            return;
        }

        Participante participante=new Participante(jugador,Integer.parseInt(id));


        partida.getParticipantes().put(jugador,participante);

        resp.getWriter().println("Partida creada correctamente");

    }
}
