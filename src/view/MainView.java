package view;

import model.Product; // For JList/TableModel
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * MainView.java (View - Formerly ProductView.java)
 * GUI for product management, aligned with the document's simpler product structure.
 * Fields: id, name, category, stock, price, supplier.
 */
public class MainView extends JFrame {

    // UI Components
    private JTextField idField;
    private JTextField nameField;
    private JComboBox<String> categoryComboBox; // Can still be used for category input
    private JTextField stockField;
    private JTextField priceField;
    private JTextField supplierField;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton; // Added search button
    private JTextField searchField; // Field for search term
    private JComboBox<String> searchCategoryComboBox; // To select search by name or category

    private JTable productTable;
    private DefaultTableModel tableModel;


    public MainView() {
        setTitle("Retail Inventory Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600); // Adjusted size
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        idField = new JTextField(5);
        idField.setEditable(false);
        nameField = new JTextField(20);
        // Example categories, can be populated dynamically or from a fixed list
        categoryComboBox = new JComboBox<>(new String[]{"Electronics", "Clothing", "Stationery", "Food", "Books", "Generic"});
        stockField = new JTextField(7);
        priceField = new JTextField(10);
        supplierField = new JTextField(20);

        addButton = new JButton("Add Product");
        updateButton = new JButton("Update Product");
        deleteButton = new JButton("Delete Product");
        clearButton = new JButton("Clear Fields");

        searchField = new JTextField(15);
        searchCategoryComboBox = new JComboBox<>(new String[]{"Name", "Category"});
        searchButton = new JButton("Search");


        // Table columns based on the document's schema
        String[] columnNames = {"ID", "Name", "Category", "Stock", "Price", "Supplier"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        // --- Input Panel (Top) ---
        JPanel inputFormPanel = new JPanel(new GridBagLayout());
        inputFormPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: ID and Name
        gbc.gridx = 0; gbc.gridy = 0; inputFormPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputFormPanel.add(idField, gbc);
        gbc.gridx = 2; gbc.gridy = 0; inputFormPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; inputFormPanel.add(nameField, gbc);

        // Row 1: Category and Stock
        gbc.gridx = 0; gbc.gridy = 1; inputFormPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputFormPanel.add(categoryComboBox, gbc);
        gbc.gridx = 2; gbc.gridy = 1; inputFormPanel.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; inputFormPanel.add(stockField, gbc);

        // Row 2: Price and Supplier
        gbc.gridx = 0; gbc.gridy = 2; inputFormPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; inputFormPanel.add(priceField, gbc);
        gbc.gridx = 2; gbc.gridy = 2; inputFormPanel.add(new JLabel("Supplier:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; inputFormPanel.add(supplierField, gbc);

        // --- Button Panel (Below Input Form) ---
        JPanel crudButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        crudButtonPanel.add(addButton);
        crudButtonPanel.add(updateButton);
        crudButtonPanel.add(deleteButton);
        crudButtonPanel.add(clearButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputFormPanel, BorderLayout.NORTH);
        topPanel.add(crudButtonPanel, BorderLayout.CENTER);

        // --- Search Panel (Below CRUD buttons) ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Products"));
        searchPanel.add(new JLabel("Search Term:"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Search By:"));
        searchPanel.add(searchCategoryComboBox);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.SOUTH);


        add(topPanel, BorderLayout.NORTH);

        // --- Table Panel (Center/South) ---
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Product List"));
        add(scrollPane, BorderLayout.CENTER);
    }

    // --- Getters for Controller ---
    public String getIdFieldText() { return idField.getText(); }
    public String getNameFieldText() { return nameField.getText(); }
    public String getSelectedCategory() { return (String) categoryComboBox.getSelectedItem(); }
    public String getStockFieldText() { return stockField.getText(); }
    public String getPriceFieldText() { return priceField.getText(); }
    public String getSupplierFieldText() { return supplierField.getText(); }
    public String getSearchTerm() { return searchField.getText(); }
    public String getSearchCategory() { return (String) searchCategoryComboBox.getSelectedItem(); }


    public JTable getProductTable() { return productTable; }
    public DefaultTableModel getTableModel() { return tableModel; }

    // --- Setters for Controller ---
    public void setIdFieldText(String id) { idField.setText(id); }
    public void setNameFieldText(String name) { nameField.setText(name); }
    public void setCategoryComboBox(String category) { categoryComboBox.setSelectedItem(category); }
    public void setStockFieldText(String stock) { stockField.setText(stock); }
    public void setPriceFieldText(String price) { priceField.setText(price); }
    public void setSupplierFieldText(String supplier) { supplierField.setText(supplier); }


    public void displayProducts(List<Product> products) {
        tableModel.setRowCount(0); // Clear existing rows
        for (Product p : products) {
            // Using the common attributes from the Product class based on the document
            Object[] rowData = {
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    p.getStock(),
                    p.getPrice(),
                    p.getSupplier()
            };
            tableModel.addRow(rowData);
        }
    }

    public void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        categoryComboBox.setSelectedIndex(0); // Reset to the first category
        stockField.setText("");
        priceField.setText("");
        supplierField.setText("");
        searchField.setText(""); // Also clear search field
        productTable.clearSelection();
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public int getSelectedProductIdFromTable() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) tableModel.getValueAt(selectedRow, 0); // ID is in the first column
        }
        return -1;
    }

    public void setUpdateDeleteButtonState(boolean enabled) {
        updateButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
    }

    // --- ActionListeners ---
    public void addAddButtonListener(java.awt.event.ActionListener listener) { addButton.addActionListener(listener); }
    public void addUpdateButtonListener(java.awt.event.ActionListener listener) { updateButton.addActionListener(listener); }
    public void addDeleteButtonListener(java.awt.event.ActionListener listener) { deleteButton.addActionListener(listener); }
    public void addClearButtonListener(java.awt.event.ActionListener listener) { clearButton.addActionListener(listener); }
    public void addSearchButtonListener(java.awt.event.ActionListener listener) { searchButton.addActionListener(listener); }
    public void addProductTableSelectionListener(javax.swing.event.ListSelectionListener listener) {
        productTable.getSelectionModel().addListSelectionListener(listener);
    }
}
