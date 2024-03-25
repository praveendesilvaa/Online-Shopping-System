import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ShoppingCart class represents a shopping cart that can contain various products
public class ShoppingCart {
    private final List<CartItem> shoppingCartItems;  // List to store CartItems in the shopping cart

    // Constructor to initialize a new shopping cart
    public ShoppingCart() {
        this.shoppingCartItems = new ArrayList<>();
    }

    // Method to add a product to the shopping cart
    public void addProduct(Product product) {
        // Check if the product is already in the cart
        for (CartItem cartItem : shoppingCartItems) {
            if (cartItem.getProduct().getProductId().equals(product.getProductId())) {
                // Increment the quantity if the product already exists
                cartItem.incrementQuantity();
                return;
            }
        }

        // If the product is not in the cart, add a new cart item
        shoppingCartItems.add(new CartItem(product, 1));
    }

    // Method to get the list of CartItems in the shopping cart
    public List<CartItem> getCartItems() {
        return shoppingCartItems;
    }

    // Method to calculate the total cost of the items in the shopping cart
    public double totalCost() {
        return shoppingCartItems.stream()
                .mapToDouble(cartItem -> cartItem.getProduct().getProductPrice() * cartItem.getQuantity())
                .sum();
    }

    // Method to calculate a 10% discount on the total cost
    public double calculate10percentDiscount() {
        return totalCost() * 0.1;
    }

    // Method to calculate a 20% discount based on the quantity of items in the same category
    public double calculate20percentDiscount() {
        if (SameThreeCategory()) {
            return totalCost() * 0.2;
        } else {
            return 0;
        }
    }

    // Method to get the final total after applying discounts
    public double getTotalFinal() {
        return totalCost() - calculate10percentDiscount() - calculate20percentDiscount();
    }

    // Private method to check if there are three or more items in the same category
    private boolean SameThreeCategory() {
        Map<String, Integer> counts = new HashMap<>();

        for (CartItem cartItem : shoppingCartItems) {
            String category = getCategory(cartItem.getProduct().getProductName());

            int count = counts.getOrDefault(category, 0);
            counts.put(category, count + 1);
        }

        return counts.values().stream()
                .anyMatch(count -> count >= 3);
    }

    // Private method to determine the category of a product based on its name
    private String getCategory(String productName) {
        if (productName.contains("Electronics")) {
            return "Electronics";
        } else {
            return "Clothing";
        }
    }

    // Static nested class representing an item in the shopping cart
    public static class CartItem {
        private final Product product;  // Product in the cart item
        private int quantity;  // Quantity of the product in the cart item

        // Constructor to initialize a new cart item with a product and quantity
        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        // Getter method to retrieve the product in the cart item
        public Product getProduct() {
            return product;
        }

        // Getter method to retrieve the quantity of the product in the cart item
        public int getQuantity() {
            return quantity;
        }

        // Method to increment the quantity of the product in the cart item
        public void incrementQuantity() {
            quantity++;
        }


    }

    // Method to get a summary of the shopping cart including additional details and discounts
    public String getSummary() {
        double finalTotalValue = getTotalFinal();

        // Additional details only
        StringBuilder summary = new StringBuilder();
        summary.append("\nAdditional Details:\n");
        summary.append(String.format("%-25s%.2f\n", "Total:", totalCost()));
        summary.append(String.format("%-25s%.2f\n", "First Purchase Discount (10%):", calculate10percentDiscount()));
        summary.append(String.format("%-25s%.2f\n", "Three items in the same category discount (20%):", calculate20percentDiscount()));
        summary.append(String.format("%-25s%.2f\n", "Final Total:", finalTotalValue));

        return summary.toString();
    }

}
