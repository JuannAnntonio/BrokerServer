package mx.sigmact.broker.dao;

import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.core.util.SBGeneralUtilities;
import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.pojo.StandingStatus;
import mx.sigmact.broker.pojo.StandingType;
import mx.sigmact.broker.pojo.TraderInstrumentView;
import mx.sigmact.broker.pojo.trader.MarketPosition;
import mx.sigmact.broker.repositories.BrokerStandingRepository;
import mx.sigmact.broker.repositories.BrokerUserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by norberto on 16/01/17.
 */
@Service
public class SpringDataMarketDaoImplementation implements MarketDao {

    @Resource
    TraderDao traderDao;

    @Resource
    BrokerStandingRepository stdRepo;

    @Resource
    BrokerUserRepository userRepo;

    @Override
    public HashMap<Integer, List<StandingEntity>> getCurrentMarketStandingsForUser(UserEntity user) {
        List<TraderInstrumentView> traderInstruments = traderDao.getTraderInstruments(user.getIdUser());
        List<StandingEntity> standings = stdRepo.findByFkIdStandingStatus(StandingStatus.INMARKET);
        filterStandingsByInstruments(traderInstruments, standings);
        return generateMap(standings);
    }

    @Override
    public List<MarketPosition> getCurrentMarketPositionsForUser(UserEntity user) {
        HashMap<Integer, List<StandingEntity>> marketStandings = getCurrentMarketStandingsForUser(user);
        return generateMarketList(marketStandings);
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
                    standing.getAmount()/1000,
                    null
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
    private List<MarketPosition> mergeStandingsInPosition(Integer idInstrument, List<StandingEntity> list) {
        Integer lAmountBid = 0;
        Double lRateBid = 0.0;
        Integer lAmountOffer = 0;
        Double lRateOffer = 0.0;
        for (StandingEntity std : list) {
            String lBiddingType = SBGeneralUtilities.getStandingTypeNameFromId(std.getFkIdStandingType());
            if (std.getFkIdStandingType() == StandingType.BIDID){
                lRateBid = std.getValue();
                lAmountBid += std.getAmount();
            }else if (std.getFkIdStandingType() == StandingType.OFFERID){
                lRateOffer = std.getValue();
                lAmountOffer += std.getAmount();
            }
        }
        List<MarketPosition> mp = new ArrayList<>();
        if (lAmountBid > 0) {
            mp.add(new MarketPosition(idInstrument, lRateBid, lAmountBid/1000, StandingType.BID));
        }
        if (lAmountOffer > 0) {
            mp.add(new MarketPosition(idInstrument, lRateOffer, lAmountOffer/1000, StandingType.OFFER));
        }
        return mp;
    }

    /**
     * Creates market positions from a map of standings.
     *
     * @param standings
     * @return
     */
    private List<MarketPosition> generateMarketList(HashMap<Integer, List<StandingEntity>> standings) {
        List<MarketPosition> retVal = new ArrayList<>();
        for (Map.Entry<Integer, List<StandingEntity>> entry : standings.entrySet()) {
            Integer key = entry.getKey();
            List<StandingEntity> list = entry.getValue();
            retVal.addAll(mergeStandingsInPosition(key, list));
        }
        return retVal;
    }
}
