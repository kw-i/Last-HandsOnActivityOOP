import java.util.Scanner;

public class InvoiceApp {
    public static void main(String[] args) {
        InvoiceDatabase dbManager = new InvoiceDatabase();
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("--- Invoice Menu ---");
            System.out.println("1. Add Invoice");
            System.out.println("2. Displace Invoices with Balance");
            System.out.println("3. Pay Invoice");
            System.out.println("4. Delete Invoice");
            System.out.println("5. Exit");
            System.out.print("Choose an Option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            System.out.println();

            if (choice == 1){
                System.out.print("Enter Invoice Number: ");
                String invno = scanner.nextLine();
                System.out.print("Enter Customer Name: ");
                String customer = scanner.nextLine();
                System.out.print("Enter Invoice Amount: ");
                int amount = scanner.nextInt();
                int payment = 0;
                if (dbManager.addInfo(invno,customer,amount,payment)) {
                        System.out.println("User successfully added.");
                    } else {
                        System.out.println("Failed to add user.");
                    }
            }
            else if (choice == 2){
                dbManager.displayInvoice();
            }
            else if (choice == 3){
                System.out.print("Enter Invoice Number to pay: ");
                String invno = scanner.nextLine();
                System.out.print("Enter payment amount: ");
                int payment = scanner.nextInt();
                if (dbManager.updatePayment(invno,payment)) {
                        System.out.println("Payment recorded.");
                    } else {
                        System.out.println("Payment Failed.");
                    }
                System.out.println();
            }
            else if (choice == 4){
                System.out.print("Enter Invoice Number to Delete: ");
                String invnodel = scanner.nextLine();
                Boolean Result = dbManager.deleteInvoice(invnodel);
                if (Result == true){
                    System.out.println("Invoice deleted successfully.");
                }
                else{
                    System.out.println("Cannot delete invoice. Payment has already been made");
                }
            }
            else if (choice == 5){
                scanner.close();
                break;
            }
            else{
                 System.out.println("Rioben <3 Kate");
            }
        }
    }
}
