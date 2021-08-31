package mx.sigmact.broker.dao;

import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.pojo.trader.BlockDetail;
import mx.sigmact.broker.pojo.trader.MarketPosition;
import mx.sigmact.broker.pojo.trader.MarketPositionDetail;

import java.util.HashMap;
import java.util.List;

/**
 * Created by norberto on 16/01/17.
 */
public interface MarketDao {
    /**
     * Get the hash map for the current standings in market
     * @param user The StandingEntity representing the user
     * @return
     */
    HashMap<Integer,List<StandingEntity>> getCurrentMarketStandingsForUser(UserEntity user);

    /**
     * Get the market positions to display for a user
     * @param user The StandingEntity representing the user
     * @return
     */
    List<MarketPosition> getCurrentMarketPositionsForUser(UserEntity user);

    /**
     * Get the positions for the user that are currently active
     * @param user The StandingEntity representing the user
     * @return
     */
    List<MarketPosition> getCurrentActiveUserPositions(UserEntity user);

    /**
     * Get the standings for the user that are currently active
     * @param user The StandingEntity representing the user
     * @return
     */
    List<StandingEntity> getCurrentActiveUserStandings(UserEntity user);
    /**
     * Get the hash map for the current standings in market
     * @param user The String representing the user
     * @return
     */
    HashMap<Integer,List<StandingEntity>> getCurrentMarketStandingsForUser(String user);
    /**
     * Get the market positions to display for a user
     * @param user The String representing the user
     * @return
     */
    List<MarketPosition> getCurrentMarketPositionsForUser(String user);
    /**
     * Get the positions for the user that are currently active
     * @param user The String representing the user
     * @return
     */
    List<MarketPosition> getCurrentActiveUserPositions(String user);
    /**
     * Get the standings for the user that are currently active
     * @param user The String representing the user
     * @return
     */
    List<StandingEntity> getCurrentActiveUserStandings(String user);
    /**
     * Get the market positions to display for a user
     * @param instrumentoId ID for position in market
     * @param user The StandingEntity representing the user
     * @return
     */
    MarketPositionDetail getCurrentMarketPositionsDetailForUser(Integer instrumentoId,String biddingType,UserEntity user);
    List<BlockDetail> getBlockDetailByPosition(Integer instrumentoId, String biddingType, UserEntity user);
}
