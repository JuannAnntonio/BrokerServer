package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.RolesEntity;
import mx.sigmact.broker.model.RolesEntityPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created on 31/10/16.
 */
public interface BrokerRolesEntity extends CrudRepository<RolesEntity, RolesEntityPK> {
    RolesEntity findOneByFkIdUser(Integer fkIdUser);

    @Query("SELECT DISTINCT role FROM RolesEntity")
    List<String> findDistinctRoles();
}
