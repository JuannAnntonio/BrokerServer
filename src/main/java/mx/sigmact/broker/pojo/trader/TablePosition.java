package mx.sigmact.broker.pojo.trader;

/**
 * A class adapter for displaying positions
 * Created on 23/12/2016.
 */
public class TablePosition {
    private String biddingType;
    private int amount;
    private double rate;
    private int idVPV;

    public String getBiddingType() {
        return biddingType;
    }

    public void setBiddingType(String biddingType) {
        this.biddingType = biddingType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getIdVPV() {
        return idVPV;
    }

    public void setIdVPV(int idVPV) {
        this.idVPV = idVPV;
    }

    public TablePosition() {
    }

    public TablePosition(String biddingType, int amount, double rate, int idVPV) {
        this.biddingType = biddingType;
        this.amount = amount;
        this.rate = rate;
        this.idVPV = idVPV;
    }
}
