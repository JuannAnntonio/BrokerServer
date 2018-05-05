package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.UserxinstrumentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created on 31/10/16.
 */
public interface BrokerUserxInstrumentRepository extends CrudRepository<UserxinstrumentEntity, Integer> {
    List<UserxinstrumentEntity> findByFkIdUser(Integer fkIdUser);

}
