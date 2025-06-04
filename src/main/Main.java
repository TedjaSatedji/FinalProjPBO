package main;

import controller.ProductController;
import controller.CashierController;
import model.ProductDAO;
import model.SaleDAO;
import view.MainView;
import view.CashierView;
import config.DBConnection;

import javax.swing.*;
import java.sql.SQLException;

public class Main {

    // DAOs can be static if shared, or re-instantiated. For simplicity here, they are local to showRoleSelection.
    // In a larger app, consider dependency injection or a service locator.
    private static ProductDAO productDAO;
    private static SaleDAO saleDAO;


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set System Look and Feel. Using default.");
        }

        SwingUtilities.invokeLater(() -> {
            try {
                DBConnection.getConnection();
                System.out.println("Initial database connection (toko_retail) test successful from Main.");

                // Initialize DAOs once
                productDAO = new ProductDAO();
                saleDAO = new SaleDAO();

                showRoleSelection(); // Initial call to show role selection

            } catch (SQLException e) {
                handleFatalError("Could not connect to the database on application startup.", e);
            } catch (Exception e) {
                handleFatalError("An unexpected error occurred during application startup.", e);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Application shutting down. Closing database connection...");
            DBConnection.closeConnection();
        }));
    }

    /**
     * Displays the role selection dialog and launches the appropriate view.
     * This method can be called to return to the main menu.
     */
    public static void showRoleSelection() {
        // Ensure DAOs are initialized (they should be by main method, but good check if called independently)
        if (productDAO == null) productDAO = new ProductDAO();
        if (saleDAO == null) saleDAO = new SaleDAO();

        String[] options = {"Inventory Management", "Point of Sale (Cashier)", "Exit Application"};
        int choice = JOptionPane.showOptionDialog(null,
                "Select your role or action:",
                "Retail Application - Main Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) { // Inventory Management
            MainView mainView = new MainView();
            // Set default close operation to DISPOSE_ON_CLOSE so it doesn't exit the whole app
            mainView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            new ProductController(mainView, productDAO);
            mainView.setVisible(true);
        } else if (choice == 1) { // Point of Sale
            CashierView cashierView = new CashierView();
            // setDefaultCloseOperation is already DISPOSE_ON_CLOSE for CashierView
            new CashierController(cashierView, productDAO, saleDAO);
            cashierView.setVisible(true);
        } else { // Includes JOptionPane.CLOSED_OPTION or "Exit Application"
            System.out.println("Exiting application from role selection.");
            DBConnection.closeConnection();
            System.exit(0);
        }
    }


    private static void handleFatalError(String message, Exception e) {
        System.err.println("FATAL: " + message);
        if (e != null) e.printStackTrace();
        JOptionPane.showMessageDialog(null,
                message + (e != null ? "\nError: " + e.getMessage() : ""),
                "Application Error",
                JOptionPane.ERROR_MESSAGE);
        DBConnection.closeConnection();
        System.exit(1);
    }
}
