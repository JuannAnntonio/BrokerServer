package mx.sigmact.broker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.pojo.MarketPositionUser;

public class StandingDAOImpl implements StandingDAO {

	Logger log = Logger.getLogger(StandingDAOImpl.class);

	@Resource(name = "formatDate")
	private SimpleDateFormat sdf;
	@Resource
	private DataSource dataSource;
	@Resource
	private DaoHelper daoHelper;
	@Value("${query.update.standing}")
	private String updateStanding;
	@Value("${query.update.standing.busy}")
	private String updateStandingBusy;
	@Value("${query.update.standing.select}")
	private String updateStandingSelect;

	@Override
	public List<MarketPositionUser> findByIdStandingStatusAndIdsInstruments(Set<Integer> idsIntruments) {

		List<MarketPositionUser> lstMarket = new ArrayList<>();
		if (null != idsIntruments && !idsIntruments.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			idsIntruments.forEach(i -> builder.append("?,"));

			String placeHolders = builder.deleteCharAt(builder.length() - 1).toString();
			String sql = "select id_standing, fk_id_standing_type,fk_id_valmer_price_vector,fk_id_user, usr.fk_id_institution \n"
					+ "from standing std \n" + "join user usr \n" + "    on std.fk_id_user = usr.id_user \n"
					+ "where fk_id_standing_status=3 \n" + "and fk_id_valmer_price_vector IN (" + placeHolders + ")";

			try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

				int index = 1;
				for (Integer o : idsIntruments) {
					ps.setInt(index++, o);
				}

				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					MarketPositionUser mpu = new MarketPositionUser();

					mpu.setIdStanding(rs.getInt("id_standing"));
					mpu.setIdStandingType(rs.getInt("fk_id_standing_type"));
					mpu.setIdValmerPriceVector(rs.getInt("fk_id_valmer_price_vector"));
					mpu.setIdUser(rs.getInt("fk_id_user"));
					mpu.setIdInstitution(rs.getInt("fk_id_institution"));

					lstMarket.add(mpu);

				}
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return lstMarket;
	}

	@Override
	public MarketPositionUser findByIdStandingStatusAndIdInstrument(Integer idIntrument) {

		String sql = "select distinct fk_id_standing_type,fk_id_valmer_price_vector,fk_id_user, usr.fk_id_institution, ins.name\n"
				+ "from standing std \n" + "join user usr \n" + "    on std.fk_id_user = usr.id_user \n"
				+ "join institution ins\n" + "    on ins.id_institution = usr.fk_id_institution \n"
				+ "where fk_id_standing_status=3 \n" + "and fk_id_valmer_price_vector = ?";

		MarketPositionUser mpu = new MarketPositionUser();
		try (Connection con = dataSource.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, idIntrument);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				mpu.setIdStandingType(rs.getInt("fk_id_standing_type"));
				mpu.setIdValmerPriceVector(rs.getInt("fk_id_valmer_price_vector"));
				mpu.setIdUser(rs.getInt("fk_id_user"));
				mpu.setIdInstitution(rs.getInt("fk_id_institution"));
				mpu.setNameInstitution(rs.getString("name"));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mpu;
	}

	@Override
	public int busyStanding(int available, int id_standing) {

		log.info("[JdbcStandingDao][busyStanding]");

		log.info("[JdbcStandingDao][busyStanding] exec query: query.update.standing");

		log.info("[JdbcStandingDao][busyStanding] available: " + available);

		log.info("[JdbcStandingDao][busyStanding] id_standing: " + id_standing);

		int rows = 0;

		try {

			if (available == 1) {
				// set standing in busy

				log.info("[JdbcStandingDao][busyStanding] set standing in busy");

				Connection con = dataSource.getConnection();

				PreparedStatement ps = daoHelper.createPreparedStatementVarInt(con, updateStandingSelect, id_standing);
				ResultSet rs = ps.executeQuery();

				int y = 0;

				while (rs.next()) {

					y = rs.getInt(1);

				}

				log.info("[JdbcStandingDao][busyStanding] id_standing available? busy: " + y);

				if (y == 0) {

					ps = daoHelper.createPreparedStatementVar(con, updateStandingBusy, available, id_standing);
					rows = ps.executeUpdate();

					log.info("[JdbcStandingDao][busyStanding] ocupar standing con exito?: " + rows);

					con.close();

					if (rows == 1) {

						log.info("[JdbcStandingDao][busyStanding] set standing in busy success");

						return 1;
					} else {

						log.error("[JdbcStandingDao][busyStanding] ERROR: set standing in busy");

						return -1;
					}

				} else {
					return -1;
				}
			} else {
				// set standing in not busy

				log.info("[JdbcStandingDao][busyStanding] set standing in not busy");

				Connection con = dataSource.getConnection();

				PreparedStatement ps = daoHelper.createPreparedStatementVar(con, updateStandingBusy, available,
						id_standing);

				rows = ps.executeUpdate();

				log.info("[JdbcStandingDao][busyStanding] desocupar standing con exito?: " + rows);

				con.close();

				if (rows == 1) {

					log.info("[JdbcStandingDao][busyStanding] set standing in not busy success");

					return 1;

				} else {

					log.error("[JdbcStandingDao][busyStanding] ERROR: set standing in not busy");

					return -1;

				}

			}

		} catch (SQLException e) {
			log.error("[JdbcStandingDao][busyStanding] ERROR: Error al update en la tabla Standing: " + e.getMessage());
		}

		return -1;
	}

	@Override
	public int updateStanding(int fk_id_standing_status, int id_standing) {

		log.info("[JdbcStandingDao][updateStanding]");

		log.info("[JdbcStandingDao][updateStanding] exec query: query.update.standing");

		log.info("[JdbcStandingDao][updateStanding] fk_id_standing_status: " + fk_id_standing_status);

		log.info("[JdbcStandingDao][updateStanding] id_standing: " + id_standing);

		int rows = 0;

		Calendar today = Calendar.getInstance();

		log.info("[JdbcStandingDao][updateStanding] today: " + today.getTime().toString());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sDate = sdf.format(today.getTime());

		log.info("[JdbcStandingDao][updateStanding] today: " + sDate.toString());

		try {

			Connection con = dataSource.getConnection();
			PreparedStatement ps = daoHelper.createPreparedStatementVar(con, updateStanding, fk_id_standing_status,
					sDate, id_standing);
			rows = ps.executeUpdate();

			log.info("[JdbcStandingDao][updateStanding] exito?: " + rows);

			con.close();

			if (rows == 1) {
				return 1;
			} else {
				return -1;
			}

		} catch (SQLException e) {
			log.error(
					"[JdbcStandingDao][updateStanding] ERROR: Error al update en la tabla Standing: " + e.getMessage());
		}

		return -1;

	}

}
