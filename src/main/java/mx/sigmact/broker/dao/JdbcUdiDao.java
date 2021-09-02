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
import mx.sigmact.broker.pojo.fondeo.FondeoBancario;
import mx.sigmact.broker.pojo.fondeo.FondeoTiie;
import mx.sigmact.broker.pojo.fondeo.FondeoCetes;
import mx.sigmact.broker.pojo.fondeo.FondeoGubernamental;
import mx.sigmact.broker.pojo.parameter.ValueParameter;
import mx.sigmact.broker.pojo.udi.Udi;

public class JdbcUdiDao implements UdiDao {

	Logger log = Logger.getLogger(JdbcUdiDao.class);

	@Resource(name = "formatDate")
	private SimpleDateFormat sdf;

	@Resource
	DataSource dataSource;
	
	@Resource
	DaoHelper daoHelper;

	@Value("${query.udi.get}")
	String getUdi;

	Udi udi = null;

	@Override
	public Udi getUdi(String date) {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression, ticket_sent" tienen que empatar con la misma fecha.
		 */
		
		log.info("[JdbcUdiDao][getUdi]");

		log.info("[JdbcUdiDao][getUdi] exec query: query.fonde_bancario.getLastID");
		
		log.info("[JdbcUdiDao][getUdi] date: " + date);
		
		this.udi = null;
		
		try (Connection con = dataSource.getConnection();
				
				PreparedStatement ps = daoHelper.createPreparedStatementVar(con, getUdi, date);
				ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {

				log.info("[JdbcUdiDao][getUdi] exec query Res id: " + rs.getInt(1));
				log.info("[JdbcUdiDao][getUdi] exec query Res date: " + rs.getString(2));
				log.info("[JdbcUdiDao][getUdi] exec query Res value: " + rs.getDouble(3));
				Integer id = rs.getInt(1);
				String date_ = rs.getString(2);
				Double value = rs.getDouble(3);
				
				this.udi = new Udi(id, date_, value);

			}
			con.close();
		} catch (SQLException e) {
			log.error("[JdbcUdiDao][getUdi] ERROR: Error al cargar la tabla udi_value: " + e.getMessage());
		}
		
		return this.udi;
		
	}
	
}
