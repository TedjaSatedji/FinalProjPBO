package view;

import model.SaleItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CashierView extends JFrame {

    private JTextField productIdOrNameField;
    private JTextField quantityField;
    private JButton addItemButton;

    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel totalAmountLabel;

    private JButton completeSaleButton;
    private JButton newSaleButton;
    private JButton searchProductButton;
    private JButton backToMenuButton; // New button

    private JLabel cashierNameLabel;

    public CashierView() {
        setTitle("Point of Sale - Cashier");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 650); // Adjusted size for new button
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        productIdOrNameField = new JTextField(15);
        quantityField = new JTextField("1", 5);
        addItemButton = new JButton("Add to Cart");

        String[] cartColumnNames = {"Product ID", "Product Name", "Qty", "Unit Price", "Subtotal"};
        cartTableModel = new DefaultTableModel(cartColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartTableModel);
        totalAmountLabel = new JLabel("Total: Rp 0.00");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));

        completeSaleButton = new JButton("Complete Sale");
        newSaleButton = new JButton("New Sale / Clear Cart");
        searchProductButton = new JButton("Find Product");
        backToMenuButton = new JButton("Back to Main Menu"); // New

        cashierNameLabel = new JLabel("Cashier: [Default]");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Scan/Enter Product"));
        inputPanel.add(new JLabel("Product ID/Name:"));
        inputPanel.add(productIdOrNameField);
        inputPanel.add(new JLabel("Qty:"));
        inputPanel.add(quantityField);
        inputPanel.add(addItemButton);
        inputPanel.add(searchProductButton);

        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBorder(BorderFactory.createTitledBorder("Current Sale Cart"));

        JPanel bottomPanel = new JPanel(new BorderLayout(10,5));
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(totalAmountLabel);
        bottomPanel.add(totalPanel, BorderLayout.NORTH);

        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionButtonPanel.add(newSaleButton);
        actionButtonPanel.add(completeSaleButton);
        // Add the Back to Menu button to the action panel or a separate one
        // For simplicity, adding to the same panel
        actionButtonPanel.add(backToMenuButton);
        bottomPanel.add(actionButtonPanel, BorderLayout.CENTER); // Changed to CENTER to accommodate more buttons

        add(inputPanel, BorderLayout.NORTH);
        add(cartScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- Getters for Controller ---
    public JTextField getProductIdOrNameFieldComponent() { return productIdOrNameField; }
    public JTextField getQuantityFieldComponent() { return quantityField; }
    public String getProductIdOrNameText() { return productIdOrNameField.getText(); }
    public String getQuantityText() { return quantityField.getText(); }
    public DefaultTableModel getCartTableModel() { return cartTableModel; }

    // --- Methods to update UI (called by Controller) ---
    public void clearCartDisplay() {
        cartTableModel.setRowCount(0);
        updateTotalAmountDisplay(0.0);
        productIdOrNameField.setText("");
        quantityField.setText("1");
        productIdOrNameField.requestFocusInWindow();
    }

    public void addCartItemToDisplay(SaleItem item) {
        cartTableModel.addRow(new Object[]{
                item.getProductId(), item.getProductName(),
                item.getQuantitySold(), String.format("%.2f", item.getPricePerUnit()),
                String.format("%.2f", item.getSubtotal())
        });
    }

    public void displayCartItems(List<SaleItem> items) {
        cartTableModel.setRowCount(0);
        double currentTotal = 0.0;
        for (SaleItem item : items) {
            addCartItemToDisplay(item);
            currentTotal += item.getSubtotal();
        }
        updateTotalAmountDisplay(currentTotal);
    }

    public void updateTotalAmountDisplay(double total) {
        totalAmountLabel.setText(String.format("Total: Rp %.2f", total));
    }

    public void setCashierName(String name) {
        cashierNameLabel.setText("Cashier: " + name);
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public boolean confirmAction(String message, String title) {
        int response = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return response == JOptionPane.YES_OPTION;
    }

    // --- ActionListeners (to be set by Controller) ---
    public void addAddItemButtonListener(java.awt.event.ActionListener listener) {
        addItemButton.addActionListener(listener);
    }
    public void addCompleteSaleButtonListener(java.awt.event.ActionListener listener) {
        completeSaleButton.addActionListener(listener);
    }
    public void addNewSaleButtonListener(java.awt.event.ActionListener listener) {
        newSaleButton.addActionListener(listener);
    }
    public void addSearchProductButtonListener(java.awt.event.ActionListener listener) {
        searchProductButton.addActionListener(listener);
    }
    public void addBackToMenuButtonListener(java.awt.event.ActionListener listener) { // New
        backToMenuButton.addActionListener(listener);
    }
    public void addProductIdFieldEnterListener(java.awt.event.ActionListener listener) {
        productIdOrNameField.addActionListener(listener);
    }
    public void addQuantityFieldEnterListener(java.awt.event.ActionListener listener) {
        quantityField.addActionListener(listener);
    }
}
