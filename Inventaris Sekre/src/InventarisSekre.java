import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InventarisSekre extends JFrame {
    private static final String URL = "jdbc:postgresql://localhost:5432/invetaris sekre"; // Nama database baru
    private static final String USER = "postgres"; // Ganti dengan username Anda
    private static final String PASSWORD = "akram1101"; // Ganti dengan password Anda

    private JTextField idField, nameField, quantityField, dateField;
    private JTextArea outputArea;

    public InventarisSekre() {
        setTitle("Inventaris Sekre");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Input fields
        idField = new JTextField(10);
        nameField = new JTextField(10);
        quantityField = new JTextField(10);
        dateField = new JTextField(10);

        // Buttons
        JButton addButton = new JButton("Add Product");
        JButton updateButton = new JButton("Update Product");
        JButton deleteButton = new JButton("Delete Product");
        JButton viewButton = new JButton("View Product");
        JButton viewAllButton = new JButton("View All Products");

        // Output area
        outputArea = new JTextArea(20, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add components to the frame
        add(new JLabel("ID:"));
        add(idField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Quantity:"));
        add(quantityField);
        add(new JLabel("Date (yyyy-MM-dd):"));
        add(dateField);
        add(addButton);
        add(updateButton);
        add(deleteButton);
        add(viewButton);
        add(viewAllButton);
        add(scrollPane);

        // Button actions
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addItem();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateItem();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteItem();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewItem();
            }
        });

        viewAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewAllItems();
            }
        });
    }

    // Method to add a product to the database
    public void addItem() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO inventaris (id, name, quantity, date_added) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                Date dateAdded = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());

                pstmt.setInt(1, id);
                pstmt.setString(2, name);
                pstmt.setInt(3, quantity);
                pstmt.setDate(4, new java.sql.Date(dateAdded.getTime()));
                pstmt.executeUpdate();
                outputArea.append("Product added successfully.\n");
            }
        } catch (Exception e) {
            outputArea.append("Error adding product: " + e.getMessage() + "\n");
        }
    }

    // Method to update a product in the database
    public void updateItem() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD )) {
            String sql = "UPDATE inventaris SET name = ?, quantity = ?, date_added = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                Date dateAdded = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());

                pstmt.setString(1, name);
                pstmt.setInt(2, quantity);
                pstmt.setDate(3, new java.sql.Date(dateAdded.getTime()));
                pstmt.setInt(4, id); // Memperbaiki kesalahan di sini
                pstmt.executeUpdate();
                outputArea.append("Product updated successfully.\n");
            }
        } catch (Exception e) {
            outputArea.append("Error updating product: " + e.getMessage() + "\n");
        }
    }

    // Method to delete a product from the database
    public void deleteItem() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM inventaris WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int id = Integer.parseInt(idField.getText());
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                outputArea.append("Product deleted successfully.\n");
            }
        } catch (Exception e) {
            outputArea.append("Error deleting product: " + e.getMessage() + "\n");
        }
    }

    // Method to view a specific product
    public void viewItem() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM inventaris WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int id = Integer.parseInt(idField.getText());
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    Date dateAdded = rs.getDate("date_added");
                    outputArea.append("ID: " + id + ", Name: " + name + ", Quantity: " + quantity + ", Date Added: " + dateAdded + "\n");
                } else {
                    outputArea.append("Product not found.\n");
                }
            }
        } catch (Exception e) {
            outputArea.append("Error viewing product: " + e.getMessage() + "\n");
        }
    }

    // Method to view all products
    public void viewAllItems() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM inventaris";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery(sql);
                outputArea.setText(""); // Clear previous output
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int quantity = rs.getInt("quantity");
                    Date dateAdded = rs.getDate("date_added");
                    outputArea.append("ID: " + id + ", Name: " + name + ", Quantity: " + quantity + ", Date Added: " + dateAdded + "\n");
                }
            }
        } catch (Exception e) {
            outputArea.append("Error viewing all products: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InventarisSekre app = new InventarisSekre();
            app.setVisible(true);
        });
    }
}