package mx.sigmact.broker.pojo;

/**
 * Created on 02/11/16.
 */
public class StandingStatus {

    public static final int QUEUED = 1;
    public static final int MARKETPOST = 2;
    public static final int INMARKET = 3;
    public static final int AGGRESSED = 4; //A lock is created so no user besides bidder and aggressor can modify the value
                                                      // the bidder can offer extra amount and aggressor can accept
    public static final int PENDING = 5; // If new offer is done while the transaction completes
    public static final int CANCELLED = 6;
    public static final int COMPLETED = 7;


}
