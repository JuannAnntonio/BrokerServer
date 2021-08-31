package mx.sigmact.broker.services.rest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import mx.sigmact.broker.core.helper.AggressionHelper;
import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.core.util.SBGeneralUtilities;
import mx.sigmact.broker.dao.MarketDao;
import mx.sigmact.broker.dao.StandingDAO;
import mx.sigmact.broker.dao.TraderDao;
import mx.sigmact.broker.dao.UtilDao;
import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.model.StandingTypeEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.model.ValmerPriceVectorEntity;
import mx.sigmact.broker.pojo.ErrorStatus;
import mx.sigmact.broker.pojo.MarketMessage;
import mx.sigmact.broker.pojo.MarketPositionUser;
import mx.sigmact.broker.pojo.StandingStatus;
import mx.sigmact.broker.pojo.StandingType;
import mx.sigmact.broker.pojo.TraderActivityView;
import mx.sigmact.broker.pojo.TraderGraph;
import mx.sigmact.broker.pojo.TraderInstrumentView;
import mx.sigmact.broker.pojo.TraderTicketView;
import mx.sigmact.broker.pojo.trader.BlockDetail;
import mx.sigmact.broker.pojo.trader.CancelledStatus;
import mx.sigmact.broker.pojo.trader.MarketPosition;
import mx.sigmact.broker.pojo.trader.MarketPositionDetail;
import mx.sigmact.broker.pojo.trader.TablePosition;
import mx.sigmact.broker.repositories.BrokerStandingRepository;
import mx.sigmact.broker.repositories.BrokerStandingTypeRepository;
import mx.sigmact.broker.repositories.BrokerUserRepository;
import mx.sigmact.broker.repositories.BrokerValmerPriceVectorRepository;

/**
 * Created on 07/12/16.
 */

@RestController
@RequestMapping("trader/rest")
public class RESTTrader {

	Logger log = Logger.getLogger(RESTTrader.class);

	@Resource
	private UtilDao utilDao;
	@Value("${timeout}")
	Integer timeout;
	@Resource
	BrokerValmerPriceVectorRepository dao;
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
	@Autowired
	private AggressionHelper aggressionHelper;
	@Autowired
	private StandingDAO standingDAO;

	@Value("${calc_base}")
	Integer calc_base;

	private static final String market_channel = "/market/announce";
	private static final String market_channel_users = "/market/announcements";

	/*
	 * ********************* Trader inital set up
	 ************************************/

