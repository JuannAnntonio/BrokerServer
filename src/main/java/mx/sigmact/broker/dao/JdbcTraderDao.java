package mx.sigmact.broker.dao;

import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.pojo.DatePoint;
import mx.sigmact.broker.pojo.TraderActivityView;
import mx.sigmact.broker.pojo.TraderInstrumentView;
import mx.sigmact.broker.pojo.TraderTicketView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created on 09/12/16.
 */

@Service
public class JdbcTraderDao implements TraderDao {


    @Resource
    DataSource dataSource;

    @Resource
    DaoHelper daoHelper;

    @Value("${query.trader.instruments}")
    String sqlTraderInstruments;

    @Value("${query.trader.tickets}")
    String sqlTraderTickets;

    @Value("${query.trader.activity}")
    String sqlTraderActivity;

    @Value("${query.trader.market}")
    String sqlTraderMarketPositions;

    @Value("${query.trader.graph}")
    String sqlTraderGraph;

    @Value("${query.trader.graph.one}")
    String sqlTraderGraphOne;

    @Value("${query.trader.workbench}")
    String sqlTraderWorkbench;


    private static final Logger log = LoggerFactory.getLogger(JdbcAdminDao.class);

    @Override
    public List<TraderInstrumentView> getTraderInstruments(Integer userId) {
        List<TraderInstrumentView> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVarInt(con, sqlTraderInstruments, userId);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TraderInstrumentView(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3)
                ));
            }
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
            log.error("Localized error: " + e.getLocalizedMessage());
        }
        return list;
    }

    @Override
    public List<TraderTicketView> getTraderTickets(Integer userId, Calendar calendar) {
        List<TraderTicketView> list = new ArrayList<>();
        String todayDate = daoHelper.getTodayDate(calendar);
        //# UID UID UID UID UID UID UID DATE UID UID
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVar(con,
                     sqlTraderTickets,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     todayDate,
                     userId,
                     userId);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TraderTicketView(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDouble(5),
                        rs.getLong(6),
                        rs.getDouble(7),
                        rs.getLong(8),
                        rs.getDouble(9)
                ));
            }
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
            log.error("Localized error: " + e.getLocalizedMessage());
        }
        return list;
    }

    @Override
    public List<TraderActivityView> getTraderActivity(Integer userId, Calendar calendar) {
        List<TraderActivityView> list = new ArrayList<>();
        String todayDate = daoHelper.getTodayDate(calendar);
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVar(con,
                     sqlTraderActivity,
                     userId,
                     userId,
                     userId,
                     todayDate,
                     userId,
                     userId);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new TraderActivityView(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDouble(4) ));
            }
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
            log.error("Localized error: " + e.getLocalizedMessage());
        }
        return list;
    }

    @Override
    public List<DatePoint> getGraphDataPoints(Integer days, String instrumentIdentifier) {
        List<DatePoint> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVar(con,
                     sqlTraderGraph, days, instrumentIdentifier);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DatePoint(
                        rs.getString(1),
                        rs.getDouble(2)
                ));
            }
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
            log.error("Localized error: " + e.getLocalizedMessage());
        }
        return list;
    }

    @Override
    public List<DatePoint> getGraphDataPointsCurrentDay(String instrumentIdentifier) {
        List<DatePoint> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVar(con,
                     sqlTraderGraph, instrumentIdentifier);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DatePoint(
                        rs.getString(1),
                        rs.getDouble(2)
                ));
            }
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
            log.error("Localized error: " + e.getLocalizedMessage());
        }
        return list;
    }

    @Override
    public List<String> getCommonWorkbench(Integer aggressor, Integer bidder, Integer idInstrument) {
        List<String> list = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVar(
                     con, sqlTraderWorkbench,
                     aggressor,
                     bidder,
                     idInstrument,
                     aggressor,
                     bidder,
                     aggressor,
                     idInstrument,
                     aggressor,
                     bidder
             );
             ResultSet rs = ps.executeQuery()) {
            if(rs.next()){
                list = new ArrayList(2);
                list.add(rs.getString(1));
                list.add(rs.getString(2));
            }
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
        }
        return list;
    }
}
