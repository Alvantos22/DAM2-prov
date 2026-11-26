package org.example;


import org.example.model.Partida;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/crear")
public class CrearPartida extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Partida partida = (Partida) req.getSession().getAttribute("partida");


        String biblio = req.getParameter("biblio");
        String admin = req.getParameter("admin");
        String contrasenia=req.getParameter("contrasenia");
        String palabraOculta=req.getParameter("palabraOculta");

        resp.setContentType("text/plain;charset=UTF-8");


        if (partida != null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Partida ya creada");
            return;
        }


        if (biblio==null||contrasenia==null||admin==null||palabraOculta==null||palabraOculta.isBlank()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan parametros");
            return;
        }

        if (!"123".equals(contrasenia)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Contraseña incorrecta");
            return;
        }



        partida = new Partida(biblio,palabraOculta);


        req.getSession().setAttribute("partida", partida);

        resp.getWriter().println("Partida creada correctamente");
    }
}
