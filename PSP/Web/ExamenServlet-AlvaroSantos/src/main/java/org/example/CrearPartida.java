package org.example;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Admin;
import org.example.model.Partida;

import java.io.IOException;

@WebServlet("/crear")
public class CrearPartida extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Partida partida = (Partida) req.getSession().getAttribute("partida");
        String admin = req.getParameter("admin");
        String contrasenia=req.getParameter("contrasenia");
        String numCasillas= req.getParameter("numCasillas");

        resp.setContentType("text/plain;charset=UTF-8");

        if (partida != null) {
            if (!partida.isTerminada()) {
                resp.getWriter().println("Ya tienes una partida en curso");
            }
            return;
        }
        if (numCasillas==null||admin==null||contrasenia==null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametros requeridos");
            return;
        }

        Admin admin1 = new Admin(admin);
        if (!admin1.getContrasenia().equalsIgnoreCase(contrasenia)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Contraseña de administrador incorrecta");
            return;
        }


        int casillas=Integer.parseInt(numCasillas);


        partida = new Partida(casillas);

        req.getSession().setAttribute("partida", partida);

        resp.getWriter().println("Partida creada correctamente");
    }
}
