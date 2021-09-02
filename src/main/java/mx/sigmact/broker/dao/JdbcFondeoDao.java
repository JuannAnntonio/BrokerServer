package mx.sigmact.broker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.pojo.fondeo.FondeoBancario;
import mx.sigmact.broker.pojo.fondeo.FondeoCetes;
import mx.sigmact.broker.pojo.fondeo.FondeoGubernamental;
import mx.sigmact.broker.pojo.fondeo.FondeoTiie;

public class JdbcFondeoDao implements FondeoDao {

	Logger log = Logger.getLogger(JdbcFondeoDao.class);

	@Resource(name = "formatDate")
	private SimpleDateFormat sdf;

	@Resource
	DataSource dataSource;

	@Resource
	DaoHelper daoHelper;

	@Value("${query.fonde_bancario.getLastID}")
	String getFondeoLastRegister;

	@Value("${query.fonde_gubernamental.getLastID}")
	String getFondeoGubernamentalLastRegister;

	@Value("${query.fonde_cetes.getLastID}")
	String getFondeoCetesLastRegister;

	@Value("${query.fonde_tiie.getLastID}")
	String getFondeoTiieLastRegister;

	FondeoCetes fondeoCetes = null;

	FondeoGubernamental fondeoGubernamental = null;

	FondeoBancario fondeoBancario = null;

	FondeoTiie fondeoTiie = null;

	@Override
	public FondeoBancario getFondeoLastRegister() {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression,
		 * ticket_sent" tienen que empatar con la misma fecha.
		 */

		log.info("[JdbcFondeoDAO][getFondeoLastRegister]");

		log.info("[JdbcFondeoDAO][getFondeoLastRegister] exec query: query.fonde_bancario.getLastID");

		this.fondeoBancario = null;

		try (Connection con = dataSource.getConnection();

				PreparedStatement ps = daoHelper.createPreparedStatement(con, getFondeoLastRegister);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				log.info("[JdbcFondeoDAO][getFondeoLastRegister] exec query Res Date: " + rs.getString(1));
				log.info("[JdbcFondeoDAO][getFondeoLastRegister] exec query Res Rate: " + rs.getString(2));
				String date = rs.getString(1);
				String rate = rs.getString(2);

				this.fondeoBancario = new FondeoBancario(date, rate);

			}
			con.close();
		} catch (SQLException e) {
			log.error("[JdbcFondeoDAO][getFondeoLastRegister] ERROR: Error al cargar la tabla fondeo_bancario: "
					+ e.getMessage());
		}

		return this.fondeoBancario;

	}

	@Override
	public FondeoTiie getFondeoTiieLastRegister() {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression,
		 * ticket_sent" tienen que empatar con la misma fecha.
		 */

		log.info("[JdbcFondeoDAO][getFondeoTiieLastRegister]");

		log.info("[JdbcFondeoDAO][getFondeoTiieLastRegister] exec query: query.fondeo_tiie1.getLastID");

		this.fondeoTiie = null;

		try (Connection con = dataSource.getConnection();

				PreparedStatement ps = daoHelper.createPreparedStatement(con, getFondeoTiieLastRegister);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				log.info("[JdbcFondeoDAO][getFondeoTiieLastRegister] exec query Res Date: " + rs.getString(1));
				log.info("[JdbcFondeoDAO][getFondeoTiieLastRegister] exec query Res Rate: " + rs.getString(2));
				String date = rs.getString(1);
				String rate = rs.getString(2);

				this.fondeoTiie = new FondeoTiie(date, rate);

			}
			con.close();
		} catch (SQLException e) {
			log.error("[JdbcFondeoDAO][getFondeoTiieLastRegister] ERROR: Error al cargar la tabla fondeo_tiie1: "
					+ e.getMessage());
		}

		return this.fondeoTiie;

	}

	@Override
	public FondeoGubernamental getFondeoGubernamentalLastRegister() {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression,
		 * ticket_sent" tienen que empatar con la misma fecha.
		 */

		log.info("[JdbcFondeoDAO][getFondeoGubernamentalLastRegister]");

		log.info("[JdbcFondeoDAO][getFondeoGubernamentalLastRegister] exec query: query.fonde_gubernamental.getLastID");

		this.fondeoGubernamental = null;

		try (Connection con = dataSource.getConnection();

				PreparedStatement ps = daoHelper.createPreparedStatement(con, getFondeoGubernamentalLastRegister);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				log.info("[JdbcFondeoDAO][getFondeoGubernamentalLastRegister] exec query Res Date: " + rs.getString(1));
				log.info("[JdbcFondeoDAO][getFondeoGubernamentalLastRegister] exec query Res Rate: " + rs.getString(2));
				String date = rs.getString(1);
				String rate = rs.getString(2);

				this.fondeoGubernamental = new FondeoGubernamental(date, rate);

			}
			con.close();
		} catch (SQLException e) {
			log.error(
					"[JdbcFondeoDAO][getFondeoGubernamentalLastRegister] ERROR: Error al cargar la tabla fondeo_bancario: "
							+ e.getMessage());
		}

		return this.fondeoGubernamental;

	}

	@Override
	public FondeoCetes getFondeoCetesLastRegister() {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression,
		 * ticket_sent" tienen que empatar con la misma fecha.
		 */

		log.info("[JdbcFondeoDAO][getFondeoCetesLastRegister]");

		log.info("[JdbcFondeoDAO][getFondeoCetesLastRegister] exec query: query.fonde_cetes.getLastID");

		this.fondeoCetes = null;

		try (Connection con = dataSource.getConnection();

				PreparedStatement ps = daoHelper.createPreparedStatement(con, getFondeoCetesLastRegister);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {

				log.info("[JdbcFondeoDAO][getFondeoCetesLastRegister] exec query Res Date: " + rs.getString(1));
				log.info("[JdbcFondeoDAO][getFondeoCetesLastRegister] exec query Res Rate: " + rs.getString(2));
				String date = rs.getString(1);
				String rate = rs.getString(2);

				this.fondeoCetes = new FondeoCetes(date, rate);

			}
			con.close();
		} catch (SQLException e) {
			log.error("[JdbcFondeoDAO][getFondeoCetesLastRegister] ERROR: Error al cargar la tabla fondeo_bancario: "
					+ e.getMessage());
		}

		return this.fondeoCetes;

	}
}
