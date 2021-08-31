package mx.sigmact.broker.repositories;

import java.util.Calendar;
import java.util.List;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import mx.sigmact.broker.model.StandingEntity;

/**
 * Spring Data Repository for the Standing entity.
 *
 * Created on 28/10/16.
 */
public interface BrokerStandingRepository extends CrudRepository<StandingEntity, Integer> {
    List<StandingEntity> findByDatetimeGreaterThanEqual(Calendar datetime);
    List<StandingEntity> findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatusLessThanEqual(int fkIdValmerPriceVector, Calendar datetime, int fkIdStandingType, int fkIdStandingStatus);
    List<StandingEntity> findByFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingTypeAndFkIdStandingStatus(int fkIdValmerPriceVector, Calendar datetime, int fkIdStandingType, int fkIdStandingStatus);
    List<StandingEntity> findByFkIdStandingStatus(Integer fkIdStandingStatus);
    StandingEntity findOneByFkIdUserAndFkIdValmerPriceVectorAndDatetimeGreaterThanEqualAndFkIdStandingStatusLessThanEqualAndFkIdStandingType
            (int fkIdUser, int fkIdValmerPriceVector, Calendar datetime, int fkIdStandingStatus, int fkIdStandingType);
    List<StandingEntity> findByFkIdUserAndFkIdStandingStatusLessThanEqualAndDatetimeGreaterThanEqual(Integer fkIdUser, Integer fkIdStandingStatus, Calendar day);
    List<StandingEntity> findByFkIdStandingStatusAndDatetimeGreaterThanEqual(int fkIdStandingStatus, Calendar day);
    
    StandingEntity findOneByFkIdStandingStatusAndFkIdValmerPriceVectorAndFkIdStandingType(int fkIdStandingStatus, int priceVector, int standingType);


    List<StandingEntity> findByFkIdUserAndDatetimeGreaterThanEqualAndFkIdStandingStatusLessThanEqual(int fkIdUser, Calendar datetime, int fkIdStandingStatus);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    StandingEntity findByIdStanding(Integer id);
    
    
    @Query(value = "select max(orden) from sigmact_broker.standing\n" + 
    		"where fk_id_standing_status IN (3,1)\n" + 
    		"AND Fecha_Fin is null\n" + 
    		"and fk_id_valmer_price_vector=:priceVector", nativeQuery = true)
	public Integer getMaxOrden(@Param("priceVector") Integer priceVector);
}
