package mx.sigmact.broker.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.model.ValmerPriceVectorEntity;
import mx.sigmact.broker.pojo.MarketPositionUser;
import mx.sigmact.broker.pojo.StandingStatus;
import mx.sigmact.broker.pojo.StandingType;
import mx.sigmact.broker.pojo.TraderInstrumentView;
import mx.sigmact.broker.pojo.instrument.Instrument;
import mx.sigmact.broker.pojo.trader.BlockDetail;
import mx.sigmact.broker.pojo.trader.MarketPosition;
import mx.sigmact.broker.pojo.trader.MarketPositionDetail;
import mx.sigmact.broker.repositories.BrokerStandingRepository;
import mx.sigmact.broker.repositories.BrokerUserRepository;
import mx.sigmact.broker.repositories.BrokerValmerPriceVectorRepository;

/**
 * Created by norberto on 16/01/17.
 */
@Service
public class SpringDataMarketDaoImplementation implements MarketDao {
	Logger log = Logger.getLogger(SpringDataMarketDaoImplementation.class);

    @Resource
    private UtilDao utilDao;
    
    @Resource
    BrokerValmerPriceVectorRepository dao;

    @Resource
    TraderDao traderDao;

    @Resource
    BrokerStandingRepository stdRepo;

    @Resource
    BrokerUserRepository userRepo;
    
    @Resource
    private InstrumentDao instrumentDao;
    
    private Instrument instrument = null;

    @Value("${calc_base}")
    Integer calc_base;
    
    @Resource
    private StandingDAO standingDAO;

    @Override
    public HashMap<Integer, List<StandingEntity>> getCurrentMarketStandingsForUser(UserEntity user) {
        Calendar day = utilDao.today(); //CalendarUtil.zeroTimeCalendar(Calendar.getInstance());
        List<TraderInstrumentView> traderInstruments = traderDao.getTraderInstruments(user.getIdUser());
        //List<StandingEntity> standings = stdRepo.findByFkIdStandingStatus(StandingStatus.INMARKET);
        List<StandingEntity> standings = stdRepo.findByFkIdStandingStatusAndDatetimeGreaterThanEqual(StandingStatus.INMARKET,day);
        filterStandingsByInstruments(traderInstruments, standings);
        return generateMap(standings);
    }

	@Override
	public List<MarketPosition> getCurrentMarketPositionsForUser(UserEntity user) {
		HashMap<Integer, List<StandingEntity>> marketStandings = getCurrentMarketStandingsForUser(user);
		return getSameInstitutions(user, generateMarketList(marketStandings, user));
	}

	private List<MarketPosition> getSameInstitutions(UserEntity user, List<MarketPosition> marketsPositions) {
		Set<Integer> idsIntruments = new HashSet<>();
		marketsPositions.forEach(mp -> idsIntruments.add(mp.getInstrumentId()));
		List<MarketPositionUser> marketsUser = standingDAO.findByIdStandingStatusAndIdsInstruments(idsIntruments);

		marketsPositions.forEach(mp -> marketsUser.stream()
				.filter(mpu -> mpu.getIdValmerPriceVector().equals(mp.getInstrumentId())
						&& mpu.getIdStandingType().equals(StandingType.getBidTypeId(mp.getBiddingType())))
				.forEach(mpu -> {
					mp.setSameInstitution(mpu.getIdInstitution().equals(user.getFkIdInstitution())
							&& !mpu.getIdUser().equals(user.getIdUser()) );
				})
		);

		return marketsPositions;
	}
  

    @Override
    public List<MarketPosition> getCurrentActiveUserPositions(UserEntity user) {
        List<StandingEntity> standings = getCurrentActiveUserStandings(user);
        return changeStandingToPosition(standings);
    }

    @Override
    public List<StandingEntity> getCurrentActiveUserStandings(UserEntity user) {
        return stdRepo.findByFkIdUserAndFkIdStandingStatusLessThanEqualAndDatetimeGreaterThanEqual(user.getIdUser(), StandingStatus.AGGRESSED, CalendarUtil.zeroTimeCalendar(Calendar.getInstance()));
    }

    @Override
    public HashMap<Integer, List<StandingEntity>> getCurrentMarketStandingsForUser(String user) {
        UserEntity lUser = userRepo.findOneByUsername(user);
        return getCurrentMarketStandingsForUser(lUser);
    }

    @Override
    public List<MarketPosition> getCurrentMarketPositionsForUser(String user) {
        UserEntity lUser = userRepo.findOneByUsername(user);
        return getCurrentMarketPositionsForUser(lUser);
    }

    @Override
    public List<MarketPosition> getCurrentActiveUserPositions(String user) {
        UserEntity lUser = userRepo.findOneByUsername(user);
        return getCurrentActiveUserPositions(lUser);
    }

    @Override
    public List<StandingEntity> getCurrentActiveUserStandings(String user) {
        UserEntity lUser = userRepo.findOneByUsername(user);
        return getCurrentActiveUserStandings(lUser);
    }

