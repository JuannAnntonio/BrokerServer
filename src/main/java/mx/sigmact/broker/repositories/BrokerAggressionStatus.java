package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.AggressionStatusEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created on 31/10/16.
 */
public interface BrokerAggressionStatus extends CrudRepository<AggressionStatusEntity, Integer> {
}
