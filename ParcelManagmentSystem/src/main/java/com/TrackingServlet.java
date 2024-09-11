package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@WebServlet("/trackParcel")
public class TrackingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameter
        String bookingId = request.getParameter("bookingId");

        // Initialize database connection
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Get connection from DatabaseConnection class
            conn = DatabaseConnection.getConnection();

            // SQL query to get tracking details
            String sql = "SELECT booking_id, status, par_pickup_time AS last_update FROM Bookings WHERE booking_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(bookingId));
            rs = pstmt.executeQuery();

            // Prepare JSON response
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();

            if (rs.next()) {
                // Retrieve details from result set
                String status = rs.getString("status");
                String lastUpdate = rs.getString("last_update");

                // Create JSON response
                String jsonResponse = String.format(
                    "{\"bookingId\":\"%s\",\"status\":\"%s\",\"lastUpdate\":\"%s\"}",
                    bookingId, status, lastUpdate
                );

                out.print(jsonResponse);
            } else {
                // Booking ID not found
                String jsonResponse = "{\"message\":\"No booking found with this ID.\"}";
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(jsonResponse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.print("{\"message\":\"An error occurred. Please try again later.\"}");
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
