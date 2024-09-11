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


@WebServlet("/bookService")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters including userId
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.isEmpty()) {
            response.getWriter().println("Error: userId is required.");
            return;
        }

        int userId = Integer.parseInt(userIdStr); // Convert userId to integer
        String senderName = request.getParameter("senderName");
        String senderAddress = request.getParameter("senderAddress");
        String senderContact = request.getParameter("senderContact");

        String receiverName = request.getParameter("receiverName");
        String receiverAddress = request.getParameter("receiverAddress");
        String receiverPin = request.getParameter("receiverPin");
        String receiverContact = request.getParameter("receiverContact");

        String parcelSize = request.getParameter("parcelSize");
        String parcelWeight = request.getParameter("parcelWeight");
        String contentsDescription = request.getParameter("contentsDescription");

        String deliverySpeed = request.getParameter("deliverySpeed");
        String packaging = request.getParameter("packaging");

        String pickupDate = request.getParameter("pickupDate");
        String pickupTime = request.getParameter("pickupTime");

        String serviceCost = request.getParameter("serviceCost");
        String paymentMethod = request.getParameter("paymentMethod");

        // Initialize database connection using DatabaseConnection utility
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Get connection from your DatabaseConnection class
            conn = DatabaseConnection.getConnection();

            // SQL query to insert booking details into the Bookings table
            String sql = "INSERT INTO Bookings (user_id, rec_name, rec_address, rec_pin, rec_mobile, par_weight_gram, par_contents_description, par_delivery_type, par_packing_preference, par_pickup_time, par_dropoff_time, par_service_cost, par_payment_time) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

            // Prepare the statement and set the parameters
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId); // Pass userId from request
            pstmt.setString(2, receiverName);
            pstmt.setString(3, receiverAddress);
            pstmt.setString(4, receiverPin);
            pstmt.setString(5, receiverContact);
            pstmt.setDouble(6, Double.parseDouble(parcelWeight));
            pstmt.setString(7, contentsDescription);
            pstmt.setString(8, deliverySpeed);
            pstmt.setString(9, packaging);
            pstmt.setString(10, pickupDate + " " + pickupTime); // Combining date and time
            pstmt.setString(11, null); // dropoff time (set later during delivery)
            pstmt.setDouble(12, Double.parseDouble(serviceCost));

            // Execute the update
            int rowCount = pstmt.executeUpdate();

            // Send success or failure response
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            if (rowCount > 0) {
                out.println("<h3>Booking successfully created!</h3>");
            } else {
                out.println("<h3>Error: Unable to create booking. Please try again.</h3>");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        } finally {
            // Close the database resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
