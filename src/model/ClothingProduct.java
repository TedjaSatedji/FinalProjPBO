package model;


public class ClothingProduct extends Product {
    private String size;     // Example: "M", "L", "XL"
    private String color;    // Example: "Blue", "Red"
    private String material; // Example: "Cotton", "Polyester"

    public ClothingProduct() {
        super();
        super.setCategory("Clothing"); // Set default category
    }

    public ClothingProduct(int id, String name, int stock, double price, String supplier, String size, String color, String material) {
        super(id, name, "Clothing", stock, price, supplier); // Sets category to "Clothing"
        this.size = size;
        this.color = color;
        this.material = material;
    }

    // Getters and Setters for specific attributes
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }


    @Override
    public String getDisplayInfo() {
        return super.getDisplayInfo() +
                ", Size: " + (size != null ? size : "N/A") +
                ", Color: " + (color != null ? color : "N/A") +
                ", Material: " + (material != null ? material : "N/A");
    }
}
