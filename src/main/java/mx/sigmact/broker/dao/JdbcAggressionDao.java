package mx.sigmact.broker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.pojo.aggression.Aggression;
import mx.sigmact.broker.pojo.fondeo.FondeoBancario;
import mx.sigmact.broker.pojo.fondeo.FondeoTiie;
import mx.sigmact.broker.pojo.fondeo.FondeoCetes;
import mx.sigmact.broker.pojo.fondeo.FondeoGubernamental;
import mx.sigmact.broker.pojo.parameter.ValueParameter;

public class JdbcAggressionDao implements AggressionDao {

	Logger log = Logger.getLogger(JdbcAggressionDao.class);

	@Resource(name = "formatDate")
	private SimpleDateFormat sdf;

	@Resource
	DataSource dataSource;
	
	@Resource
	DaoHelper daoHelper;

	@Value("${query.aggression.insert}")
	String addAggression;
	
	@Override
	public int addAggression(Aggression aggression) {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression, ticket_sent" tienen que empatar con la misma fecha.
		 */
		
		log.info("[JdbcAggression][addAggression]");

		log.info("[JdbcAggression][addAggression] exec query: query.aggression.insert");
		
		log.info("[JdbcAggression][addAggression] aggression.getFkIdStanding(): " + aggression.getFkIdStanding());
		log.info("[JdbcAggression][addAggression] aggression.getFkIdTransactionStatus(): " + aggression.getFkIdTransactionStatus());
		log.info("[JdbcAggression][addAggression] aggression.getFkIdUser(): " + aggression.getFkIdUser());
		log.info("[JdbcAggression][addAggression] aggression.getFkIdInstitution(): " + aggression.getFkIdInstitution());
		log.info("[JdbcAggression][addAggression] aggression.getDatetime(): " + aggression.getDatetime().getTime());
		log.info("[JdbcAggression][addAggression] aggression.getAmount(): " + aggression.getAmount());
		log.info("[JdbcAggression][addAggression] aggression.getAggressionDirtyPrice(): " + aggression.getAggressionDirtyPrice());
		
		Timestamp timestamp = new Timestamp(aggression.getDatetime().getTimeInMillis());
		log.info("[JdbcAggression][addAggression] timestamp: " + timestamp);
		log.info("[JdbcAggression][addAggression] timestamp.toString(): " + timestamp.toString());
		
		String timestamp_tmp = timestamp.toString();
		
		if (timestamp_tmp.contains(".")) {

			log.info("[JdbcAggression][addAggression] tiene punto");
			String[] timestamp_date = timestamp.toString().split(Pattern.quote("."));
			timestamp_tmp = timestamp_date[0].toString();
		}
		
		log.info("[JdbcAggression][addAggression] timestamp_tmp: " + timestamp_tmp);
		
        int rows = 0;
		
		try {
			
			Connection con = dataSource.getConnection();
			PreparedStatement ps = daoHelper.createPreparedStatementVar(con, addAggression, aggression.getFkIdStanding(), aggression.getFkIdTransactionStatus(), aggression.getFkIdUser(), aggression.getFkIdInstitution(), timestamp_tmp, aggression.getAmount(), aggression.getAggressionDirtyPrice());
			rows = ps.executeUpdate();

			con.close();
			
			if(rows==1) {
				return 1;
			} else {
				return -1;
			}
			
		} catch (SQLException e) {
			log.error("[JdbcAggression][addAggression] ERROR: Error al insertar en la tabla Aggression: " + e.getMessage());
		}
		
		return -1;
		
	}
	
}
