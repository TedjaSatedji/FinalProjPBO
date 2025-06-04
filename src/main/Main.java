package main;

import controller.ProductController;
import controller.CashierController;
import controller.SalesHistoryController; // New Controller
import model.ProductDAO;
import model.SaleDAO;
import view.MainView;
import view.CashierView;
import view.SalesHistoryView; // New View
import config.DBConnection;

import javax.swing.*;
import java.awt.*; // Required for Font
import java.sql.SQLException;

public class Main {

    private static ProductDAO productDAO;
    private static SaleDAO saleDAO;

    // --- Styling Constants for Main Menu Dialog ---
    private static final Font MENU_MESSAGE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font MENU_BUTTON_FONT = new Font("Arial", Font.PLAIN, 13); // Font for JOptionPane buttons
    private static final Color MENU_TITLE_COLOR = new Color(50, 50, 50); // Dark gray for title (if we could set it)
    private static final String APP_TITLE = "Retail Application - Main Menu";


    public static void main(String[] args) {
        try {
            // Set a more modern Look and Feel if available
            // This also influences JOptionPane's appearance
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Failed to set System Look and Feel. Using default.");
        }

        SwingUtilities.invokeLater(() -> {
            try {
                DBConnection.getConnection();
                System.out.println("Initial database connection (toko_retail) test successful from Main.");

                productDAO = new ProductDAO();
                saleDAO = new SaleDAO();

                showRoleSelection();

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

    public static void showRoleSelection() {
        if (productDAO == null) productDAO = new ProductDAO();
        if (saleDAO == null) saleDAO = new SaleDAO();

        // --- Style the message for JOptionPane ---
        JLabel messageLabel = new JLabel("What so you want to do?");
        messageLabel.setFont(MENU_MESSAGE_FONT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // You could add padding to the label if needed:
        // messageLabel.setBorder(new EmptyBorder(10, 0, 10, 0));


        // --- Attempt to style JOptionPane buttons font ---
        // Store the original font to restore it later
        Object oldOptionPaneButtonFont = UIManager.get("OptionPane.buttonFont");
        UIManager.put("OptionPane.buttonFont", MENU_BUTTON_FONT);
        // You can also try to style other JOptionPane properties if desired, e.g.:
        // UIManager.put("OptionPane.messageFont", MENU_MESSAGE_FONT);
        // UIManager.put("OptionPane.background", new Color(230,240,250));
        // UIManager.put("Panel.background", new Color(230,240,250)); // For the panel inside JOptionPane

        String[] options = {"Inventory Management", "Point of Sale (Cashier)", "Sales History", "Exit Application"};

        // Use PLAIN_MESSAGE to avoid default icons if you are using a custom message component
        int choice = JOptionPane.showOptionDialog(null, // parentComponent (null centers on screen)
                messageLabel,                         // message (our styled JLabel)
                APP_TITLE,                            // title
                JOptionPane.DEFAULT_OPTION,           // optionType
                JOptionPane.PLAIN_MESSAGE,            // messageType
                null,                                 // icon (null for no custom icon here)
                options,                              // options (buttons)
                options[0]);                          // initialValue

        // --- Restore original JOptionPane button font ---
        UIManager.put("OptionPane.buttonFont", oldOptionPaneButtonFont);
        // Restore other UIManager properties if you changed them


        if (choice == 0) { // Inventory Management
            MainView mainView = new MainView();
            mainView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            new ProductController(mainView, productDAO);
            mainView.setVisible(true);
        } else if (choice == 1) { // Point of Sale
            CashierView cashierView = new CashierView();
            // CashierView's defaultCloseOperation is already DISPOSE_ON_CLOSE
            new CashierController(cashierView, productDAO, saleDAO);
            cashierView.setVisible(true);
        } else if (choice == 2) { // Sales History
            SalesHistoryView salesHistoryView = new SalesHistoryView();
            // SalesHistoryView's defaultCloseOperation is already DISPOSE_ON_CLOSE
            new SalesHistoryController(salesHistoryView, saleDAO);
            salesHistoryView.setVisible(true);
        }
        else { // Includes JOptionPane.CLOSED_OPTION (-1) or "Exit Application"
            System.out.println("Exiting application from role selection or dialog closed.");
            DBConnection.closeConnection();
            System.exit(0);
        }
    }


    private static void handleFatalError(String message, Exception e) {
        System.err.println("FATAL: " + message);
        if (e != null) e.printStackTrace();

        // Style the error message dialog slightly
        JLabel errorLabel = new JLabel("<html><body style='width: 300px;'>" + message +
                (e != null ? "<br>Error: " + e.getMessage() : "") +
                "</body></html>"); // HTML for word wrap
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JOptionPane.showMessageDialog(null,
                errorLabel,
                "Application Error",
                JOptionPane.ERROR_MESSAGE);
        DBConnection.closeConnection();
        System.exit(1);
    }
}