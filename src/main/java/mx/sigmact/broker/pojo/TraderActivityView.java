package mx.sigmact.broker.pojo;

/**
 * Created on 08/12/16.
 */
public class TraderActivityView {
    private String instrument;
    private String standing;
    private String rate;
    private Double profitAndLoss;

    public TraderActivityView(String instrument, String standing, String rate, Double profitAndLoss) {
        this.instrument = instrument;
        this.standing = standing;
        this.rate = rate;
        this.profitAndLoss = profitAndLoss;
    }

    public TraderActivityView() {
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getStanding() {
        return standing;
    }

    public void setStanding(String standing) {
        this.standing = standing;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Double getProfitAndLoss() {
        return profitAndLoss;
    }

    public void setProfitAndLoss(Double profitAndLoss) {
        this.profitAndLoss = profitAndLoss;
    }
}
