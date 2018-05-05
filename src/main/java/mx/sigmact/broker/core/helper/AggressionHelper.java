package mx.sigmact.broker.core.helper;

import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.core.util.SBGeneralUtilities;
import mx.sigmact.broker.dao.TraderDao;
import mx.sigmact.broker.model.AggressionEntity;
import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.pojo.ExecutionStatus;
import mx.sigmact.broker.pojo.MarketMessage;
import mx.sigmact.broker.pojo.StandingStatus;
import mx.sigmact.broker.pojo.StandingType;
import mx.sigmact.broker.pojo.trader.AggresionStatus;
import mx.sigmact.broker.pojo.trader.MarketPosition;
import mx.sigmact.broker.repositories.BrokerAggressionRepository;
import mx.sigmact.broker.repositories.BrokerInstitutionWorkbenchPriorityRepository;
import mx.sigmact.broker.repositories.BrokerStandingRepository;
import mx.sigmact.broker.repositories.BrokerUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by norberto on 23/01/17.
 */
public class AggressionHelper {

    @Resource
    private BrokerStandingRepository stdRepo;

    @Resource
    private BrokerAggressionRepository aggRepo;

    @Resource
    private BrokerUserRepository userRepo;

    @Resource
    private BrokerInstitutionWorkbenchPriorityRepository iwpRepo;

    @Resource
    private TraderDao traderDao;

    @Resource
    private DirtyPriceCalculator dpCalc;

    @Resource
    private SimpMessageSendingOperations messagingTemplate;

    @Value("${waiting_seconds_after_aggression}")
    Integer aggressionDelay;

    private static final String market_channel = "/market/announce";
    private static final String market_channel_users = "/market/announcements";

    Logger log = LoggerFactory.getLogger(AggressionHelper.class);

    public AggressionHelper() {
    }

