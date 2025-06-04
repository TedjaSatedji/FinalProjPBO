package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Sale.java (Model)
 * Represents a single sales transaction.
 */
public class Sale {
    private int saleId;
    private Timestamp saleDate;
    private double totalAmount;
    private String cashierName; // Optional
    private List<SaleItem> items; // List of items in this sale

    public Sale() {
        this.items = new ArrayList<>();
        this.saleDate = new Timestamp(System.currentTimeMillis()); // Default to now
    }

    public Sale(int saleId, Timestamp saleDate, double totalAmount, String cashierName) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.cashierName = cashierName;
        this.items = new ArrayList<>();
    }

    // Getters and Setters
    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public Timestamp getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }

    public double getTotalAmount() {
        // Recalculate total amount based on items if needed, or set directly
        double calculatedTotal = 0;
        for (SaleItem item : items) {
            calculatedTotal += item.getSubtotal();
        }
        return calculatedTotal; // Or return this.totalAmount if set explicitly after calculation
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
    }

    public void addItem(SaleItem item) {
        this.items.add(item);
        // Optionally recalculate totalAmount here
    }

    // You might add methods to calculate total, add items, etc.
}
