package mx.sigmact.broker.dao;

import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.core.util.CalendarUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class JdbcUtilDao implements UtilDao {
	
	Logger log = Logger.getLogger(JdbcUtilDao.class);

    @Resource
    private DataSource dataSource;
    @Value("${query.trader.today}")
    private String sqlTodayDate;
    @Value("${query.trader.latest_valmer}")
    private String sqlLatestValmer;
    @Resource
    private DaoHelper daoHelper;
    @Value("${sql.update.sysdate}")
    private String sqlUpdateSysdate;
    @Value("${query.trader.liqDay}")
    private String sqlLiqDay;
    @Value("${query.trader.liqDay1}")
    private String sqlLiqDay1;
    @Value("${query.trader.DiaAnt}")
    private String sqlDiaAnt;
    @Value("${query.trader.DiaHabil}")
    private String sqlDiaHabil;
    
    @Override
    public Calendar today(){
        Date fechaValmer = null;
        Calendar c = Calendar.getInstance();
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
        if(fechaValmer!= null) c.setTime(fechaValmer);
        return CalendarUtil.zeroTimeCalendar(c);
    }

    @Override
    public Calendar todayWithTime(){
        Date fechaValmer = null;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int seg = c.get(Calendar.SECOND);
        int mseg = c.get(Calendar.MILLISECOND);
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
        if(fechaValmer!= null){
            c.setTime(fechaValmer);
            c.set(Calendar.HOUR, hour);
            c.set(Calendar.MINUTE, min);
            c.set(Calendar.SECOND, seg);
            c.set(Calendar.MILLISECOND, mseg);
        }
        return c;
    }
    @Override
    public Calendar valmer_date(){
        Date fechaValmer = null;
        Calendar c = Calendar.getInstance();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVar(
                     con, sqlLatestValmer
             );
             ResultSet rs = ps.executeQuery()) {
            if(rs.next()){
                fechaValmer = rs.getDate(1);
            }
			con.close();
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
        }
        if(fechaValmer!= null) c.setTime(fechaValmer);
        return CalendarUtil.zeroTimeCalendar(c);
    }
    @Override
    public int updateSystemDate(Date newDate){
        int rows = 0;
        try {
            Connection con = dataSource.getConnection();
            PreparedStatement ps = daoHelper.createPreparedStatementVar(con, sqlUpdateSysdate,newDate);
             rows = ps.executeUpdate();
 			con.close();
        } catch (Exception e) {
            log.error("General error: " + e.getMessage());
        }
        return rows;
    }
    @Override
    public Calendar liquidation_day(){
        Date liqDay = null;
        Calendar c = Calendar.getInstance();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVar(
                     con, sqlLiqDay
             );
             ResultSet rs = ps.executeQuery()) {
            if(rs.next()){
                liqDay = rs.getDate(1);
            }
			con.close();
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
        }
        if(liqDay!= null) c.setTime(liqDay);
        return CalendarUtil.zeroTimeCalendar(c);
    }
    
    @Override
    public Calendar liquidation_day1(){
        Date liqDay = null;
        Calendar c = Calendar.getInstance();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVar(
                     con, sqlLiqDay1
             );
             ResultSet rs = ps.executeQuery()) {
            if(rs.next()){
                liqDay = rs.getDate(1);
            }
			con.close();
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
        }
        if(liqDay!= null) c.setTime(liqDay);
        return CalendarUtil.zeroTimeCalendar(c);
    }
    
    @Override
    public Calendar Dia_Ant(String newDate,  Integer NuDays){
    	
    	log.info("[JdbcUtilDao][Dia_Ant]");
    	log.info("[JdbcUtilDao][Dia_Ant] newDate: " + newDate);
    	log.info("[JdbcUtilDao][Dia_Ant] NuDays: " + NuDays);
    	
        String DiaAnt = null;
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = daoHelper.createPreparedStatementVar(
                     con, sqlDiaAnt, newDate,NuDays
             );
             ResultSet rs = ps.executeQuery()) {
            if(rs.next()){
	        	log.info("[JdbcUtilDao][Dia_Habil] rs.getString(1): " + rs.getString(1));
	        	
                DiaAnt = rs.getString(1);
            }
			con.close();
        } catch (SQLException e) {
            log.error("[JdbcUtilDao][Dia_Ant]  error: " + e.getMessage());
        }
        
    	
        log.info("[JdbcUtilDao][Dia_Habil] DiaAnt: " + DiaAnt);
    	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
		try {
			date = sdf. parse(DiaAnt);

	        log.info("[JdbcUtilDao][Dia_Habil] date: " + date.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Calendar cal = Calendar.getInstance();
        cal. setTime(date);

        log.info("[JdbcUtilDao][Dia_Habil] cal: " + cal.getTime().toString());
        
        return cal;
    }
    @Override
    public int Dia_Habil(String newDate){
    	
    	log.info("[JdbcUtilDao][Dia_Habil]");
    	log.info("[JdbcUtilDao][Dia_Habil] newDate: " + newDate);

        int diaHabil = 0;
        Connection con = null;
        
        try {
        	
            con = dataSource.getConnection();
            PreparedStatement ps = daoHelper.createPreparedStatementVar(con, sqlDiaHabil, newDate);
            ResultSet rs = ps.executeQuery();
	        if(rs.next()){
	        	log.info("[JdbcUtilDao][Dia_Habil] rs.getInt(1): " + rs.getInt(1));
	        	diaHabil = rs.getInt(1);
	        }
        	
        	con.close();
            
        } catch (Exception e) {
            log.error("[JdbcUtilDao][Dia_Habil] General error: " + e.getMessage());
        }
        
        return diaHabil;
    }
}
