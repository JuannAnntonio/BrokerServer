package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.UdiValueEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created on 09/12/16.
 */
public interface BrokerUdiValueRepository extends CrudRepository<UdiValueEntity, Integer> {
    UdiValueEntity findOneByUdiDate(java.sql.Date udiDate);

}
