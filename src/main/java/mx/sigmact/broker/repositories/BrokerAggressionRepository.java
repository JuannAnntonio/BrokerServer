package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.AggressionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Calendar;
import java.util.List;

/**
 * Created on 28/10/16.
 */
public interface BrokerAggressionRepository extends CrudRepository<AggressionEntity,Integer>{
    List<AggressionEntity> findByDatetimeGreaterThanEqual(Calendar datetime);
    List<AggressionEntity> findByFkIdUserAndFkIdTransactionStatus(int user, int status);
    List<AggressionEntity> findByFkIdUserAndFkIdTransactionStatusAndDatetime(int user, int status, Calendar datetime);
    List<AggressionEntity> findByFkIdUserAndFkIdTransactionStatusAndDatetimeGreaterThanEqual(int user, int status, Calendar calendar);
    List<AggressionEntity> findByFkIdUserAndFkIdTransactionStatusOrFkIdTransactionStatus(int user, int status, int status2);
}
