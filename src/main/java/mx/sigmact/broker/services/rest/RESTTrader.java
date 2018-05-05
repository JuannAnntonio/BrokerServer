package mx.sigmact.broker.services.rest;

import mx.sigmact.broker.core.helper.AggressionHelper;
import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.core.util.SBGeneralUtilities;
import mx.sigmact.broker.dao.MarketDao;
import mx.sigmact.broker.dao.TraderDao;
import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.model.StandingTypeEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.pojo.*;
import mx.sigmact.broker.pojo.trader.CancelledStatus;
import mx.sigmact.broker.pojo.trader.MarketPosition;
import mx.sigmact.broker.pojo.trader.TablePosition;
import mx.sigmact.broker.repositories.BrokerStandingRepository;
import mx.sigmact.broker.repositories.BrokerStandingTypeRepository;
import mx.sigmact.broker.repositories.BrokerUserRepository;
import mx.sigmact.broker.repositories.BrokerValmerPriceVectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.*;

/**
 * Created on 07/12/16.
 */

@RestController
@RequestMapping("trader/rest")
public class RESTTrader {

    @Value("${timeout}")
    Integer timeout;

    @Resource
    private DirtyPriceCalculator dpCalc;

    @Resource
    private TraderDao traderDao;

    @Resource
    private BrokerUserRepository userRepo;

    @Resource
    private BrokerStandingRepository standingRepo;

    @Resource
    private BrokerStandingTypeRepository stdTypRepo;

    @Resource
    private BrokerValmerPriceVectorRepository valVecRepo;

    @Resource
    private SimpMessagingTemplate simpOp;

    @Resource
    private SimpMessageSendingOperations messagingTemplate;

    @Resource
    private MarketDao mktDao;

    @Resource
    private AggressionHelper aggressionHelper;

    private static final String market_channel = "/market/announce";
    private static final String market_channel_users = "/market/announcements";

    private static Logger log = LoggerFactory.getLogger(RESTTrader.class);

    /* ********************* Trader inital set up ************************************/

