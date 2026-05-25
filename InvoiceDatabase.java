import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDatabase {

    private static final String URL      = "jdbc:mysql://localhost:3306/invoice_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ── Add a new invoice ────────────────────────────────────────────────────
    public void addInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoices (invoice_number, customer_name, amount, paid) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, invoice.getInvoiceNumber());
            ps.setString(2, invoice.getCustomerName());
            ps.setDouble(3, invoice.getAmount());
            ps.setDouble(4, invoice.getPaid());
            ps.executeUpdate();
            System.out.println("Invoice added successfully.");

        } catch (SQLException e) {
            System.out.println("Error adding invoice: " + e.getMessage());
        }
    }

    // ── Fetch invoices with remaining balance for a customer ─────────────────
    public List<Invoice> getInvoicesWithBalance(String customerName) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT invoice_number, customer_name, amount, paid " +
                     "FROM invoices " +
                     "WHERE customer_name = ? AND amount > paid " +
                     "ORDER BY invoice_number ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Invoice(
                        rs.getString("invoice_number"),
                        rs.getString("customer_name"),
                        rs.getDouble("amount"),
                        rs.getDouble("paid")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching invoices: " + e.getMessage());
        }
        return list;
    }

    // ── Update paid amount for a specific invoice ─────────────────────────────
    public void updatePaid(String invoiceNumber, double newPaid) {
        String sql = "UPDATE invoices SET paid = ? WHERE invoice_number = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newPaid);
            ps.setString(2, invoiceNumber);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error updating payment: " + e.getMessage());
        }
    }

    // ── Distribute payment across invoices (FIFO) ────────────────────────────
    public void distributePayment(String customerName, double totalPayment) {
        List<Invoice> invoices = getInvoicesWithBalance(customerName);

        if (invoices.isEmpty()) {
            System.out.println("No outstanding invoices for " + customerName + ".");
            return;
        }

        System.out.println("\n--- Invoices with Balance ---");
        for (Invoice inv : invoices) {
            System.out.println(inv);
        }

        System.out.print("Enter total payment amount: ");
        // totalPayment is passed in from InvoiceApp after reading from scanner

        double remaining = totalPayment;

        for (Invoice inv : invoices) {
            if (remaining <= 0) break;
            double balance = inv.getBalance();
            if (remaining >= balance) {
                inv.setPaid(inv.getAmount());          // fully paid
                remaining -= balance;
            } else {
                inv.setPaid(inv.getPaid() + remaining); // partial payment
                remaining = 0;
            }
            updatePaid(inv.getInvoiceNumber(), inv.getPaid());
        }

        System.out.println("Payment distributed successfully.");

        // Compute updated total balance
        double updatedBalance = getTotalBalance(customerName);
        System.out.println("Updated Total Balance for " + customerName + ": " + (int) updatedBalance);
    }

    // ── Calculate remaining total balance for a customer ─────────────────────
    public double getTotalBalance(String customerName) {
        String sql = "SELECT SUM(amount - paid) AS total_balance FROM invoices WHERE customer_name = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_balance");
            }
        } catch (SQLException e) {
            System.out.println("Error getting balance: " + e.getMessage());
        }
        return 0;
    }
}
