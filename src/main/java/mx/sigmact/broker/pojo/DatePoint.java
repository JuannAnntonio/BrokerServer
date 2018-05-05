package mx.sigmact.broker.pojo;

/**
 * Created on 09/12/16.
 */
public class DatePoint {
    private String date;
    private Double yield;

    public DatePoint() {
    }

    public DatePoint(String date, Double yield) {
        this.date = date;
        this.yield = yield;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getYield() {
        return yield;
    }

    public void setYield(Double yield) {
        this.yield = yield;
    }
}
