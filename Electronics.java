import java.io.Serializable;

// Electronics class extends the Product class and implements the Serializable interface
public class Electronics extends Product implements Serializable {

    // Private instance variables specific to Electronics
    private String brand;            // Brand of the electronics product
    private String warrantyPeriod;   // Warranty period of the electronics product

    // Constructor to initialize the Electronics object
    Electronics(String productID, String productName, double price, String brand, String warrantyPeriod, int availableItems) {
        // Call the constructor of the superclass (Product) with productID, productName, and price
        super(productID, productName, price);

        // Set the additional attributes specific to Electronics
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
        this.availableItems = availableItems;  // Set availableItems inherited from the superclass
    }

    // Setter method for updating the brand of the electronics product
    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Setter method for updating the warranty period of the electronics product
    public void setWarranty(String warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    // Getter method to retrieve the brand of the electronics product
    public String getBrand() {
        return brand;
    }

    // Getter method to retrieve the warranty period of the electronics product
    public String getWarranty() {
        return warrantyPeriod;
    }
}
