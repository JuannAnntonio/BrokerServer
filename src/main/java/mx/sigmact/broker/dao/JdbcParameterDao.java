package mx.sigmact.broker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.pojo.parameter.ValueParameter;

public class JdbcParameterDao implements ParameterDao {

	Logger log = Logger.getLogger(JdbcParameterDao.class);

	@Resource(name = "formatDate")
	private SimpleDateFormat sdf;

	@Resource
	DataSource dataSource;
	
	@Resource
	DaoHelper daoHelper;

	@Value("${query.get.parameter}")
	String getParameter;

	ValueParameter valueParameter = null;

	@Override
	public ValueParameter getParameter(String id_parameter_value) {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression, ticket_sent" tienen que empatar con la misma fecha.
		 */
		
		log.info("[JdbcParameterDao][getParameter]");
		
        log.info("[JdbcParameterDao][getParameter] id_parameter_value: " + id_parameter_value);

		log.info("[JdbcParameterDao][getParameter] exec query: query.get.parameter");

		String value = "";
		
		this.valueParameter = null;
		
		try (Connection con = dataSource.getConnection();
				
				PreparedStatement ps = daoHelper.createPreparedStatementVar(con, getParameter, id_parameter_value);
				ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {

				log.info("[JdbcParameterDao][getParameter] exec query Res : " + rs.getString(1));
				value = rs.getString(1);
				
				this.valueParameter = new ValueParameter(id_parameter_value, value);

			}
			con.close();
		} catch (SQLException e) {
			log.error("[JdbcParameterDao][getParameter] ERROR: Error al cargar la tabla parameter: " + e.getMessage());
		}
		
		return this.valueParameter;
		
	}

}
