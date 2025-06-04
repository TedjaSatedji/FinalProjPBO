import controller.ProductController;
import model.ProductDAO;
import view.MainView; // Updated to MainView
import config.DBConnection; // Updated to DBConnection

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.SQLException;

/**
 * Main.java
 * Entry point for the Retail Inventory Manager application.
 * Aligned with the provided document.
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set System Look and Feel. Using default.");
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Test DB connection on startup
                    DBConnection.getConnection(); // Using renamed DBConnection
                    System.out.println("Initial database connection (toko_retail) test successful from Main.");

                    // Initialize MVC components
                    ProductDAO productDAO = new ProductDAO();
                    MainView mainView = new MainView(); // Using renamed MainView
                    new ProductController(mainView, productDAO);

                    mainView.setVisible(true);

                } catch (SQLException e) {
                    System.err.println("FATAL: Could not connect to the database on application startup.");
                    e.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(null,
                            "Could not connect to the database. Please check your connection settings and ensure the database server (toko_retail) is running.\nError: " + e.getMessage(),
                            "Database Connection Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                } catch (Exception e) {
                    System.err.println("An unexpected error occurred during application startup:");
                    e.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(null,
                            "An unexpected error occurred: " + e.getMessage(),
                            "Application Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Application shutting down. Closing database connection...");
            DBConnection.closeConnection(); // Using renamed DBConnection
        }));
    }
}
