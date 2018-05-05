package mx.sigmact.broker.pojo.backoffice;

/**
 * Created on 08/12/16.
 */
public class BackOfficeMainView {
    private String date;
    private String transactionType;
    private String instrument;
    private String buyer;
    private String seller;
    private String workbench;
    private String wbBuyPrice;
    private String wbSellPrice;
    private String amount;
    private String wbAmount;
    private String titles;
    private String rate;
    private String surcharge;
    private String systemCommission;

    public BackOfficeMainView(String date, String transactionType, String instrument, String buyer,
                              String seller, String workbench, String wbBuyPrice, String wbSellPrice,
                              String amount, String wbAmount, String titles, String rate, String surcharge,
                              String systemCommission) {
        this.date = date;
        this.transactionType = transactionType;
        this.instrument = instrument;
        this.buyer = buyer;
        this.seller = seller;
        this.workbench = workbench;
        this.wbBuyPrice = wbBuyPrice;
        this.wbSellPrice = wbSellPrice;
        this.amount = amount;
        this.wbAmount = wbAmount;
        this.titles = titles;
        this.rate = rate;
        this.surcharge = surcharge;
        this.systemCommission = systemCommission;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
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

    public String getWorkbench() {
        return workbench;
    }

    public void setWorkbench(String workbench) {
        this.workbench = workbench;
    }

    public String getWbBuyPrice() {
        return wbBuyPrice;
    }

    public void setWbBuyPrice(String wbBuyPrice) {
        this.wbBuyPrice = wbBuyPrice;
    }

    public String getWbSellPrice() {
        return wbSellPrice;
    }

    public void setWbSellPrice(String wbSellPrice) {
        this.wbSellPrice = wbSellPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWbAmount() {
        return wbAmount;
    }

    public void setWbAmount(String wbAmount) {
        this.wbAmount = wbAmount;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(String surcharge) {
        this.surcharge = surcharge;
    }

    public String getSystemCommission() {
        return systemCommission;
    }

    public void setSystemCommission(String systemCOmmission) {
        this.systemCommission = systemCOmmission;
    }
}
