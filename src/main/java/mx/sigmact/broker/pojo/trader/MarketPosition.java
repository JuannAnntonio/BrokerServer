package mx.sigmact.broker.pojo.trader;

/**
 * Class for showing positions in the bidding table for traders. This implementation does not tie the
 * instrument to the position so it can be used in a duple (idVPV, MarketPosition) This also tells if the
 * position is from the trader currently requesting the position this might change in the future so it is not
 * recommended to rely on this field.
 * Created on 19/12/16.
 */
public class MarketPosition extends Bidding{
    private double rate;
    private int amount;
    private boolean isOwnOffer;

    public MarketPosition() {
    }

    public MarketPosition(int instrumentId, double rate, int amount, String biddingType) {
        this.instrumentId = instrumentId;
        this.rate = rate;
        this.amount = amount;
        this.biddingType = biddingType;
    }

    public MarketPosition(int instrumentId, double rate, int amount, String biddingType, boolean isOwnOffer) {
        this.instrumentId = instrumentId;
        this.rate = rate;
        this.amount = amount;
        this.biddingType = biddingType;
        this.isOwnOffer = isOwnOffer;
    }

    public int getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(int instrumentId) {
        this.instrumentId = instrumentId;
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

    public String getBiddingType() {
        return biddingType;
    }

    public void setBiddingType(String biddingType) {
        this.biddingType = biddingType;
    }

    public boolean isOwnOffer() {
        return isOwnOffer;
    }

    public void setOwnOffer(boolean ownOffer) {
        isOwnOffer = ownOffer;
    }
}