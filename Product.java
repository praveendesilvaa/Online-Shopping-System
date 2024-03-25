import java.io.Serializable;

/**
 * An abstract class representing a product in a shopping system.
 * This class implements the Serializable interface to support object serialization.
 */
abstract class Product implements Serializable {

    // Fields representing common attributes of a product
    private String productID;
    private String productName;
    private double price;
    protected int availableItems; // Changed to protected to allow access in derived classes

    /**
     * Constructor to initialize common product attributes.
     *
     * @param productID    The unique identifier for the product.
     * @param productName  The name of the product.
     * @param price        The price of the product.
     */
    Product(String productID, String productName, double price) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
    }

    /**
     * Getter method retrieving the product ID.
     *
     * @return The product ID.
     */
    public String getProductId() {
        return productID;
    }

    /**
     * Getter method retrieving the product name.
     *
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Getter method retrieving the product price.
     *
     * @return The product price.
     */
    public double getProductPrice() {
        return price;
    }

    /**
     * Getter method retrieving the number of available items.
     *
     * @return The number of available items for the product.
     */
    public int getAvailableItems() {
        return availableItems;
    }

    /**
     * Setter method updating the number of available items.
     *
     * @param availableItems The new number of available items for the product.
     */
    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    /**
     * Setter method updating the product ID.
     *
     * @param productID The new product ID.
     */
    public void setProductID(String productID) {
        this.productID = productID;
    }

    /**
     * Setter method updating the product name.
     *
     * @param productName The new product name.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Setter method updating the product price.
     *
     * @param price The new product price.
     */
    /*public void setPrice(double price) {
        this.price = price;
    }*/
}
