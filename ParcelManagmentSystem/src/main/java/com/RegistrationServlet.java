package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve form data
        String customerName = request.getParameter("customerName");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String address = request.getParameter("address");
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Basic validation (you can expand this as needed)
        if (!password.equals(confirmPassword)) {
            out.println("<h3>Error: Passwords do not match!</h3>");
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Get database connection from utility
            conn = DatabaseConnection.getConnection();

            // SQL query to insert the new user into the Users table
            String sql = "INSERT INTO Users (username, password, name, email, mobile_number, address, role) VALUES (?, ?, ?, ?, ?, ?, 'customer')";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            pstmt.setString(3, customerName);
            pstmt.setString(4, email);
            pstmt.setString(5, mobile);
            pstmt.setString(6, address);

            // Execute the update
            int rowAffected = pstmt.executeUpdate();

            if (rowAffected > 0) {
                // Registration successful
                out.println("<h3>Registration successful! You can now login.</h3>");
            } else {
                // Failed to insert
                out.println("<h3>Error: Registration failed. Please try again.</h3>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        } finally {
            // Close PreparedStatement and Connection using utility
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DatabaseConnection.closeConnection(conn);
        }
    }
}
