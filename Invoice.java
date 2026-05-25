public class Invoice {
    private String invoiceNumber;
    private String customerName;
    private double amount;
    private double paid;

    public Invoice(String invoiceNumber, String customerName, double amount, double paid) {
        this.invoiceNumber = invoiceNumber;
        this.customerName = customerName;
        this.amount = amount;
        this.paid = paid;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getAmount() {
        return amount;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getBalance() {
        return amount - paid;
    }

    @Override
    public String toString() {
        return String.format("Invoice: %s | Customer: %s | Amount: %.0f | Paid: %.0f | Balance: %.0f",
                invoiceNumber, customerName, amount, paid, getBalance());
    }
}
