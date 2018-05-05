package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.ValmerPriceVectorEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created on 28/10/16.
 */
public interface BrokerValmerPriceVectorRepository extends CrudRepository<ValmerPriceVectorEntity, Integer>{
    List<ValmerPriceVectorEntity> findTop1ByOrderByIdValmerPriceVectorDesc();
}
