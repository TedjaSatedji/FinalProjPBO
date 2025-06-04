package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class Sale {
    private int saleId;
    private Timestamp saleDate;
    private double totalAmount; // This field stores the total amount
    private String cashierName; // Optional
    private List<SaleItem> items; // List of items in this sale

    public Sale() {
        this.items = new ArrayList<>();
        this.saleDate = new Timestamp(System.currentTimeMillis()); // Default to now
    }

    public Sale(int saleId, Timestamp saleDate, double totalAmount, String cashierName) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount; // Ensure this is set
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
        if (items != null && !items.isEmpty()) {
            double calculatedTotal = 0;
            for (SaleItem item : items) {
                calculatedTotal += item.getSubtotal();
            }
            // It's good practice to ensure the field reflects the calculated value if items are present
            this.totalAmount = calculatedTotal;
            return calculatedTotal;
        }
        // If items list is empty or null, return the value of the totalAmount field.
        // This field should have been set when the Sale was created/loaded from DB.
        return this.totalAmount;
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
        // When items are set, it might be a good idea to update the totalAmount field as well
        if (this.items != null && !this.items.isEmpty()) {
            this.totalAmount = 0; // Reset before recalculating
            for (SaleItem item : this.items) {
                this.totalAmount += item.getSubtotal();
            }
        } else if (this.items != null && this.items.isEmpty()) {
            // If items list is explicitly set to empty, and totalAmount was based on calculation,
            // it might need to be reset or handled based on application logic.
            // For now, if items are cleared, the explicit totalAmount field still holds its value
            // until a new calculation happens or it's explicitly set.
        }
    }

    public void addItem(SaleItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
        // Recalculate total when an item is added
        this.totalAmount = 0; // Reset before recalculating
        for (SaleItem saleItem : this.items) {
            this.totalAmount += saleItem.getSubtotal();
        }
    }
}
