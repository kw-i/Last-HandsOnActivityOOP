import java.sql.*;

public class InvoiceDatabase{
    private String url = "jdbc:mysql://localhost:3306/javadb";
    private String dbUser = "root";
    private String dbPassword = "";

    public boolean addInfo(String invno, String customer, int amount, int payment) {
        String insertQuery = "INSERT INTO receivable (invno,customer,amount, payment) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement pst = conn.prepareStatement(insertQuery)) {

                pst.setString(1, invno);
                pst.setString(2, customer);
                pst.setInt(3, amount);
                pst.setInt(4, payment);

                int rowsAffected = pst.executeUpdate();
                return rowsAffected > 0;

            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) { 
                    System.out.println("Invoice Number already Exists");
                } else {
                    e.printStackTrace();
                }
                return false;
            }
        }
    
        public boolean updatePayment(String invno, int payment) {
        String updateQuery = "UPDATE receivable SET payment = ? WHERE invno = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            
            PreparedStatement pst = conn.prepareStatement(updateQuery)) {

            pst.setInt(1, payment);
            pst.setString(2, invno);

            int rowsUpdated = pst.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        public void displayInvoice() {
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM receivable")) {

            while (rs.next()) {
                String invno = rs.getString("invno");
                String customer = rs.getString("customer");
                int amount = rs.getInt("amount");
                int payment = rs.getInt("payment");
                System.out.println("Invoice: " + invno + " | Customer: " + customer + " | Balance: " + (amount - payment));
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        public boolean deleteInvoice(String invno) {
        String deleteQuery = "DELETE FROM receivable WHERE invno = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
            PreparedStatement pst = conn.prepareStatement(deleteQuery)) {
            
            pst.setString(1, invno);
            int rowsDeleted = pst.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}