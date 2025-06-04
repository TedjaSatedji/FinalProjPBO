package view;

import model.Product;
import model.ProductDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JButton searchButton;
    private JButton selectButton;
    private Product selectedProduct = null;

    public ProductSearchView(Frame owner, ProductDAO dao) {
        super(owner, "Search Product", true); // true for modal dialog
        this.productDAO = dao;
        setSize(500, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(5, 5));

        initComponents();
        layoutComponents();
        loadAllProducts(); // Load initial list
    }

    private void initComponents() {
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        selectButton = new JButton("Select Product");
        selectButton.setEnabled(false); // Enabled when a product is selected

        String[] columnNames = {"ID", "Name", "Category", "Stock", "Price"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        searchButton.addActionListener(e -> performSearch());
        selectButton.addActionListener(e -> selectAndClose());

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                selectButton.setEnabled(true);
            } else {
                selectButton.setEnabled(false);
            }
        });

        productTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click
                    selectAndClose();
                }
            }
        });
    }

    private void layoutComponents() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JScrollPane tableScrollPane = new JScrollPane(productTable);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(selectButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        bottomPanel.add(cancelButton);

        add(searchPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadAllProducts() {
        List<Product> products = productDAO.getAllProducts();
        displayProductsInTable(products);
    }

    private void performSearch() {
        String searchTerm = searchField.getText();
        if (searchTerm.isEmpty()) {
            loadAllProducts();
            return;
        }
        // Default search by name, can add a JComboBox for category search too
        List<Product> products = productDAO.searchProducts(searchTerm, "name");
        displayProductsInTable(products);
    }

    private void displayProductsInTable(List<Product> products) {
        tableModel.setRowCount(0); // Clear table
        if (products != null) {
            for (Product p : products) {
                tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getCategory(), p.getStock(), p.getPrice()});
            }
        }
    }

    private void selectAndClose() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            int productId = (int) tableModel.getValueAt(selectedRow, 0);
            // Fetch the full product object to return
            this.selectedProduct = productDAO.getProductById(productId);
            dispose(); // Close the dialog
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    public Product getSelectedProduct() {
        return this.selectedProduct;
    }
}
