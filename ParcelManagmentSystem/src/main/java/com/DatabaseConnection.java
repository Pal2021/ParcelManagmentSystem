package com;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/parcelManagmentSystem";
    private static final String DB_USER = "root";  // Your DB username
    private static final String DB_PASSWORD = "159Atg45@";  // Your DB password

    // Static method to get a database connection
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Return the database connection
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Static method to close the connection
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
