package org.david.proyectofinal.servlets;

import org.david.proyectofinal.utils.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/documento")
@MultipartConfig(maxFileSize = 16177215) // Aproximadamente 16MB
public class DocumentoServlet extends HttpServlet {

    // Inserta un nuevo documento en la base de datos
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int contribuyenteId = Integer.parseInt(request.getParameter("contribuyente_id"));
        String tipo = request.getParameter("tipo");
        String fecha = request.getParameter("fecha_emision");
        String xml = request.getParameter("xml");

        // Para el PDF, se espera un input tipo "file"
        Part pdfPart = request.getPart("pdf");
        byte[] pdfBytes = null;
        if (pdfPart != null) {
            try (InputStream is = pdfPart.getInputStream();
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }
                pdfBytes = baos.toByteArray();
            }
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO documento (contribuyente_id, tipo, fecha_emision, xml, pdf) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, contribuyenteId);
            stmt.setString(2, tipo);
            stmt.setString(3, fecha);
            stmt.setString(4, xml);
            if (pdfBytes != null) {
                stmt.setBytes(5, pdfBytes);
            } else {
                stmt.setNull(5, java.sql.Types.BLOB);
            }
            stmt.executeUpdate();
            response.getWriter().println("Documento almacenado correctamente.");
        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM documento";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject documento = new JSONObject();
                documento.put("id", rs.getInt("id"));
                documento.put("contribuyente_id", rs.getInt("contribuyente_id"));
                documento.put("tipo", rs.getString("tipo"));
                documento.put("fecha_emision", rs.getDate("fecha_emision").toString());
                documento.put("xml", rs.getString("xml"));
                jsonArray.put(documento);
            }

            response.getWriter().println(jsonArray.toString());
        } catch (Exception e) {
            response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

}