	/**
	 * This method get the trader instruments
	 * 
	 * @return A list with the trader instruments for display ine
	 */
	@RequestMapping(value = "getTraderInstruments", method = RequestMethod.GET, produces = "application/json")
	public List<TraderInstrumentView> doGetTraderInstruments() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		UserEntity user = userRepo.findOneByUsername(name);
		return traderDao.getTraderInstruments(user.getIdUser());
	}

	/**
	 * Returns the current logged trader activity as specified by the
	 * {@code SecurityContextHolder} this is used to get the trader activity at
	 * login. This method should be used with {@code doGetMarketPositions} to get
	 * the initial state of the bidding table.
	 * 
	 * @return A {@code List} with the trader past activity.
	 */
	@RequestMapping(value = "getTraderActivity", method = RequestMethod.GET, produces = "application/json")
	public List<TraderActivityView> doGetTraderActivity() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Calendar cal = utilDao.today();// Calendar.getInstance();
		UserEntity user = userRepo.findOneByUsername(name);
		return traderDao.getTraderActivity(user.getIdUser(), cal);
	}

	/**
	 * This method returns the current market positions it is used to get the market
	 * positions when logging in. It is preffered to load this information prior to
	 * displaying any controls. This method should be used with
	 * {@code doGetTraderActivity} to get the initial state of the bidding table.
	 * 
	 * @return A {@code List} with the market positions.
	 */
	@RequestMapping(value = "marketPositions", method = RequestMethod.GET, produces = "application/json")
	public List<TablePosition> doGetMarketPositions() {

		log.info("[RESTTrader][doGetMarketPositions]");

		List<TablePosition> stdForTrader = new ArrayList<>();
		List<StandingEntity> stdInMarket = standingRepo.findByFkIdStandingStatus(StandingStatus.INMARKET);
		List<TraderInstrumentView> traderInstrumentViews = doGetTraderInstruments();
		Calendar day = utilDao.today();// CalendarUtil.zeroTimeCalendar(Calendar.getInstance());
		Iterable<StandingTypeEntity> stdTypesEnt = stdTypRepo.findAll();
		HashMap<Integer, String> stdTypes = new HashMap<>();
		for (StandingTypeEntity stdTyp : stdTypesEnt) {
			stdTypes.put(stdTyp.getIdStandingType(), stdTyp.getName());
		}
		for (StandingEntity std : stdInMarket) {
			for (TraderInstrumentView instrument : traderInstrumentViews) {
				if (day.compareTo(std.getDatetime()) > 0) {
					if (instrument.getIdVPV() == std.getFkIdValmerPriceVector()) {
						stdForTrader.add(new TablePosition(stdTypes.get(std.getFkIdStandingType()),
								std.getCurrentAmount() / calc_base, std.getValue(), std.getFkIdValmerPriceVector()));
					}
				} else {

					log.info("[RESTTrader][doGetMarketPositions] update cancel");

					// standingDao.updateStanding(StandingStatus.CANCELLED, std.getIdStanding());

					std.setFkIdStandingStatus(StandingStatus.CANCELLED);
				}
			}
		}
		return stdForTrader;
	}

	/**
	 * For displaying trader tickets this method should be called each time you want
	 * to refresh the tickets. It is a poll service.
	 * 
	 * @return A{@code List} with the information for different tickets.
	 */
	@RequestMapping(value = "getTraderTickets", method = RequestMethod.GET, produces = "application/json")
	public List<TraderTicketView> doGetTraderTickets() {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Calendar cal = utilDao.today();// Calendar.getInstance();
		UserEntity user = userRepo.findOneByUsername(name);
		return traderDao.getTraderTickets(user.getIdUser(), cal);
	}

	/**
	 * Retruns a graph for a give Instrument with the point availiable for the
	 * period specified in days.
	 * 
	 * @param days       The amount of days of data to collect.
	 * @param instrument The instrument you wish to get the information.
	 * @return A {@code TraderGraph} whit thye information to plot the desired
	 *         graph.
	 */
	@RequestMapping(value = "getTraderGraph", method = RequestMethod.GET, produces = "application/json")
	public TraderGraph doGetTraderGraphByDays(@RequestParam("days") Integer days,
			@RequestParam("instrument") String instrument) {
		TraderGraph retVal = new TraderGraph();
		if (days > 1) {
			retVal.setList(traderDao.getGraphDataPoints(days, instrument));
		} else if (days == 1) {
			retVal.setList(traderDao.getGraphDataPointsCurrentDay(instrument));
		}
		return retVal;
	}

	/*
	 * ************************ End of trader initial setup
	 ******************************************/

	/*
	 * ************************ Trader STOMP (synched) services
	 **************************************/

	/**
	 * Handles a client aggression
	 * 
	 * @param aggression
	 * @param headerAccessor
	 */
	// @Transactional(isolation = Isolation.READ_COMMITTED)//, propagation =
	// Propagation.REQUIRES_NEW)
	@MessageMapping("/aggress")
	public void doAggress(MarketPosition aggression, SimpMessageHeaderAccessor headerAccessor) {

		log.info("[RESTTrader][doAggress]");

		log.info("[RESTTrader][doAggress] headerAccessor: " + headerAccessor.toString());
		log.info("[RESTTrader][doAggress] aggression.getAmount(): " + aggression.getAmount());
		log.info("[RESTTrader][doAggress] aggression.getBiddingType(): " + aggression.getBiddingType());
		log.info("[RESTTrader][doAggress] aggression.getInstrumentId(): " + aggression.getInstrumentId());
		log.info("[RESTTrader][doAggress] aggression.getRate(): " + aggression.getRate());

		MarketPositionUser mpu = standingDAO.findByIdStandingStatusAndIdInstrument(aggression.getInstrumentId());
		UserEntity aggressor = userRepo.findOneByUsername(headerAccessor.getUser().getName());
		if (mpu.getIdInstitution().equals(aggressor.getFkIdInstitution())
				&& !mpu.getIdUser().equals(aggressor.getIdUser())) {
			messagingTemplate.convertAndSendToUser(aggressor.getUsername(), market_channel_users, new MarketMessage(405,
					"Error aggression", "No es posible agredir una postura de tu misma institución", null));
		} else if (mpu.getIdUser().equals(aggressor.getIdUser())) {// si el usuario agresor (clic verde) es == al
																	// usuario de stading
			messagingTemplate.convertAndSendToUser(aggressor.getUsername(), market_channel_users,
					new MarketMessage(405, "Error aggression",
							"Estas agrediendo una posición con el mismo rendimiento que el que ofreces", null));
		} else {
			try {

				Calendar cal = utilDao.todayWithTime();// Calendar.getInstance();
				MarketPosition marketPosition = aggressionHelper.checkPositionInMarker(aggression.getInstrumentId(),
						aggression.getBiddingType());
				if (marketPosition != null) {
					String sessionId = headerAccessor.getSessionId();
					// if (aggression.getAmount() <= marketPosition.getAmount()) {

					UserEntity aggressorOk = null;

					if (headerAccessor.getFirstNativeHeader("user-name") == null) {
						aggressorOk = userRepo.findOneByUsername(headerAccessor.getUser().getName());
					} else {
						aggressorOk = userRepo.findOneByUsername(headerAccessor.getFirstNativeHeader("user-name"));
					}

					log.info("[RESTTrader][doAggress] aggressor.getFkIdInstitution(): "
							+ aggressorOk.getFkIdInstitution());

					if (!aggressionHelper.doAggression(aggression, aggressorOk, sessionId, cal)) {

						log.info("[RESTTrader][doAggress] nada");

						messagingTemplate.convertAndSendToUser(aggressorOk.getUsername(), market_channel_users,
								new MarketMessage(405, "Error aggression", "Error while aggressing", null));
					} else {
						// mandar mensaje al usuario objetivo para que suena. (idea)
					}
					// }
				} else {
					// mandar mensaje al usuario objetivo para que suena. (idea)

					log.info("[RESTTrader][doAggress] market null");
				}

			} catch (Exception e) {
				log.info("[RESTTrader][doAggress] Exception: " + e.toString());
			}
		}
	}

	/**
	 * This method recieves messages from the clients and sends a standing if a new
	 * standing is available in the market. The channel is /BBBroker/position
	 *
	 * @param position The position to post to market.
	 * @return
	 */
	@MessageMapping("/position")
	@SendTo("/market/announce")
	public MarketMessage doPostPosition(MarketPosition position, SimpMessageHeaderAccessor headerAccessor) {

		log.info("[RESTTrader][doPostPosition]");

		log.info("[RESTTrader][doPostPosition] headerAccessor: " + headerAccessor.toString());
		log.info("[RESTTrader][doPostPosition] aggression.getAmount(): " + position.getAmount());
		log.info("[RESTTrader][doPostPosition] aggression.getBiddingType(): " + position.getBiddingType());
		log.info("[RESTTrader][doPostPosition] aggression.getInstrumentId(): " + position.getInstrumentId());
		log.info("[RESTTrader][doPostPosition] aggression.getRate(): " + position.getRate());
		log.info("[RESTTrader][doPostPosition] aggression.getOrden(): " + position.getOrden());

		 Principal wsUser = headerAccessor.getUser();
		 UserEntity user = this.userRepo.findOneByUsername(wsUser.getName());

		Calendar day = utilDao.today();

		Calendar liqDay = CalendarUtil.today();// CalendarUtil.zeroTimeCalendar(Calendar.getInstance());

		String sessionId = headerAccessor.getSessionId();
		String userChannel = "/market/announcements";
		if (position.getAmount() == 0 || position.getRate() == 0.0) {
			messagingTemplate.convertAndSendToUser(user.getUsername(), market_channel_users,
					new ErrorStatus(405, "Error", "Invalid rate or amount.", position),
					Collections.singletonMap(SimpMessageHeaderAccessor.SESSION_ID_HEADER, sessionId));
			return null;
		}

		log.error("[RESTTrader][doPostPosition] position.amount: " + position.getAmount());
		log.error("[RESTTrader][doPostPosition] base: " + (calc_base));
		log.error("[RESTTrader][doPostPosition] position.amoun*baset: " + (position.getAmount() * calc_base));

		StandingEntity standing = new StandingEntity(
				stdTypRepo.findByName(position.getBiddingType()).get(0).getIdStandingType(), position.getInstrumentId(),
				user.getIdUser(), position.getAmount() * calc_base, position.getRate(), StandingStatus.MARKETPOST,
				utilDao.todayWithTime(),
				// Calendar.getInstance(),
				position.getAmount() * calc_base, 0.0, position.getOrden());

		ValmerPriceVectorEntity priceVectorData = dao.findOne(standing.getFkIdValmerPriceVector());

		/*
		 * switch (priceVectorData.getTv()) { //casos míos case "LD": liqDay =
		 * (Calendar)utilDao.liquidation_day1().clone(); break; case "IQ": liqDay =
		 * (Calendar)utilDao.liquidation_day1().clone(); break; case "IS": liqDay =
		 * (Calendar)utilDao.liquidation_day1().clone(); break; case "IM": liqDay =
		 * (Calendar)utilDao.liquidation_day1().clone(); break; case "M":
		 * 
		 * liqDay = (Calendar)utilDao.liquidation_day().clone();
		 * 
		 * break; case "S":
		 * 
		 * liqDay = (Calendar)utilDao.liquidation_day().clone();
		 * 
		 * break; case "BI":
		 * 
		 * liqDay = (Calendar)utilDao.liquidation_day().clone();
		 * 
		 * break; }
		 */

		switch (priceVectorData.getHr()) {
		// casos míos
		case "0":
			liqDay = CalendarUtil.today();
			break;
		case "1":
			liqDay = (Calendar) utilDao.liquidation_day1().clone();
			break;
		case "2":
			liqDay = (Calendar) utilDao.liquidation_day().clone();

			break;
		}

		log.info("[RESTTrader][doPostPosition] liqDay: " + liqDay);

		try {

			standing.setStandingDirtyPrice(dpCalc.calcDirtyPrice(standing, liqDay));

		} catch (Exception e) {

			messagingTemplate.convertAndSendToUser(user.getUsername(), userChannel,
					new ErrorStatus(405, "Error", "The price could not be computed.",
							SBGeneralUtilities.getPositionFromStanding(standing)),
					Collections.singletonMap(SimpMessageHeaderAccessor.SESSION_ID_HEADER, sessionId));
			return null;
		}

		String newInMarket = null;
		List<StandingEntity> standings = // TODO change the new identification method for a more efficient version
				sortQueueNew(standing, day);

		if (isNewInMarket(standings, standing)) {
			newInMarket = "NEW";
		}

		List<BlockDetail> blockDetailList = mktDao.getBlockDetailByPosition(position.getInstrumentId(),
				position.getBiddingType(), user);
		MarketPosition marketPosition = SBGeneralUtilities.mergeStandings(standings, StandingStatus.INMARKET);

		/*
		 * messagingTemplate.convertAndSendToUser(user.getUsername(), userChannel, new
		 * MarketMessage(303, "POSITION", newInMarket, marketPosition));
		 */

		marketPosition.setBlockDetailList(blockDetailList);
		messagingTemplate.convertAndSend(market_channel,
				new MarketMessage(303, "POSITION", newInMarket, getSameInstitution(marketPosition,headerAccessor)));
		try {
			Thread.sleep(timeout * 1000);

			log.info("[RESTTrader][doPostPosition] Thread.sleep: " + timeout * 1000);

		} catch (InterruptedException e) {
			log.error("Thread waiting for post position interrupted");
		}

		standing = standingRepo.findOne(standing.getIdStanding());
		MarketMessage retVal;

		if (standing.getFkIdStandingStatus() != StandingStatus.CANCELLED) {
			retVal = new MarketMessage(311, "ENABLE AGGRESSION", newInMarket,
					getSameInstitution(marketPosition, headerAccessor));
		} else {
			retVal = new MarketMessage(0, "NULL OPERATION", newInMarket,
					getSameInstitution(marketPosition, headerAccessor));
		}
		return retVal;
	}

	private MarketPosition getSameInstitution(MarketPosition marketPosition, SimpMessageHeaderAccessor headerAccessor) {
		MarketPositionUser mpu = standingDAO.findByIdStandingStatusAndIdInstrument(marketPosition.getInstrumentId());
		UserEntity aggressor = userRepo.findOneByUsername(headerAccessor.getUser().getName());

		marketPosition.setUserName(aggressor.getUsername());
		marketPosition.setInstitucion(mpu.getNameInstitution());
		if (mpu.getIdInstitution().equals(aggressor.getFkIdInstitution())
				&& !mpu.getIdUser().equals(aggressor.getIdUser())) {
			marketPosition.setSameInstitution(true);
		}
		return marketPosition;
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
	 * 
	 * @param position       The position you wish to cancel.
	 * @param headerAccessor The credentials sent by your client in th STOMP
	 *                       message.
	 * @return
	 */
	@MessageMapping("/cancelPosition")
	@SendToUser("/market/canceled")
	public CancelledStatus doCancelPosition(MarketPosition position, SimpMessageHeaderAccessor headerAccessor) {

		log.info("[RESTTrader][doCancelPosition]");

		Calendar day = utilDao.today();// CalendarUtil.zeroTimeCalendar(Calendar.getInstance());
		CancelledStatus status = new CancelledStatus();
		status.setBiddingType(position.getBiddingType());
		status.setInstrumentId(position.getInstrumentId());
		Integer valmerId = position.getInstrumentId();
		String userName = headerAccessor.getUser().getName();
		UserEntity user = userRepo.findOneByUsername(userName);
		int bid_type = StandingType.getBidTypeId(position.getBiddingType());
		StandingEntity userStanding = standingRepo
				.findOneByFkIdUserAndFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingStatusLessThanEqualAndFkIdStandingType(
						user.getIdUser(), valmerId, day, StandingStatus.INMARKET, bid_type);
		if (userStanding == null) {

			status.setCode(501);
			status.setMessage("La postura no ha podido ser encontrada error de sistema");
			status.setStatus("Error");
		} else if (userStanding.getFkIdStandingStatus() == StandingStatus.AGGRESSED) {
			status.setCode(502);
			status.setMessage("La postura se encuentra agredida y no ha podido ser cancelada");
			status.setStatus("Error");
		} else {

			log.info("[RESTTrader][doCancelPosition] Update");

			int stdStatus = userStanding.getFkIdStandingStatus();

			int stdId = userStanding.getIdStanding();

			log.info("[RESTTrader][doCancelPosition] stdStatus: " + stdStatus);

			log.info("[RESTTrader][doCancelPosition] stdId: " + stdId);

			standingDAO.updateStanding(StandingStatus.CANCELLED, stdId);

			userStanding.setFkIdStandingStatus(StandingStatus.CANCELLED);
			
			
			// consultar posturas similares
			
			// recorrer el orden ... si tenias Orden 1 ahora seras 0

			standingRepo.save(userStanding);

			List<BlockDetail> blockDetailList = mktDao.getBlockDetailByPosition(position.getInstrumentId(),
					position.getBiddingType(), user);
			// if (stdStatus == StandingStatus.INMARKET) {
			MarketPosition inMarket = sortQueue(userStanding, CalendarUtil.zeroTimeCalendar(Calendar.getInstance()),
					false);
			if (inMarket != null) {
				inMarket.setBlockDetailList(blockDetailList);
				MarketMessage mm = new MarketMessage(303, "CANCEL", null, inMarket);
				messagingTemplate.convertAndSend("/market/announce", mm);
			} else {

				MarketPosition tablePosition = new MarketPosition();
				tablePosition.setBiddingType(position.getBiddingType());
				tablePosition.setBlockDetailList(blockDetailList);
				MarketMessage mm = new MarketMessage(303, "CANCEL", null, tablePosition);
				messagingTemplate.convertAndSend("/market/announce", mm);

			}
			status.setCode(201);
			status.setMessage("La postura ha sido eliminada");
			status.setStatus("Exito");
			// }
		}
		return status;
	}

	/**
	 * Cancels all position posted by the trader logged in when position in status
	 * InMarket.
	 * 
	 * @param headerAccessor The credentials sent by your client in th STOMP
	 *                       message.
	 * @return
	 */
	// @Transactional(isolation = Isolation.SERIALIZABLE)
	@MessageMapping("/cancelAllPosition")
	public void doCancelAllPosition(SimpMessageHeaderAccessor headerAccessor) {

		log.info("[RESTTrader][doCancelAllPosition]");

		Calendar day = utilDao.today();// CalendarUtil.zeroTimeCalendar(Calendar.getInstance());
		String userName = headerAccessor.getUser().getName();
		UserEntity user = userRepo.findOneByUsername(userName);

		List<StandingEntity> userStandings = standingRepo
				.findByFkIdUserAndDatetimeGreaterThanEqualAndFkIdStandingStatusLessThanEqual(user.getIdUser(), day,
						StandingStatus.INMARKET);
		if (userStandings.isEmpty()) {
			CancelledStatus status = new CancelledStatus();
			status.setCode(501);
			status.setMessage("No han sido encontradas posturas en estaus InMarket");
			status.setStatus("Error");
			messagingTemplate.convertAndSendToUser(userName, "/market/canceled", status);
		} else {
			for (StandingEntity userStanding : userStandings) {
				int stdStatus = userStanding.getFkIdStandingStatus();
				userStanding.setFkIdStandingStatus(StandingStatus.CANCELLED);
				standingRepo.save(userStandings);
				// fecha_fin
				standingDAO.updateStanding(StandingStatus.CANCELLED, userStanding.getIdStanding());

				CancelledStatus statusInstrumento = new CancelledStatus();
				statusInstrumento.setBiddingType(StandingType.getBidTypeName(userStanding.getFkIdStandingType()));
				statusInstrumento.setInstrumentId(userStanding.getFkIdValmerPriceVector());
				statusInstrumento.setCode(201);
				statusInstrumento.setMessage("La postura ha sido eliminada");
				statusInstrumento.setStatus("Exito");
				messagingTemplate.convertAndSendToUser(userName, "/market/canceled", statusInstrumento);
				if (stdStatus == StandingStatus.INMARKET) {
					MarketPosition inMarket = sortQueue(userStanding,
							CalendarUtil.zeroTimeCalendar(Calendar.getInstance()), false);
					if (inMarket != null) {
						MarketMessage mm = new MarketMessage(303, "CANCEL", null, inMarket);
						messagingTemplate.convertAndSend("/market/announce", mm);
					} else {
						MarketPosition tablePosition = new MarketPosition();
						tablePosition.setBiddingType(StandingType.getBidTypeName(userStanding.getFkIdStandingType()));
						MarketMessage mm = new MarketMessage(303, "CANCEL", null, tablePosition);
						messagingTemplate.convertAndSend("/market/announce", mm);
					}
				}

			}
		}
	}

	/**
	 * This method recieves messages from the clients and sends a standing if a new
	 * standing is available in the market. The channel is /BBBroker/position
	 *
	 * @return A {@code java.util.List} with {@link MarketPosition} objects.
	 */
	@Transactional
	@MessageMapping("/positions")
	@SendToUser("/user/market/announce")
	public List<TablePosition> doPostGetPositions() {

		log.info("[RESTTrader][doPostGetPositions]");

		return doGetMarketPositions();
	}

	// TODO Test cancel logic
	/*
	 * @Transactional(isolation = Isolation.READ_COMMITTED)
	 * 
	 * @MessageMapping("/cancelAggression")
	 * 
	 * @SendTo("/market/announce") public MarketMessage
	 * doCancelAggression(MarketPosition position, SimpMessageHeaderAccessor
	 * headerAccessor) { UserEntity aggressor =
	 * userRepo.findOneByUsername(headerAccessor.getUser().getName());
	 * MarketPosition marketPosition = aggressionHelper.cancelAggression(position,
	 * aggressor); log.
	 * info("[RESTTrader][MarkerMessege doCancelAggression]Se cancelo el agression"
	 * ); return new MarketMessage( 302,// An aggression was cancelled so unlock
	 * table "CANCELLED AGGRESSION", null, marketPosition ); }
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	@MessageMapping("/cancelAggression")
	@SendTo("/market/announce")
	public MarketMessage doCancelAggression(MarketPosition position, SimpMessageHeaderAccessor headerAccessor) {
		UserEntity aggressor = userRepo.findOneByUsername(headerAccessor.getUser().getName());
		MarketPosition marketPosition = aggressionHelper.cancelAggression(position, aggressor);
		log.info("[RESTTrader][MarkerMessege doSilenAggression]");
		return new MarketMessage(320, // An aggression was cancelled so unlock table
				"CANCEL AGGRESSION", null, marketPosition);
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	@MessageMapping("/getUserMarketPositionsDetail")
	@SendToUser("/market/user_positions_detail")
	public MarketPositionDetail getMarketPositionDetail(MarketPosition position,
			SimpMessageHeaderAccessor headerAccessor) {
		String userName = headerAccessor.getUser().getName();
		UserEntity user = userRepo.findOneByUsername(userName);
		Integer instrumentoId = position.getInstrumentId();
		String biddingType = position.getBiddingType();
		return mktDao.getCurrentMarketPositionsDetailForUser(instrumentoId, biddingType, user);
	}

	/**
	 * Notify all clients of a standing entity.
	 * 
	 * @param std
	 */
	private void notifyClients(StandingEntity std) {
		String destination = "/marketPositions";
		MarketPosition mp = new MarketPosition(std.getFkIdValmerPriceVector(), std.getValue(),
				std.getAmount() / calc_base, stdTypRepo.findOne(std.getFkIdStandingType()).getName(), std.getOrden());
		simpOp.convertAndSend(destination, mp);
	}

	// TODO add positions composition perhaps it can be set when sending the
	// information to the clients
	@Transactional(isolation = Isolation.SERIALIZABLE)
	private synchronized List<StandingEntity> sortQueueNew(StandingEntity standing, Calendar day) {

		log.info("[RESTTrader][sortQueueNew]");

		List<StandingEntity> dayStandings = null;
		if (standing.getFkIdStandingType() == StandingType.BIDID) {
			dayStandings = standingRepo
					.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual(
							standing.getFkIdValmerPriceVector(), day, StandingType.BIDID, StandingStatus.INMARKET);
		} else if (standing.getFkIdStandingType() == StandingType.OFFERID) {
			dayStandings = standingRepo
					.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual(
							standing.getFkIdValmerPriceVector(), day, StandingType.OFFERID, StandingStatus.INMARKET);
		}
		SBGeneralUtilities.sortQueueAndAdd(dayStandings, standing);

		// aquí guarda standing

		log.info("[RESTTrader][sortQueueNew] for dayStandings.size(): " + dayStandings.size());
		for (StandingEntity stading: dayStandings) {

			log.info("[RESTTrader][sortQueueNew] for stading.getAmount(): "
					+ stading.getAmount());
			log.info("[RESTTrader][sortQueueNew] for stading.getCurrentAmount(): "
					+ stading.getCurrentAmount());
			log.info("[RESTTrader][sortQueueNew] for stading.getFkIdStandingStatus(): "
					+ stading.getFkIdStandingStatus());
			log.info("[RESTTrader][sortQueueNew] for stading.getFkIdStandingType(): "
					+ stading.getFkIdStandingType());
			log.info("[RESTTrader][sortQueueNew] for stading.getFkIdUser(): "
					+ stading.getFkIdUser());
			log.info("[RESTTrader][sortQueueNew] for stading.getFkIdValmerPriceVector(): "
					+ stading.getFkIdValmerPriceVector());
			log.info("[RESTTrader][sortQueueNew] for stading.getIdStanding(): "
					+ stading.getIdStanding());
			log.info(
					"[RESTTrader][sortQueueNew] for stading.getValue(): " + stading.getValue());
			log.info("[RESTTrader][sortQueueNew] for stading.getStandingDirtyPrice(): "
					+ stading.getStandingDirtyPrice());
			log.info("[RESTTrader][sortQueueNew] for stading.getDatetime(): "
					+ stading.getDatetime().getTime().toString());
			
			if(stading.getOrden()==null) {
				Integer orden = standingRepo.getMaxOrden(stading.getFkIdValmerPriceVector());
				stading.setOrden(orden==null?0:orden+1);
			}
		}

		log.info("[RESTTrader][sortQueueNew] save standing (insert)"); //TODO_JUAN: MONTO AND BID
		standingRepo.save(dayStandings);

		return dayStandings;
	}

	private boolean isNewInMarket(List<StandingEntity> standings, StandingEntity standing) {
		boolean retVal = false;
		if (standings != null && standings.size() > 0) {
			if (standings.get(0).getValue() == standing.getValue()
					&& standings.get(0).getFkIdUser() == standing.getFkIdUser()) {
				retVal = true;
			}
		}
		return retVal;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	private synchronized MarketPosition sortQueue(StandingEntity standing, Calendar day, boolean addToQueue) {

		log.info("[RESTTrader][sortQueue]");

		List<StandingEntity> dayStandings = null;
		StandingEntity inMarketStanding = null;
		String bidType = null;
		if (standing.getFkIdStandingType() == StandingType.BIDID) {
			bidType = StandingType.BID;
			dayStandings = standingRepo
					.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual(
							standing.getFkIdValmerPriceVector(), day, StandingType.BIDID, StandingStatus.INMARKET);
			// TODO: check StandingStatus is valid or Rise to pending
		} else if (standing.getFkIdStandingType() == StandingType.OFFERID) {
			bidType = StandingType.OFFER;
			dayStandings = standingRepo
					.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual(
							standing.getFkIdValmerPriceVector(), day, StandingType.OFFERID, StandingStatus.INMARKET);
		}
		if (addToQueue) {
			dayStandings.add(standing);
		}

		if (bidType.equals(StandingType.OFFER))
			Collections.sort(dayStandings, Collections.reverseOrder());
		else
			Collections.sort(dayStandings);

		MarketPosition result = new MarketPosition();
		result.setBiddingType(bidType);
		result.setInstrumentId(standing.getFkIdValmerPriceVector());
		if (dayStandings.size() > 0) {
			inMarketStanding = dayStandings.get(0);
			inMarketStanding.setFkIdStandingStatus(StandingStatus.INMARKET);
			standingRepo.save(inMarketStanding);
			result.setAmount(inMarketStanding.getAmount() / calc_base);
			result.setRate(inMarketStanding.getValue());
			result.setInstrumentId(inMarketStanding.getFkIdValmerPriceVector());
		}
		return result;
	}
}
