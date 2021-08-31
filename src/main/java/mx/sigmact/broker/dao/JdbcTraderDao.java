package mx.sigmact.broker.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.pojo.DatePoint;
import mx.sigmact.broker.pojo.TraderActivityView;
import mx.sigmact.broker.pojo.TraderInstrumentView;
import mx.sigmact.broker.pojo.TraderTicketView;
import mx.sigmact.broker.pojo.parameter.ValueParameter;

/**
 * Created on 09/12/16.
 */

@Service
public class JdbcTraderDao implements TraderDao {

	Logger log = Logger.getLogger(JdbcTraderDao.class);

	@Resource
	private ParameterDao parameterDao;

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

    @Value("${query.trader.today}")
    String sqlTodayDate;

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
                        rs.getString(3),
                        rs.getDouble(4),
                        rs.getDouble("Nu_Rango")
                ));
            }
			con.close();
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
            log.error("Localized error: " + e.getLocalizedMessage());
        }
        return list;
    }

    @Override
    public List<TraderTicketView> getTraderTickets(Integer userId, Calendar calendar) {
        List<TraderTicketView> list = new ArrayList<>();

		log.info("[JdbcTraderDao][getTraderTickets]");


		log.info("[JdbcTraderDao][getTraderTickets] userId: " + userId);
		log.info("[JdbcTraderDao][getTraderTickets] calendar: " + calendar.getTime());
		
        //calendar.setTime(getValmerDate());

		ValueParameter valueParameter = parameterDao.getParameter("today");

        String todayDate = daoHelper.getTodayDate(calendar);
        
		log.info("[JdbcTraderDao][getTraderTickets] todayDate daoHelper.getTodayDate(): " + todayDate.toString());
		
        todayDate = valueParameter.getValue();

		log.info("[JdbcTraderDao][getTraderTickets] todayDate valueParameter.getValue(): " + todayDate.toString());
        
        //# UID UID UID UID UID UID UID DATE UID UID
        try (Connection con = dataSource.getConnection();
        		PreparedStatement ps = daoHelper.createPreparedStatementVar(con,
                        sqlTraderTickets,
                        todayDate,
                        userId);
             ResultSet rs = ps.executeQuery()) {
            /*
            String fecha
            String instrumento
            String operacion
            String contraparte
            Long titulos
            Double tasaNegociada
            Double tasaComision
            Double tasaAgredida
            Double precio
            Double monto
            */
            while (rs.next()) {

            	
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getString(1): " + rs.getString(1));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getString(2): " + rs.getString(2));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getString(3): " + rs.getString(3));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getString(4): " + rs.getString(4));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getLong(5): " + rs.getLong(5));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getDouble(8): " + rs.getDouble(6));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getDouble(7): " + rs.getDouble(7));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getDouble(6): " + rs.getDouble(8));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getDouble(9): " + rs.getDouble(9));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getDouble(10): " + rs.getDouble(10));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getLong(11): " + rs.getLong(11));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getString(12): " + rs.getString(12));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getString(13): " + rs.getString(13));
        		log.info("[JdbcTraderDao][getTraderTickets] rs.getLong(14): " + rs.getLong(14));
            	
            	
                list.add(new TraderTicketView(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getLong(5),
                        rs.getDouble(6),
                        rs.getDouble(7),
                        rs.getDouble(8),
                        rs.getDouble(9),
                        rs.getDouble(10),
                        rs.getLong(11),
                        rs.getString(12),
                        rs.getString(13),
                        rs.getLong(14)
                ));
            }
			con.close();
        } catch (SQLException e) {
            log.error("[JdbcTraderDao][getTraderTickets] General error: " + e.getMessage());
            log.error("[JdbcTraderDao][getTraderTickets] Localized error: " + e.getLocalizedMessage());
        }
        return list;
    }

    @Override
    public List<TraderActivityView> getTraderActivity(Integer userId, Calendar calendar) {

		log.info("[JdbcTraderDao][getTraderActivity]");


		log.info("[JdbcTraderDao][getTraderActivity] userId: " + userId);
		log.info("[JdbcTraderDao][getTraderActivity] calendar: " + calendar.getTime());
		
        List<TraderActivityView> list = new ArrayList<>();
        String todayDate = daoHelper.getTodayDate(calendar);
        
        ValueParameter valueParameter = parameterDao.getParameter("today");
		
        todayDate = valueParameter.getValue();
        
		log.info("[JdbcTraderDao][getTraderActivity] todayDate: " + todayDate.toString());
		
		
		
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVar(con,
                     sqlTraderActivity,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
                     userId,
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
            	

        		log.info("[JdbcTraderDao][getTraderActivity] rs.getString(1): " + rs.getString(1));
        		log.info("[JdbcTraderDao][getTraderActivity] rs.getString(2): " + rs.getString(1));
        		log.info("[JdbcTraderDao][getTraderActivity] rs.getString(12): " + rs.getString(12));
        		log.info("[JdbcTraderDao][getTraderActivity] rs.getString(13): " + rs.getString(13));
            	            	
                list.add(new TraderActivityView(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(12),
                        rs.getDouble(13) ));
            }
			con.close();
        } catch (SQLException e) {
            log.error("[JdbcTraderDao][getTraderActivity] General error: " + e.getMessage());
            log.error("[JdbcTraderDao][getTraderActivity] Localized error: " + e.getLocalizedMessage());
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
			con.close();
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
                     sqlTraderGraphOne, instrumentIdentifier);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new DatePoint(
                        rs.getString(1),
                        rs.getDouble(2)
                ));
            }
			con.close();
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
            log.error("Localized error: " + e.getLocalizedMessage());
        }
        return list;
    }

    @Override
    public List<String> getCommonWorkbench(Integer aggressor, Integer bidder, Integer idInstrument) {

		log.info("[JdbcTraderDao][getCommonWorkbench]");
		log.info("[JdbcTraderDao][getCommonWorkbench] aggressor: " + aggressor);
		log.info("[JdbcTraderDao][getCommonWorkbench] bidder: " + bidder);
		log.info("[JdbcTraderDao][getCommonWorkbench] idInstrument: " + idInstrument);
		

		log.info("[JdbcTraderDao][getCommonWorkbench] sql: " + sqlTraderWorkbench);
		
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
        	

    		log.info("[JdbcTraderDao][getCommonWorkbench] 1");
        	
            if(rs.next()){

        		log.info("[JdbcTraderDao][getCommonWorkbench] rs.getString(1): " + rs.getString(1));
        		log.info("[JdbcTraderDao][getCommonWorkbench] rs.getString(2): " + rs.getString(2));
            	
                list = new ArrayList(2);
                list.add(rs.getString(1));
                list.add(rs.getString(2));
            }
			con.close();
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
        }
        return list;
    }

    public Date getToday() {
        Date fechaValmer = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVar(
                     con, sqlTodayDate
             );
             ResultSet rs = ps.executeQuery()) {
            if(rs.next()){
                fechaValmer = rs.getDate(1);
            }
			con.close();
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
        }
        return fechaValmer;
    }
}
