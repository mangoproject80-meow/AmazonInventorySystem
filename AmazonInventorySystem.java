import java.io.*;
import java.util.*;

// Class representing a Product record
class Product implements Serializable {
    private String id;
    private String name;
    private double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "Product [ID=" + id + ", Name=" + name + ", Price=$" + price + "]";
    }

    // Convert object state to comma-separated format for file persistence
    public String toFileFormat() {
        return id + "," + name + "," + price;
    }

    // Reconstruct Product object from file text record
    public static Product fromFileFormat(String line) {
        String[] parts = line.split(",");
        return new Product(parts[0], parts[1], Double.parseDouble(parts[2]));
    }
}

// Class representing a Customer Order record
class Order implements Serializable {
    private String orderId;
    private String customerName;
    private String productId;

    public Order(String orderId, String customerName, String productId) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.productId = productId;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getProductId() { return productId; }

    @Override
    public String toString() {
        return "Order [ID=" + orderId + ", Customer=" + customerName + ", ProductID=" + productId + "]";
    }

    // Convert object state to comma-separated format for file persistence
    public String toFileFormat() {
        return orderId + "," + customerName + "," + productId;
    }

    // Reconstruct Order object from file text record
    public static Order fromFileFormat(String line) {
        String[] parts = line.split(",");
        return new Order(parts[0], parts[1], parts[2]);
    }
}

public class AmazonInventorySystem {
    // 1. ArrayList: Dynamic array for managing active product records
    private ArrayList<Product> productList = new ArrayList<>();

    // 2. Queue (LinkedList implementation): First-In-First-Out (FIFO) processing for pending customer orders
    private Queue<Order> pendingOrders = new LinkedList<>();

    // 3. Stack: Last-In-First-Out (LIFO) stack for tracking recently cancelled orders
    private Stack<Order> cancelledOrders = new Stack<>();

    // 4. Hashtable: Synchronized key-value pair map for looking up supplier details
    private Hashtable<String, String> supplierDetails = new Hashtable<>();

    // 5. Vector: Thread-safe dynamic array for storing legacy/archived product records
    private Vector<Product> archivedProducts = new Vector<>();

    // File paths for persistence
    private static final String PRODUCT_FILE = "products.txt";
    private static final String ORDERS_FILE = "pending_orders.txt";
    private static final String CANCELLED_FILE = "cancelled_orders.txt";
    private static final String SUPPLIER_FILE = "suppliers.txt";
    private static final String ARCHIVE_FILE = "archived_products.txt";

    // --- Product Operations (ArrayList) ---
    public void addProduct(Product product) {
        productList.add(product);
    }

    // --- Archive Operations (Vector) ---
    public void archiveProduct(String productId) {
        Iterator<Product> iterator = productList.iterator();
        while (iterator.hasNext()) {
            Product p = iterator.next();
            if (p.getId().equalsIgnoreCase(productId)) {
                archivedProducts.add(p);
                iterator.remove();
                System.out.println("[Archive] Moved Product " + productId + " to Vector archive.");
                return;
            }
        }
        System.out.println("[Archive Warning] Product ID " + productId + " not found.");
    }

    // --- Order Operations (Queue) ---
    public void addPendingOrder(Order order) {
        pendingOrders.add(order);
    }

    public void processOrder() {
        if (!pendingOrders.isEmpty()) {
            Order processed = pendingOrders.poll();
            System.out.println("[Queue Processed] Order ID " + processed.getOrderId() + " fulfilled.");
        } else {
            System.out.println("[Queue Warning] No pending orders in queue.");
        }
    }

    // --- Cancelled Order Operations (Stack) ---
    public void cancelOrder(Order order) {
        cancelledOrders.push(order);
        System.out.println("[Stack Push] Order " + order.getOrderId() + " added to cancelled stack.");
    }

    // --- Supplier Operations (Hashtable) ---
    public void addSupplier(String supplierId, String supplierInfo) {
        supplierDetails.put(supplierId, supplierInfo);
    }

    // --- Display Method ---
    public void displayAllInfo() {
        System.out.println("\n---------------- SYSTEM RECORDS SUMMARY ----------------");

        System.out.println("\n1. Active Products (ArrayList - Size: " + productList.size() + "):");
        if (productList.isEmpty()) System.out.println("   None");
        for (Product p : productList) System.out.println("   " + p);

        System.out.println("\n2. Pending Orders Queue (FIFO - Size: " + pendingOrders.size() + "):");
        if (pendingOrders.isEmpty()) System.out.println("   None");
        for (Order o : pendingOrders) System.out.println("   " + o);

        System.out.println("\n3. Cancelled Orders Stack (LIFO - Top to Bottom - Size: " + cancelledOrders.size() + "):");
        if (cancelledOrders.isEmpty()) System.out.println("   None");
        for (int i = cancelledOrders.size() - 1; i >= 0; i--) {
            System.out.println("   " + cancelledOrders.get(i));
        }

        System.out.println("\n4. Supplier Directory (Hashtable - Count: " + supplierDetails.size() + "):");
        if (supplierDetails.isEmpty()) System.out.println("   None");
        supplierDetails.forEach((id, info) -> System.out.println("   Supplier ID: " + id + " -> " + info));

        System.out.println("\n5. Archived Products (Vector - Size: " + archivedProducts.size() + "):");
        if (archivedProducts.isEmpty()) System.out.println("   None");
        for (Product p : archivedProducts) System.out.println("   " + p);

        System.out.println("--------------------------------------------------------\n");
    }

