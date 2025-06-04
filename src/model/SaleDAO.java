package model;

import config.DBConnection;

import java.sql.*;
import java.util.List;
// Note: Methods for retrieving sales are not implemented here for brevity but would be needed for reporting.

/**
 * SaleDAO.java
 * Handles database operations for sales transactions.
 */
public class SaleDAO {
    private Connection connection;
    private ProductDAO productDAO; // To update stock

    public SaleDAO() {
        try {
            this.connection = DBConnection.getConnection();
            this.productDAO = new ProductDAO(); // Assuming ProductDAO is accessible
        } catch (SQLException e) {
            System.err.println("SaleDAO: Failed to connect to database.");
            e.printStackTrace();
        }
    }

    /**
     * Saves a sale and its items to the database.
     * This should ideally be a transaction to ensure atomicity.
     * @param sale The Sale object to save.
     * @return true if the sale was saved successfully, false otherwise.
     */
    public boolean recordSale(Sale sale) {
        String insertSaleSQL = "INSERT INTO sales (total_amount, cashier_name) VALUES (?, ?)";
        String insertSaleItemSQL = "INSERT INTO sale_items (sale_id, product_id, quantity_sold, price_per_unit, subtotal) VALUES (?, ?, ?, ?, ?)";
        String updateStockSQL = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

        try {
            // Start transaction
            connection.setAutoCommit(false);

            // 1. Insert into 'sales' table
            PreparedStatement salePstmt = connection.prepareStatement(insertSaleSQL, Statement.RETURN_GENERATED_KEYS);
            salePstmt.setDouble(1, sale.getTotalAmount());
            salePstmt.setString(2, sale.getCashierName()); // Can be null
            int affectedRows = salePstmt.executeUpdate();

            if (affectedRows == 0) {
                connection.rollback(); // Rollback if sale record failed
                return false;
            }

            // Get the generated sale_id
            int generatedSaleId;
            try (ResultSet generatedKeys = salePstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedSaleId = generatedKeys.getInt(1);
                    sale.setSaleId(generatedSaleId); // Set it back to the Sale object
                } else {
                    connection.rollback();
                    throw new SQLException("Creating sale failed, no ID obtained.");
                }
            }
            salePstmt.close();

            // 2. Insert each item into 'sale_items' table and update stock
            PreparedStatement itemPstmt = connection.prepareStatement(insertSaleItemSQL);
            PreparedStatement stockPstmt = connection.prepareStatement(updateStockSQL);

            for (SaleItem item : sale.getItems()) {
                // Check stock before attempting to update
                Product product = productDAO.getProductById(item.getProductId());
                if (product == null || product.getStock() < item.getQuantitySold()) {
                    connection.rollback();
                    System.err.println("Insufficient stock for product ID: " + item.getProductId());
                    // You should throw a custom exception or return a specific error code/message here
                    throw new SQLException("Insufficient stock for product: " + item.getProductName());
                }

                // Insert into sale_items
                itemPstmt.setInt(1, generatedSaleId);
                itemPstmt.setInt(2, item.getProductId());
                itemPstmt.setInt(3, item.getQuantitySold());
                itemPstmt.setDouble(4, item.getPricePerUnit());
                itemPstmt.setDouble(5, item.getSubtotal());
                itemPstmt.addBatch();

                // Prepare stock update
                stockPstmt.setInt(1, item.getQuantitySold());
                stockPstmt.setInt(2, item.getProductId());
                stockPstmt.setInt(3, item.getQuantitySold()); // Ensure stock doesn't go negative due to concurrent ops
                stockPstmt.addBatch();
            }
            itemPstmt.executeBatch();
            stockPstmt.executeBatch();

            // Commit transaction
            connection.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("SaleDAO: Error recording sale. Transaction is being rolled back.");
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("SaleDAO: Error rolling back transaction.");
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Reset auto-commit
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    // Add methods like getSaleById, getAllSales, getSalesByDateRange etc. for reporting features.
}
