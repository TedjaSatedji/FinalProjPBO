package view;

import model.Product;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class MainView extends JFrame {

    // UI Components
    private JTextField idField;
    private JTextField nameField;
    private JComboBox<String> categoryComboBox;
    private JTextField stockField;
    private JTextField priceField;
    private JTextField supplierField;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton searchButton;
    private JTextField searchField;
    private JComboBox<String> searchCategoryComboBox;
    private JButton backToMenuButton;

    private JTable productTable;
    private DefaultTableModel tableModel;

    // --- Styling Constants ---
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 13);
    private static final Font FIELD_FONT = new Font("Arial", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Color BACKGROUND_COLOR = new Color(183, 226, 232); // Light blue-gray
    private static final Color PANEL_BACKGROUND_COLOR = new Color(250, 250, 255); // Off-white
    private static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel Blue
    private static final Color BUTTON_TEXT_COLOR = Color.BLACK;
    private static final Color TABLE_HEADER_BG_COLOR = new Color(100, 149, 237); // Cornflower Blue
    private static final Color TABLE_HEADER_FG_COLOR = Color.WHITE;
    private static final Color TABLE_TEXT_COLOR = Color.BLACK;

    public MainView() {
        setTitle("Retail Inventory Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700); // Slightly larger for better spacing
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        initComponents();
        styleComponents();
        layoutComponents();
    }

    private void initComponents() {
        idField = new JTextField(7);
        idField.setEditable(false);
        nameField = new JTextField(22);
        categoryComboBox = new JComboBox<>(new String[]{"Electronics", "Clothing", "Stationery", "Food", "Books", "Generic"});
        stockField = new JTextField(8);
        priceField = new JTextField(12);
        supplierField = new JTextField(22);

        addButton = new JButton("Add Product");
        updateButton = new JButton("Update Product");
        deleteButton = new JButton("Delete Product");
        clearButton = new JButton("Clear Fields");

        searchField = new JTextField(18);
        searchCategoryComboBox = new JComboBox<>(new String[]{"Name", "Category"});
        searchButton = new JButton("Search");
        backToMenuButton = new JButton("Back to Main Menu");

        String[] columnNames = {"ID", "Name", "Category", "Stock", "Price (Rp)", "Supplier"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowHeight(25); // Increase row height
        productTable.getTableHeader().setReorderingAllowed(false);


        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    private void styleComponents() {
        // Fonts
        idField.setFont(FIELD_FONT);
        nameField.setFont(FIELD_FONT);
        categoryComboBox.setFont(FIELD_FONT);
        stockField.setFont(FIELD_FONT);
        priceField.setFont(FIELD_FONT);
        supplierField.setFont(FIELD_FONT);
        searchField.setFont(FIELD_FONT);
        searchCategoryComboBox.setFont(FIELD_FONT);
        productTable.setFont(FIELD_FONT);
        productTable.getTableHeader().setFont(LABEL_FONT);
        productTable.setForeground(TABLE_TEXT_COLOR);

        // Buttons
        styleButton(addButton);
        styleButton(updateButton);
        styleButton(deleteButton);
        styleButton(clearButton);
        styleButton(searchButton);
        styleButton(backToMenuButton, new Color(220, 53, 69), BUTTON_TEXT_COLOR); // Reddish for back/exit

        // Table Header
        JTableHeader tableHeader = productTable.getTableHeader();
        tableHeader.setBackground(TABLE_HEADER_BG_COLOR);
        tableHeader.setForeground(TABLE_TEXT_COLOR);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 30)); // Header height

        // ComboBox custom renderer for better appearance (optional)
        // categoryComboBox.setRenderer(new DefaultListCellRenderer() { ... });
    }

    private void styleButton(JButton button) {
        styleButton(button, BUTTON_COLOR, BUTTON_TEXT_COLOR);
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1), // Subtle border
                new EmptyBorder(8, 15, 8, 15) // Padding
        ));
        // Optional: Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }


    private void layoutComponents() {
        setLayout(new BorderLayout(15, 15)); // Increased gaps
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding for the whole frame

        // --- Input Panel (Top) ---
        JPanel inputFormPanel = new JPanel(new GridBagLayout());
        inputFormPanel.setBackground(PANEL_BACKGROUND_COLOR);
        inputFormPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Product Details", 0, 0, LABEL_FONT, Color.DARK_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Increased padding
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow fields to expand a bit

        // Helper method to add label and field
        addLabelAndField(inputFormPanel, gbc, "ID:", idField, 0, 0);
        addLabelAndField(inputFormPanel, gbc, "Name:", nameField, 2, 0);
        addLabelAndField(inputFormPanel, gbc, "Category:", categoryComboBox, 0, 1);
        addLabelAndField(inputFormPanel, gbc, "Stock:", stockField, 2, 1);
        addLabelAndField(inputFormPanel, gbc, "Price (Rp):", priceField, 0, 2);
        addLabelAndField(inputFormPanel, gbc, "Supplier:", supplierField, 2, 2);

        JPanel crudButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        crudButtonPanel.setBackground(PANEL_BACKGROUND_COLOR);
        crudButtonPanel.add(addButton);
        crudButtonPanel.add(updateButton);
        crudButtonPanel.add(deleteButton);
        crudButtonPanel.add(clearButton);

        JPanel topPanelContainer = new JPanel(new BorderLayout(10,10));
        topPanelContainer.setBackground(BACKGROUND_COLOR); // Match frame background
        topPanelContainer.add(inputFormPanel, BorderLayout.NORTH);
        topPanelContainer.add(crudButtonPanel, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(PANEL_BACKGROUND_COLOR);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Search Products",0,0, LABEL_FONT, Color.DARK_GRAY),
                new EmptyBorder(5,5,5,5)
        ));
        searchPanel.add(createStyledLabel("Search Term:"));
        searchPanel.add(searchField);
        searchPanel.add(createStyledLabel("Search By:"));
        searchPanel.add(searchCategoryComboBox);
        searchPanel.add(searchButton);
        topPanelContainer.add(searchPanel, BorderLayout.SOUTH);

        add(topPanelContainer, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        scrollPane.getViewport().setBackground(Color.WHITE); // Table background
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomButtonPanel.setBackground(BACKGROUND_COLOR);
        bottomButtonPanel.add(backToMenuButton);
        add(bottomButtonPanel, BorderLayout.SOUTH);
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int x, int y) {
        JLabel label = createStyledLabel(labelText);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = 0.1; // Give some weight to label column
        panel.add(label, gbc);

        gbc.gridx = x + 1;
        gbc.weightx = 0.9; // More weight to field column
        panel.add(field, gbc);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(Color.DARK_GRAY);
        return label;
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
        tableModel.setRowCount(0);
        for (Product p : products) {
            Object[] rowData = {
                    p.getId(), p.getName(), p.getCategory(),
                    p.getStock(), "Rp " + String.format("%,.0f", p.getPrice()), // Format price
                    p.getSupplier()
            };
            tableModel.addRow(rowData);
        }
    }

    public void clearInputFields() {
        idField.setText("");
        nameField.setText("");
        categoryComboBox.setSelectedIndex(0);
        stockField.setText("");
        priceField.setText("");
        supplierField.setText("");
        searchField.setText("");
        productTable.clearSelection();
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    public void showMessage(String message) {
        // Use a slightly more styled dialog
        JLabel label = new JLabel(message);
        label.setFont(FIELD_FONT);
        JOptionPane.showMessageDialog(this, label, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage(String message) {
        JLabel label = new JLabel(message);
        label.setFont(FIELD_FONT);
        JOptionPane.showMessageDialog(this, label, "Error", JOptionPane.ERROR_MESSAGE);
    }


    public int getSelectedProductIdFromTable() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) tableModel.getValueAt(selectedRow, 0);
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
    public void addBackToMenuButtonListener(java.awt.event.ActionListener listener) { backToMenuButton.addActionListener(listener); }
    public void addProductTableSelectionListener(javax.swing.event.ListSelectionListener listener) {
        productTable.getSelectionModel().addListSelectionListener(listener);
    }
}