    @Override
    public MarketPositionDetail getCurrentMarketPositionsDetailForUser(Integer instrumentoId,String biddingType, UserEntity user) {
        return generateMarketDetailList(instrumentoId, biddingType,user);
    }

    @Override
    public List<BlockDetail> getBlockDetailByPosition(Integer instrumentoId, String biddingType, UserEntity user){
        return generateBlockDetailList(instrumentoId,biddingType,user);
    }
    /**
     * Change a StandingEntity to a MarketPosition.
     *
     * @param standings The standing to change to a MarketPosition
     * @return Returns a MarketPosition with the values of the StandingEntity.
     */
    private List<MarketPosition> changeStandingToPosition(List<StandingEntity> standings) {
        List<MarketPosition> list = new ArrayList<>(standings.size());
        for (StandingEntity standing : standings) {
            MarketPosition pos = new MarketPosition(
                    standing.getFkIdValmerPriceVector(),
                    standing.getValue(),
                    standing.getAmount()/calc_base,
                    null,standing.getOrden()
            );
            if (standing.getFkIdStandingType() == StandingType.BIDID) {
                pos.setBiddingType(StandingType.BID);
            } else if (standing.getFkIdStandingType() == StandingType.OFFERID) {
                pos.setBiddingType(StandingType.OFFER);
            }
            list.add(pos);
        }
        return list;
    }

    /**
     * Creates a hash map that contains list of standings.
     *
     * @param standings A List of standings to create the hash map
     * @return A HashMap with integer keys and list of standings as values.
     */
    private HashMap<Integer, List<StandingEntity>> generateMap(List<StandingEntity> standings) {
        HashMap<Integer, List<StandingEntity>> map = new HashMap<>();
        for (StandingEntity std : standings) {
            List<StandingEntity> listStd = map.get(std.getFkIdValmerPriceVector());
            if (listStd == null) {
                List<StandingEntity> list = new ArrayList<>();
                list.add(std);
                map.put(std.getFkIdValmerPriceVector(), list);
            } else {
                listStd.add(std);
                map.put(std.getFkIdValmerPriceVector(), listStd);
            }
        }
        return map;

    }

    /**
     * Filters the standings by the trader instruments.
     *
     * @param traderInstruments
     * @param standings
     * @return
     */
    private List<StandingEntity> filterStandingsByInstruments(List<TraderInstrumentView> traderInstruments, List<StandingEntity> standings) {
        for (int i = 0; i < standings.size(); i++) {
            StandingEntity std = standings.get(i);
            if (!foundStandingInInstruments(std, traderInstruments)) {
                standings.remove(i);
                i--;
            }
        }
        return standings;
    }

    private boolean foundStandingInInstruments(StandingEntity std, List<TraderInstrumentView> traderInstruments) {
        boolean retVal = false;
        for (TraderInstrumentView inst : traderInstruments) {
            if (inst.getIdVPV() == std.getFkIdValmerPriceVector()) {
                retVal = true;
                break;
            }
        }
        return retVal;
    }

    /**
     * Merges a list of standings in a market positions.
     *
     * @param list A list of StandingEntity to be added and merged in a MarketPosition.
     * @return Returns a merket position with the added amount of each standing
     */
    private List<MarketPosition> mergeStandingsInPosition(Integer idInstrument, List<StandingEntity> list,UserEntity user) {
        Integer lAmountBid = 0;
        Double lRateBid = 0.0;
        Integer lAmountOffer = 0;
        Double lRateOffer = 0.0;
        Integer orden = 0;

        for (StandingEntity std : list) {
            if (std.getFkIdStandingType() == StandingType.BIDID){
                lRateBid = std.getValue();
                lAmountBid += std.getAmount();
            }else if (std.getFkIdStandingType() == StandingType.OFFERID){
                lRateOffer = std.getValue();
                lAmountOffer += std.getAmount();
            }
            orden = std.getOrden();
        }
        List<MarketPosition> mp = new ArrayList<>();
		if (lAmountBid > 0) {
			MarketPositionDetail marketPositionDetailBid = generateMarketDetailList(idInstrument, StandingType.BID,
					user);
			mp.add(new MarketPosition(idInstrument, lRateBid, lAmountBid / calc_base, StandingType.BID,
					marketPositionDetailBid.getBlockDetailList(), orden));
		}
		if (lAmountOffer > 0) {
			MarketPositionDetail marketPositionDetailOffer = generateMarketDetailList(idInstrument, StandingType.OFFER,
					user);
			mp.add(new MarketPosition(idInstrument, lRateOffer, lAmountOffer / calc_base, StandingType.OFFER,
					marketPositionDetailOffer.getBlockDetailList(),orden));
		}
		return mp;
    }

    /**
     * Creates market positions from a map of standings.
     *
     * @param standings
     * @return
     */
    private List<MarketPosition> generateMarketList(HashMap<Integer, List<StandingEntity>> standings,UserEntity user) {
        List<MarketPosition> retVal = new ArrayList<>();
        for (Map.Entry<Integer, List<StandingEntity>> entry : standings.entrySet()) {
            Integer key = entry.getKey();
            List<StandingEntity> list = entry.getValue();
            retVal.addAll(mergeStandingsInPosition(key, list,user));
        }
        return retVal;
    }


