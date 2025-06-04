package model;


public class SaleItem {
    private int saleItemId;
    private int saleId; // Foreign key to Sale
    private int productId; // Foreign key to Product
    private String productName; // For display purposes
    private int quantitySold;
    private double pricePerUnit; // Price at the time of sale
    private double subtotal;

    public SaleItem() {}

    public SaleItem(int productId, String productName, int quantitySold, double pricePerUnit) {
        this.productId = productId;
        this.productName = productName; // Good to store for display if product name changes later
        this.quantitySold = quantitySold;
        this.pricePerUnit = pricePerUnit;
        this.subtotal = quantitySold * pricePerUnit;
    }

    public SaleItem(int saleItemId, int saleId, int productId, String productName, int quantitySold, double pricePerUnit, double subtotal) {
        this.saleItemId = saleItemId;
        this.saleId = saleId;
        this.productId = productId;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.pricePerUnit = pricePerUnit;
        this.subtotal = subtotal;
    }


    // Getters and Setters
    public int getSaleItemId() { return saleItemId; }
    public void setSaleItemId(int saleItemId) { this.saleItemId = saleItemId; }
    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantitySold() { return quantitySold; }
    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
        this.subtotal = this.quantitySold * this.pricePerUnit; // Recalculate subtotal
    }
    public double getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
        this.subtotal = this.quantitySold * this.pricePerUnit; // Recalculate subtotal
    }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
