package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;

@WebServlet("/configurar")
public class ConfigurarCajaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            int n1 = Integer.parseInt(req.getParameter("n1"));
            int n2 = Integer.parseInt(req.getParameter("n2"));
            int n3 = Integer.parseInt(req.getParameter("n3"));

            var session = req.getSession();

            int[] combinacion = {n1, n2, n3};

            session.setAttribute("combinacion", combinacion);
            session.setAttribute("posicion", 0);
            session.setAttribute("abierta", false);
            session.setAttribute("secretos", new HashMap<String, String>());

            resp.getWriter().println("Caja configurada correctamente");

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Los tres números deben ser números enteros");
        } catch (NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Faltan parámetros");
        }
    }
}