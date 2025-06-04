package view;

import model.Sale;
import model.SaleItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class SalesHistoryView extends JFrame {

    private JTable salesTable;
    private DefaultTableModel salesTableModel;
    private JTable saleItemsTable;
    private DefaultTableModel saleItemsTableModel;
    private JButton backToMenuButton;
    private JLabel selectedSaleLabel;

    // Styling Constants (similar to other views)
    private static final Font HEADING_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 13);
    private static final Font TABLE_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Color BACKGROUND_COLOR = new Color(183, 226, 232);
    private static final Color PANEL_BACKGROUND_COLOR = Color.WHITE;
    private static final Color TABLE_HEADER_BG_COLOR = new Color(70, 130, 180); // Steel Blue
    private static final Color TABLE_HEADER_FG_COLOR = Color.WHITE;
    private static final Color TABLE_TEXT_COLOR = Color.BLACK; // Explicitly set table text to black
    private static final Color BUTTON_DANGER_COLOR = new Color(220, 53, 69);
    private static final Color BUTTON_TEXT_COLOR = Color.BLACK;

    public SalesHistoryView() {
        setTitle("Sales History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        initComponents();
        styleComponents();
        layoutComponents();
    }

    private void initComponents() {
        // Sales Table
        String[] salesColumnNames = {"Sale ID", "Date", "Total Amount (Rp)", "Cashier"};
        salesTableModel = new DefaultTableModel(salesColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        salesTable = new JTable(salesTableModel);
        salesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Sale Items Table
        String[] saleItemsColumnNames = {"Product ID", "Product Name", "Qty Sold", "Price/Unit (Rp)", "Subtotal (Rp)"};
        saleItemsTableModel = new DefaultTableModel(saleItemsColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        saleItemsTable = new JTable(saleItemsTableModel);

        selectedSaleLabel = new JLabel("Select a sale to view items.");
        backToMenuButton = new JButton("Back to Main Menu");
    }

    private void styleComponents() {
        selectedSaleLabel.setFont(HEADING_FONT);
        selectedSaleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Tables
        styleTable(salesTable);
        styleTable(saleItemsTable);

        // Button
        styleButton(backToMenuButton, BUTTON_DANGER_COLOR, BUTTON_TEXT_COLOR);

        // Table Header
        JTableHeader tableHeader = salesTable.getTableHeader();
        tableHeader.setBackground(TABLE_HEADER_BG_COLOR);
        tableHeader.setForeground(TABLE_TEXT_COLOR);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 30)); // Header height

        // Table Header
        JTableHeader tableHeader1 = saleItemsTable.getTableHeader();
        tableHeader1.setBackground(TABLE_HEADER_BG_COLOR);
        tableHeader1.setForeground(TABLE_TEXT_COLOR);
        tableHeader1.setPreferredSize(new Dimension(tableHeader.getWidth(), 30)); // Header height
    }

    private void styleTable(JTable table) {
        table.setFont(TABLE_FONT);
        // table.setForeground(TABLE_TEXT_COLOR); // Setting on table directly might be overridden

        table.setRowHeight(22);
        table.getTableHeader().setFont(LABEL_FONT);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(TABLE_HEADER_BG_COLOR);
        tableHeader.setForeground(TABLE_HEADER_FG_COLOR);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 28));
        table.getTableHeader().setReorderingAllowed(false);

        // --- MODIFICATION START: Apply color to default cell renderer ---
        TableCellRenderer renderer = table.getDefaultRenderer(Object.class);
        if (renderer instanceof Component) { // Check if it's a component (usually a JLabel based renderer)
            ((Component) renderer).setForeground(TABLE_TEXT_COLOR);
        }
        // For more specific control, you might need to iterate through column renderers
        // or set a custom renderer that respects this color.
        // This approach targets the most common case.
        //
        // Alternative: Create a custom renderer and apply it to all columns
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(TABLE_TEXT_COLOR); // Set text color here
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground()); // Keep selection background
                    c.setForeground(table.getSelectionForeground()); // Keep selection foreground
                } else {
                    c.setBackground(table.getBackground()); // Use table's default background
                }
                return c;
            }
        };

        // Apply the custom renderer to all columns of type Object (which is the default for most data)
        table.setDefaultRenderer(Object.class, customRenderer);
        // If you have columns with specific data types (e.g., Number.class),
        // you might need to set the renderer for those types as well:
        // table.setDefaultRenderer(Number.class, customRenderer);
        // --- MODIFICATION END ---
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(LABEL_FONT); // Using LABEL_FONT for consistency
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                new EmptyBorder(8, 18, 8, 18)
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
        setLayout(new BorderLayout(10, 10));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Sales Transaction History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(5,0,15,0));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JScrollPane salesScrollPane = new JScrollPane(salesTable);
        salesScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "All Sales",0,0,LABEL_FONT, Color.DARK_GRAY));
        salesScrollPane.getViewport().setBackground(PANEL_BACKGROUND_COLOR);

        JPanel itemsPanel = new JPanel(new BorderLayout(5,5));
        itemsPanel.setBackground(PANEL_BACKGROUND_COLOR);
        itemsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Items in Selected Sale",0,0,LABEL_FONT, Color.DARK_GRAY));
        selectedSaleLabel.setFont(LABEL_FONT);
        selectedSaleLabel.setBorder(new EmptyBorder(5,5,5,5));
        itemsPanel.add(selectedSaleLabel, BorderLayout.NORTH);
        JScrollPane itemsScrollPane = new JScrollPane(saleItemsTable);
        itemsScrollPane.getViewport().setBackground(PANEL_BACKGROUND_COLOR);
        itemsPanel.add(itemsScrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, salesScrollPane, itemsPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setBackground(BACKGROUND_COLOR);
        splitPane.setBorder(null);

        topPanel.add(splitPane, BorderLayout.CENTER);
        add(topPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.add(backToMenuButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void displaySales(List<Sale> sales) {
        salesTableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Sale sale : sales) {
            salesTableModel.addRow(new Object[]{
                    sale.getSaleId(),
                    dateFormat.format(sale.getSaleDate()),
                    String.format("%,.0f", sale.getTotalAmount()),
                    sale.getCashierName() != null ? sale.getCashierName() : "N/A"
            });
        }
    }

    public void displaySaleItems(List<SaleItem> items, int saleId) {
        saleItemsTableModel.setRowCount(0);
        if (items.isEmpty() && saleId > 0) {
            selectedSaleLabel.setText("No items found for Sale ID: " + saleId);
        } else if (saleId <= 0) {
            selectedSaleLabel.setText("Select a sale to view items.");
        }
        else {
            selectedSaleLabel.setText("Details for Sale ID: " + saleId);
        }

        for (SaleItem item : items) {
            saleItemsTableModel.addRow(new Object[]{
                    item.getProductId(),
                    item.getProductName(),
                    item.getQuantitySold(),
                    String.format("%,.0f", item.getPricePerUnit()),
                    String.format("%,.0f", item.getSubtotal())
            });
        }
    }

    public int getSelectedSaleId() {
        int selectedRow = salesTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) salesTableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }

    // Listener attachments
    public void addSalesTableSelectionListener(javax.swing.event.ListSelectionListener listener) {
        salesTable.getSelectionModel().addListSelectionListener(listener);
    }

    public void addBackToMenuButtonListener(java.awt.event.ActionListener listener) {
        backToMenuButton.addActionListener(listener);
    }
    public void showMessage(String message, String title, int messageType) {
        JLabel label = new JLabel(message);
        label.setFont(TABLE_FONT);
        JOptionPane.showMessageDialog(this, label, title, messageType);
    }
}
