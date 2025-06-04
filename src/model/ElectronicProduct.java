package model;

/**
 * ElectronicProduct.java (Model)
 * Represents an electronic product, inheriting from the updated Product class.
 * Demonstrates Inheritance and Polymorphism.
 * Unique attributes (brand, warrantyPeriod) are part of the class but not
 * directly stored in separate columns in the simplified 'products' table from the document.
 */
public class ElectronicProduct extends Product {
    private String brand;       // Example: "Samsung", "LG"
    private int warrantyPeriod; // Example: 12 (in months)

    public ElectronicProduct() {
        super(); // Calls constructor of Product
        // It's good practice to set a default category if known
        super.setCategory("Electronics");
    }

    public ElectronicProduct(int id, String name, int stock, double price, String supplier, String brand, int warrantyPeriod) {
        super(id, name, "Electronics", stock, price, supplier); // Sets category to "Electronics"
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    // Getters and Setters for specific attributes
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        if (warrantyPeriod < 0) {
            throw new IllegalArgumentException("Warranty period cannot be negative.");
        }
        this.warrantyPeriod = warrantyPeriod;
    }

    /**
     * Overrides getDisplayInfo to include electronics-specific information.
     * Demonstrates Polymorphism.
     * @return String containing detailed product information.
     */
    @Override
    public String getDisplayInfo() {
        return super.getDisplayInfo() +
                ", Brand: " + (brand != null ? brand : "N/A") +
                ", Warranty: " + warrantyPeriod + " months";
    }
}
