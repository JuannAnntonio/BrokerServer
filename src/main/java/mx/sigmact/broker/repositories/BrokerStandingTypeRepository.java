package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.StandingTypeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created on 31/10/16.
 */
public interface BrokerStandingTypeRepository extends CrudRepository<StandingTypeEntity, Integer> {
    List<StandingTypeEntity> findByName(String name);

}