    /**
     * This method get the trader instruments
     * @return A list with the trader instruments for display ine
     */
    @RequestMapping(value = "getTraderInstruments", method = RequestMethod.GET, produces = "application/json")
    public List<TraderInstrumentView> doGetTraderInstruments() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findOneByUsername(name);
        return traderDao.getTraderInstruments(user.getIdUser());
    }

    /**
     * Returns the current logged trader activity as specified by the {@code SecurityContextHolder} this is used
     * to get the trader activity at login. This method should be used with {@code doGetMarketPositions} to get the
     * initial state of the bidding table.
     * @return A {@code List} with the trader past activity.
     */
    @RequestMapping(value = "getTraderActivity", method = RequestMethod.GET, produces = "application/json")
    public List<TraderActivityView> doGetTraderActivity() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Calendar cal = Calendar.getInstance();
        UserEntity user = userRepo.findOneByUsername(name);
        return traderDao.getTraderActivity(user.getIdUser(), cal);
    }

    /**
     * This method returns the current market positions it is used to get the market positions when logging in.
     * It is preffered to load this information prior to displaying any controls. This method should be used with
     * {@code doGetTraderActivity} to get the initial state of the bidding table.
     * @return A {@code List} with the market positions.
     */
    @RequestMapping(value = "marketPositions", method = RequestMethod.GET, produces = "application/json")
    public List<TablePosition> doGetMarketPositions() {
        List<TablePosition> stdForTrader = new ArrayList<>();
        List<StandingEntity> stdInMarket = standingRepo.findByFkIdStandingStatus(StandingStatus.INMARKET);
        List<TraderInstrumentView> traderInstrumentViews = doGetTraderInstruments();
        Calendar day = CalendarUtil.zeroTimeCalendar(Calendar.getInstance());
        Iterable<StandingTypeEntity> stdTypesEnt = stdTypRepo.findAll();
        HashMap<Integer, String> stdTypes = new HashMap<>();
        for (StandingTypeEntity stdTyp : stdTypesEnt) {
            stdTypes.put(stdTyp.getIdStandingType(), stdTyp.getName());
        }
        for (StandingEntity std : stdInMarket) {
            for (TraderInstrumentView instrument : traderInstrumentViews) {
                if (day.compareTo(std.getDatetime()) > 0) {
                    if (instrument.getIdVPV() == std.getFkIdValmerPriceVector()) {
                        stdForTrader.add(new TablePosition(
                                stdTypes.get(std.getFkIdStandingType()),
                                std.getCurrentAmount()/1000,
                                std.getValue(),
                                std.getFkIdValmerPriceVector()
                        ));
                    }
                } else {
                    std.setFkIdStandingStatus(StandingStatus.CANCELLED);
                }
            }
        }
        return stdForTrader;
    }

    /**
     * For displaying trader tickets this method should be called each time you  want to refresh the tickets.
     * It is a poll service.
     * @return A{@code List} with the information for different tickets.
     */
    @RequestMapping(value = "getTraderTickets", method = RequestMethod.GET, produces = "application/json")
    public List<TraderTicketView> doGetTraderTickets() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Calendar cal = Calendar.getInstance();
        UserEntity user = userRepo.findOneByUsername(name);
        return traderDao.getTraderTickets(user.getIdUser(), cal);
    }

    /**
     * Retruns a graph for a give Instrument with the point availiable for the period specified in days.
     * @param days The amount of days of data to collect.
     * @param instrument The instrument you wish to get the information.
     * @return A {@code TraderGraph} whit thye information to plot the desired graph.
     */
    @RequestMapping(value = "getTraderGraph", method = RequestMethod.GET, produces = "application/json")
    public TraderGraph doGetTraderGraphByDays(@RequestParam("days") Integer days, @RequestParam("instrument") String instrument) {
        TraderGraph retVal = new TraderGraph();
        if (days > 1) {
            retVal.setList(traderDao.getGraphDataPoints(days, instrument));
        } else if (days == 1) {
            retVal.setList(traderDao.getGraphDataPointsCurrentDay(instrument));
        }
        return retVal;
    }

    /* ************************ End of trader initial setup ******************************************/

    /* ************************ Trader STOMP (synched) services **************************************/

    /**
     * Handles a client aggression
     * @param aggression
     * @param headerAccessor
     */
    //@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRES_NEW)
    @MessageMapping("/aggress")
    public void doAggress(MarketPosition aggression, SimpMessageHeaderAccessor headerAccessor) {
        Calendar cal = Calendar.getInstance();
        MarketPosition retVal = null;
        MarketPosition marketPosition = aggressionHelper.checkPositionInMarker(aggression.getInstrumentId(), aggression.getBiddingType());
        String sessionId = headerAccessor.getSessionId();
        if (aggression.getAmount() <= marketPosition.getAmount()) {
            UserEntity aggressor = userRepo.findOneByUsername(headerAccessor.getUser().getName());
            if (!aggressionHelper.doAggression(aggression, aggressor, sessionId, cal)) {
                messagingTemplate.convertAndSendToUser(aggressor.getUsername(), market_channel_users,
                        new MarketMessage(405,
                                "Error aggression",
                                "Error while aggressing",
                                null));
            }
        }
    }

    /**
     * This method recieves messages from the clients and sends a standing if a new standing is available in the market.
     * The channel is /BBBroker/position
     *
     * @param position The position to post to market.
     * @return
     */
    @MessageMapping("/position")
    @SendTo("/market/announce")
    public MarketMessage doPostPosition(MarketPosition position, SimpMessageHeaderAccessor headerAccessor) {
        Principal wsUser = headerAccessor.getUser();
        UserEntity user = userRepo.findOneByUsername(wsUser.getName());
        Calendar day = CalendarUtil.zeroTimeCalendar(Calendar.getInstance());
        String sessionId = headerAccessor.getSessionId();
        if(position.getAmount() == 0 || position.getRate() == 0.0){
            messagingTemplate.convertAndSendToUser(user.getUsername(), market_channel_users,
                    new ErrorStatus(405, "Error", "Invalid rate or amount.",
                            position),
                    Collections.singletonMap(SimpMessageHeaderAccessor.SESSION_ID_HEADER, sessionId));
            return null;
        }
        StandingEntity standing = new StandingEntity(
                stdTypRepo.findByName(position.getBiddingType()).get(0).getIdStandingType(),
                position.getInstrumentId(),
                user.getIdUser(),
                position.getAmount()*1000,
                position.getRate(),
                StandingStatus.MARKETPOST,
                Calendar.getInstance(),
                position.getAmount()*1000,
                0.0
        );
        try {
            standing.setStandingDirtyPrice(dpCalc.calcDirtyPrice(standing, day));
        } catch (Exception e) {
            String userChannel = "/market/announcements";
            messagingTemplate.convertAndSendToUser(user.getUsername(), userChannel,
                    new ErrorStatus(405, "Error", "The price could not be computed.",
                            SBGeneralUtilities.getPositionFromStanding(standing)),
                    Collections.singletonMap(SimpMessageHeaderAccessor.SESSION_ID_HEADER, sessionId));
            return null;
        }
        String newInMarket = null;
        List<StandingEntity> standings =//TODO change the new identification method for a more efficient version
                sortQueueNew(standing, day);
        if (isNewInMarket(standings, standing)) {
            newInMarket = "NEW";
        }
        MarketPosition marketPosition = SBGeneralUtilities.mergeStandings(standings, StandingStatus.INMARKET);
        messagingTemplate.convertAndSend(market_channel,
                new MarketMessage(303, "POSITION", newInMarket, marketPosition));
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            log.error("Thread waiting for post position interrupted");
        }
        standing = standingRepo.findOne(standing.getIdStanding());
        MarketMessage retVal;
        if (standing.getFkIdStandingStatus() != StandingStatus.CANCELLED) {
            retVal = new MarketMessage(311, "ENABLE AGGRESSION", newInMarket, marketPosition);
        } else {
            retVal = new MarketMessage(0, "NULL OPERATION", newInMarket, marketPosition);
        }
        return retVal;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @MessageMapping("/getMarketPositions")
    @SendToUser("/market/positions")
    public List<MarketPosition> doGetMarketList(SimpMessageHeaderAccessor headerAccessor) {
        String userName = headerAccessor.getUser().getName();
        return mktDao.getCurrentMarketPositionsForUser(userName);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @MessageMapping("/getUserMarketPositions")
    @SendToUser("/market/user_positions")
    public List<MarketPosition> doGetUserActivePositions(SimpMessageHeaderAccessor headerAccessor) {
        String userName = headerAccessor.getUser().getName();
        return mktDao.getCurrentActiveUserPositions(userName);
    }

    /**
     * Cancels a position posted by the trader logged in.
     * @param position The position you wish to cancel.
     * @param headerAccessor The credentials sent by your client in th STOMP message.
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @MessageMapping("/cancelPosition")
    @SendToUser("/market/canceled")
    public CancelledStatus doCancelPosition(MarketPosition position, SimpMessageHeaderAccessor headerAccessor) {
        Calendar day = CalendarUtil.zeroTimeCalendar(Calendar.getInstance());
        CancelledStatus status = new CancelledStatus();
        status.setBiddingType(position.getBiddingType());
        status.setInstrumentId(position.getInstrumentId());
        Integer valmerId = position.getInstrumentId();
        String userName = headerAccessor.getUser().getName();
        UserEntity user = userRepo.findOneByUsername(userName);
        int bid_type = StandingType.getBidTypeId(position.getBiddingType());
        StandingEntity userStanding = standingRepo.findOneByFkIdUserAndFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingStatusLessThanEqualAndFkIdStandingType
                (user.getIdUser(), valmerId, day, StandingStatus.INMARKET, bid_type);
        if (userStanding == null) {
            status.setCode(501);
            status.setMessage("La postura no ha podido ser encontrada error de sistema");
            status.setStatus("Error");
        } else if (userStanding.getFkIdStandingStatus() == StandingStatus.AGGRESSED) {
            status.setCode(502);
            status.setMessage("La postura se encuentra agredida y no ha podido ser cancelada");
            status.setStatus("Error");
        } else {
            int stdStatus = userStanding.getFkIdStandingStatus();
            userStanding.setFkIdStandingStatus(StandingStatus.CANCELLED);
            standingRepo.save(userStanding);
            if (stdStatus == StandingStatus.INMARKET) {
                MarketPosition inMarket = sortQueue(userStanding, CalendarUtil.zeroTimeCalendar(Calendar.getInstance()), false);
                if (inMarket != null) {
                    MarketMessage mm = new MarketMessage(
                            303,
                            "POSITION",
                            null,
                            inMarket
                    );
                    messagingTemplate.convertAndSend("/market/announce", mm);
                } else {

                    MarketPosition tablePosition = new MarketPosition();
                    tablePosition.setBiddingType(position.getBiddingType());
                    MarketMessage mm = new MarketMessage(
                            303,
                            "POSITION",
                            null,
                            tablePosition
                    );
                    messagingTemplate.convertAndSend("/market/announce", mm);

                }
                status.setCode(201);
                status.setMessage("La postura ha sido eliminada");
                status.setStatus("Exito");
            }
        }
        return status;
    }

    /**
     * This method recieves messages from the clients and sends a standing if a new standing is available in the market.
     * The channel is /BBBroker/position
     *
     * @return A {@code java.util.List} with {@link MarketPosition} objects.
     */
    @Transactional
    @MessageMapping("/positions")
    @SendToUser("/user/market/announce")
    public List<TablePosition> doPostGetPositions() {
        return doGetMarketPositions();
    }


    //TODO Test cancel logic
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @MessageMapping("/cancelAggression")
    @SendTo("/market/announce")
    public MarketMessage doCancelAggression(MarketPosition position, SimpMessageHeaderAccessor headerAccessor) {
        UserEntity aggressor = userRepo.findOneByUsername(headerAccessor.getUser().getName());
        MarketPosition marketPosition = aggressionHelper.cancelAggression(position, aggressor);

        return new MarketMessage(
                302,// An aggression was cancelled so unlock table
                "CANCELLED AGGRESSION",
                null,
                marketPosition
        );
    }

    /**
     * Notify all clients of a standing entity.
     * @param std
     */
    private void notifyClients(StandingEntity std) {
        String destination = "/marketPositions";
        MarketPosition mp = new MarketPosition(
                std.getFkIdValmerPriceVector(),
                std.getValue(),
                std.getAmount()/1000,
                stdTypRepo.findOne(std.getFkIdStandingType()).getName()
        );
        simpOp.convertAndSend(destination, mp);
    }

    //TODO add positions composition perhaps it can be set when sending the information to the clients
    @Transactional(isolation = Isolation.SERIALIZABLE)
    private synchronized List<StandingEntity> sortQueueNew(StandingEntity standing, Calendar day) {
        List<StandingEntity> dayStandings = null;
        if (standing.getFkIdStandingType() == StandingType.BIDID) {
            dayStandings = standingRepo.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual
                    (standing.getFkIdValmerPriceVector(), day, StandingType.BIDID, StandingStatus.INMARKET);
        } else if (standing.getFkIdStandingType() == StandingType.OFFERID) {
            dayStandings = standingRepo.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual
                    (standing.getFkIdValmerPriceVector(), day, StandingType.OFFERID, StandingStatus.INMARKET);
        }
        SBGeneralUtilities.sortQueueAndAdd(dayStandings, standing);
        standingRepo.save(dayStandings);
        return dayStandings;
    }

    private boolean isNewInMarket(List<StandingEntity> standings, StandingEntity standing) {
        boolean retVal = false;
        if (standings != null && standings.size() > 0) {
            if (standings.get(0).getValue() == standing.getValue() && standings.get(0).getFkIdUser() == standing.getFkIdUser()) {
                retVal = true;
            }
        }
        return retVal;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    private synchronized MarketPosition sortQueue(StandingEntity standing, Calendar day, boolean addToQueue) {
        List<StandingEntity> dayStandings = null;
        StandingEntity inMarketStanding = null;
        String bidType = null;
        if (standing.getFkIdStandingType() == StandingType.BIDID) {
            bidType = StandingType.BID;
            dayStandings = standingRepo.
                    findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual
                            (standing.getFkIdValmerPriceVector(), day, StandingType.BIDID, StandingStatus.INMARKET);//TODO check StandingStatus is valid or Rise to pending
        } else if (standing.getFkIdStandingType() == StandingType.OFFERID) {
            bidType = StandingType.OFFER;
            dayStandings = standingRepo.
                    findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual
                            (standing.getFkIdValmerPriceVector(), day, StandingType.OFFERID, StandingStatus.INMARKET);
        }
        if (addToQueue) {
            dayStandings.add(standing);
        }
        Collections.sort(dayStandings);
        MarketPosition result = new MarketPosition();
        result.setBiddingType(bidType);
        result.setInstrumentId(standing.getFkIdValmerPriceVector());
        if (dayStandings.size() > 0) {
            inMarketStanding = dayStandings.get(0);
            inMarketStanding.setFkIdStandingStatus(StandingStatus.INMARKET);
            standingRepo.save(inMarketStanding);
            result.setAmount(inMarketStanding.getAmount()/1000);
            result.setRate(inMarketStanding.getValue());
            result.setInstrumentId(inMarketStanding.getFkIdValmerPriceVector());
        }
        return result;
    }
}
