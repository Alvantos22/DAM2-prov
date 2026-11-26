package org.example;

import org.example.model.Jugador;
import org.example.model.Partida;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/estadisticas")
public class EstadisticasPartida extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Partida partida = (Partida) req.getSession().getAttribute("partida");
        resp.setContentType("text/plain;charset=UTF-8");

        if (partida == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Primero crea la partida");
            return;
        }

        synchronized (partida) {
            resp.getWriter().println("ESTADISTICAS DE LA PARTIDA");
            for (Jugador jugador : partida.getJugadores().values()) {
                resp.getWriter().println(jugador.getNombre() + ": ");
                resp.getWriter().println(jugador.getContadorVictorias());
            }
        }
    }
}
