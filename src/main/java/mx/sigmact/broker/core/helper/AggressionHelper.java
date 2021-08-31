package mx.sigmact.broker.core.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.core.util.SBGeneralUtilities;
import mx.sigmact.broker.dao.AggressionDao;
import mx.sigmact.broker.dao.InstrumentDao;
import mx.sigmact.broker.dao.ParameterDao;
import mx.sigmact.broker.dao.StandingDAO;
import mx.sigmact.broker.dao.TraderDao;
import mx.sigmact.broker.dao.UtilDao;
import mx.sigmact.broker.model.AggressionEntity;
import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.model.ValmerPriceVectorEntity;
import mx.sigmact.broker.pojo.ExecutionStatus;
import mx.sigmact.broker.pojo.MarketMessage;
import mx.sigmact.broker.pojo.StandingStatus;
import mx.sigmact.broker.pojo.StandingType;
import mx.sigmact.broker.pojo.aggression.Aggression;
import mx.sigmact.broker.pojo.instrument.Instrument;
import mx.sigmact.broker.pojo.trader.AggresionStatus;
import mx.sigmact.broker.pojo.trader.MarketPosition;
import mx.sigmact.broker.repositories.BrokerAggressionRepository;
import mx.sigmact.broker.repositories.BrokerInstitutionWorkbenchPriorityRepository;
import mx.sigmact.broker.repositories.BrokerStandingRepository;
import mx.sigmact.broker.repositories.BrokerUserRepository;
import mx.sigmact.broker.repositories.BrokerValmerPriceVectorRepository;

/**
 * Created by norberto on 23/01/17.
 */
public class AggressionHelper {
	Logger log = Logger.getLogger(AggressionHelper.class);

	@Resource
	private ParameterDao parameterDao;
    @Resource
    private BrokerValmerPriceVectorRepository dao;
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
    @Resource
    private UtilDao utilDao;
    @Resource
    private AggressionDao aggressionDao;
    @Resource(name = "standingDao")
    private StandingDAO standingDAO;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Value("${waiting_seconds_after_aggression}")
    Integer aggressionDelay;
    @Value("${calc_base}")
    Integer calc_base;
    
    private Instrument instrument = null;    
    @Resource
    private InstrumentDao instrumentDao;


