# 🛒 Amazon Inventory & Order Management System

A comprehensive Java micro-project demonstrating real-world applications of the **Java Collection Framework (JCF)** and **File Handling** by simulating back-end operations for an e-commerce platform like Amazon.

---

## 📌 Project Overview

This application manages active product listings, customer order processing pipelines, cancellation logs, supplier directories, and archived items using optimal Java collection classes. Additionally, it implements text-file persistence (`java.io`) to ensure all operations and system states are preserved across application restarts.

---

## ⚙️ Data Structures & Collection Mapping

| Domain Entity | Java Collection | Data Structure Logic | Reason for Choice |
| :--- | :--- | :--- | :--- |
| **Active Products** | `ArrayList<Product>` | Dynamic Array | Allows fast $O(1)$ random access and dynamic sizing for active inventory listings. |
| **Pending Orders** | `Queue<Order>` (`LinkedList`) | First-In-First-Out (FIFO) | Ensures customer orders are processed and fulfilled strictly in the order they were placed. |
| **Cancelled Orders** | `Stack<Order>` | Last-In-First-Out (LIFO) | Keeps track of recently cancelled orders, allowing instant access to the top/latest cancellation. |
| **Supplier Directory** | `Hashtable<String, String>` | Hash Table (Key-Value) | Thread-safe, synchronized mapping for instantaneous $O(1)$ lookup of supplier details by ID. |
| **Archived Products** | `Vector<Product>` | Synchronized Dynamic Array | Thread-safe collection for storing legacy or discontinued product records. |

---

## 💾 File Handling & Persistence

All collection data is automatically stored in and restored from `.txt` files using `BufferedWriter` and `BufferedReader`:

- `products.txt` — Stores active product inventory.
- `pending_orders.txt` — Stores unfulfilled orders queue.
- `cancelled_orders.txt` — Stores the stack of cancelled orders.
- `suppliers.txt` — Stores supplier mapping data.
- `archived_products.txt` — Stores legacy/archived product records.

---

## 📂 Project Structure

```text
.
├── AmazonInventorySystem.java   # Main Java source file containing model classes & logic
├── products.txt                 # Auto-generated persistence file
├── pending_orders.txt          # Auto-generated persistence file
├── cancelled_orders.txt        # Auto-generated persistence file
├── suppliers.txt                # Auto-generated persistence file
├── archived_products.txt        # Auto-generated persistence file
└── README.md                    # Project documentation
```

---

## 🚀 How to Run

### Prerequisites
- **Java Development Kit (JDK):** Version 8 or higher installed.

### Compilation and Execution

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/mangoproject80-meow/AmazonInventorySystem
   cd AmazonInventorySystem
   ```

2. **Compile the Java Program:**
   ```bash
   javac AmazonInventorySystem.java
   ```

3. **Run the Application:**
   ```bash
   java AmazonInventorySystem
   ```

---

## 📸 Demo Output Workflow

1. **Initialization:** Populates all collections with sample product, order, and supplier records.
2. **Operations Execution:**
   - Dequeues and processes the oldest order from the pending `Queue`.
   - Pushes a new cancellation onto the `Stack`.
   - Moves an active item from `ArrayList` to the `Vector` archive.
3. **File Persistence:** Exports current states to local text files.
4. **State Restoration:** Creates a fresh system instance and reloads records directly from text files to demonstrate file-handling restoration.

---

## 📄 License

This project is created for educational and micro-project evaluation purposes. Feel free to use and modify it!
