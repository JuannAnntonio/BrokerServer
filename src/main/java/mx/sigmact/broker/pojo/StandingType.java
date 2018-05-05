package mx.sigmact.broker.pojo;

/**
 * Created on 02/11/16.
 */
public class StandingType {
    public static final String BID = "Bid";
    public static final String OFFER = "Offer";
    public static final int BIDID = 1;
    public static final int OFFERID = 2;


    public static int getBidTypeId(String biddingType) {
        int retVal = -1;
        switch (biddingType){
            case StandingType.BID:
                retVal = StandingType.BIDID;
                break;
            case StandingType.OFFER:
                retVal = StandingType.OFFERID;
                break;
        }
        return retVal;
    }
}
