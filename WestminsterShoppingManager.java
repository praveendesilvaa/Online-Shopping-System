// imports
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Comparator;

public class WestminsterShoppingManager implements ShoppingManager{
    // Constant for the filename used for saving data
    private static final String Filename = "SavedData";

    // List to store user objects
    private static ArrayList<User> usersList = new ArrayList<>();

    // Scanner for user input
    public static Scanner scanner = new Scanner(System.in);

    // List to store products
    public static ArrayList<Product> productsList = new ArrayList<>();


    // Main method to run the shopping manager program
    public static void main(String[] args) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();

        try {
            shoppingManager.loadFromFile();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data from file: " + e.getMessage());
        }

        try {
            shoppingManager.loadUsersFromFile();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users from file: " + e.getMessage());
        }

        // Variable to store user's menu choice
        String managerChoice;
        do {
            // Display menu options to the manager
            System.out.println("""
                    ----------------------------
                    Westminster Shopping Manager
                    ----------------------------
                    1.  Add product
                    2.  Delete a product
                    3.  Display list of products in the system
                    4.  Save to a file
                    5.  Runs the GUI
                    6.  Exit""");

            // Switch case manager input for menu
            System.out.print("Please choose an option between 1 - 6: ");
            managerChoice = scanner.next();

            switch (managerChoice) {
                case "1" -> shoppingManager.addProduct();
                case "2" -> shoppingManager.deleteProduct();
                case "3" -> shoppingManager.displayProducts();
                case "4" -> {
                    try {
                        shoppingManager.saveFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "5" -> authenticationAndRunGUI();
                case "6" -> {
                    System.out.println("Exiting the system...");
                    System.exit(0); // Exit the program
                }
                default -> System.out.println("Invalid number, Please choose an option between 1 - 6: !");
            }
        } while (true);
    }


    private static void startTheGUI() {
        SwingUtilities.invokeLater(() -> {
            ShoppingSystemGUI.startTheGUI();
        });
    }

    /**
     * This method handles user authentication and initiates the GUI if successful.
     * It prompts the user to enter a username and password, attempts to authenticate the user,
     * and provides options to retry authentication, sign up as a new user, or exit the system.
     * If the authentication is successful or a new user signs up, the GUI is started.
     */
    private static void authenticationAndRunGUI() {
        // Create a Scanner object to take user input
        Scanner inputScanner = new Scanner(System.in);

        // Infinite loop to keep prompting for authentication until successful or user chooses to exit
        while (true) {
            // Prompt the user to enter a username and password
            System.out.print("Enter your username: ");
            String username = inputScanner.nextLine();
            System.out.print("Enter your password: ");
            String password = inputScanner.nextLine();

            // Authenticate the user with the entered credentials
            User user = authenticateUser(username, password);

            // Check if authentication is successful
            if (user != null) {
                System.out.println("Authentication successful! Starting the GUI...");
                startTheGUI();  // Start the graphical user interface (GUI)
                break;  // Exit the loop after GUI is started
            } else {
                System.out.println("Invalid username or password.");   // Display an error message for invalid username or password

                // Prompt the user for the next action
                System.out.print("1. Try again with different credentials\n2. Sign up\n3. Exit\nEnter your choice: ");
                int choice = inputScanner.nextInt();

                if (choice == 1) {                   // Process user's choice
                    authenticationAndRunGUI();        // Retry authentication with different credentials
                } else if (choice == 2) {
                    // Sign up a new user
                    User newUser = signUpUser();

                    // Check if sign-up is successful and start the GUI
                    if (newUser != null) {
                        System.out.println("Sign up successful! Starting the GUI...");
                        startTheGUI();
                        break;       // Exit the loop after GUI is started
                    } else {
                        System.out.println("Error creating the new user. Please try again.");  // Display an error message for failed user creation
                    }
                } else if (choice == 3) {
                    // Exit the system if the user chooses to exit
                    System.out.println("Exiting the system...");
                    System.exit(0);
                } else {
                    // Display an error message for an invalid choice
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
        }
    }

    /**
     * Allows a user to sign up by entering a username and password.
     * Creates a new User object with the provided credentials and adds it to the usersList.
     * The updated usersList is then saved to a file named "UsersData".
     *
     * @return The newly created User object if sign-up is successful, or null otherwise.
     */
    private static User signUpUser() {
        // Prompt the user to enter a username
        System.out.print("Enter an username: ");
        String username = scanner.next();

        // Prompt the user to enter a password
        System.out.print("Enter password: ");
        String password = scanner.next();

        // Create a new user and add it to the usersList
        User newUser = new User(username, password);
        usersList.add(newUser);    // Add the new user to the usersList

        // Save the updated usersList to the file
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("UserData"))) {
            outputStream.writeObject(usersList);
            System.out.println("User successfully signed up and added to the system.");
            return newUser;
        } catch (IOException e) {
            System.out.println("Error saving user data to file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Loads the user data from a file named "UserData".
     * If the file exists, it reads the serialized ArrayList<User> using ObjectInputStream
     * and initializes the usersList with the loaded data.
     *
     * @throws IOException            If there is an issue reading from the file.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    private void loadUsersFromFile() throws IOException, ClassNotFoundException {
        // Create a File object for the "UserData" file
        File file = new File("UserData");

        // Check if the file exists
        if (file.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("UserData"))) {
                usersList = (ArrayList<User>) inputStream.readObject();         // Read the serialized ArrayList<User> from the file and assign it to usersList
                System.out.println("Users list loaded from file: UsersData");
            }
        } else {
            // Display a message if the file does not exist
            System.out.println("Users file does not exist: UserData");
        }
    }

    /**
     * Authenticates a user based on the provided username and password.
     * Iterates through the usersList and checks if there is a match for both username and password.
     *
     * @param username The username entered by the user for authentication.
     * @param password The password entered by the user for authentication.
     * @return The authenticated User object if a match is found, otherwise returns null.
     */
    private static User authenticateUser(String username, String password) {
        // Iterate through the list of users
        for (User user : usersList) {
            // Check if the current user's username and password match the provided credentials
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                // Return the authenticated user
                return user;
            }
        }
        // Return null if no match is found
        return null;
    }

    /**
     * Allows the manager to add a new product to the system, either Electronics or Clothing.
     * Collects product details such as ID, name, price, and availability based on user input.
     * Checks for duplicate product IDs before adding a new product to the system.
     * Limits the system's capacity to 50 products and provides appropriate messages.
     */
    @Override
    public void addProduct() {

        if (productsList.size() == 50) {
            System.out.println("System full, The system has reached its maximum capacity of 50 products.");
            return;
        }

        while (true) {
            // Prompt the manager to choose between adding Electronics or Clothing
            System.out.print("Add Electronics or Clothing : ");
            String user_response = scanner.next().toLowerCase();

            // Validate the user's input for correctness
            if (!user_response.equals("electronics") && !user_response.equals("clothing")) {
                System.out.print("Wrong input! Enter either 'Clothing' or 'Electronics' ");
                continue;
            }

            // Get common product details
            String productId = getUserProductIDInput();

            // Check for duplicate product IDs
            if (isProductIDDuplicate(productId)) {
                System.out.println("Product with the same ID already exists. Please choose a different ID.");
                continue;
            }

            String productName = getUserProductInput();
            double productPrice = getPriceInput();

            // Get the number of available items for the new product
            int noOfAvailableItems = getNoOfAvailableItems();

            // Add Electronics item to the system
            if (user_response.equals("electronics")) {
                System.out.print("Product Brand: ");
                String productBrand = scanner.next();

                System.out.print("Product Warranty (in months): ");
                String productWarranty = scanner.next();

                Electronics electronic = new Electronics(productId, productName, productPrice, productBrand, productWarranty, noOfAvailableItems);

                // Check if the system is not full before adding the new product
                if (productsList.size() != 50) {
                    // Add the Electronics product to the list
                    productsList.add(electronic);
                    //display a successful message if the product get added correctly
                    System.out.println("Product successfully added!");
                } else {
                    // Display an error message if the system is full
                    System.out.println("System full, The system has reached its maximum capacity of 50 products.");
                }

                // Add Clothing item to the system
            } else {
                System.out.print("Product Size: ");
                String productSize = scanner.next();

                System.out.print("Product Colour: ");
                String productColour = scanner.next();

                Clothing clothing = new Clothing(productId, productName, productPrice, productSize, productColour, noOfAvailableItems);

                // Check if the system is not full before adding the new product
                if (productsList.size() != 50) {
                    // Add the Clothing product to the list
                    productsList.add(clothing);
                    //display a successful message if the product get added correctly
                    System.out.println("Product successfully added!");
                } else {
                    //Display an error message if the system is full
                    System.out.println("System full, The system has reached its maximum capacity of 50 products.");
                }
            }
            // Exit the loop after adding the product
            break;
        }
    }

    /**
     * Prompts the user to enter the number of available items for a new product.
     * Ensures the input is an integer and handles exceptions by displaying an error message.
     * Returns the valid number of available items.
     *
     * @return The number of available items entered by the user.
     */
    private int getNoOfAvailableItems() {
        int numOfAvailableItems;
        while (true) {
            try {
                System.out.print("Enter the number of available items: ");
                numOfAvailableItems = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("Invalid input, please enter a valid integer.");
            }
            scanner.nextLine(); // Consume the remaining newline character
        }
        return numOfAvailableItems;
    }


    /**
     * Prompts the user to enter the product ID for a new product.
     *
     * @return The product ID entered by the user.
     */
    private String getUserProductIDInput() {
        System.out.print("Product ID: ");
        return scanner.next();
    }

    /**
     * Checks whether the provided product ID already exists in the list of products.
     *
     * @param productId The product ID to be checked for duplication.
     * @return True if the product ID is a duplicate, false otherwise.
     */
    private boolean isProductIDDuplicate(String productId) {
        for (Product product : productsList) {
            if (productId.equals(product.getProductId())) {
                return true; // Duplicate product ID found
            }
        }
        return false; // Product ID is not duplicate
    }


    //get user input for product name
    private String getUserProductInput() {
        System.out.print("Product Name: ");
        return scanner.next();
    }

    //get user input for product price
    private double getPriceInput() {
        while (true) {
            System.out.print("Product Price: ");
            if (scanner.hasNextDouble()) {
                return scanner.nextDouble();
            } else {
                System.out.println("Error! Please enter a valid numeric value for the price.");
                scanner.next(); // Consume the invalid input
            }
        }
    }

    /**
     * Deletes a product from the system based on the user's input of the product ID.
     * The user is prompted to enter the product ID until a product is successfully deleted.
     * If the entered product ID is not found, the user is informed, and the operation stops.
     */
    @Override
    public void deleteProduct() {
        ArrayList<Product> productToDelete = new ArrayList<>();
        boolean productDeleted = false;

        while (!productDeleted) {
            System.out.print("Enter Product ID: ");
            String productId = scanner.next();

            // Loop until a product is successfully deleted
            for (Product product : productsList) {
                if (productId.equals(product.getProductId())) {
                    productToDelete.add(product);
                    System.out.println("Deleted product " + product.getProductName());
                    productDeleted = true;
                    break;
                }
            }

            // If the product is not found, inform the user
            if (!productDeleted) {
                System.out.println("Cannot find a product with the product ID: " + productId);
                break;
            }

            // Remove the products marked for deletion from the list
            productsList.removeAll(productToDelete);
        }
    }


    /**
     * Takes a Product object and generates formatted details for display.
     * The details include common product information such as name, ID, price, and available items.
     * If the product is an instance of Electronics, additional details like brand and warranty are included.
     * If the product is an instance of Clothing, additional details like size and color are included.
     *
     * @param product The Product object for which details are to be displayed.
     * @return A formatted string containing the product details.
     */
    public String displayProductDetails(Product product) {
        // Get the runtime class of the product object
        Class<? extends Product> productClass = product.getClass();

        // Extract common product details
        String productName = product.getProductName();
        String productID = product.getProductId();
        double productPrice = product.getProductPrice();
        int availableItems = product.getAvailableItems();
        String productType;

        // Determine the product type based on the runtime class
        if (productClass == Electronics.class) {
            productType = "Electronics";
        } else {
            productType = "Clothing";
        }

        // Format common product details
        String productDetails = """
    %s (%s)
      ID - %s
      Price - %.2f
      Available Items - %d 
    """.formatted(productName, productType, productID, productPrice, availableItems);

        // Add specific attributes for Electronics or Clothing
        if (productClass == Electronics.class) {
            String productBrand = ((Electronics) product).getBrand();
            String productWarranty = ((Electronics) product).getWarranty();
            String electronicsAttributes = """
          Brand - %s
          Warranty - %s
        """.formatted(productBrand, productWarranty);
            // Combine common and Electronics-specific details
            return productDetails + electronicsAttributes;
        } else {
            String productSize = ((Clothing) product).getSize();
            String productColour = ((Clothing) product).getColour();
            String clothingAttributes = """
          Size - %s
          Colour - %s
        """.formatted(productSize, productColour);
            // Combine common and Clothing-specific details
            return productDetails + clothingAttributes;
        }
    }


    /**
     * Overrides the displayProducts method from the ProductManager interface.
     * Displays the list of products sorted by their product IDs.
     * For each product, calls the displayProductDetails method to retrieve
     * formatted details and prints them to the console.
     */
    @Override
    public void displayProducts() {
        // Sort the productsList based on product IDs using Comparator
        productsList.sort(Comparator.comparing(Product::getProductId));
        // Print header for the list of products
        System.out.println("List of Products:");
        System.out.println("------------------");

        // Iterate through each product in the sorted list
        for (Product product : productsList) {
            // Call displayProductDetails to get formatted details for the product
            String productDetails = displayProductDetails(product);

            // Print the formatted product details to the console
            System.out.println(productDetails);

            // Print a separator line after each product
            System.out.println("------------------");
        }
    }

    /**
     * Saves the list of products to a file.
     * This method uses ObjectOutputStream to serialize and write the 'productsList'
     * to a file specified by the 'Filename'.
     *
     * @throws IOException If an I/O error occurs while writing the file.
     */
    public void saveFile() throws IOException {
        // Try-with-resources block to automatically close the ObjectOutputStream
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(Filename))) {
            // Serialize and write the ArrayList of Products to the file
            outputStream.writeObject(productsList);
        }
        // Print a message indicating that the product list has been saved successfully
        System.out.println("Product list saved to file: " + Filename);
    }

    /**
     * Loads the list of products from a file.
     * This method uses ObjectInputStream to read serialized objects from a file
     * and populates the 'productsList' with the deserialized ArrayList of Products.
     *
     * @throws IOException            If an I/O error occurs while reading the file.
     * @throws ClassNotFoundException If the class of the serialized object is not found.
     */
    public void loadFromFile() throws IOException, ClassNotFoundException {
        // Create a File object with the specified filename
        File file = new File(Filename);

        // Check if the file exists
        if (file.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(Filename))) {
                // Read the ArrayList of Products from the file and cast it to the appropriate type
                productsList = (ArrayList<Product>) inputStream.readObject();
                // Print a message indicating that the product list has been loaded successfully
                System.out.println("Product list loaded from file: " + Filename);
            }
        } else {
            // Print a message if the file does not exist
            System.out.println("File does not exist: " + Filename);
        }
    }
}





