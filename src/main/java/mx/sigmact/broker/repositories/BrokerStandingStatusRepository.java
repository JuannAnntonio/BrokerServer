package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.StandingStatusEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created on 31/10/16.
 */
public interface BrokerStandingStatusRepository extends CrudRepository<StandingStatusEntity, Integer>{
}
