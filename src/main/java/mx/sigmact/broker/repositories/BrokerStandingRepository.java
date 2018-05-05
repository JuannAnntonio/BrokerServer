package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.StandingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Calendar;
import java.util.List;

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
    List<StandingEntity> findByFkIdStandingStatusAndDatetime(int fkIdStandingStatus, Calendar day);

}
