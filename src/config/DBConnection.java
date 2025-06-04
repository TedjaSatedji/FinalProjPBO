package config; // Package remains the same for consistency with other files

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java (Formerly DatabaseConnection.java)
 * This utility class handles the connection to the MySQL database.
 */
public class DBConnection {
    // --- Database Credentials ---
    // !!! IMPORTANT: Replace with your actual database URL, username, and password !!!
    // The document suggests database name 'toko_retail'
    private static final String DB_URL = "jdbc:mysql://localhost:3306/toko_retail"; // Ensure 'toko_retail' is your DB name
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = ""; // Replace with your MySQL password
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static Connection connection = null;

    /**
     * Private constructor to prevent instantiation.
     */
    private DBConnection() {
    }

    /**
     * Establishes and returns a connection to the database.
     *
     * @return A Connection object to the database.
     * @throws SQLException if a database access error occurs or the url is null.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName(JDBC_DRIVER);
                System.out.println("Connecting to database (toko_retail)...");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connection successful!");
            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found.");
                e.printStackTrace();
                throw new SQLException("MySQL JDBC Driver not found.", e);
            } catch (SQLException se) {
                System.err.println("Database connection failed.");
                se.printStackTrace();
                throw se;
            }
        }
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException se) {
            System.err.println("Error closing database connection.");
            se.printStackTrace();
        }
    }

    // Optional: Main method for testing the connection
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("Successfully connected to the database (toko_retail) for testing.");
                DBConnection.closeConnection();
            } else {
                System.out.println("Failed to connect to the database (toko_retail) for testing.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
