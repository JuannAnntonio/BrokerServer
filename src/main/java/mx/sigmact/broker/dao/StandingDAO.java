package mx.sigmact.broker.dao;

import java.util.List;
import java.util.Set;

import mx.sigmact.broker.pojo.MarketPositionUser;

public interface StandingDAO {

	List<MarketPositionUser> findByIdStandingStatusAndIdsInstruments(Set<Integer> idsIntruments);

	MarketPositionUser findByIdStandingStatusAndIdInstrument(Integer idIntrument);

	int updateStanding(int fk_id_standing_status, int id_standing);

	int busyStanding(int available, int id_standing);

}
