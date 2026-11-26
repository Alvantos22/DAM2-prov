package org.example;

import org.example.model.Partida;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/borrar")
public class BorrarPartida extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String biblio = req.getParameter("biblio");
        Partida partida = (Partida) req.getSession().getAttribute("partida");

        if (partida.getNombreBiblioteca().equalsIgnoreCase(biblio)){
            partida.setProgreso(null);
            partida.setJugadores(null);
            partida.setPalabraOculta(null);
            resp.getWriter().println("Se ha borrado correctamente");

        }

    }
}
