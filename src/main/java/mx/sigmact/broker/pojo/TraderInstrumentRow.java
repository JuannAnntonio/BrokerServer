package mx.sigmact.broker.pojo;

/**
 * Created on 07/12/16.
 */
public class TraderInstrumentRow {
    private Integer dxv;
    private String name;
    private String marketButtonAcceptBid;
    private String markertBidAmount;
    private String marketBid;
    private String marketSeparator;
    private String marketOffer;
    private String marketOfferAmount;
    private String marketButtonAcceptOffer;
    private String userButtonPostBid;
    private String userBidAmount;
    private String userBid;
    private String userSeparator;
    private String userOffer;
    private String userOfferAmount;
    private String userButtonPostOffer;
    private String cancelButton;

    public TraderInstrumentRow() {
        super();
    }

    public TraderInstrumentRow(Integer dxv, String name, String marketButtonAcceptBid, String markertBidAmount,
                               String marketBid, String marketSeparator, String marketOffer, String marketOfferAmount,
                               String marketButtonAcceptOffer, String userButtonPostBid, String userBidAmount,
                               String userBid, String userSeparator, String userOffer, String userOfferAmount,
                               String userButtonPostOffer, String cancelButton) {
        this.dxv = dxv;
        this.name = name;
        this.marketButtonAcceptBid = marketButtonAcceptBid;
        this.markertBidAmount = markertBidAmount;
        this.marketBid = marketBid;
        this.marketSeparator = marketSeparator;
        this.marketOffer = marketOffer;
        this.marketOfferAmount = marketOfferAmount;
        this.marketButtonAcceptOffer = marketButtonAcceptOffer;
        this.userButtonPostBid = userButtonPostBid;
        this.userBidAmount = userBidAmount;
        this.userBid = userBid;
        this.userSeparator = userSeparator;
        this.userOffer = userOffer;
        this.userOfferAmount = userOfferAmount;
        this.userButtonPostOffer = userButtonPostOffer;
        this.cancelButton = cancelButton;
    }

    public Integer getDxv() {
        return dxv;
    }

    public void setDxv(Integer dxv) {
        this.dxv = dxv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarketButtonAcceptBid() {
        return marketButtonAcceptBid;
    }

    public void setMarketButtonAcceptBid(String marketButtonAcceptBid) {
        this.marketButtonAcceptBid = marketButtonAcceptBid;
    }

    public String getMarkertBidAmount() {
        return markertBidAmount;
    }

    public void setMarkertBidAmount(String markertBidAmount) {
        this.markertBidAmount = markertBidAmount;
    }

    public String getMarketBid() {
        return marketBid;
    }

    public void setMarketBid(String marketBid) {
        this.marketBid = marketBid;
    }

    public String getMarketSeparator() {
        return marketSeparator;
    }

    public void setMarketSeparator(String marketSeparator) {
        this.marketSeparator = marketSeparator;
    }

    public String getMarketOffer() {
        return marketOffer;
    }

    public void setMarketOffer(String marketOffer) {
        this.marketOffer = marketOffer;
    }

    public String getMarketOfferAmount() {
        return marketOfferAmount;
    }

    public void setMarketOfferAmount(String marketOfferAmount) {
        this.marketOfferAmount = marketOfferAmount;
    }

    public String getMarketButtonAcceptOffer() {
        return marketButtonAcceptOffer;
    }

    public void setMarketButtonAcceptOffer(String marketButtonAcceptOffer) {
        this.marketButtonAcceptOffer = marketButtonAcceptOffer;
    }

    public String getUserButtonPostBid() {
        return userButtonPostBid;
    }

    public void setUserButtonPostBid(String userButtonPostBid) {
        this.userButtonPostBid = userButtonPostBid;
    }

    public String getUserBidAmount() {
        return userBidAmount;
    }

    public void setUserBidAmount(String userBidAmount) {
        this.userBidAmount = userBidAmount;
    }

    public String getUserBid() {
        return userBid;
    }

    public void setUserBid(String userBid) {
        this.userBid = userBid;
    }

    public String getUserSeparator() {
        return userSeparator;
    }

    public void setUserSeparator(String userSeparator) {
        this.userSeparator = userSeparator;
    }

    public String getUserOffer() {
        return userOffer;
    }

    public void setUserOffer(String userOffer) {
        this.userOffer = userOffer;
    }

    public String getUserOfferAmount() {
        return userOfferAmount;
    }

    public void setUserOfferAmount(String userOfferAmount) {
        this.userOfferAmount = userOfferAmount;
    }

    public String getUserButtonPostOffer() {
        return userButtonPostOffer;
    }

    public void setUserButtonPostOffer(String userButtonPostOffer) {
        this.userButtonPostOffer = userButtonPostOffer;
    }

    public String getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(String cancelButton) {
        this.cancelButton = cancelButton;
    }
}
