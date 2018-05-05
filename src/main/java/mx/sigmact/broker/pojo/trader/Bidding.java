package mx.sigmact.broker.pojo.trader;

/**
 * Created on 02/01/2017.
 */
public abstract class Bidding {
    protected int instrumentId;
    protected String biddingType;

    public Bidding() {
    }

    public Bidding(int instrumentId, String biddingType) {
        this.instrumentId = instrumentId;
        this.biddingType = biddingType;
    }

    public int getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(int instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getBiddingType() {
        return biddingType;
    }

    public void setBiddingType(String biddingType) {
        this.biddingType = biddingType;
    }
}
