package controller;

import model.Sale;
import model.SaleItem;
import model.SaleDAO;
import view.SalesHistoryView;
import main.Main; // To navigate back

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SalesHistoryController {
    private SalesHistoryView view;
    private SaleDAO saleDAO;

    public SalesHistoryController(SalesHistoryView view, SaleDAO saleDAO) {
        this.view = view;
        this.saleDAO = saleDAO;

        // Attach listeners
        this.view.addSalesTableSelectionListener(new SaleSelectionListener());
        this.view.addBackToMenuButtonListener(new BackToMenuListener());

        // Load initial data
        loadAllSales();
        this.view.displaySaleItems(java.util.Collections.emptyList(), 0); // Clear items table initially
    }

    private void loadAllSales() {
        List<Sale> sales = saleDAO.getAllSales();
        view.displaySales(sales);
    }

    class SaleSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int selectedSaleId = view.getSelectedSaleId();
                if (selectedSaleId != -1) {
                    List<SaleItem> items = saleDAO.getSaleItemsBySaleId(selectedSaleId);
                    view.displaySaleItems(items, selectedSaleId);
                } else {
                    view.displaySaleItems(java.util.Collections.emptyList(), 0); // Clear if no selection
                }
            }
        }
    }

    class BackToMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose(); // Close the sales history view
            Main.showRoleSelection(); // Show the main role selection dialog
        }
    }
}

