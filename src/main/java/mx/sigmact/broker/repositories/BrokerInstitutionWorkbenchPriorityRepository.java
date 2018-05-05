package mx.sigmact.broker.repositories;

import mx.sigmact.broker.model.InstitutionWorkbenchPriorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created on 31/10/16.
 */
public interface BrokerInstitutionWorkbenchPriorityRepository extends JpaRepository<InstitutionWorkbenchPriorityEntity, Integer> {
    List<InstitutionWorkbenchPriorityEntity> findByFkIdMainInstitution(Integer fkIdMainInstitution);
    List<InstitutionWorkbenchPriorityEntity> findByFkIdWorkbenchInstitution(Integer fkIdWorkbenchInstitution);
}
