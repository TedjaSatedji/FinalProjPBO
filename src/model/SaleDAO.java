package model;

import config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SaleDAO {
    private Connection connection;
    private ProductDAO productDAO; // To update stock

    public SaleDAO() {
        try {
            this.connection = DBConnection.getConnection();
            this.productDAO = new ProductDAO();
        } catch (SQLException e) {
            System.err.println("SaleDAO: Failed to connect to database.");
            e.printStackTrace();
        }
    }


    public boolean recordSale(Sale sale) {
        String insertSaleSQL = "INSERT INTO sales (total_amount, cashier_name, sale_date) VALUES (?, ?, ?)"; // Added sale_date
        String insertSaleItemSQL = "INSERT INTO sale_items (sale_id, product_id, quantity_sold, price_per_unit, subtotal) VALUES (?, ?, ?, ?, ?)";
        String updateStockSQL = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

        try {
            connection.setAutoCommit(false);

            PreparedStatement salePstmt = connection.prepareStatement(insertSaleSQL, Statement.RETURN_GENERATED_KEYS);
            salePstmt.setDouble(1, sale.getTotalAmount());
            salePstmt.setString(2, sale.getCashierName());
            salePstmt.setTimestamp(3, sale.getSaleDate()); // Set the sale date
            int affectedRows = salePstmt.executeUpdate();

            if (affectedRows == 0) {
                connection.rollback();
                return false;
            }

            int generatedSaleId;
            try (ResultSet generatedKeys = salePstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedSaleId = generatedKeys.getInt(1);
                    sale.setSaleId(generatedSaleId);
                } else {
                    connection.rollback();
                    throw new SQLException("Creating sale failed, no ID obtained.");
                }
            }
            salePstmt.close();

            PreparedStatement itemPstmt = connection.prepareStatement(insertSaleItemSQL);
            PreparedStatement stockPstmt = connection.prepareStatement(updateStockSQL);

            for (SaleItem item : sale.getItems()) {
                Product product = productDAO.getProductById(item.getProductId());
                if (product == null || product.getStock() < item.getQuantitySold()) {
                    connection.rollback();
                    System.err.println("Insufficient stock for product ID: " + item.getProductId());
                    throw new SQLException("Insufficient stock for product: " + (product != null ? product.getName() : "ID " + item.getProductId()));
                }

                itemPstmt.setInt(1, generatedSaleId);
                itemPstmt.setInt(2, item.getProductId());
                itemPstmt.setInt(3, item.getQuantitySold());
                itemPstmt.setDouble(4, item.getPricePerUnit());
                itemPstmt.setDouble(5, item.getSubtotal());
                itemPstmt.addBatch();

                stockPstmt.setInt(1, item.getQuantitySold());
                stockPstmt.setInt(2, item.getProductId());
                stockPstmt.setInt(3, item.getQuantitySold());
                stockPstmt.addBatch();
            }
            itemPstmt.executeBatch();
            stockPstmt.executeBatch();

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
                    connection.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    public List<Sale> getAllSales() {
        List<Sale> salesList = new ArrayList<>();
        String sql = "SELECT * FROM sales ORDER BY sale_date DESC"; // Show newest first
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Sale sale = new Sale();
                sale.setSaleId(rs.getInt("sale_id"));
                sale.setSaleDate(rs.getTimestamp("sale_date"));
                sale.setTotalAmount(rs.getDouble("total_amount"));
                sale.setCashierName(rs.getString("cashier_name"));
                // Note: Sale items are not loaded here for performance. Load them on demand.
                salesList.add(sale);
            }
        } catch (SQLException e) {
            System.err.println("SaleDAO: Error retrieving all sales.");
            e.printStackTrace();
        }
        return salesList;
    }


    public List<SaleItem> getSaleItemsBySaleId(int saleId) {
        List<SaleItem> saleItems = new ArrayList<>();
        // Join with products table to get product name for display
        String sql = "SELECT si.*, p.name as product_name FROM sale_items si " +
                "JOIN products p ON si.product_id = p.id " +
                "WHERE si.sale_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, saleId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                SaleItem item = new SaleItem();
                item.setSaleItemId(rs.getInt("sale_item_id"));
                item.setSaleId(rs.getInt("sale_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setProductName(rs.getString("product_name")); // Get product name from join
                item.setQuantitySold(rs.getInt("quantity_sold"));
                item.setPricePerUnit(rs.getDouble("price_per_unit"));
                item.setSubtotal(rs.getDouble("subtotal"));
                saleItems.add(item);
            }
        } catch (SQLException e) {
            System.err.println("SaleDAO: Error retrieving sale items for sale ID: " + saleId);
            e.printStackTrace();
        }
        return saleItems;
    }
}