import org.junit.Test;
import static org.junit.Assert.*;

public class WestminsterShoppingManagerTest {

    @Test
    public void testAddProduct() {
        // Create WestminsterShoppingManager instance for testing
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();

        // Create a sample electronic product
        Electronics electronicProduct = new Electronics("W001", "Laptop", 1200, "Asus", "12", 10);

        // Add the product to the system
        shoppingManager.addProduct();

        // Check if the product is added successfully
        assertTrue(WestminsterShoppingManager.productsList.contains(electronicProduct));
    }


    @Test
    public void testDeleteProduct() {
        // Create WestminsterShoppingManager instance for testing
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();

        // Create a sample clothing product
        Clothing clothingProduct = new Clothing("W001", "Trouser", 20.00, "medium", "black", 10);

        // Add the product to the system
        shoppingManager.addProduct();

        // Delete the product from the system
        shoppingManager.deleteProduct();

        // Check if the product is deleted successfully
        assertFalse(WestminsterShoppingManager.productsList.contains(clothingProduct));
    }

    @Test
    public void testLoadAndSaveFile() {
        // Create WestminsterShoppingManager instance for testing
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();

        // Create some sample products
        Electronics electronicProduct = new Electronics("W002", "phone", 700, "Apple", "2", 5);
        Clothing clothingProduct = new Clothing("W003", "Pants", 40.00, "Large", "Black", 2);

        // Add products to the system
        shoppingManager.addProduct();
        shoppingManager.addProduct();

        // Save the products to a file
        try {
            shoppingManager.saveFile();
        } catch (Exception e) {
            fail("Exception thrown when saving the file: " + e.getMessage());
        }

        // Load the products from the file
        try {
            shoppingManager.loadFromFile();
        } catch (Exception e) {
            fail("Exception thrown when loading the file: " + e.getMessage());
        }

        // Check if the loaded products match the added products
        assertTrue(WestminsterShoppingManager.productsList.contains(electronicProduct));
        assertTrue(WestminsterShoppingManager.productsList.contains(clothingProduct));
    }

    @Test
    public void testDeleteNonExistingProduct() {
        // Create WestminsterShoppingManager instance for testing
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();

        //Adding a new product
        Electronics electronic = new Electronics("W005", "TV", 10, "Samsung", "12", 12);
        shoppingManager.addProduct();

        // Deleting a non-existing product
        shoppingManager.deleteProduct();

        // Checking if nothing was deleted
        assertEquals(1, WestminsterShoppingManager.productsList.size());
    }

    // Add more test methods based on your specific functionalities
}
