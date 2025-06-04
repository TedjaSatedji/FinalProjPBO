package view;

import model.SaleItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
    private JButton backToMenuButton;

    private JLabel cashierNameLabel;

    // --- Styling Constants (can be shared or defined per view) ---
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 13);
    private static final Font FIELD_FONT = new Font("Arial", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font TOTAL_LABEL_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Color BACKGROUND_COLOR = new Color(183, 226, 232); // Light Gray
    private static final Color PANEL_BACKGROUND_COLOR = Color.WHITE;
    private static final Color BUTTON_PRIMARY_COLOR = new Color(0, 123, 255); // Blue
    private static final Color BUTTON_SUCCESS_COLOR = new Color(40, 167, 69); // Green
    private static final Color BUTTON_WARNING_COLOR = new Color(255, 193, 7); // Yellow
    private static final Color BUTTON_DANGER_COLOR = new Color(220, 53, 69); // Red
    private static final Color BUTTON_TEXT_COLOR = Color.BLACK;
    private static final Color TABLE_HEADER_BG_COLOR = new Color(52, 58, 64); // Dark Gray
    private static final Color TABLE_HEADER_FG_COLOR = Color.WHITE;
    private static final Color TABLE_TEXT_COLOR = Color.BLACK;


    public CashierView() {
        setTitle("Point of Sale - Cashier");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        initComponents();
        styleComponents();
        layoutComponents();
    }

    private void initComponents() {
        productIdOrNameField = new JTextField(20);
        quantityField = new JTextField("1", 5);
        addItemButton = new JButton("Add to Cart"); // (+) Icon could be added

        String[] cartColumnNames = {"ID", "Product Name", "Qty", "Unit Price (Rp)", "Subtotal (Rp)"};
        cartTableModel = new DefaultTableModel(cartColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(25);
        cartTable.getTableHeader().setReorderingAllowed(false);

        totalAmountLabel = new JLabel("Total: Rp 0.00");

        completeSaleButton = new JButton("Complete Sale"); // (✓) Icon
        newSaleButton = new JButton("New Sale");      // (Clear) Icon
        searchProductButton = new JButton("Find Product"); // (🔍) Icon
        backToMenuButton = new JButton("Back to Menu"); // (←) Icon

        cashierNameLabel = new JLabel("Cashier: [Default]");
    }

    private void styleComponents() {
        // Fonts
        productIdOrNameField.setFont(FIELD_FONT);
        quantityField.setFont(FIELD_FONT);
        cartTable.setFont(FIELD_FONT);
        cartTable.getTableHeader().setFont(LABEL_FONT);
        cartTable.setForeground(TABLE_TEXT_COLOR);
        totalAmountLabel.setFont(TOTAL_LABEL_FONT);
        totalAmountLabel.setForeground(new Color(50,50,50));
        cashierNameLabel.setFont(LABEL_FONT);

        // Buttons
        styleButton(addItemButton, BUTTON_PRIMARY_COLOR, BUTTON_TEXT_COLOR);
        styleButton(completeSaleButton, BUTTON_SUCCESS_COLOR, BUTTON_TEXT_COLOR);
        styleButton(newSaleButton, BUTTON_WARNING_COLOR, Color.DARK_GRAY); // Yellow with dark text
        styleButton(searchProductButton, new Color(108, 117, 125), BUTTON_TEXT_COLOR); // Gray
        styleButton(backToMenuButton, BUTTON_DANGER_COLOR, BUTTON_TEXT_COLOR);

        // Table Header
        JTableHeader tableHeader = cartTable.getTableHeader();
        tableHeader.setBackground(TABLE_HEADER_BG_COLOR);
        tableHeader.setForeground(TABLE_TEXT_COLOR);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 30));
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                new EmptyBorder(8, 18, 8, 18) // More padding for POS buttons
        ));
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
        setLayout(new BorderLayout(15, 15));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));


        // --- Top Panel: Product Input ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(PANEL_BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Scan/Enter Product",0,0, LABEL_FONT, Color.DARK_GRAY),
                new EmptyBorder(10,10,10,10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(createStyledLabel("Product ID/Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8; gbc.fill = GridBagConstraints.HORIZONTAL; inputPanel.add(productIdOrNameField, gbc);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; inputPanel.add(createStyledLabel("Qty:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; inputPanel.add(quantityField, gbc);
        gbc.gridx = 4; gbc.gridy = 0; gbc.gridwidth=2; inputPanel.add(addItemButton, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth=1; inputPanel.add(searchProductButton, gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(cashierNameLabel, gbc); // Display cashier name

        // --- Center Panel: Cart Table ---
        JScrollPane cartScrollPane = new JScrollPane(cartTable);
        cartScrollPane.setBorder(BorderFactory.createEtchedBorder());
        cartScrollPane.getViewport().setBackground(Color.WHITE);

        // --- Bottom Panel: Total and Actions ---
        JPanel bottomOuterPanel = new JPanel(new BorderLayout(10,10));
        bottomOuterPanel.setBackground(BACKGROUND_COLOR);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(PANEL_BACKGROUND_COLOR);
        totalPanel.setBorder(new EmptyBorder(10,10,10,10));
        totalPanel.add(totalAmountLabel);
        bottomOuterPanel.add(totalPanel, BorderLayout.NORTH);

        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionButtonPanel.setBackground(PANEL_BACKGROUND_COLOR);
        actionButtonPanel.setBorder(new EmptyBorder(5,5,10,5));
        actionButtonPanel.add(newSaleButton);
        actionButtonPanel.add(completeSaleButton);
        actionButtonPanel.add(backToMenuButton);
        bottomOuterPanel.add(actionButtonPanel, BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(cartScrollPane, BorderLayout.CENTER);
        add(bottomOuterPanel, BorderLayout.SOUTH);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(Color.DARK_GRAY);
        return label;
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
                item.getQuantitySold(), "Rp " + String.format("%,.0f", item.getPricePerUnit()),
                "Rp " + String.format("%,.0f", item.getSubtotal())
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
        totalAmountLabel.setText(String.format("Total: Rp %,.0f", total));
    }

    public void setCashierName(String name) {
        cashierNameLabel.setText("Cashier: " + name);
    }

    public void showMessage(String message, String title, int messageType) {
        JLabel label = new JLabel(message);
        label.setFont(FIELD_FONT);
        JOptionPane.showMessageDialog(this, label, title, messageType);
    }

    public boolean confirmAction(String message, String title) {
        JLabel label = new JLabel(message);
        label.setFont(FIELD_FONT);
        int response = JOptionPane.showConfirmDialog(this, label, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
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
    public void addBackToMenuButtonListener(java.awt.event.ActionListener listener) {
        backToMenuButton.addActionListener(listener);
    }
    public void addProductIdFieldEnterListener(java.awt.event.ActionListener listener) {
        productIdOrNameField.addActionListener(listener);
    }
    public void addQuantityFieldEnterListener(java.awt.event.ActionListener listener) {
        quantityField.addActionListener(listener);
    }
}
