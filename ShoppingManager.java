import java.io.IOException;

// ShoppingManager interface outlines methods for managing a shopping system
public interface ShoppingManager {

    // Method to add a new product to the shopping system
    public void addProduct();

    // Method to delete a product from the shopping system
    public void deleteProduct();

    // Method to display the list of products in the shopping system
    public void displayProducts();

    // Method to save the current state of the shopping system to a file
    // Throws IOException in case of an error during file I/O
    public void saveFile() throws IOException;

}
