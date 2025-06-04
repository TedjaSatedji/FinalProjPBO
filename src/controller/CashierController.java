package controller;

import model.Product;
import model.ProductDAO;
import model.Sale;
import model.SaleItem;
import model.SaleDAO;
import view.CashierView;
import view.ProductSearchView;
import main.Main; // Import Main class

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CashierController {
    private CashierView view;
    private ProductDAO productDAO;
    private SaleDAO saleDAO;
    private Sale currentSale;

    public CashierController(CashierView view, ProductDAO productDAO, SaleDAO saleDAO) {
        this.view = view;
        this.productDAO = productDAO;
        this.saleDAO = saleDAO;
        this.currentSale = new Sale();

        this.view.addAddItemButtonListener(new AddItemListener());
        this.view.addProductIdFieldEnterListener(new AddItemListener());
        this.view.addQuantityFieldEnterListener(new AddItemListener());
        this.view.addCompleteSaleButtonListener(new CompleteSaleListener());
        this.view.addNewSaleButtonListener(new NewSaleListener());
        this.view.addSearchProductButtonListener(new SearchProductListener());
        this.view.addBackToMenuButtonListener(new BackToMenuListener()); // New listener

        this.view.setCashierName("Default Cashier");
        this.view.clearCartDisplay();
    }

    private void refreshCartView() {
        view.displayCartItems(currentSale.getItems());
        view.updateTotalAmountDisplay(currentSale.getTotalAmount());
    }

    // ... (AddItemListener, CompleteSaleListener, etc. remain the same) ...

    class AddItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String productIdOrName = view.getProductIdOrNameText().trim();
            String quantityStr = view.getQuantityText().trim();

            if (productIdOrName.isEmpty()) {
                view.showMessage("Please enter Product ID or Name.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (quantityStr.isEmpty()) {
                view.showMessage("Please enter Quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    view.showMessage("Quantity must be a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                view.showMessage("Invalid quantity format.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product = null;
            try {
                int productId = Integer.parseInt(productIdOrName);
                product = productDAO.getProductById(productId);
            } catch (NumberFormatException ex) {
                List<Product> products = productDAO.searchProducts(productIdOrName, "name");
                if (!products.isEmpty()) {
                    if (products.size() == 1) {
                        product = products.get(0);
                    } else {
                        Product selectedProduct = (Product) JOptionPane.showInputDialog(
                                view, "Multiple products found. Please select one:",
                                "Select Product", JOptionPane.PLAIN_MESSAGE, null,
                                products.toArray(), products.get(0)
                        );
                        if (selectedProduct != null) product = selectedProduct;
                        else { view.showMessage("Product selection cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE); return; }
                    }
                }
            }

            if (product == null) {
                view.showMessage("Product not found: " + productIdOrName, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (product.getStock() < quantity) {
                view.showMessage("Insufficient stock for " + product.getName() + ". Available: " + product.getStock(), "Stock Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean itemUpdated = false;
            for(SaleItem item : currentSale.getItems()){
                if(item.getProductId() == product.getId()){
                    if(product.getStock() < (item.getQuantitySold() + quantity) ){
                        view.showMessage("Insufficient stock for " + product.getName() + " to add more. Available: " + product.getStock() + ", In Cart: " + item.getQuantitySold(), "Stock Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    item.setQuantitySold(item.getQuantitySold() + quantity);
                    itemUpdated = true;
                    break;
                }
            }

            if(!itemUpdated){
                SaleItem saleItem = new SaleItem(product.getId(), product.getName(), quantity, product.getPrice());
                currentSale.addItem(saleItem);
            }

            refreshCartView();
            view.getProductIdOrNameFieldComponent().setText("");
            view.getQuantityFieldComponent().setText("1");
            view.getProductIdOrNameFieldComponent().requestFocusInWindow();
        }
    }

    class CompleteSaleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentSale.getItems().isEmpty()) {
                view.showMessage("Cart is empty. Add items to complete sale.", "Empty Cart", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (!view.confirmAction("Confirm to complete sale for " + String.format("Rp %.2f",currentSale.getTotalAmount()) + "?", "Complete Sale")) {
                return;
            }

            currentSale.setCashierName(view.getTitle());

            if (saleDAO.recordSale(currentSale)) {
                view.showMessage("Sale completed successfully! Sale ID: " + currentSale.getSaleId(), "Sale Success", JOptionPane.INFORMATION_MESSAGE);
                startNewSale();
            } else {
                view.showMessage("Failed to complete sale. Stock might have changed or database error.", "Sale Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void startNewSale(){
        currentSale = new Sale();
        view.clearCartDisplay();
        view.getProductIdOrNameFieldComponent().requestFocusInWindow();
    }

    class NewSaleListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!currentSale.getItems().isEmpty()) {
                if (view.confirmAction("Are you sure you want to clear the current cart and start a new sale?", "New Sale Confirmation")) {
                    startNewSale();
                }
            } else {
                startNewSale();
            }
        }
    }

    class SearchProductListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ProductSearchView searchView = new ProductSearchView(view, productDAO);
            searchView.setVisible(true);

            Product selectedProduct = searchView.getSelectedProduct();
            if (selectedProduct != null) {
                view.getProductIdOrNameFieldComponent().setText(String.valueOf(selectedProduct.getId()));
                view.getQuantityFieldComponent().requestFocusInWindow();
            }
        }
    }

    class BackToMenuListener implements ActionListener { // New Listener
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose(); // Close the current cashier view
            Main.showRoleSelection(); // Call the static method in Main to show role selection
        }
    }
}
