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
    private String operation;
    private String counterpart;
    private Double agressionRate;
    private Double commissionRate;
    private Double negotiatedRate;
    private String createDate;
    private String liquidationDate;
    private Long dxv;

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
    /**
     * Nueva vista para Trader
     * @fecha
     * @instrumento
     * @operacion
     * @contraparte
     * @titulos
     * @tasaNegociada
     * @tasaComision
     * @tasaAgredida
     * @precio
     * @monto
     * */
    public TraderTicketView(String fecha,
                            String instrumento,
                            String operacion,
                            String contraparte,
                            Long titulos,
                            Double tasaNegociada,
                            Double tasaComision,
                            Double tasaAgredida,
                            Double precio,
                            Double montoLiquidar,
                            Long montoNominal,
                            String fechaCreacion,
                            String fechaLiquidacion,
                            Long dxv) {
        this.dateTime = fecha;
        this.instrument = instrumento;
        this.operation = operacion;
        this.counterpart = contraparte;
        this.titles = titulos;
        this.negotiatedRate = tasaNegociada;
        this.commissionRate = tasaComision;
        this.agressionRate = tasaAgredida;
        this.price = precio;
        this.realAmount = montoLiquidar;
        this.amount = montoNominal;
        this.createDate = fechaCreacion;
        this.liquidationDate = fechaLiquidacion;
        this.dxv = dxv;
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCounterpart() {
        return counterpart;
    }

    public void setCounterpart(String counterpart) {
        this.counterpart = counterpart;
    }

    public Double getAgressionRate() {
        return agressionRate;
    }

    public void setAgressionRate(Double agressionRate) {
        this.agressionRate = agressionRate;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Double getNegotiatedRate() {
        return negotiatedRate;
    }

    public void setNegotiatedRate(Double negotiatedRate) {
        this.negotiatedRate = negotiatedRate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLiquidationDate() {
        return liquidationDate;
    }

    public void setLiquidationDate(String liquidationDate) {
        this.liquidationDate = liquidationDate;
    }

    public Long getDxv() {
        return dxv;
    }

    public void setDxv(Long dxv) {
        this.dxv = dxv;
    }
}
