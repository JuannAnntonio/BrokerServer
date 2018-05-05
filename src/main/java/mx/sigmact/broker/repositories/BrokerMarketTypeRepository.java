package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.MarketTypeEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created on 31/10/16.
 */
public interface BrokerMarketTypeRepository extends CrudRepository<MarketTypeEntity, Integer> {
}
