package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created on 31/10/16.
 */
public interface BrokerInstitutionRepository extends JpaRepository<InstitutionEntity, Integer> {
    InstitutionEntity findOneByName(String name);

}
