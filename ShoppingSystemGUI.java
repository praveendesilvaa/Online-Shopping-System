import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ShoppingSystemGUI {
    // Static variables to maintain GUI components and shopping cart
    private static DefaultTableModel productTableModel;
    private static JTextArea productDetailsDisplayArea;
    private static ShoppingCart shoppingCart;
    private static JFrame frame;
    private static JComboBox<String> filterComboBox;

    // Method to initialize and start the GUI
    public static void startTheGUI() {
        // Initialize shopping cart and main frame
        shoppingCart = new ShoppingCart();
        frame = new JFrame("Westminster Shopping Centre");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create main panel and set its layout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.ORANGE); // Set background color for the main panel

        // Create combo box panel and set its layout
        JPanel comboPanel = new JPanel();
        comboPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        comboPanel.setBackground(Color.LIGHT_GRAY); // Set background color for the combo box panel

        // Create and configure label, combo box, and shopping cart button
        JLabel label = new JLabel("Select Product Category:");
        String[] productTypes = {"All", "Electronics", "Clothing"};
        filterComboBox = new JComboBox<>(productTypes);
        filterComboBox.addActionListener(e -> updateProductTable());

        JButton shoppingCartButton = new JButton("Shopping Cart");
        shoppingCartButton.setBackground(Color.CYAN); // Set background color for the shopping cart button
        shoppingCartButton.addActionListener(e -> openShoppingCartWindow());

        // Add components to the combo box panel
        comboPanel.add(label, BorderLayout.CENTER);
        comboPanel.add(filterComboBox, BorderLayout.CENTER);
        comboPanel.add(shoppingCartButton);
        comboPanel.setBackground(Color.LIGHT_GRAY);

        // Add combo box panel to the main panel
        panel.add(comboPanel);
        panel.add(Box.createRigidArea(new Dimension(30, 10)));

        // Initialize product table and add it to the main panel
        productTableModel = new DefaultTableModel(new Object[]{"Product ID", "Name", "Category", "Price(£)", "Info"}, 0);
        updateProductTable();
        JTable table = new JTable(productTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(400, 600));
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                displaySelectedProductDetails(selectedRow);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add product details section to the main panel
        JLabel productDetailsTitleLabel = new JLabel("Product Details");
        productDetailsTitleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(productDetailsTitleLabel);

        productDetailsDisplayArea = new JTextArea();
        productDetailsDisplayArea.setEditable(false);
        productDetailsDisplayArea.setPreferredSize(new Dimension(500, 300));
        panel.add(productDetailsDisplayArea);

        // Add "Add to Shopping Cart" button to the main panel
        JButton addToCartButton = new JButton("Add to Shopping Cart");
        addToCartButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String productId = (String) table.getValueAt(selectedRow, 0);
                String actualProductId = productId.replaceAll("<[^>]*>", "");
                Product selectedProduct = findProductById(actualProductId);

                if (selectedProduct != null) {
                    if (selectedProduct.getAvailableItems() > 0) {
                        shoppingCart.addProduct(selectedProduct);
                        Product originalProduct = findProductById(actualProductId);
                        if (originalProduct != null) {
                            originalProduct.setAvailableItems(originalProduct.getAvailableItems() - 1);
                        }
                        JOptionPane.showMessageDialog(frame, "Product added to the shopping cart!");
                        updateProductTable();
                        displaySelectedProductDetails(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Error! No items available for the product!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Error! Product not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add button panel to the main panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addToCartButton);
        panel.add(buttonPanel);

        // Configure main frame and make it visible
        frame.getContentPane().add(panel);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Method to update the product table based on the selected category
    public static void updateProductTable() {
        productTableModel.setRowCount(0);
        String filter = (String) filterComboBox.getSelectedItem();

        for (Product product : WestminsterShoppingManager.productsList) {
            if (filter.equals("All") ||
                    (filter.equals("Electronics") && product instanceof Electronics) ||
                    (filter.equals("Clothing") && product instanceof Clothing)) {
                displayProductInTable(product);
            }
        }
    }

    // Method to display details of the selected product in the text area
    private static void displaySelectedProductDetails(int selectedRow) {
        String productID = (String) productTableModel.getValueAt(selectedRow, 0);
        Product selectedProduct = findProductById(productID);

        if (selectedProduct != null) {
            String productDetails;

            if (selectedProduct instanceof Electronics) {
                productDetails = String.format("""
                        Selected Product Details:
                        
                        Product ID: %s
                        Name: %s
                        Price: %.2f
                        Brand: %s
                        Warranty: %s
                        Available Items: %d""",
                        selectedProduct.getProductId(), selectedProduct.getProductName(),
                        selectedProduct.getProductPrice(), ((Electronics) selectedProduct).getBrand(),
                        ((Electronics) selectedProduct).getWarranty(), selectedProduct.getAvailableItems());
            } else {
                productDetails = String.format("""
                        Selected Product Details:
                        
                        Product ID: %s
                        Name: %s
                        Price: %.2f
                        Size: %s
                        Colour: %s
                        Available Items: %d""",
                        selectedProduct.getProductId(), selectedProduct.getProductName(),
                        selectedProduct.getProductPrice(), ((Clothing) selectedProduct).getSize(),
                        ((Clothing) selectedProduct).getColour(), selectedProduct.getAvailableItems());
            }
            productDetailsDisplayArea.setText(productDetails);
        }
    }

    // Method to find a product by its ID
    private static Product findProductById(String productID) {
        for (Product product : WestminsterShoppingManager.productsList) {
            if (product.getProductId().equals(productID)) {
                return product;
            }
        }
        return null;
    }

    // Method to open the shopping cart window
    private static void openShoppingCartWindow() {
        JFrame shoppingCartFrame = new JFrame("Shopping Cart");
        shoppingCartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a table model for the shopping cart
        DefaultTableModel shoppingCartTableModel = new DefaultTableModel(new Object[]{"Product ID", "Name", "Quantity", "Price"}, 0);
        JTable shoppingCartTable = new JTable(shoppingCartTableModel);
        JScrollPane scrollPane = new JScrollPane(shoppingCartTable);

        // Set the preferred size of the scrollPane
        scrollPane.setPreferredSize(new Dimension(400, 350));

        JTextArea summaryTextArea = new JTextArea();
        summaryTextArea.setEditable(false);
        updateSummaryTextArea(summaryTextArea);

        // Add components to the shopping cart frame
        shoppingCartFrame.getContentPane().add(scrollPane, BorderLayout.NORTH);
        shoppingCartFrame.getContentPane().add(summaryTextArea, BorderLayout.SOUTH);

        // Populate the shopping cart table with items
        List<ShoppingCart.CartItem> cartItems = shoppingCart.getCartItems();
        for (ShoppingCart.CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();
            Object[] row = {product.getProductId(), product.getProductName(), quantity, product.getProductPrice()};
            shoppingCartTableModel.addRow(row);
        }

        // Configure shopping cart frame and make it visible
        shoppingCartFrame.setSize(500, 500);
        shoppingCartFrame.setLocationRelativeTo(frame);
        shoppingCartFrame.setVisible(true);
    }

    // Method to update the summary text area in the shopping cart window
    private static void updateSummaryTextArea(JTextArea summaryTextArea) {
        String summary = shoppingCart.getSummary();
        summaryTextArea.setText(summary);
    }

    // Method to display a product in the product table with color coding for low availability
    private static void displayProductInTable(Product product) {
        String priceAsString = String.format("%.2f", product.getProductPrice());
        Object[] row;

        // Determine the type of product and create a row accordingly
        if (product instanceof Electronics) {
            row = new Object[]{product.getProductId(), product.getProductName(), "Electronics",
                    priceAsString, ((Electronics) product).getWarranty()};
        } else if (product instanceof Clothing) {
            row = new Object[]{product.getProductId(), product.getProductName(), "Clothes",
                    priceAsString, "Size: " + ((Clothing) product).getSize() + ", Color: " + ((Clothing) product).getColour()};
        } else {
            String category = "Unknown";
            row = new Object[]{product.getProductId(), product.getProductName(), category,
                    priceAsString, ""};
        }

        int availableItems = product.getAvailableItems();

        // Check if available items are less than 3, and set the text color to red
        if (availableItems < 3) {
            row[0] = "<html><font color='red'>" + row[0] + "</font></html>";
            row[1] = "<html><font color='red'>" + row[1] + "</font></html>";
            row[2] = "<html><font color='red'>" + row[2] + "</font></html>";
            row[3] = "<html><font color='red'>" + row[3] + "</font></html>";
            row[4] = "<html><font color='red'>" + row[4] + "</font></html>";
        }

        // Add the row to the product table model
        productTableModel.addRow(row);
    }
}
