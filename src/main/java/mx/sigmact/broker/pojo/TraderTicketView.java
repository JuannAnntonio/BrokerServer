package mx.sigmact.broker.pojo;

/**
 * Created on 08/12/16.
 */
public class TraderTicketView {
    private String dateTime;
    private String instrument;
    private String buyer;
    private String seller;
    private Double rate;
    private Long amount;
    private Double realAmount;
    private Long titles;
    private Double price;

    public TraderTicketView() {
    }

    public TraderTicketView(String dateTime, String instrument, String buyer, String seller, Double rate, Long amount, Double price) {
        this.dateTime = dateTime;
        this.instrument = instrument;
        this.buyer = buyer;
        this.seller = seller;
        this.rate = rate;
        this.amount = amount;
        this.price = price;
    }

    public TraderTicketView(String dateTime, String instrument, String buyer, String seller, Double rate, Long amount, Double realAmount, Long titles, Double price) {
        this.dateTime = dateTime;
        this.instrument = instrument;
        this.buyer = buyer;
        this.seller = seller;
        this.rate = rate;
        this.amount = amount;
        this.realAmount = realAmount;
        this.titles = titles;
        this.price = price;
    }

    public Double getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(Double realAmount) {
        this.realAmount = realAmount;
    }

    public Long getTitles() {
        return titles;
    }

    public void setTitles(Long titles) {
        this.titles = titles;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
