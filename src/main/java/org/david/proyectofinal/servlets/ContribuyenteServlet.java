package org.david.proyectofinal.servlets;

import org.david.proyectofinal.utils.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import org.json.JSONArray;
import org.json.JSONObject;



@WebServlet("/contribuyente")
public class ContribuyenteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        String ruc = request.getParameter("ruc");
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");

        // Validar que los parámetros no sean nulos o vacíos
        if (ruc == null || nombre == null || direccion == null || ruc.isEmpty() || nombre.isEmpty() || direccion.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Código 400: Solicitud incorrecta
            response.getWriter().println("{\"error\": \"Todos los campos son obligatorios.\"}");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO contribuyente (ruc, nombre, direccion) VALUES (?, ?, ?)")) {

            stmt.setString(1, ruc);
            stmt.setString(2, nombre);
            stmt.setString(3, direccion);
            stmt.executeUpdate();

            response.setStatus(HttpServletResponse.SC_OK); // Código 200: Éxito
            response.getWriter().println("{\"mensaje\": \"Contribuyente registrado correctamente.\"}");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Código 500: Error interno
            response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT id, ruc, nombre, direccion, telefono, email FROM contribuyente;");
            ResultSet rs = stmt.executeQuery();

            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject contribuyente = new JSONObject();
                contribuyente.put("id", rs.getInt("id"));
                contribuyente.put("ruc", rs.getString("ruc"));
                contribuyente.put("nombre", rs.getString("nombre"));
                contribuyente.put("direccion", rs.getString("direccion"));
                contribuyente.put("telefono", rs.getString("telefono"));
                contribuyente.put("email", rs.getString("email"));

                jsonArray.put(contribuyente);
            }
            response.getWriter().println(jsonArray.toString());
        } catch (Exception e) {
            response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");
        }

    }
}






