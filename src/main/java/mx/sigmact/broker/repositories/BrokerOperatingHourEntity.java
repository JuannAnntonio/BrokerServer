package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.OperatingHourEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created on 31/10/16.
 */
public interface BrokerOperatingHourEntity extends CrudRepository<OperatingHourEntity, Integer> {
}
