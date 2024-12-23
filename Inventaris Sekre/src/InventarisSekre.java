import java.sql.*; // Import untuk JDBC
import java.text.SimpleDateFormat; // Import untuk format tanggal
import java.util.ArrayList; // Import untuk ArrayList
import java.util.Date; // Import untuk Date
import java.util.List; // Import untuk List
import java.util.Scanner; // Import untuk Scanner

public class InventarisSekre {
    // URL koneksi ke database PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/invetaris sekre"; // Nama database baru
    private static final String USER = "postgres"; // Ganti dengan username Anda
    private static final String PASSWORD = "akram1101"; // Ganti dengan password Anda

    public static void main(String[] args) {
        InventarisSekre app = new InventarisSekre(); // Membuat instance dari kelas InventarisSekre
        Scanner scanner = new Scanner(System.in); // Scanner untuk input pengguna
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Format tanggal

        // Loop utama untuk menu aplikasi
        while (true) {
            // Menampilkan menu pilihan
            System.out.println("1. Add Product");
            System.out.println("2. Update Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View Product");
            System.out.println("5. View All Products");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt(); // Mengambil pilihan dari pengguna

            switch (choice) {
                case 1: // Menambahkan produk
                    System.out.print("Enter product ID: ");
                    int id = scanner.nextInt(); // Input ID
                    System.out.print("Enter product name: ");
                    String name = scanner.next(); // Input nama produk
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt(); // Input kuantitas
                    System.out.print("Enter date added (yyyy-MM-dd): ");
                    Date dateAdded = null; // Inisialisasi tanggal ditambahkan
                    try {
                        dateAdded = sdf.parse(scanner.next()); // Parsing input tanggal
                    } catch (Exception e) {
                        e.printStackTrace(); // Menangani kesalahan parsing
                    }
                    app.addItem(new Product(id, name, quantity, dateAdded)); // Menambahkan produk
                    break;
                case 2: // Memperbarui produk
                    System.out.print("Enter product ID to update: ");
                    int updateId = scanner.nextInt(); // Input ID produk yang akan diperbarui
                    System.out.print("Enter new product name: ");
                    String newName = scanner.next(); // Input nama produk baru
                    System.out.print("Enter new quantity: ");
                    int newQuantity = scanner.nextInt(); // Input kuantitas baru
                    System.out.print("Enter new date added (yyyy-MM-dd): ");
                    Date newDateAdded = null; // Inisialisasi tanggal baru
                    try {
                        newDateAdded = sdf.parse(scanner.next()); // Parsing input tanggal baru
                    } catch (Exception e) {
                        e.printStackTrace(); // Menangani kesalahan parsing
                    }
                    app.updateItem(new Product(updateId, newName, newQuantity, newDateAdded)); // Memperbarui produk
                    break;
                case 3: // Menghapus produk
                    System.out.print("Enter product ID to delete: ");
                    int deleteId = scanner.nextInt(); // Input ID produk yang akan dihapus
                    app.deleteItem(deleteId); // Menghapus produk
                    break;
                case 4: // Melihat produk berdasarkan ID
                    System.out.print("Enter product ID to view: ");
                    int viewId = scanner.nextInt(); // Input ID produk yang akan dilihat
                    Product product = app.getItem(viewId); // Mengambil produk berdasarkan ID
                    if (product != null) {
                        // Menampilkan informasi produk
                        System.out.println("Product ID: " + product.getId());
                        System.out.println("Product Name: " + product.getName());
                        System.out.println("Quantity: " + product.getQuantity());
                        System.out.println("Date Added: " + sdf.format(product.getDateAdded()));
                    } else {
                        System.out.println("Product not found."); // Jika produk tidak ditemukan
                    }
                    break;
                case 5: // Melihat semua produk
                    List<Product> allProducts = app.getAllItems(); // Mengambil semua produk
                    for (Product p : allProducts) {
                        // Menampilkan informasi semua produk
                        System.out.println("ID: " + p.getId() + ", Name: " + p.getName() + ", Quantity: " + p.getQuantity() + ", Date Added: " + sdf.format(p.getDateAdded()));
                    }
                    break;
                case 6: // Keluar dari aplikasi
                    System.out.println("Exiting...");
                    scanner.close(); // Menutup scanner
                    return; // Menghentikan program
                default: // Pilihan tidak valid
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Method untuk menambahkan produk ke database
    public void addItem(Product product) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) { // Membuka koneksi ke database
            String sql = "INSERT INTO inventaris (id, name, quantity, date_added) VALUES (?, ?, ?, ?)"; // Query untuk menambahkan produk
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) { // Menyiapkan statement
                pstmt.setInt(1, product.getId()); // Mengatur ID produk
                pstmt.setString(2, product.getName()); // Mengatur nama produk
                pstmt.setInt(3, product.getQuantity()); // Mengatur kuantitas
                pstmt.setDate(4, new java.sql.Date(product.getDateAdded().getTime())); // Mengatur tanggal ditambahkan
                pstmt.executeUpdate(); // Menjalankan query
                System.out.println("Product added successfully."); // Konfirmasi penambahan
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Menangani kesalahan SQL
        }
    }

    // Method untuk memperbarui produk dalam database
    public void updateItem(Product product) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) { // Membuka koneksi ke database
            String sql = "UPDATE inventaris SET name = ?, quantity = ?, date_added = ? WHERE id = ?"; // Query untuk memperbarui produk
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) { // Menyiapkan statement
                pstmt.setString(1, product.getName()); // Mengatur nama produk baru
                pstmt.setInt(2, product.getQuantity()); // Mengatur kuantitas baru
                pstmt.setDate(3, new java.sql.Date(product.getDateAdded().getTime())); // Mengatur tanggal baru
                pstmt.setInt(4, product.getId()); // Mengatur ID produk
                pstmt.executeUpdate(); // Menjalankan query
                System.out.println("Product updated successfully."); // Konfirmasi pembaruan
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Menangani kesalahan SQL
        }
    }

    // Method untuk menghapus produk dari database
    public void deleteItem(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) { // Membuka koneksi ke database
            String sql = "DELETE FROM inventaris WHERE id = ?"; // Query untuk menghapus produk
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) { // Menyiapkan statement
                pstmt.setInt(1, id); // Mengatur ID produk yang akan dihapus
                pstmt.executeUpdate(); // Menjalankan query
                System.out.println("Product deleted successfully."); // Konfirmasi penghapusan
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Menangani kesalahan SQL
        }
    }

    // Method untuk mendapatkan produk dari database
    public Product getItem(int id) {
        Product product = null; // Inisialisasi produk
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) { // Membuka koneksi ke database
            String sql = "SELECT * FROM inventaris WHERE id = ?"; // Query untuk mendapatkan produk berdasarkan ID
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) { // Menyiapkan statement
                pstmt.setInt(1, id); // Mengatur ID produk
                ResultSet rs = pstmt.executeQuery(); // Menjalankan query
                if (rs.next()) { // Jika ada hasil
                    product = new Product(rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"), rs.getDate("date_added")); // Membuat objek produk
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Menangani kesalahan SQL
        }
        return product; // Mengembalikan produk
    }

    // Method untuk mendapatkan semua produk dari database
    public List<Product> getAllItems() {
        List<Product> products = new ArrayList<>(); // Membuat list untuk menyimpan produk
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) { // Membuka koneksi ke database
            String sql = "SELECT * FROM inventaris"; // Query untuk mendapatkan semua produk
            try ( Statement stmt = conn.createStatement()) { // Menyiapkan statement
                ResultSet rs = stmt.executeQuery(sql); // Menjalankan query
                while (rs.next()) { // Mengambil setiap hasil
                    products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"), rs.getDate("date_added"))); // Menambahkan produk ke list
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Menangani kesalahan SQL
        }
        return products; // Mengembalikan list produk
    }
}

// Kelas untuk merepresentasikan sebuah Produk
class Product {
    private int id; // ID produk
    private String name; // Nama produk
    private int quantity; // Kuantitas produk
    private Date dateAdded; // Tanggal produk ditambahkan

    public Product(int id, String name, int quantity, Date dateAdded) {
        this.id = id; // Mengatur ID produk
        this.name = name; // Mengatur nama produk
        this.quantity = quantity; // Mengatur kuantitas produk
        this.dateAdded = dateAdded; // Mengatur tanggal ditambahkan
    }

    public int getId() {
        return id; // Mengembalikan ID produk
    }

    public String getName() {
        return name; // Mengembalikan nama produk
    }

    public int getQuantity() {
        return quantity; // Mengembalikan kuantitas produk
    }

    public Date getDateAdded() {
        return dateAdded; // Mengembalikan tanggal ditambahkan
    }
}