    /**
     * Cancel aggression, sends all aggression the user has on this particular instrument in aggression. The standings
     * are sent to the queue status and then the queue is redone. Persisting two times the standings.
     *
     * @param aggression A MarketPosition with the information of he aggression to cancel.
     * @param aggressor  The UserEntity who wants to cancel the aggression.
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public MarketPosition cancelAggression(MarketPosition aggression, UserEntity aggressor) {
        List<AggressionEntity> userAggressions =
                aggRepo.findByFkIdUserAndFkIdTransactionStatusOrFkIdTransactionStatus(
                        aggressor.getIdUser(),
                        AggresionStatus.ACTIVE,
                        AggresionStatus.COUNTEROFFER);//Get the aggressions for a certain user
        List<AggressionEntity> aggressionEntityList = new ArrayList<>();
        List<StandingEntity> standingEntityList = new ArrayList<>();
        for (AggressionEntity agg : userAggressions) {
            StandingEntity std = stdRepo.findOne(agg.getFkIdStanding());
            if (std.getFkIdValmerPriceVector() == aggression.getInstrumentId()) {
                agg.setFkIdTransactionStatus(AggresionStatus.CANCELLED);
                std.setFkIdStandingStatus(StandingStatus.QUEUED);
                aggressionEntityList.add(agg);
                standingEntityList.add(std);
            }
        }
        aggRepo.save(aggressionEntityList);
        stdRepo.save(standingEntityList);
        MarketPosition marketPosition = redoQueue(
                aggression.getInstrumentId(),
                SBGeneralUtilities.getStandingTypeIdFromName(aggression.getBiddingType()));
        return marketPosition;

    }

    //@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public boolean doAggression(MarketPosition aggression, UserEntity aggressor,
                                String sessionId, Calendar aggressionTime) {
        Integer aggressorInstitution = aggressor.getFkIdInstitution();
        List<StandingEntity> standings = stdRepo.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatus(
                aggression.getInstrumentId(),
                CalendarUtil.zeroTimeCalendar(Calendar.getInstance()),
                SBGeneralUtilities.getStandingTypeIdFromName(aggression.getBiddingType()),
                StandingStatus.INMARKET
        );
        boolean error = false;
        Map<Integer, List<String>> workbenchesForStandings =
                getWorkbenchesForStandings(aggressorInstitution, standings);
        int amountToFulfill = aggression.getAmount()*1000;
        Iterator it = workbenchesForStandings.entrySet().iterator();
        while (it.hasNext()) {//TODO all the transactions have a common workbench
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue() == null) {
                error = true;
            }
        }
        if (!error) {
            for (StandingEntity std : standings) {
                List<String> wb = workbenchesForStandings.get(std.getIdStanding());
                int i = 0;
                if (wb != null) {
                    //TODO correct create aggression it has extra parameters also check run condintions in for if so send all to create aggression
                    int amount = createAggression(aggressor, amountToFulfill, std, wb, i, sessionId, aggressionTime);
                    if (amount >= 0) {
                        amountToFulfill -= amount;
                    } else if (amount == -1) {
                        return false;
                    }
                    if (amountToFulfill == 0) {
                        break;
                    }
                }
            }
            try {
                messagingTemplate.convertAndSend(market_channel,
                        new MarketMessage(
                                301, // code 301 Lock table aggression
                                "LOCK",
                                String.valueOf(aggressionDelay),
                                aggression
                        ));
                messagingTemplate.convertAndSendToUser(aggressor.getUsername(), market_channel_users,
                        new MarketMessage(
                                310,
                                "Enable cancel",
                                String.valueOf(aggressionDelay),
                                aggression
                        ),
                        Collections.singletonMap(SimpMessageHeaderAccessor.SESSION_ID_HEADER, sessionId));
                Thread.sleep(Math.round(aggressionDelay * 1000));
            } catch (InterruptedException e) {
                log.error("Error en el delay, mensaje: " + e.getMessage());
            }
            if (this.isCancelled(aggression, aggressor)) {
                MarketPosition marketPosition = this.completeAggression(aggression, aggressor, aggressionTime);
                if (marketPosition != null) {
                    messagingTemplate.convertAndSend(market_channel,
                            new MarketMessage(
                                    302,  // unlock table with new position if posible
                                    "NSAA",
                                    "New position after aggression",
                                    marketPosition
                            ));
                }
            }
        } else {
            messagingTemplate.convertAndSendToUser(aggressor.getUsername(), "/market/announcements",
                    new MarketMessage(
                            400,  // updateTable
                            "No wb availiable",
                            "No cuentas con bancos de trabajo para hacer esta agresión",
                            null
                    ));
        }
        return true;
    }

    /**
     * This method checks if an aggression has been canceled. Use within a transaction because this method
     * checks if the AGGRESSION table.
     *
     * @param aggression The MarketPosition representing this aggression.
     * @param aggressor  The UserEntity of the aggressor.
     * @return Either true if this aggression is canceled or false if this aggression is not cancelled.
     */
    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    private boolean isCancelled(MarketPosition aggression, UserEntity aggressor) {
        boolean retVal = false;
        List<AggressionEntity> userAggressions = aggRepo.findByFkIdUserAndFkIdTransactionStatusAndDatetimeGreaterThanEqual(
                aggressor.getIdUser(), AggresionStatus.ACTIVE, CalendarUtil.today());

        for (AggressionEntity agg : userAggressions) {
            StandingEntity std = stdRepo.findOne(agg.getFkIdStanding());//TODO refactor AggressionEntity so it stores the id of the valmer instrument.
            if (std.getFkIdValmerPriceVector() == aggression.getInstrumentId()) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public MarketPosition completeAggression(MarketPosition aggression, UserEntity aggressor, Calendar aggressionTime) {
        int standingType = -1;
        List<AggressionEntity> aggressions = aggRepo.findByFkIdUserAndFkIdTransactionStatus(
                aggressor.getIdUser(), AggresionStatus.ACTIVE);
        for (AggressionEntity agg : aggressions) {
            long timeDifference = Math.abs(aggressionTime.getTimeInMillis() - agg.getDatetime().getTimeInMillis());
            if (timeDifference <= 1000) {
                StandingEntity standing = stdRepo.findOne(agg.getFkIdStanding());
                if (!notInTransaction(standing, aggression.getInstrumentId())) {//TODO Sean del mismo bidding
                    if (standingType < 0) {
                        standingType = standing.getFkIdStandingType();
                    }
                    completeAggressionAndStandings(agg, standing);
                }
            } else {
                log.error("AggressionTime:" + aggressionTime.getTimeInMillis());
                log.error("DB aggresion time:" + agg.getDatetime().getTimeInMillis());
            }
        }
        if (standingType != -1) {
            return redoQueue(aggression.getInstrumentId(), standingType);
        } else {
            return null;
        }

    }

    /**
     * This redoes the queue persisting the new queue in the database the transaction should be isolated so no other process adds a position
     * or cancels it while doing the queue.
     *
     * @param instrumentId The instrument id to redoe the queue on
     * @param standingType The type of queue either 1 or 2 for BID or OFFER.
     * @return A mArketPosition to post to market.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)//TODO check if isolation level can be set to
    public MarketPosition redoQueue(int instrumentId, int standingType) {
        List<StandingEntity> queue = stdRepo.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual(
                instrumentId, CalendarUtil.today(), standingType, StandingStatus.INMARKET);
        Collections.sort(queue);
        double rate = 0.0;
        if (queue.size() > 0) {
            rate = queue.get(0).getValue();
        }
        List<StandingEntity> inMarket = new ArrayList<>();
        for (int i = 0; i < queue.size(); i++) {
            StandingEntity std = queue.get(i);
            if (std.getValue() == rate) {
                std.setFkIdStandingStatus(StandingStatus.INMARKET);
                inMarket.add(std);
            } else {
                std.setFkIdStandingStatus(StandingStatus.QUEUED);
                inMarket.add(std);
            }
        }
        stdRepo.save(inMarket);
        MarketPosition marketPosition = SBGeneralUtilities.mergeStandings(inMarket);
        if (marketPosition == null) {
            marketPosition = new MarketPosition();
            marketPosition.setBiddingType(SBGeneralUtilities.getStandingTypeNameFromId(standingType));
            marketPosition.setInstrumentId(instrumentId);
        }
        return marketPosition;
    }

    private void completeAggressionAndStandings(AggressionEntity aggression, StandingEntity std) {
        int stdCurrentAmount = std.getCurrentAmount();
        std.setFkIdStandingStatus(StandingStatus.COMPLETED);
        aggression.setFkIdTransactionStatus(AggresionStatus.COMPLETED);
        if (aggression.getAmount() < stdCurrentAmount) {
            StandingEntity newStd = new StandingEntity(std.getFkIdStandingType(),
                    std.getFkIdValmerPriceVector(),
                    std.getFkIdUser(),
                    stdCurrentAmount - aggression.getAmount(),
                    std.getValue(),
                    StandingStatus.QUEUED,
                    std.getDatetime(),
                    stdCurrentAmount - aggression.getAmount(),
                    std.getStandingDirtyPrice());
            std.setCurrentAmount(aggression.getAmount());
            std.setCurrentAmount(aggression.getAmount());
            stdRepo.save(newStd);
            stdRepo.save(std);
            UserEntity user = userRepo.findOne(newStd.getFkIdUser());
            messagingTemplate.convertAndSendToUser(user.getUsername(), market_channel_users,
                    new MarketMessage(
                            210, "New position to bidder.", null,
                            new MarketPosition(newStd.getFkIdValmerPriceVector(),
                                    newStd.getValue(),
                                    newStd.getAmount()/1000,
                                    SBGeneralUtilities.getStandingTypeNameFromId(newStd.getFkIdStandingType()))
                    ));
            //TODO notify the bidder his/her position have changed
        } else {
            stdRepo.save(std);
        }
        aggRepo.save(aggression);
    }

    private boolean notInTransaction(StandingEntity std, int instrumentId) {
        if (std.getFkIdValmerPriceVector() == instrumentId) {
            return false;
        }
        return true;
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public int createAggression(UserEntity aggressor, int amountToFulfill, StandingEntity std,
                                List<String> wb, int option, String sessionId, Calendar aggressionTime) {
        int standingAmount = std.getCurrentAmount();
        AggressionEntity aggressionEntity = null;
        StandingEntity tmp = stdRepo.findOne(std.getIdStanding());
        if (tmp.getFkIdStandingStatus() != std.getFkIdStandingStatus()) {
            return -1;
        }
        int retVal = -1;
        Double dirtyPrice = dpCalc.calcDirtyPrice(
                std,
                CalendarUtil.today(),
                Double.parseDouble(wb.get(1)) + std.getValue()
        );

        int aggressionAmount = 0;
        if (standingAmount >= amountToFulfill) {
            aggressionAmount = amountToFulfill;
        } else {
            aggressionAmount = standingAmount;
        }
        aggressionEntity =
                new AggressionEntity(
                        std.getIdStanding(),
                        AggresionStatus.ACTIVE,
                        aggressor.getIdUser(),
                        Integer.parseInt(wb.get(0)),
                        aggressionTime,
                        aggressionAmount,
                        dirtyPrice
                );
        log.info("aggTime:" + aggressionTime.getTimeInMillis());
        log.info("aggTime aggression: " + aggressionEntity.getDatetime().getTimeInMillis());
        retVal = aggressionAmount;
        std.setFkIdStandingStatus(StandingStatus.AGGRESSED);

        if (aggressionEntity != null) {
            aggRepo.save(aggressionEntity);
        }
        log.info("aggTime:" + aggressionTime.getTimeInMillis());
        log.info("aggTime aggression: " + aggressionEntity.getDatetime().getTimeInMillis());
        return retVal;
    }

    private void offerOption(UserEntity aggressor, StandingEntity std, UserEntity user, String sessionId) {
        String userChannel = "/market/announcements";
        messagingTemplate.convertAndSendToUser(user.getUsername(), userChannel,
                new ExecutionStatus(300, "Offer", aggressor.getUsername()));

    }

    /**
     * This method recieves a institution from the aggressor and a list of standigns to collect all the
     * workbenches for each standing.
     *
     * @param instA
     * @param standings
     * @return
     */
    private Map<Integer, List<String>> getWorkbenchesForStandings(Integer instA, List<StandingEntity> standings) {
        HashMap<Integer, List<String>> map = new HashMap<>();
        for (StandingEntity std : standings) {
            UserEntity user = userRepo.findOne(std.getFkIdUser());
            int stdInst = user.getFkIdInstitution();
            int idInst = std.getFkIdValmerPriceVector();
            int stdTyp = std.getFkIdStandingType();
            map.put(std.getIdStanding(), getCommonWorkBench(instA, stdInst, stdTyp));
        }
        return map;
    }

    private List<String> getCommonWorkBench(int aggressor, int bidder, int idInstrument) {
        return traderDao.getCommonWorkbench(aggressor, bidder, idInstrument);
    }

    public MarketPosition checkPositionInMarker(int instrumentId, String biddingType) {
        List<StandingEntity> standings = checkStandingInMarket(instrumentId, biddingType);
        return SBGeneralUtilities.mergeStandings(standings, biddingType);
    }

    private List<StandingEntity> checkStandingInMarket(int instrumentId, int standingType) {
        List<StandingEntity> standings = stdRepo.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatus(
                instrumentId, CalendarUtil.today(), standingType, StandingStatus.INMARKET);
        return standings;
    }

    private List<StandingEntity> checkStandingInMarket(int instrumentId, String standingType) {
        int stdTyp = -1;
        if (standingType.equals(StandingType.BID)) {
            stdTyp = StandingType.BIDID;
        } else if (standingType.equals(StandingType.OFFER)) {
            stdTyp = StandingType.OFFERID;
        }
        return checkStandingInMarket(instrumentId, stdTyp);
    }
}
