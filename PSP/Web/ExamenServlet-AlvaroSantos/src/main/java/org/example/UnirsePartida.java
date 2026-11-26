package org.example;

import org.example.model.Jugador;
import org.example.model.Partida;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        String biblio = req.getParameter("biblio");
        String contrasenia= req.getParameter("contrasenia");
        String user= req.getParameter("user");
        String id = req.getParameter("id");


        if (user==null||id==null||biblio==null||contrasenia==null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametros requeridos");
            return;
        }
        if (!"123".equals(contrasenia)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Contraseña incorrecta");
            return;
        }

        int identificador;
        try {
            identificador = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "El id debe ser un numero");
            return;
        }
        if (partida.getJugadores().containsKey(user)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "El jugador ya esta registrado");
            return;
        }

        if (partida.getJugadores().size()==2){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No se puede jugar mas de 2 jugadores");
            return;
        }

        Jugador jugador=new Jugador(user,identificador);
        partida.getJugadores().put(user,jugador);

        resp.getWriter().println("Jugador registrado correctamente: "+jugador);

    }
}
