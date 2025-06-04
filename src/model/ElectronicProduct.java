package model;


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


    @Override
    public String getDisplayInfo() {
        return super.getDisplayInfo() +
                ", Brand: " + (brand != null ? brand : "N/A") +
                ", Warranty: " + warrantyPeriod + " months";
    }
}
