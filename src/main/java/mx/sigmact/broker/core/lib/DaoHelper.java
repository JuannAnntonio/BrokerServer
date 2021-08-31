package mx.sigmact.broker.core.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

/**
 * Created on 09/12/16.
 */

public class DaoHelper {

	Logger log = Logger.getLogger(DaoHelper.class);

    @Resource
    SimpleDateFormat formatTimeStamp;

    @Resource
    SimpleDateFormat formatDate;

    public PreparedStatement createPreparedStatement(Connection con, String sql) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        return ps;
    }

    public PreparedStatement createPreparedStatementVars(Connection con, String sql, String... vars) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        if (vars != null) {
            for (int i = 0; i < vars.length; i++) {
                ps.setString(i + 1, vars[i]);
            }
        }
        return ps;
    }

    public PreparedStatement createPreparedStatementVarInt(Connection con, String sql, Integer... vars) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        if (vars != null) {
            for (int i = 0; i < vars.length; i++) {
                ps.setInt(i + 1, vars[i]);
            }
        }
        return ps;
    }

    public PreparedStatement createPreparedStatementVar(Connection con, String sql, Object... vars) throws SQLException {
    	
    	log.info("[DaoHelper][createPreparedStatementVar]");
    	
        PreparedStatement ps = con.prepareStatement(sql);
        if (vars != null) {
            for (int i = 0; i < vars.length; i++) {
                Object obj = vars[i];
                if(obj instanceof String){
                	log.info("[DaoHelper][createPreparedStatementVar] String");
                	
                    ps.setString(i+1,(String) obj);
                } else if(obj instanceof  Integer){
                	log.info("[DaoHelper][createPreparedStatementVar] Integer");
                	
                    ps.setInt(i+1,(Integer) obj);
                } else if(obj instanceof Double){
                	log.info("[DaoHelper][createPreparedStatementVar] Double");
                	
                    ps.setDouble(i+1, (Double) obj);
                } else if(obj instanceof  Long){
                	log.info("[DaoHelper][createPreparedStatementVar] Long");
                    ps.setLong(i+1, (Long)obj);
                    
                } else if(obj instanceof  Date){
                	log.info("[DaoHelper][createPreparedStatementVar] Date");
                    ps.setDate(i+1, new java.sql.Date(((Date) obj).getTime()));
                    
                }
            }
        }
        return ps;
    }


    /*

     */
    public String getTodayDate() {
        return formatDate.format(Calendar.getInstance().getTime());
    }

    public String getTodayTimeStamp() {
        return formatTimeStamp.format(Calendar.getInstance().getTime());
    }

    public String getTodayDate(Calendar cal) {
        return formatDate.format(cal.getTime());
    }

    public String getTodayTimeStamp(Calendar cal ) {
        return formatTimeStamp.format(cal.getTime());
    }

}
