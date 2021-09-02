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
import mx.sigmact.broker.pojo.fondeo.FondeoCetes;
import mx.sigmact.broker.pojo.fondeo.FondeoGubernamental;
import mx.sigmact.broker.pojo.instrument.Instrument;
import mx.sigmact.broker.pojo.parameter.ValueParameter;
import mx.sigmact.broker.pojo.udi.Udi;

public class JdbcInstrumentDao implements InstrumentDao {

	Logger log = Logger.getLogger(JdbcInstrumentDao.class);

	@Resource(name = "formatDate")
	private SimpleDateFormat sdf;

	@Resource
	DataSource dataSource;
	
	@Resource
	DaoHelper daoHelper;

	@Value("${query.instrument.get}")
	String getInstrument;

	Instrument instrument = null;

	@Override
	public Instrument getInstrument(String tv) {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression, ticket_sent" tienen que empatar con la misma fecha.
		 */
		
		log.info("[JdbcInstrumentDao][getInstrument]");
		
		log.info("[JdbcInstrumentDao][getInstrument] exec query: query.instrument.get");
		log.info("[JdbcInstrumentDao][getInstrument] tv: " + tv);
		
		this.instrument = null;
		
		try (Connection con = dataSource.getConnection();
				
				PreparedStatement ps = daoHelper.createPreparedStatementVar(con, getInstrument, tv);
				ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {

				log.info("[JdbcInstrumentDao][getInstrument] exec query Res id_instrument: " + rs.getInt(1));
				log.info("[JdbcInstrumentDao][getInstrument] exec query Res issuing_company: " + rs.getString(2));
				log.info("[JdbcInstrumentDao][getInstrument] exec query Res tv: " + rs.getString(3));

				Integer id_instrument = rs.getInt(1);
				String issuing_company = rs.getString(2);
				String tv2 = rs.getString(3);
				
				this.instrument = new Instrument(id_instrument, issuing_company, tv2);

			}
			con.close();
		} catch (SQLException e) {
			log.error("[JdbcInstrumentDao][getInstrument] ERROR: Error al cargar la tabla instrument_type: " + e.getMessage());
		}
		
		return this.instrument;
		
	}
	
}
