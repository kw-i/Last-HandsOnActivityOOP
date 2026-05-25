import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class InvoiceApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InvoiceDatabase db = new InvoiceDatabase();
        int choice;

        do {
            System.out.println("\n--- Invoice Menu ---");
            System.out.println("1. Add Invoice");
            System.out.println("2. Pay Invoice");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice) {
                case 1:
                    // ── Add Invoice ──────────────────────────────────────
                    System.out.print("Enter Invoice Number: ");
                    String invoiceNumber = scanner.nextLine().trim();

                    System.out.print("Enter Customer Name: ");
                    String customerName = scanner.nextLine().trim();

                    System.out.print("Enter Invoice Amount: ");
                    double amount = Double.parseDouble(scanner.nextLine().trim());

                    Invoice invoice = new Invoice(invoiceNumber, customerName, amount, 0);
                    db.addInvoice(invoice);
                    break;

                case 2:
                    // ── Pay Invoice ──────────────────────────────────────
                    System.out.print("Enter Customer Name: ");
                    String payCustomer = scanner.nextLine().trim();

                    List<Invoice> invoices = db.getInvoicesWithBalance(payCustomer);

                    if (invoices.isEmpty()) {
                        System.out.println("No outstanding invoices for " + payCustomer + ".");
                        break;
                    }

                    System.out.println("\n--- Invoices with Balance ---");
                    for (Invoice inv : invoices) {
                        System.out.println(inv);
                    }

                    System.out.print("Enter total payment amount: ");
                    double payment = Double.parseDouble(scanner.nextLine().trim());

                    // Distribute payment FIFO
                    double remaining = payment;
                    for (Invoice inv : invoices) {
                        if (remaining <= 0) break;
                        double balance = inv.getBalance();
                        if (remaining >= balance) {
                            inv.setPaid(inv.getAmount());
                            remaining -= balance;
                        } else {
                            inv.setPaid(inv.getPaid() + remaining);
                            remaining = 0;
                        }
                        db.updatePaid(inv.getInvoiceNumber(), inv.getPaid());
                    }

                    System.out.println("Payment distributed successfully.");
                    double updatedBalance = db.getTotalBalance(payCustomer);
                    System.out.println("Updated Total Balance for " + payCustomer + ": " + (int) updatedBalance);
                    break;

                case 3:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } while (choice != 3);

        scanner.close();
    }
}