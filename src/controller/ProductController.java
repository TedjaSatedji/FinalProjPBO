package controller;

import model.*;
import view.MainView;
import main.Main; // Import Main class to call showRoleSelection

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductController {
    private MainView view;
    private ProductDAO dao;

    public ProductController(MainView view, ProductDAO dao) {
        this.view = view;
        this.dao = dao;

        this.view.addAddButtonListener(new AddProductListener());
        this.view.addUpdateButtonListener(new UpdateProductListener());
        this.view.addDeleteButtonListener(new DeleteProductListener());
        this.view.addClearButtonListener(new ClearFieldsListener());
        this.view.addSearchButtonListener(new SearchProductListener());
        this.view.addProductTableSelectionListener(new TableRowSelectionListener());
        this.view.addBackToMenuButtonListener(new BackToMenuListener()); // Listener for Back to Menu

        loadAllProducts();
    }

    private void loadAllProducts() {
        List<Product> products = dao.getAllProducts();
        view.displayProducts(products);
        view.clearInputFields();
    }

    // --- Listener Inner Classes ---
    class AddProductListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = view.getNameFieldText();
                String category = view.getSelectedCategory();
                int stock = Integer.parseInt(view.getStockFieldText());
                double price = Double.parseDouble(view.getPriceFieldText());
                String supplier = view.getSupplierFieldText();

                if (name.isEmpty()) {
                    view.showMessage("Product name cannot be empty.");
                    return;
                }
                if (supplier.isEmpty()) {
                    view.showMessage("Supplier cannot be empty.");
                    return;
                }

                Product product;
                if ("Electronics".equalsIgnoreCase(category)) {
                    product = new ElectronicProduct(0, name, stock, price, supplier, "DefaultBrand", 0);
                } else if ("Clothing".equalsIgnoreCase(category)) {
                    product = new ClothingProduct(0, name, stock, price, supplier, "N/A", "N/A", "N/A");
                } else {
                    product = new Product(0, name, category, stock, price, supplier);
                }

                if (dao.addProduct(product)) {
                    view.showMessage("Product added successfully! ID: " + product.getId());
                    loadAllProducts();
                } else {
                    view.showMessage("Failed to add product.");
                }
            } catch (NumberFormatException ex) {
                view.showMessage("Invalid input: Stock must be an integer, Price must be a number.");
            } catch (IllegalArgumentException ex) {
                view.showMessage("Error: " + ex.getMessage());
            }
        }
    }

    class UpdateProductListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (view.getIdFieldText().isEmpty()) {
                    view.showMessage("Please select a product to update or ensure ID is filled.");
                    return;
                }
                int id = Integer.parseInt(view.getIdFieldText());
                String name = view.getNameFieldText();
                String category = view.getSelectedCategory();
                int stock = Integer.parseInt(view.getStockFieldText());
                double price = Double.parseDouble(view.getPriceFieldText());
                String supplier = view.getSupplierFieldText();

                if (name.isEmpty() || supplier.isEmpty()) {
                    view.showMessage("Name and Supplier cannot be empty.");
                    return;
                }

                Product product;
                if ("Electronics".equalsIgnoreCase(category)) {
                    product = new ElectronicProduct(id, name, stock, price, supplier, "DefaultBrand", 0);
                } else if ("Clothing".equalsIgnoreCase(category)) {
                    product = new ClothingProduct(id, name, stock, price, supplier, "N/A", "N/A", "N/A");
                } else {
                    product = new Product(id, name, category, stock, price, supplier);
                }

                if (dao.updateProduct(product)) {
                    view.showMessage("Product updated successfully!");
                    loadAllProducts();
                } else {
                    view.showMessage("Failed to update product. Make sure the ID is correct.");
                }
            } catch (NumberFormatException ex) {
                view.showMessage("Invalid input: ID/Stock must be integers, Price must be a number.");
            } catch (IllegalArgumentException ex) {
                view.showMessage("Error: " + ex.getMessage());
            }
        }
    }

    class DeleteProductListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int productId = -1;
                if (!view.getIdFieldText().isEmpty()) {
                    productId = Integer.parseInt(view.getIdFieldText());
                } else {
                    productId = view.getSelectedProductIdFromTable();
                }

                if (productId == -1) {
                    view.showMessage("Please select a product from the table or enter an ID to delete.");
                    return;
                }

                int confirmation = javax.swing.JOptionPane.showConfirmDialog(view,
                        "Are you sure you want to delete product ID: " + productId + "?",
                        "Confirm Deletion", javax.swing.JOptionPane.YES_NO_OPTION);
                if (confirmation == javax.swing.JOptionPane.YES_OPTION) {
                    if (dao.deleteProduct(productId)) {
                        view.showMessage("Product deleted successfully!");
                        loadAllProducts();
                    } else {
                        view.showMessage("Failed to delete product.");
                    }
                }
            } catch (NumberFormatException ex) {
                view.showMessage("Invalid Product ID for deletion.");
            }
        }
    }

    class SearchProductListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchTerm = view.getSearchTerm();
            String searchBy = view.getSearchCategory().toLowerCase();

            if (searchTerm.isEmpty()) {
                view.showMessage("Please enter a search term.");
                loadAllProducts();
                return;
            }
            List<Product> products = dao.searchProducts(searchTerm, searchBy);
            if (products.isEmpty()) {
                view.showMessage("No products found matching your search criteria.");
            }
            view.displayProducts(products);
        }
    }

    class ClearFieldsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.clearInputFields();
            loadAllProducts();
        }
    }

    class TableRowSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting() && view.getProductTable().getSelectedRow() != -1) {
                int selectedRow = view.getProductTable().getSelectedRow();

                view.setIdFieldText(view.getTableModel().getValueAt(selectedRow, 0).toString());
                view.setNameFieldText(view.getTableModel().getValueAt(selectedRow, 1).toString());
                view.setCategoryComboBox(view.getTableModel().getValueAt(selectedRow, 2).toString());
                view.setStockFieldText(view.getTableModel().getValueAt(selectedRow, 3).toString());
                view.setPriceFieldText(view.getTableModel().getValueAt(selectedRow, 4).toString());
                view.setSupplierFieldText(view.getTableModel().getValueAt(selectedRow, 5).toString());

                view.setUpdateDeleteButtonState(true);
            }
        }
    }

    class BackToMenuListener implements ActionListener { // Listener for the "Back to Main Menu" button
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose(); // Close the current inventory view (MainView)
            Main.showRoleSelection(); // Call the static method in Main to show role selection dialog
        }
    }
}
