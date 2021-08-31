package mx.sigmact.broker.pojo.trader;

public class BlockDetail {
    private double rate;
    private int amount;
    private double surcharge;

    public BlockDetail() {
    }

    public BlockDetail(double rate, int amount, double surcharge) {
        this.rate = rate;
        this.amount = amount;
        this.surcharge = surcharge;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(double surcharge) {
        this.surcharge = surcharge;
    }
}