    private static final String market_channel = "/market/announce";
    private static final String market_channel_users = "/market/announcements";
    
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
    //@Transactional(isolation = Isolation.SERIALIZABLE)
    public MarketPosition cancelAggression(MarketPosition aggression, UserEntity aggressor) {

		log.info("[AggressionHelper][cancelAggression]");
		
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

    //@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.NOT_SUPPORTED)
    public boolean doAggression(MarketPosition aggression, UserEntity aggressor,
                                String sessionId, Calendar aggressionTime) {

		log.info("[AggressionHelper][doAggression]");
    	
        Integer aggressorInstitution = aggressor.getFkIdInstitution();
        List<StandingEntity> standings = stdRepo.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatus(
                aggression.getInstrumentId(),
                utilDao.today(),
                //CalendarUtil.zeroTimeCalendar(Calendar.getInstance()),
                SBGeneralUtilities.getStandingTypeIdFromName(aggression.getBiddingType()),
                StandingStatus.INMARKET
        );

        List<String> userStanding = new ArrayList<String>();
        boolean error = false;
        
        //busca el banco de trabajo
        Map<Integer, List<String>> workbenchesForStandings =
                getWorkbenchesForStandings(aggressorInstitution, standings);

		log.info("[AggressionHelper][doAggression] aggression.getAmount(): " + aggression.getAmount());
        
        int amountToFulfill = aggression.getAmount()*calc_base;

		log.info("[AggressionHelper][doAggression] calc_base: " + calc_base);
		log.info("[AggressionHelper][doAggression] amountToFulfill: " + amountToFulfill);
        
        Iterator it = workbenchesForStandings.entrySet().iterator();
        
        while (it.hasNext()) {
        	//TODO all the transactions have a common workbench
        	
        	//verifica que sea el mismo banco de trabajo
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue() == null) {
                error = true;
            }
        }
        
        int id_standing = 0;
        int ciclo = 0;
        
        if (!error) {
            for (StandingEntity std : standings) {

        		log.info("[AggressionHelper][doAggression] ciclo: " + ciclo);
            	
                List<String> wb = workbenchesForStandings.get(std.getIdStanding());

                int i = 0;
                if (wb != null) {
                    userStanding.add(userRepo.findOne(std.getFkIdUser()).getUsername());
                    //TODO correct create aggression it has extra parameters also check run conditions in for if so send all to create aggression
                    
                    int amount = 0;
                    id_standing = std.getIdStanding();
                    
                    if(standingDAO.busyStanding(1, std.getIdStanding())==1) {

                    		amount = createAggression(aggressor, amountToFulfill, std, wb, i, sessionId, aggressionTime);
                    	
                        
                    } else {
                    	messagingTemplate.convertAndSendToUser(aggressor.getUsername(), "/market/announcements",
                                new MarketMessage(
                                        400,  // updateTable
                                        "Standing Ocupado",
                                        "Standing Ocupado",
                                        null
                                ));
                    	standingDAO.busyStanding(0, std.getIdStanding());
                    	return true;
                    }
                    

            		log.info("[AggressionHelper][doAggression] amount: " + amount);
            		log.info("[AggressionHelper][doAggression] amountToFulfill: " + amountToFulfill);
                    
            		if (amount < amountToFulfill) {

            			//aqui entra profundidad mayor y dos o más niveles de agresión en profunidad.
                		log.info("[AggressionHelper][createAggression] Profundidad mayor");
                		//break;
                    }
                    
                    if (amount >= 0) {
                        amountToFulfill -= amount;
                    } else if (amount == -1) {
                        
                        if(id_standing!=0)
                        	standingDAO.busyStanding(0, id_standing);
                        
                        return false;
                    }
                    

            		log.info("[AggressionHelper][doAggression] amountToFulfill: " + amountToFulfill);
                    
                    if (amountToFulfill == 0) {

                		log.info("[AggressionHelper][doAggression] break");
                		
                        break;
                    }

            		log.info("[AggressionHelper][doAggression] amountToFulfill: " + amountToFulfill);

            		log.info("[AggressionHelper][doAggression] amount: " + amount);
            		
                }
                ciclo++;
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
                for(String userStd : userStanding) {
                    messagingTemplate.convertAndSendToUser(userStd, market_channel_users,
                            new MarketMessage(
                                    315,
                                    "Confirmed to postor",
                                    String.valueOf(aggressionDelay),
                                    aggression
                            ),
                            Collections.singletonMap(SimpMessageHeaderAccessor.SESSION_ID_HEADER, sessionId));
                }
                Thread.sleep(Math.round(aggressionDelay * 1000));
            } catch (InterruptedException e) {
                log.error("Error en el delay, mensaje: " + e.getMessage());
            }
            if (this.isCancelled(aggression, aggressor)) {
                MarketPosition marketPosition = this.completeAggression(aggression, aggressor, aggressionTime, amountToFulfill);
                if (marketPosition != null) {
                	
                	log.info("[AggressionHelper][doAggression][IsCancel = false] Se acepto el agression");

                	log.info("[AggressionHelper][doAggression][IsCancel = false] marketPosition.getAmount(): " + marketPosition.getAmount());
                	log.info("[AggressionHelper][doAggression][IsCancel = false] marketPosition.getBiddingType(): " + marketPosition.getBiddingType());
                	log.info("[AggressionHelper][doAggression][IsCancel = false] marketPosition.getInstrumentId(): " + marketPosition.getInstrumentId());
                	log.info("[AggressionHelper][doAggression][IsCancel = false] marketPosition.getRate(): " + marketPosition.getRate());
                	
                	//no hay código 99999 en front, se envía solo para limpiar el corro
                    messagingTemplate.convertAndSend(market_channel,
                            new MarketMessage(
                                    309,  // unlock table with new position if posible
                                    "NSAA",
                                    "New position after aggression",
                                    marketPosition
                            ));
                	
                    messagingTemplate.convertAndSendToUser(aggressor.getUsername(), market_channel_users,
                            new MarketMessage(
                                    302,  // unlock table with new position if posible
                                    "NSAA",
                                    "New position after aggression",
                                    marketPosition
                            ));
                    
                    for(String userStd : userStanding) {

                    	log.info("[AggressionHelper][doAggression][IsCancel = false] userStd: " + userStd);
                        
                        messagingTemplate.convertAndSendToUser(userStd, market_channel_users,
	                        new MarketMessage(
	                                302,  // unlock table with new position if posible
	                                "NSAA",
	                                "New position after aggression",
	                                marketPosition
	                        ));
                    }
                }
            } 
        } else {
        	
        	log.info("[AggressionHelp][doAggression][else-259][error diferente pareja]");
        	
            messagingTemplate.convertAndSendToUser(aggressor.getUsername(), "/market/announcements",
                    new MarketMessage(
                            400,  // updateTable
                            "Error aggression",
                            "No cuentas con bancos de trabajo para hacer esta agresión",
                            null
                    ));
        }

        if(id_standing!=0)
        	standingDAO.busyStanding(0, id_standing);
        
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
    //@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED)
    boolean isCancelled(MarketPosition aggression, UserEntity aggressor) {
    	log.info("[AggressionHelper][isCancelled]");
    	
        boolean retVal = false;
        List<AggressionEntity> userAggressions = aggRepo.findByFkIdUserAndFkIdTransactionStatusAndDatetimeGreaterThanEqual(
                aggressor.getIdUser(), AggresionStatus.ACTIVE, utilDao.today());

        for (AggressionEntity agg : userAggressions) {
            StandingEntity std = stdRepo.findOne(agg.getFkIdStanding());//TODO refactor AggressionEntity so it stores the id of the valmer instrument.
            if (std.getFkIdValmerPriceVector() == aggression.getInstrumentId()) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    //@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public MarketPosition completeAggression(MarketPosition aggression, UserEntity aggressor, Calendar aggressionTime, int amountToFulfill) {
        
    	log.info("[AggressionHelper][completeAggression]");
        
        int standingType = -1;
        List<AggressionEntity> aggressions = aggRepo.findByFkIdUserAndFkIdTransactionStatus(
                aggressor.getIdUser(), AggresionStatus.ACTIVE);
        
        int i=0;
        int ciclo = 0;
        
        for (AggressionEntity agg : aggressions) {
            long timeDifference = Math.abs(aggressionTime.getTimeInMillis() - agg.getDatetime().getTimeInMillis());
        	log.info("[AggressionHelper][completeAggression] entra");

        	log.info("[AggressionHelper][completeAggression] i: " + i);
        	log.info("[AggressionHelper][completeAggression] agg.getFkIdInstitution(): " + agg.getFkIdInstitution());
        	log.info("[AggressionHelper][completeAggression] agg.getFkIdStanding(): " + agg.getFkIdStanding());
        	log.info("[AggressionHelper][completeAggression] agg.getFkIdTransactionStatus(): " + agg.getFkIdTransactionStatus());
        	log.info("[AggressionHelper][completeAggression] agg.getFkIdUser(): " + agg.getFkIdUser());
        	log.info("[AggressionHelper][completeAggression] agg.getIdAggression(): " + agg.getIdAggression());
        	log.info("[AggressionHelper][completeAggression] agg.getAggressionDirtyPrice(): " + agg.getAggressionDirtyPrice());
        	log.info("[AggressionHelper][completeAggression] agg.getAmount(): " + agg.getAmount());
        	log.info("[AggressionHelper][completeAggression] agg.getDatetime(): " + agg.getDatetime().getTime().toString());
        	
        	i++;
        	
            if (timeDifference <= 1000) {
            	log.info("[AggressionHelper][completeAggression] entra a timeDifference <= 1000");
                StandingEntity standing = stdRepo.findOne(agg.getFkIdStanding());
                if (!notInTransaction(standing, aggression.getInstrumentId())) {
                	//Sean del mismo bidding (mismo instrumento)
                	log.info("[AggressionHelper][completeAggression] mismo instrumento");
                	
                    if (standingType < 0) {
                        standingType = standing.getFkIdStandingType();
                    }

                	log.info("[AggressionHelper][completeAggression] entra a completeAggressionAndStandings");
                	standingType = completeAggressionAndStandings(agg, standing, amountToFulfill, standingType, ciclo, aggressions);
                }
            } else {
                log.error("[AggressionHelper][completeAggression] AggressionTime:" + aggressionTime.getTimeInMillis());
                log.error("[AggressionHelper][completeAggression] DB aggresion time:" + agg.getDatetime().getTimeInMillis());
            }
            ciclo++;
        } //fin for
        if (standingType != -1) {
        	log.info("[AggressionHelper][completeAggression] entra a redoQueue");
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
    //@Transactional(isolation = Isolation.SERIALIZABLE)//TODO check if isolation level can be set to
    public MarketPosition redoQueue(int instrumentId, int standingType) {
    	//se regresa market position con los valores del 302 para actualizar la tabla
    	
    	log.info("[AggressionHelper][redoQueue]");
    	log.info("[AggressionHelper][redoQueue] instrumentId: " + instrumentId);
    	log.info("[AggressionHelper][redoQueue] standingType: " + standingType);
    	
    	//busca standings en cola
        List<StandingEntity> queue = stdRepo.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual(
                instrumentId, utilDao.today(), standingType, StandingStatus.INMARKET);
        Collections.sort(queue);

    	log.info("[AggressionHelper][redoQueue] queue.size(): " + queue.size());
        
        double rate = 0.0;
        if (queue.size() > 0) {
            rate = queue.get(0).getValue();
        }
        List<StandingEntity> inMarket = new ArrayList<>();
        List<StandingEntity> inMarketEquals = new ArrayList<>();
        
        for (int i = 0; i < queue.size(); i++) {
        	//los encolados los pone en mercado o en cola
            StandingEntity std = queue.get(i);
            
            log.info("[AggressionHelper][redoQueue] REDO_QUEUE --> Standing "+std.getIdStanding() + " Status Standing: "+std.getFkIdStandingStatus());
            log.info("[AggressionHelper][redoQueue] std.getValue(): " + std.getValue());
            log.info("[AggressionHelper][redoQueue] rate: " + rate);
            
            if (std.getValue() == rate) {
                std.setFkIdStandingStatus(StandingStatus.INMARKET);
                inMarket.add(std);
                inMarketEquals.add(std);
            } else {
                std.setFkIdStandingStatus(StandingStatus.QUEUED);
                inMarket.add(std);
            }
        }
        
        stdRepo.save(inMarket);
        
        MarketPosition marketPosition = SBGeneralUtilities.mergeStandings(inMarketEquals);
        if (marketPosition == null) {
            marketPosition = new MarketPosition();
            marketPosition.setBiddingType(SBGeneralUtilities.getStandingTypeNameFromId(standingType));
            marketPosition.setInstrumentId(instrumentId);
        }
        return marketPosition;
    }

    private int completeAggressionAndStandings(AggressionEntity aggression, StandingEntity std, int amountToFulfill, int standingType, int ciclo, List<AggressionEntity> aggressions) {
    	
    	log.info("[AggressionHelper][completeAggressionAndStandings]");
    	log.info("[AggressionHelper][completeAggressionAndStandings] aggression.getAmount(): " + aggression.getAmount());
    	log.info("[AggressionHelper][completeAggressionAndStandings] standingType: " + standingType);
    	log.info("[AggressionHelper][completeAggressionAndStandings] ciclo: " + ciclo);
    	log.info("[AggressionHelper][completeAggressionAndStandings] aggressions: " + aggressions.size());
    	
        int stdCurrentAmount = std.getCurrentAmount();
        std.setFkIdStandingStatus(StandingStatus.COMPLETED);
        aggression.setFkIdTransactionStatus(AggresionStatus.COMPLETED);
        
        //monto de la primera postura
    	log.info("[AggressionHelper][completeAggressionAndStandings] stdCurrentAmount: " + stdCurrentAmount);
    	log.info("[AggressionHelper][completeAggressionAndStandings] amountToFulfill: " + amountToFulfill);
    	log.info("[AggressionHelper][completeAggressionAndStandings] std.getFkIdUser(): " + std.getFkIdUser());
    	log.info("[AggressionHelper][completeAggressionAndStandings] aggression.getFkIdUser(): " + aggression.getFkIdUser());
    	log.info("[AggressionHelper][completeAggressionAndStandings] aggression.getIdAggression(): " + aggression.getIdAggression());
    	log.info("[AggressionHelper][completeAggressionAndStandings] aggression.getFkIdInstitution(): " + aggression.getFkIdInstitution());
    	
        
    	StandingEntity newStd = null;
    	
        if (aggression.getAmount() < stdCurrentAmount) {

        	log.info("[AggressionHelper][completeAggressionAndStandings] new standing profundidad menor");
        	
            newStd = new StandingEntity(std.getFkIdStandingType(),
                    std.getFkIdValmerPriceVector(),
                    std.getFkIdUser(),
                    stdCurrentAmount - aggression.getAmount(),
                    std.getValue(),
                    StandingStatus.QUEUED,
                    std.getDatetime(),
                    stdCurrentAmount - aggression.getAmount(),
                    std.getStandingDirtyPrice(),
                    std.getOrden());
            
            std.setCurrentAmount(aggression.getAmount());
            
            stdRepo.save(newStd);
            stdRepo.save(std);
            
            UserEntity user = userRepo.findOne(newStd.getFkIdUser());
            
        	log.info("[AggressionHelper][completeAggressionAndStandings] newStd.getFkIdUser(): " + newStd.getFkIdUser());
        	log.info("[AggressionHelper][completeAggressionAndStandings] user.getUsername(): " + user.getUsername());
            
            messagingTemplate.convertAndSendToUser(user.getUsername(), market_channel_users,
                    new MarketMessage(
                            210, "New position to bidder.", null,
                            new MarketPosition(newStd.getFkIdValmerPriceVector(),
                                    newStd.getValue(),
                                    newStd.getAmount()/calc_base,
                                    SBGeneralUtilities.getStandingTypeNameFromId(newStd.getFkIdStandingType()),
                                    newStd.getOrden())
                    ));
            
        } else if (amountToFulfill != 0 && (aggressions.size()-1)==ciclo) {

        	log.info("[AggressionHelper][completeAggressionAndStandings] new standing profundidad mayor");
        	
        	if(std.getFkIdStandingType()==1) {
        		std.setFkIdStandingType(2);
        	} else {
        		std.setFkIdStandingType(1);
        	}
        	
        	log.info("[AggressionHelper][completeAggressionAndStandings] std.getFkIdStandingType(): " + std.getFkIdStandingType());
        	
            newStd = new StandingEntity(std.getFkIdStandingType(),
                    std.getFkIdValmerPriceVector(),
                    aggression.getFkIdUser(),
                    Math.abs(amountToFulfill),
                    std.getValue(),
                    StandingStatus.QUEUED,
                    std.getDatetime(),
                    Math.abs(amountToFulfill),
                    std.getStandingDirtyPrice(),
                    std.getOrden());
                    
            std.setCurrentAmount(aggression.getAmount());
            
            stdRepo.save(newStd);
            
            if(std.getFkIdStandingType()==1) {
        		std.setFkIdStandingType(2);
        	} else {
        		std.setFkIdStandingType(1);
        	}
            
            stdRepo.save(std);
            
            if(std.getFkIdStandingType()==1) {
        		std.setFkIdStandingType(2);
        	} else {
        		std.setFkIdStandingType(1);
        	}
            
            standingType = std.getFkIdStandingType();
        	log.info("[AggressionHelper][completeAggressionAndStandings] standingType: " + standingType);
            
            UserEntity user = userRepo.findOne(newStd.getFkIdUser());
            UserEntity user2 = userRepo.findOne(std.getFkIdUser());
            
        	log.info("[AggressionHelper][completeAggressionAndStandings] newStd.getFkIdUser(): " + newStd.getFkIdUser());
        	log.info("[AggressionHelper][completeAggressionAndStandings] user.getUsername(): " + user.getUsername());
        	log.info("[AggressionHelper][completeAggressionAndStandings] std.getFkIdUser(): " + std.getFkIdUser());
        	log.info("[AggressionHelper][completeAggressionAndStandings] user2.getUsername(): " + user2.getUsername());
           
            messagingTemplate.convertAndSendToUser(user.getUsername(), market_channel_users,
                    new MarketMessage(
                            212, "New position to bidder.", null,
                            new MarketPosition(newStd.getFkIdValmerPriceVector(),
                                    newStd.getValue(),
                                    newStd.getAmount()/calc_base,
                                    SBGeneralUtilities.getStandingTypeNameFromId(newStd.getFkIdStandingType()),
                                    newStd.getOrden())
                    ));
			

        	messagingTemplate.convertAndSend(market_channel,
        			new MarketMessage(
                            211, "New position to bidder.", null,
                            new MarketPosition(newStd.getFkIdValmerPriceVector(),
                                    newStd.getValue(),
                                    newStd.getAmount()/calc_base,
                                    SBGeneralUtilities.getStandingTypeNameFromId(newStd.getFkIdStandingType()),
                                    newStd.getOrden())
                    ));
        	/*
            messagingTemplate.convertAndSendToUser(user2.getUsername(), market_channel_users,
                    new MarketMessage(
                            211, "New position to bidder.", null,
                            new MarketPosition(newStd.getFkIdValmerPriceVector(),
                                    newStd.getValue(),
                                    newStd.getAmount()/calc_base,
                                    SBGeneralUtilities.getStandingTypeNameFromId(newStd.getFkIdStandingType()))
                    ));
			*/
            
        } else {

        	log.info("[AggressionHelper][completeAggressionAndStandings] save standing else");
        	
        	log.info("[AggressionHelper][completeAggressionAndStandings] ------------------------------");
        	log.info("[AggressionHelper][completeAggressionAndStandings] std.getAmount(): " + std.getAmount());
        	log.info("[AggressionHelper][completeAggressionAndStandings] std.getCurrentAmount(): " + std.getCurrentAmount());
        	log.info("[AggressionHelper][completeAggressionAndStandings] std.getFkIdStandingStatus(): " + std.getFkIdStandingStatus());
        	log.info("[AggressionHelper][completeAggressionAndStandings] std.getFkIdStandingType(): " + std.getFkIdStandingType());
        	log.info("[AggressionHelper][completeAggressionAndStandings] std.getFkIdUser(): " + std.getFkIdUser());
        	log.info("[AggressionHelper][completeAggressionAndStandings] std.getIdStanding(): " + std.getIdStanding());
        	log.info("[AggressionHelper][completeAggressionAndStandings] std.getValue(): " + std.getValue());
        	log.info("[AggressionHelper][completeAggressionAndStandings] std.getStandingDirtyPrice(): " + std.getStandingDirtyPrice());
        	log.info("[AggressionHelper][completeAggressionAndStandings] std.getOrden(): " + std.getOrden());
        	
            UserEntity user = userRepo.findOne(std.getFkIdUser());
            
        	log.info("[AggressionHelper][completeAggressionAndStandings] user.getUsername(): " + user.getUsername());
        	
        	messagingTemplate.convertAndSendToUser(user.getUsername(), market_channel_users,
        			new MarketMessage(
                            213, "Limpiar Corro", null,
                            new MarketPosition(std.getFkIdValmerPriceVector(),
                            		std.getValue(),
                            		std.getAmount()/calc_base,
                                    SBGeneralUtilities.getStandingTypeNameFromId(std.getFkIdStandingType()),
                                    std.getOrden())
                    ));
        
        	
            stdRepo.save(std);
        }
        
        aggRepo.save(aggression);
        
        return standingType;
        
    }

    private boolean notInTransaction(StandingEntity std, int instrumentId) {
        if (std.getFkIdValmerPriceVector() == instrumentId) {
            return false;
        }
        return true;
    }


    //@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    //@Transactional(isolation = Isolation.SERIALIZABLE)
    public int createAggression(UserEntity aggressor, int amountToFulfill, StandingEntity std,
                                List<String> wb, int option, String sessionId, Calendar aggressionTime) {

		log.info("[AggressionHelper][createAggression]");
		

		log.info("[AggressionHelper][createAggression] " + wb.get(1));
		
        int standingAmount = std.getCurrentAmount();
        AggressionEntity aggressionEntity = null;
        int retVal = -1; 
        double overrideRate;
        StandingEntity tmp = stdRepo.findOne(std.getIdStanding());
        //EntityManager entityManager = entityManagerFactory.createEntityManager();
        //EntityTransaction txn = entityManager.getTransaction();
        //txn.begin();
        try {
            //do something with your database
            //StandingEntity tmp = entityManager.find(StandingEntity.class, std.getIdStanding(),LockModeType.PESSIMISTIC_WRITE);//stdRepo.findByIdStanding(std.getIdStanding())0;
            //entityManager.refresh(tmp);
            if (tmp.getFkIdStandingStatus() != StandingStatus.INMARKET){ //std.getFkIdStandingStatus()) {
                return -1;
            }
            //em.lock(std, LockModeType.PESSIMISTIC_WRITE);

    		log.info("[AggressionHelper][createAggression] wb.get(1): " + wb.get(1));
            
            if( tmp.getFkIdStandingType() == 2)
                overrideRate =  std.getValue() - Double.parseDouble(wb.get(1));
            else
                overrideRate = Double.parseDouble(wb.get(1)) + std.getValue();
            

            ValmerPriceVectorEntity priceVectorData = dao.findOne(std.getFkIdValmerPriceVector());
            
            Calendar fecha_liq = CalendarUtil.today();
            
            /*
            switch (priceVectorData.getTv()) {
    	        //casos míos
    	        case "LD":
	            	fecha_liq = (Calendar)utilDao.liquidation_day1().clone();
    	        	break;
    	        case "IQ":
	            	fecha_liq = (Calendar)utilDao.liquidation_day1().clone();
    	        	break;
    	        case "IS":
	            	fecha_liq = (Calendar)utilDao.liquidation_day1().clone();
    	        	break;
    	        case "IM":
	            	fecha_liq = (Calendar)utilDao.liquidation_day1().clone();
    	        	break;
    	        case "M":

    	            fecha_liq = (Calendar)utilDao.liquidation_day().clone();
    	            	
    	        	break;
    	        case "S":
    	        	
    	        	fecha_liq = (Calendar)utilDao.liquidation_day().clone();
    	        	
    	        	break;
    	        case "BI":
    	        	
    	        	fecha_liq = (Calendar)utilDao.liquidation_day().clone();
    	        	
    	        	break;
            }*/
            
            switch (priceVectorData.getHr()) {
		        //casos míos
		        case "0":
	            	fecha_liq = CalendarUtil.today();
		        	break;
		        case "1":
	            	fecha_liq = (Calendar)utilDao.liquidation_day1().clone();
		        	break;
		        case "2":
	            	fecha_liq = (Calendar)utilDao.liquidation_day().clone();
		            	
		        	break;
	        }

    		log.info("[AggressionHelper][createAggression] fecha_liq: " + fecha_liq);

            Double dirtyPrice = dpCalc.calcDirtyPrice(
                    tmp,
                    fecha_liq,
                    overrideRate
            );
            

    		log.info("[AggressionHelper][createAggression] standingAmount: " + standingAmount);
    		log.info("[AggressionHelper][createAggression] amountToFulfill: " + amountToFulfill);
    		
    		//profunidad
    		//standingAmount -> monto del que postula
    		//amountToFulfill -> monto del que hace profunidad

            int aggressionAmount = 0;
            if (standingAmount >= amountToFulfill) {

        		log.info("[AggressionHelper][createAggression] standingAmount es mayor o gual amountToFulfill");
        		log.info("[AggressionHelper][createAggression] profunidad a menor o igual cantidad del que acepta");
        		
                aggressionAmount = amountToFulfill;
                
            } else {

        		log.info("[AggressionHelper][createAggression] standingAmount es menor");
        		log.info("[AggressionHelper][createAggression] profunidad a mayor cantidad del que acepta");
        		
                aggressionAmount = standingAmount;
            }
            

    		log.info("[AggressionHelper][createAggression] standingAmount: " + standingAmount);
    		log.info("[AggressionHelper][createAggression] amountToFulfill: " + amountToFulfill);
    		log.info("[AggressionHelper][createAggression] aggressionAmount: " + aggressionAmount);
            
            Aggression aggression = new Aggression(tmp.getIdStanding(),
								                    AggresionStatus.ACTIVE,
								                    aggressor.getIdUser(),
								                    Integer.parseInt(wb.get(0)),
								                    aggressionTime,
								                    aggressionAmount,
								                    dirtyPrice);
            
            aggressionEntity =
                    new AggressionEntity(
                            tmp.getIdStanding(),
                            AggresionStatus.ACTIVE,
                            aggressor.getIdUser(),
                            Integer.parseInt(wb.get(0)),
                            aggressionTime,
                            aggressionAmount,
                            dirtyPrice
                    );
            
            log.info("[AggressionHelper][createAggression] aggTime:" + aggressionTime.getTimeInMillis());
            log.info("[AggressionHelper][createAggression] aggTime:" + aggressionTime.getTime().toString());
            log.info("[AggressionHelper][createAggression] aggTime aggression: " + aggressionEntity.getDatetime().getTimeInMillis());
            log.info("[AggressionHelper][createAggression] aggressionAmount: " + aggressionAmount);
            
            retVal = aggressionAmount;
            //std.setFkIdStandingStatus(StandingStatus.AGGRESSED);
            tmp.setFkIdStandingStatus(StandingStatus.AGGRESSED);
            if (aggressionEntity != null) {
                //entityManager.persist(aggressionEntity);
            	aggressionDao.addAggression(aggression);
            }
            //entityManager.persist(tmp);
            standingDAO.updateStanding(StandingStatus.AGGRESSED, std.getIdStanding());
            

    		log.info("[AggressionHelper][createAggression] commit: ");
    		
            //txn.commit();

        }
        finally {

    		log.info("[AggressionHelper][createAggression] finally: ");
    		
            //if (txn.isActive())
                //txn.rollback();
        }

        //entityManager.refresh(tmp);


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
    	

		log.info("[AggressionHelper][getWorkbenchesForStandings]");
    	
        HashMap<Integer, List<String>> map = new HashMap<>();
        for (StandingEntity std : standings) {
            UserEntity user = userRepo.findOne(std.getFkIdUser());
            int stdInst = user.getFkIdInstitution();
            int idInst = std.getFkIdValmerPriceVector();
            int stdTyp = std.getFkIdStandingType();

            ValmerPriceVectorEntity priceVectorData = dao.findOne(std.getFkIdValmerPriceVector());

    		log.info("[AggressionHelper][getWorkbenchesForStandings] stdInst: " + stdInst);
    		log.info("[AggressionHelper][getWorkbenchesForStandings] idInst: " + idInst);
    		log.info("[AggressionHelper][getWorkbenchesForStandings] stdTyp: " + stdTyp);
    		

    		log.info("[AggressionHelper][getWorkbenchesForStandings] getTv: " + priceVectorData.getTv());
            
    		//tabla instrument_Type jalar id where tv.
    		//priceVectorData.getTv()

            this.instrument = instrumentDao.getInstrument(priceVectorData.getTv());
    		log.info("[AggressionHelper][getWorkbenchesForStandings] this.instrument.getId_instrument(): " + this.instrument.getId_instrument());
    		
    		log.info("[AggressionHelper][getWorkbenchesForStandings] std.getIdStanding(): " + std.getIdStanding());
    		
    		//en vez de stdTyp usar el id de la ip que te devuelve
            map.put(std.getIdStanding(), getCommonWorkBench(instA, stdInst, this.instrument.getId_instrument()));
        }
        return map;
    }

    private List<String> getCommonWorkBench(int aggressor, int bidder, int idInstrument) {
        return traderDao.getCommonWorkbench(aggressor, bidder, idInstrument);
    }

    public MarketPosition checkPositionInMarker(int instrumentId, String biddingType) {
		
    	log.info("[AggressionHelper][checkPositionInMarker] instrumentId: " + instrumentId);
    	log.info("[AggressionHelper][checkPositionInMarker] biddingType: " + biddingType);
		
        List<StandingEntity> standings = checkStandingInMarket(instrumentId, biddingType);
        return SBGeneralUtilities.mergeStandings(standings, biddingType);
    }

    private List<StandingEntity> checkStandingInMarket(int instrumentId, int standingType) {
    	
    	log.info("[AggressionHelper][checkStandingInMarket] instrumentId: " + instrumentId);
    	log.info("[AggressionHelper][checkStandingInMarket] biddingType: " + standingType);
    	
        List<StandingEntity> standings = stdRepo.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatus(
                instrumentId, utilDao.today(), standingType, StandingStatus.INMARKET);
        
    	log.info("[AggressionHelper][checkStandingInMarket] standings.size(): " + standings.size());
        
        //em.refresh(standings);
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
