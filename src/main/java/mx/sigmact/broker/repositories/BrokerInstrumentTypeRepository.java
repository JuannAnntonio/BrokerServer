package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.InstrumentTypeEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created on 31/10/16.
 */
public interface BrokerInstrumentTypeRepository extends CrudRepository<InstrumentTypeEntity, Integer>{
}
