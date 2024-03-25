import java.io.Serializable;

// Clothing class extends the Product class and implements the Serializable interface for object serialization
public class Clothing extends Product implements Serializable {

    private String size;  // Size of the clothing item
    private String color; // Color of the clothing item

    // Constructor to initialize a Clothing object with product details
    Clothing(String productID, String productName, double price, String size, String colour, int noOfAvailableItems){
        super(productID, productName, price);
        setSize(size);  // Set the size of the clothing item
        setColour(colour);  // Set the color of the clothing item
        setAvailableItems(noOfAvailableItems);  // Set the available items of the clothing item
    }

    // Getter method to retrieve the size of the clothing item
    public String getSize(){
        return size;
    }

    // Getter method to retrieve the color of the clothing item
    public String getColour(){
        return color;
    }

    // Setter method to update the size of the clothing item
    public void setSize(String size) {
        this.size = size;
    }

    // Setter method to update the color of the clothing item
    public void setColour(String color){
        this.color = color;
    }

}
