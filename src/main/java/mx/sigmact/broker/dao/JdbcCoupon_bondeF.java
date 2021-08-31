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
import mx.sigmact.broker.pojo.coupon_bonde.Coupon_rate;

public class JdbcCoupon_bondeF implements Coupon_bondeFDao {

	Logger log = Logger.getLogger(JdbcCoupon_bondeF.class);

	@Resource(name = "formatDate")
	private SimpleDateFormat sdf;

	@Resource
	DataSource dataSource;

	@Resource
	DaoHelper daoHelper;

	@Value("${query.coupon_bondesf.get.rate}")
	String getRate;

	Coupon_rate coupon_rate = null;

	@Override
	public Coupon_rate getRate(String date, String series) {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression,
		 * ticket_sent" tienen que empatar con la misma fecha.
		 */

		log.info("[JdbcCoupon_bondeF][getRate]");
		log.info("[JdbcCoupon_bondeF][getRate] date: " + date);
		log.info("[JdbcCoupon_bondeF][getRate] series: " + series);
		log.info("[JdbcCoupon_bondeF][getRate] exec query: query.get.parameter");

		String rate = "";

		this.coupon_rate = null;

		try (Connection con = dataSource.getConnection();

				PreparedStatement ps = daoHelper.createPreparedStatementVar(con, getRate, date, series);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				log.info("[JdbcCoupon_bondeF][getRate] exec query Res : " + rs.getString(1));
				rate = rs.getString(1);
				this.coupon_rate = new Coupon_rate(rate);
			}
			con.close();
		} catch (SQLException e) {
			log.error("[JdbcCoupon_bondeF][getRate] ERROR: Error al cargar la tabla cupon_bondef: " + e.getMessage());
		}

		return this.coupon_rate;

	}

}
