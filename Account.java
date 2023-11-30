public class Account {
    private double rate;
    private int numberOfWeeks;
    private double amount;

    Account() {
        this.rate = 0D;
        this.numberOfWeeks = 0;
        this.amount = 0D;
    }

    Account(double rate, int numberOfWeeks, double amount) {
        this.rate = rate;
        this.numberOfWeeks = numberOfWeeks;
        this.amount = amount;
    }

    double getRate() {
        return this.rate;
    }

    void setRate(double rate) {
        this.rate = rate;
    }

    int getNumberOfWeeks() {
        return this.numberOfWeeks;
    }

    void setNumberOfWeeks(int numberOfWeeks) {
        this.numberOfWeeks = numberOfWeeks;
    }

    double getAmount() {
        return this.amount;
    }

    void setAmount(double amount) {
        this.amount = amount;
    }

    String calcInvestment() {
        for (int i = 0; i < this.numberOfWeeks; i++) {
            this.amount *= (1 + this.rate / 52);
        }
        return String.format("%.2f", this.amount);
    }
}
