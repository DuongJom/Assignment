import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CalculatorInterface {

    private static Client[] clients;
    private static int noClients = 0;
    private static Scanner sc = new Scanner(System.in);
    private static List<String> validChooseResidents = Arrays.asList("Y", "N", "YES", "NO");

    private static void AddClient() {

        if (noClients == 7) {
            ShowMessage("It is not possible to add more clients.", false);
            return;
        }

        // Input client name
        String name;
        do {
            System.out.print("Enter client name: ");
            sc.nextLine();
            name = sc.nextLine();

            if (name.isEmpty() || !name.matches(".*\\s.*")) {
                ShowMessage(
                        "Error: name must have at least two names (first name and last name).", true);
            }
        } while (name.isEmpty() || !name.matches(".*\\s.*"));

        // Input annual salary
        double annualSalary;
        do {
            System.out.print("Enter annual salary: ");
            annualSalary = sc.nextDouble();

            if (annualSalary <= 0) {
                ShowMessage("Error: income must be a positive number and different from zero", true);
            }
        } while (annualSalary <= 0);

        // Input resident
        boolean resident;
        String result;
        do {
            System.out.print("Are you a resident? [Y/n]: ");
            sc.nextLine();
            result = sc.nextLine();

            if (result.isEmpty() || !validChooseResidents.contains(result.toUpperCase())) {
                ShowMessage("Error: please enter [Y] for resident or [n] for non-resident", true);
            }
        } while (result.isEmpty() || !validChooseResidents.contains(result.toUpperCase()));

        if (result.toUpperCase().equals("Y") || result.toUpperCase().equals("YES")) {
            resident = true;
        } else
            resident = false;

        // Input weekly expenses
        double weeklyExpenses;
        do {
            System.out.print("Enter weekly expenses: ");
            weeklyExpenses = sc.nextDouble();
            if (weeklyExpenses <= 0 || weeklyExpenses >= annualSalary / 48) {
                ShowMessage("Error: expenditure must be a positive number and different from zero", true);
            }
        } while (weeklyExpenses <= 0 || weeklyExpenses >= annualSalary / 48);

        // Create a client
        Client client = new Client();
        client.setName(name);
        client.setGrossSalary(annualSalary);
        client.setResident(resident);
        client.setWeeklyExpenses(weeklyExpenses);

        // Add the client to the list of clients
        List<Client> tmpClients = new ArrayList<Client>();
        if (clients == null) {
            clients = new Client[7];
        } else {
            for (Client client2 : clients) {
                if (client2 != null) {
                    tmpClients.add(client2);
                }
            }
        }

        tmpClients.add(client);
        clients = tmpClients.toArray(clients);
        noClients++;
    }
    
    private static void DeleteClient() {

        System.out.print("Enter client name to delete: ");
        sc.nextLine();
        String clientNameToDelete = sc.nextLine();

        if (clientNameToDelete.trim().length() > 0) {

            Client clientToDelete = getClientByName(clientNameToDelete.trim());

            if (clientToDelete != null) {
                RemoveClient(clientToDelete);
                ShowMessage("The client was deleted.", false);
            } else {
                ShowMessage("The client does not exist.", false);
            }
        }
    }

    private static void DisplayClient() {

        System.out.print("Enter client name to display: ");
        sc.nextLine();
        String clientNameToDisplay = sc.nextLine();

        if (clientNameToDisplay.trim().length() > 0) {

            Client client = getClientByName(clientNameToDisplay.trim());

            if (client != null) {
                ShowInformationOfClient(client);
            } else {
                ShowMessage("The client does not exist.", false);
            }
        }
    }

    private static void DisplayAllClients() {

        if (noClients == 0) {
            ShowMessage("No clients", false);
            return;
        }

        int idx = 1;
        for (Client client : clients) {
            if (client != null) {
                System.out.println("Client " + idx + ": ");
                System.out.println("Name: " + client.getName());
                System.out.println("Gross Salary per week: $" + client.getGrossSalary() / 48);
                System.out.println("Residency status: " + (client.getResident() ? "Resident" : "Non-resident"));

                client.calcTax();
                double taxPerYear = client.getTax();
                double taxPerWeek = taxPerYear / 48;
                System.out.println("Tax per week: $" + taxPerWeek);

                double netSalaryPerYear = client.getNetSalary();
                double netSalaryPerWeek = netSalaryPerYear / 48;
                System.out.println("Net salary per week: $" + netSalaryPerWeek);
                client.calcMedicare();

                if (client.getMedicare() != 0.0) {
                    double medicareLevyPerYear = client.getMedicare();
                    System.out.println("Medicare Levy per year: $" + medicareLevyPerYear);
                } else {
                    System.out.println("Does not pay Medicare Levy.");
                }
                System.out.println("Expenses per week: $" + client.getWeeklyExpenses());
                System.out.println(
                        "==================================================================================================");
                idx++;
            }

        }
    }

    private static void AddAccount() {

        System.out.print("Enter name of client need to add account: ");
        sc.nextLine();
        String clientName = sc.nextLine();

        Client client = getClientByName(clientName.trim());

        if (client == null) {
            ShowMessage("Client no exist.", false);
            return;
        }

        if (client.getAccount() != null && client.getAccount().length == 3) {
            ShowMessage("Client has already 3 accounts.", false);
            return;
        }

        double investment;
        do {
            System.out.print("Enter investment value: ");
            investment = sc.nextDouble();

            if (investment > client.getInvestmentAmount()) {
                ShowMessage("Value is invalid!", true);
            }
        } while (investment > client.getInvestmentAmount());

        double rate;
        do {
            System.out.print("Enter interest rate: ");
            rate = sc.nextDouble();

            if (rate < 0.05 || rate > 0.1) {
                ShowMessage("Value is invalid!", true);
            }
        } while (rate < 0.05 || rate > 0.1);

        int numOfWeeks;
        do {
            System.out.print("Enter investment length: ");
            numOfWeeks = sc.nextInt();

            if (numOfWeeks <= 0) {
                ShowMessage("Value is invalid!", true);
            }
        } while (numOfWeeks <= 0);

        Account account = new Account(rate, numOfWeeks, investment);
        client.addAccount(account);

        // Update list of clients
        UpdateClientList(client);
    }

    private static void DisplayAccount() {

        System.out.print("Enter the name of the client: ");
        sc.nextLine();
        String name = sc.nextLine();

        Client client = getClientByName(name.trim());

        if (client == null) {
            ShowMessage("The Client does not exist!", false);
            return;
        }

        System.out.print("Enter the account number (1, 2 or 3): ");
        int accountNum = sc.nextInt();
        sc.nextLine(); // consume the newline character

        if (accountNum < 0 || accountNum > 3 || client.getAccount() == null) {
            ShowMessage("The account does not exist!", false);
            return;
        }

        Account account = client.getAccount()[accountNum - 1];

        if (account != null) {

            double amountInvested = account.getAmount();
            double interestRate = account.getRate();
            int numWeeks = account.getNumberOfWeeks();
            double totalAmount = amountInvested;
            double weeklyAmount = amountInvested;

            System.out.println("Investment details for account " + (accountNum) + ":");
            System.out.printf("%-10s %-15s %-15s\n", "Week", "Amount", "Total Amount");
            System.out.println("----------------------------------------");

            for (int i = 1; i <= numWeeks; i++) {
                double interest = weeklyAmount * (interestRate / 100.0) / 4.0;
                totalAmount += interest;
                weeklyAmount += interest;
                System.out.printf("%-10d $%-14.2f $%-14.2f\n", i, weeklyAmount, totalAmount);

            }
        }
    }

    private static void DeleteAccount() {

        System.out.print("Enter the name of client: ");
        sc.nextLine();
        String name = sc.nextLine();

        Client client = getClientByName(name.trim());

        if (client == null) {
            ShowMessage("The Client does not exist.", false);
            return;
        }

        System.out.print("Enter the account you want to delete (1, 2 or 3): ");
        int accountIndex = sc.nextInt();

        if (accountIndex < 0 || accountIndex > 3 || client.getAccount() == null) {
            ShowMessage("Account does not exist!", false);
            return;
        }

        client.deleteAccount(accountIndex);
        System.out.println("The account was deleted.");
    }

    private static void SaveFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("clients.txt");
            PrintWriter out = new PrintWriter(fileOutputStream);

            for (Client client : clients) {
                if (client != null) {
                    out.write("Name: " + client.getName() + "\n");
                    out.write("Gross Salary: " + client.getGrossSalary() + "\n");
                    out.write("Residential Status: " + (client.getResident() ? "Resident" : "Non-Resident") + "\n");
                    out.write("Weekly Expenses: " + client.getWeeklyExpenses() + "\n");
                    out.write("Number of Accounts: " + client.getAccount().length + "\n");
                    out.write("====================================\n");

                    for (Account account : client.getAccount()) {
                        if (account != null) {
                            out.write("Amount: " + account.getAmount() + "\n");
                            out.write("Interest Rate: " + account.getRate() + "\n");
                            out.write("Number of Weeks: " + account.getNumberOfWeeks() + "\n");
                            out.write(
                                    "======================================================================================\n");
                        }
                    }
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Error saving to file.");
        }
    }

    private static void HandleMenu(int choice) {

        switch (choice) {
            case 1:
                AddClient();
                break;
            case 2:
                DeleteClient();
                break;
            case 3:
                DisplayClient();
                break;
            case 4:
                DisplayAllClients();
                break;
            case 5:
                AddAccount();
                break;
            case 6:
                DisplayAccount();
                break;
            case 7:
                DeleteAccount();
                break;
            case 8:
                SaveFile();
                break;
            case 9:
                System.out.println("Thank you for using the Tax Calculator");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice");
                break;

        }
    }

    private static void ShowMenu() {

        System.out.println("");
        System.out.println("");
        System.out.println("========================================");
        System.out.println("Welcome to the Tax Calculator");
        System.out.println("1. Add a client");
        System.out.println("2. Delete a client");
        System.out.println("3. Display a client");
        System.out.println("4. Display all clients");
        System.out.println("5. Add an account");
        System.out.println("6. Display an account");
        System.out.println("7. Delete an account");
        System.out.println("8. Save to file");
        System.out.println("9. Exit");
    }

    private static Client getClientByName(String name) {

        for (Client client : clients) {
            if (client.getName().equals(name)) {
                return client;
            }
        }
        return null;
    }

    private static void ShowInformationOfClient(Client client) {

        System.out.println("Name: " + client.getName());
        System.out.println("Gross salary per year: " + client.getGrossSalary());
        System.out.println("Gross salary per week: " + client.getGrossSalary() / 48);
        System.out.println("Resident status: " + (client.getResident() ? "Resident" : "Non-Resident"));
        System.out.println("Tax per year: $" + client.getTax());
        System.out.println("Tax per week: $" + client.getTax() / 48);
        System.out.println("Net salary per year: $" + client.getNetSalary());
        System.out.println("Net salary per week: $" + client.getNetSalary() / 48);

        if (client.getMedicare() != 0) {
            System.out.println("Medicare Levy per year: $" + client.getMedicare());
        } else {
            System.out.println("Does not pay Medicare Levy.");
        }

        System.out.println("Expenses per week: $" + client.getWeeklyExpenses());

        double remainingInvestmentPerWeek = client.getNetSalary() / 48 - client.getWeeklyExpenses()
                - client.getInvestmentAmount();
        System.out.println("Remaining amount possible to be invested per week: $" + remainingInvestmentPerWeek);

        Account[] accounts = client.getAccount();

        if (accounts == null || accounts.length == 0) {
            System.out.println("No investment accounts.");
        } else {
            for (Account account : accounts) {
                if (account != null) {
                    System.out.println("Account information:");
                    System.out.println("Amount invested per week: $" + account.getAmount());
                    System.out.println("Interest rate: " + account.getRate() + "%");
                    System.out.println("Number of weeks: " + account.getNumberOfWeeks());
                    String totalAmount = account.calcInvestment();
                    System.out.println("Total amount at the end of the period: $" + totalAmount);
                }
            }
        }
    }

    private static void RemoveClient(Client client) {

        Client[] newClients = new Client[clients.length - 1];
        int idx = 0;

        for (Client client2 : clients) {
            if (client2 != null && !client2.getName().equals(client.getName())) {
                newClients[idx++] = client2;
            }
        }
        clients = newClients;
    }

    private static void UpdateClientList(Client client) {

        for (Client item : clients) {
            if (item.getName().equals(client.getName())) {
                item = client;
                break;
            }
        }
    }

    private static void ShowMessage(String message, boolean showEnterAgainMessage) {

        System.out.println(message);

        if (showEnterAgainMessage) {
            System.out.println("Please enter again!");
        }
    }

    public static void main(String[] args) {
        while (true) {
            ShowMenu();

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            HandleMenu(choice);
        }
    }
}