    // --- File Handling persistence logic ---
    public void saveDataToFiles() {
        try {
            // Write ArrayList to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE));
            for (Product p : productList) {
                writer.write(p.toFileFormat());
                writer.newLine();
            }
            writer.close();

            // Write Queue to file
            writer = new BufferedWriter(new FileWriter(ORDERS_FILE));
            for (Order o : pendingOrders) {
                writer.write(o.toFileFormat());
                writer.newLine();
            }
            writer.close();

            // Write Stack to file
            writer = new BufferedWriter(new FileWriter(CANCELLED_FILE));
            for (Order o : cancelledOrders) {
                writer.write(o.toFileFormat());
                writer.newLine();
            }
            writer.close();

            // Write Hashtable to file
            writer = new BufferedWriter(new FileWriter(SUPPLIER_FILE));
            for (String key : supplierDetails.keySet()) {
                writer.write(key + "," + supplierDetails.get(key));
                writer.newLine();
            }
            writer.close();

            // Write Vector to file
            writer = new BufferedWriter(new FileWriter(ARCHIVE_FILE));
            for (Product p : archivedProducts) {
                writer.write(p.toFileFormat());
                writer.newLine();
            }
            writer.close();

            System.out.println(">> Persistence: All collection data saved successfully to text files.");
        } catch (IOException e) {
            System.err.println("Error saving files: " + e.getMessage());
        }
    }

    public void loadDataFromFiles() {
        try {
            // Load ArrayList from file
            File pFile = new File(PRODUCT_FILE);
            if (pFile.exists()) {
                productList.clear();
                BufferedReader reader = new BufferedReader(new FileReader(pFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) productList.add(Product.fromFileFormat(line));
                }
                reader.close();
            }

            // Load Queue from file
            File oFile = new File(ORDERS_FILE);
            if (oFile.exists()) {
                pendingOrders.clear();
                BufferedReader reader = new BufferedReader(new FileReader(oFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) pendingOrders.add(Order.fromFileFormat(line));
                }
                reader.close();
            }

            // Load Stack from file
            File cFile = new File(CANCELLED_FILE);
            if (cFile.exists()) {
                cancelledOrders.clear();
                BufferedReader reader = new BufferedReader(new FileReader(cFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) cancelledOrders.push(Order.fromFileFormat(line));
                }
                reader.close();
            }

            // Load Hashtable from file
            File sFile = new File(SUPPLIER_FILE);
            if (sFile.exists()) {
                supplierDetails.clear();
                BufferedReader reader = new BufferedReader(new FileReader(sFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        String[] parts = line.split(",", 2);
                        supplierDetails.put(parts[0], parts[1]);
                    }
                }
                reader.close();
            }

            // Load Vector from file
            File aFile = new File(ARCHIVE_FILE);
            if (aFile.exists()) {
                archivedProducts.clear();
                BufferedReader reader = new BufferedReader(new FileReader(aFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) archivedProducts.add(Product.fromFileFormat(line));
                }
                reader.close();
            }

            System.out.println(">> Persistence: All collection data restored successfully from text files.");
        } catch (IOException e) {
            System.err.println("Error loading files: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        AmazonInventorySystem system = new AmazonInventorySystem();

        System.out.println("=== STEP 1: INITIALIZING DATA STRUCTURES ===");
        system.addProduct(new Product("P101", "Kindle Paperwhite", 139.99));
        system.addProduct(new Product("P102", "Echo Dot 5th Gen", 49.99));
        system.addProduct(new Product("P103", "Fire TV Stick 4K", 29.99));

        system.addPendingOrder(new Order("O501", "Alice Smith", "P101"));
        system.addPendingOrder(new Order("O502", "Bob Jones", "P102"));
        system.addPendingOrder(new Order("O503", "Charlie Brown", "P103"));

        system.addSupplier("SUP1", "Foxconn Distribution");
        system.addSupplier("SUP2", "Amazon Logistics North");

        system.displayAllInfo();

        System.out.println("=== STEP 2: PERFORMING DEMO OPERATIONS ===");
        system.processOrder(); // Dequeues O501
        system.cancelOrder(new Order("O504", "Diana Prince", "P102")); // Pushes O504 to Stack
        system.archiveProduct("P103"); // Moves P103 from ArrayList to Vector

        system.displayAllInfo();

        System.out.println("=== STEP 3: PERSISTING DATA TO DISK ===");
        system.saveDataToFiles();

        System.out.println("\n=== STEP 4: VERIFYING RESTORATION IN NEW INSTANCE ===");
        AmazonInventorySystem restoredSystem = new AmazonInventorySystem();
        restoredSystem.loadDataFromFiles();
        restoredSystem.displayAllInfo();
    }
}