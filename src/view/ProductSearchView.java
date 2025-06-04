package view;

import model.Product;
import model.ProductDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ProductSearchView extends JDialog {
    private ProductDAO productDAO;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchCriteriaComboBox; // New JComboBox for search criteria
    private JButton searchButton;
    private JButton selectButton;
    private JButton cancelButton;
    private Product selectedProduct = null;

    // --- Styling Constants ---
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 13);
    private static final Font FIELD_FONT = new Font("Arial", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font TABLE_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Color BACKGROUND_COLOR = new Color(240, 245, 250); // Consistent light blue-gray
    private static final Color PANEL_BACKGROUND_COLOR = Color.WHITE;
    private static final Color BUTTON_PRIMARY_COLOR = new Color(0, 123, 255); // Blue
    private static final Color BUTTON_SUCCESS_COLOR = new Color(40, 167, 69); // Green
    private static final Color BUTTON_SECONDARY_COLOR = new Color(108, 117, 125); // Gray
    private static final Color BUTTON_TEXT_COLOR = Color.BLACK;
    private static final Color TABLE_HEADER_BG_COLOR = new Color(70, 130, 180); // Steel Blue
    private static final Color TABLE_HEADER_FG_COLOR = Color.WHITE;
    private static final Color TABLE_TEXT_COLOR = Color.BLACK;


    public ProductSearchView(Frame owner, ProductDAO dao) {
        super(owner, "Search Product", true); // true for modal dialog
        this.productDAO = dao;
        setSize(700, 480); // Adjusted size for new component
        setLocationRelativeTo(owner);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10)); // Add gaps
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding for the dialog

        initComponents();
        styleComponents();
        layoutComponents();
        loadAllProducts(); // Load initial list
    }

    private void initComponents() {
        searchField = new JTextField(20); // Adjusted size
        searchCriteriaComboBox = new JComboBox<>(new String[]{"Name", "Category"}); // Initialize JComboBox
        searchButton = new JButton("Search");
        selectButton = new JButton("Select Product");
        selectButton.setEnabled(false); // Enabled when a product is selected
        cancelButton = new JButton("Cancel");

        String[] columnNames = {"ID", "Name", "Category", "Stock", "Price (Rp)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowHeight(24);
        productTable.getTableHeader().setReorderingAllowed(false);


        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch()); // Search on Enter in text field
        searchCriteriaComboBox.addActionListener(e -> performSearch()); // Optional: search when criteria changes
        selectButton.addActionListener(e -> selectAndClose());
        cancelButton.addActionListener(e -> dispose());

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                selectButton.setEnabled(true);
            } else {
                selectButton.setEnabled(false);
            }
        });

        productTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && productTable.getSelectedRow() != -1) { // Double-click
                    selectAndClose();
                }
            }
        });
    }

    private void styleComponents() {
        searchField.setFont(FIELD_FONT);
        searchCriteriaComboBox.setFont(FIELD_FONT); // Style the JComboBox

        styleTable(productTable);

        styleButton(searchButton, BUTTON_PRIMARY_COLOR, BUTTON_TEXT_COLOR);
        styleButton(selectButton, BUTTON_SUCCESS_COLOR, BUTTON_TEXT_COLOR);
        styleButton(cancelButton, BUTTON_SECONDARY_COLOR, BUTTON_TEXT_COLOR);

        // Table Header
        JTableHeader tableHeader = productTable.getTableHeader();
        tableHeader.setBackground(TABLE_HEADER_BG_COLOR);
        tableHeader.setForeground(TABLE_TEXT_COLOR);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 30)); // Header height
    }

    private void styleTable(JTable table) {
        table.setFont(TABLE_FONT);
        table.setRowHeight(22);
        table.getTableHeader().setFont(LABEL_FONT);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(TABLE_HEADER_BG_COLOR);
        tableHeader.setForeground(TABLE_HEADER_FG_COLOR);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 28));
        table.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(TABLE_TEXT_COLOR);
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setBackground(row % 2 == 0 ? PANEL_BACKGROUND_COLOR : new Color(245,245,245)); // Alternate row color
                }
                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); // Cell padding
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, customRenderer);
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                new EmptyBorder(8, 15, 8, 15)
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

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(Color.DARK_GRAY);
        return label;
    }


    private void layoutComponents() {
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(new EmptyBorder(5,0,5,0));
        searchPanel.add(createStyledLabel("Search Term:")); // Changed label
        searchPanel.add(searchField);
        searchPanel.add(createStyledLabel("By:")); // Label for ComboBox
        searchPanel.add(searchCriteriaComboBox); // Add ComboBox
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Table Panel
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setBorder(BorderFactory.createEtchedBorder());
        tableScrollPane.getViewport().setBackground(PANEL_BACKGROUND_COLOR);
        add(tableScrollPane, BorderLayout.CENTER);

        // Bottom Button Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(5,0,0,0));
        bottomPanel.add(selectButton);
        bottomPanel.add(cancelButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadAllProducts() {
        List<Product> products = productDAO.getAllProducts();
        displayProductsInTable(products);
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        String searchBy = ((String) searchCriteriaComboBox.getSelectedItem()).toLowerCase(); // Get selected criteria

        if (searchTerm.isEmpty()) {
            loadAllProducts(); // If search term is empty, show all products
            return;
        }

        List<Product> products = productDAO.searchProducts(searchTerm, searchBy);
        displayProductsInTable(products);
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products found matching '" + searchTerm + "' by " + searchBy + ".", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void displayProductsInTable(List<Product> products) {
        tableModel.setRowCount(0);
        if (products != null) {
            for (Product p : products) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getName(),
                        p.getCategory(),
                        p.getStock(),
                        String.format("%,.0f", p.getPrice())
                });
            }
        }
    }

    private void selectAndClose() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            this.selectedProduct = productDAO.getProductById(productId);
            if (this.selectedProduct == null) {
                JOptionPane.showMessageDialog(this, "Selected product (ID: " + productId + ") no longer exists.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Product getSelectedProduct() {
        return this.selectedProduct;
    }
}
