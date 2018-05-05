package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created on 31/10/16.
 */
public interface BrokerUserRepository extends CrudRepository<UserEntity, Integer> {
    List<UserEntity> findByFkIdInstitution(int fkIdInstitution);
    List<UserEntity> findByFkIdInstitutionAndEnabled(int fkIdInstitution, short enabled);
    UserEntity findOneByEmail(String email);
    UserEntity findOneByUsername(String username);

}