    private MarketPositionDetail generateMarketDetailList(Integer instrumentId,String biddingType, UserEntity user){
        Calendar day = utilDao.today(); //CalendarUtil.zeroTimeCalendar(Calendar.getInstance());
        List<StandingEntity> list = stdRepo.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual
                (instrumentId,day,StandingType.getBidTypeId(biddingType),StandingStatus.INMARKET);
        if(biddingType.equals(StandingType.OFFER))
            Collections.sort(list,Collections.reverseOrder());
        else
            Collections.sort(list);
        Map<Integer, List<String>> workbenchesForStandings = getWorkbenchesForStandings(user.getFkIdInstitution(), list);
        Iterator it = workbenchesForStandings.entrySet().iterator();
        boolean error = false;
        List<BlockDetail> blockDetailList = new ArrayList<>();

        while (it.hasNext()) {//TODO all the transactions have a common workbench
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue() == null) {
                error = true;
            }
        }
        if(!error) {
            for (StandingEntity std : list) {
                List<String> wb = workbenchesForStandings.get(std.getIdStanding());
                BlockDetail blockDetail = new BlockDetail(std.getValue(), std.getAmount(), Double.parseDouble(wb.get(1)));
                if (StandingType.getBidTypeName(std.getFkIdStandingType()).equalsIgnoreCase(biddingType)) {
                    blockDetailList.add(blockDetail);
                } else continue;
            }
            return new MarketPositionDetail(instrumentId, biddingType, blockDetailList, "OK", "");
        }else
            return new MarketPositionDetail(instrumentId, biddingType, blockDetailList, "ERROR", "No se pudo obtener el workbeanch.");

    }

    private Map<Integer, List<String>> getWorkbenchesForStandings(Integer instA, List<StandingEntity> standings) {

		log.info("[SpringDataMarketDaoImplementation][getWorkbenchesForStandings]");
    	
        HashMap<Integer, List<String>> map = new HashMap<>();
        for (StandingEntity std : standings) {
            UserEntity user = userRepo.findOne(std.getFkIdUser());
            int stdInst = user.getFkIdInstitution();
            int idInst = std.getFkIdValmerPriceVector();
            int stdTyp = std.getFkIdStandingType();

            ValmerPriceVectorEntity priceVectorData = dao.findOne(std.getFkIdValmerPriceVector());
            
    		log.info("[SpringDataMarketDaoImplementation][getWorkbenchesForStandings] stdInst: " + stdInst);
    		log.info("[SpringDataMarketDaoImplementation][getWorkbenchesForStandings] idInst: " + idInst);	
    		log.info("[SpringDataMarketDaoImplementation][getWorkbenchesForStandings] stdTyp: " + stdTyp);
    		
    		this.instrument = instrumentDao.getInstrument(priceVectorData.getTv());
    		
    		log.info("[SpringDataMarketDaoImplementation][getWorkbenchesForStandings] this.instrument.getId_instrument(): " + this.instrument.getId_instrument());
    		
            
            map.put(std.getIdStanding(), getCommonWorkBench(instA, stdInst, this.instrument.getId_instrument()));
        }
        return map;
    }

    private List<String> getCommonWorkBench(int aggressor, int bidder, int idInstrument) {
        return traderDao.getCommonWorkbench(aggressor, bidder, idInstrument);
    }
    private List<BlockDetail> generateBlockDetailList(Integer instrumentId,String biddingType, UserEntity user){

		log.info("[SpringDataMarketDaoImplementation][generateBlockDetailList]");
		
		log.info("[SpringDataMarketDaoImplementation][generateBlockDetailList] instrumentId: " + instrumentId);
    	
        Calendar day = utilDao.today(); //CalendarUtil.zeroTimeCalendar(Calendar.getInstance());
        List<StandingEntity> list = stdRepo.findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual
                (instrumentId,day,StandingType.getBidTypeId(biddingType),StandingStatus.INMARKET);
        if(biddingType.equals(StandingType.OFFER))
            Collections.sort(list,Collections.reverseOrder());
        else
            Collections.sort(list);
        Map<Integer, List<String>> workbenchesForStandings = getWorkbenchesForStandings(user.getFkIdInstitution(), list);
        Iterator it = workbenchesForStandings.entrySet().iterator();
        boolean error = false;
        List<BlockDetail> blockDetailList = new ArrayList<>();

        while (it.hasNext()) {//TODO all the transactions have a common workbench
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue() == null) {
                error = true;
            }
        }
        if(!error) {
            for (StandingEntity std : list) {
                List<String> wb = workbenchesForStandings.get(std.getIdStanding());
                BlockDetail blockDetail = new BlockDetail(std.getValue(), std.getAmount(), Double.parseDouble(wb.get(1)));
                if (StandingType.getBidTypeName(std.getFkIdStandingType()).equalsIgnoreCase(biddingType)) {
                    blockDetailList.add(blockDetail);
                } else continue;
            }
            return blockDetailList;
        }else
            return blockDetailList;

    }
}
