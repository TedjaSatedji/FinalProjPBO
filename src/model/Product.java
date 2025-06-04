package model;


public class Product {
    private int id;
    private String name;
    private String category;
    private int stock;
    private double price;
    private String supplier;

    // Constructors
    public Product() {
    }

    public Product(int id, String name, String category, int stock, double price, String supplier) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.supplier = supplier;
    }

    // Getters and Setters (Encapsulation)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }


    public String getDisplayInfo() {
        return "ID: " + id +
                ", Name: " + name +
                ", Category: " + category +
                ", Stock: " + stock +
                ", Price: " + String.format("%.2f", price) + // Format price
                ", Supplier: " + supplier;
    }

    @Override
    public String toString() {
        // Useful for JComboBox or JList, often just the name is sufficient.
        return getName();
    }
}
