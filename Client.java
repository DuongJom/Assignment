import java.util.ArrayList;
import java.util.List;

public class Client {
    private String name;
    private double grossSalary;
    private double netSalary;
    private boolean resident;
    private double tax;
    private double medicare;
    private double weeklyExpenses;
    private Account[] account;

    Client() {
        this.name = "";
        this.grossSalary = 0D;
        this.netSalary = 0D;
        this.resident = false;
        this.tax = 0D;
        this.medicare = 0D;
        this.weeklyExpenses = 0D;
    }

    Client(String name, double grossSalary, double netSalary, boolean resident, double tax,
            double medicare, double weeklyExpenses, Account[] account) {
        this.name = name;
        this.grossSalary = grossSalary;
        this.netSalary = netSalary;
        this.resident = resident;
        this.tax = tax;
        this.medicare = medicare;
        this.weeklyExpenses = weeklyExpenses;
        this.account = account;
    }

    String getName() {
        return this.name;
    }

    void setName(String name) {
        this.name = name;
    }

    double getGrossSalary() {
        return this.grossSalary;
    }

    void setGrossSalary(double grossSalary) {
        this.grossSalary = grossSalary;
    }

    double getNetSalary() {
        return this.netSalary;
    }

    void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    boolean getResident() {
        return this.resident;
    }

    void setResident(boolean resident) {
        this.resident = resident;
    }

    double getTax() {
        return this.tax;
    }

    void setTax(double tax) {
        this.tax = tax;
    }

    double getMedicare() {
        return this.medicare;
    }

    void setMedicare(double medicare) {
        this.medicare = medicare;
    }

    double getWeeklyExpenses() {
        return this.weeklyExpenses;
    }

    void setWeeklyExpenses(double weeklyExpenses) {
        this.weeklyExpenses = weeklyExpenses;
    }

    Account[] getAccount() {
        return this.account;
    }

    void setAccount(Account[] account) {
        this.account = account;
    }

    public void addAccount(Account account) {
        List<Account> newAccountList = new ArrayList<Account>();
        if (this.account != null) {
            for (Account acc : this.account) {
                if (acc != null) {
                    newAccountList.add(acc);
                }
            }
        } else {
            this.account = new Account[3];
        }
        newAccountList.add(account);
        this.account = newAccountList.toArray(this.account);
    }

    public double getInvestmentAmount() {
        double total = 0.0;
        if (this.account != null) {
            for (int i = 0; i < this.account.length; i++) {
                if (this.account[i] != null) {
                    total += this.account[i].getAmount();
                }
            }
        }
        return total;
    }

    public void calcTax() {
        double taxableIncome = this.grossSalary;
        double tax = 0.0;
        if (this.resident) {
            if (taxableIncome <= 18200) {
                tax = 0.0;
            } else if (taxableIncome <= 45000) {
                tax = (taxableIncome - 18200) * 0.19;
            } else if (taxableIncome <= 120000) {
                tax = 5092 + (taxableIncome - 45000) * 0.325;
            } else if (taxableIncome <= 180000) {
                tax = 29467 + (taxableIncome - 120000) * 0.37;
            } else {
                tax = 51667 + (taxableIncome - 180000) * 0.45;
            }
        } else {
            if (taxableIncome <= 120000) {
                tax = taxableIncome * 0.325;
            } else if (taxableIncome <= 180000) {
                tax = 39000 + (taxableIncome - 120000) * 0.37;
            } else {
                tax = 61200 + (taxableIncome - 180000) * 0.45;
            }
        }
        this.tax = tax;
        this.netSalary = this.grossSalary - this.tax;
    }

    public void calcMedicare() {
        if (this.resident && this.grossSalary > 29032) {
            this.medicare = this.grossSalary * 0.02;
        } else {
            this.medicare = 0.0;
        }
    }

    public void deleteAccount(int index) {
        List<Account> newAccounts = new ArrayList<Account>();
        for (int i = 0; i < 3; i++) {
            if (i == index - 1) {
                continue;
            }
            newAccounts.add(this.account[i]);
        }

        this.account = newAccounts.toArray(this.account);
    }

    public int getAccountCount() {
        int count = 0;
        for (Account acc : account) {
            if(acc != null){
                count++;
            }
        }
        return count;
    }
}
