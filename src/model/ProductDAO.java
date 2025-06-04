package model;

import config.DBConnection; // Updated to use DBConnection

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductDAO.java (Model - Data Access Object)
 * Handles CRUD operations for Product objects, aligned with the document's schema.
 * (id, name, category, stock, price, supplier)
 */
public class ProductDAO {

    private Connection connection;

    public ProductDAO() {
        try {
            // Use the renamed DBConnection class
            this.connection = DBConnection.getConnection();
        } catch (SQLException e) {
            System.err.println("DAO: Failed to connect to database on instantiation.");
            e.printStackTrace();
        }
    }

    /**
     * Adds a new product to the database.
     * @param product The Product object to add.
     * @return true if the product was added successfully, false otherwise.
     */
    public boolean addProduct(Product product) {
        // SQL query based on the document's table structure
        String sql = "INSERT INTO products (name, category, stock, price, supplier) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setInt(3, product.getStock());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setString(5, product.getSupplier());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1)); // Set the auto-generated ID
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error adding product: " + product.getName());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves a product by its ID.
     * @param productId The ID of the product to retrieve.
     * @return The Product object if found, null otherwise.
     */
    public Product getProductById(int productId) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error retrieving product with ID: " + productId);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all products from the database.
     * @return A list of all Product objects.
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products"; // Order by name for better display
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error retrieving all products.");
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Updates an existing product in the database.
     * @param product The Product object with updated information.
     * @return true if the product was updated successfully, false otherwise.
     */
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, category = ?, stock = ?, price = ?, supplier = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getCategory());
            pstmt.setInt(3, product.getStock());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setString(5, product.getSupplier());
            pstmt.setInt(6, product.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DAO: Error updating product with ID: " + product.getId());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a product from the database by its ID.
     * @param productId The ID of the product to delete.
     * @return true if the product was deleted successfully, false otherwise.
     */
    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("DAO: Error deleting product with ID: " + productId);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Searches for products by name or category.
     * @param searchTerm The term to search for.
     * @param searchBy "name" or "category".
     * @return A list of matching Product objects.
     */
    public List<Product> searchProducts(String searchTerm, String searchBy) {
        List<Product> products = new ArrayList<>();
        String sql;
        if ("name".equalsIgnoreCase(searchBy)) {
            sql = "SELECT * FROM products WHERE name LIKE ?";
        } else if ("category".equalsIgnoreCase(searchBy)) {
            sql = "SELECT * FROM products WHERE category LIKE ?";
        } else {
            return products; // Invalid search type
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            System.err.println("DAO: Error searching products by " + searchBy + " for term: " + searchTerm);
            e.printStackTrace();
        }
        return products;
    }


    /**
     * Helper method to map a ResultSet row to a Product object.
     * It can instantiate subclasses based on the 'category' field if needed for polymorphism.
     * @param rs The ResultSet to map.
     * @return A Product object (or a subclass like ElectronicProduct, ClothingProduct).
     * @throws SQLException if a database access error occurs.
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String category = rs.getString("category");
        int stock = rs.getInt("stock");
        double price = rs.getDouble("price");
        String supplier = rs.getString("supplier");

        // Instantiate specific subclasses if category matches,
        // even if their unique fields aren't in this simple DB table.
        // This is for demonstrating polymorphism as per the document's OOP requirements.
        // The specific attributes (brand, warranty, size, etc.) would be set to defaults or null
        // if not fetched from a more complex DB schema.
        if ("Electronics".equalsIgnoreCase(category)) {
            // For simplicity, we pass null/default for brand/warranty as they are not in the 'products' table
            return new ElectronicProduct(id, name, stock, price, supplier, null, 0);
        } else if ("Clothing".equalsIgnoreCase(category)) {
            // Pass null/default for size/color/material
            return new ClothingProduct(id, name, stock, price, supplier, null, null, null);
        } else {
            // For other categories or if no specific subclass handling is desired here
            return new Product(id, name, category, stock, price, supplier);
        }
    }
}
