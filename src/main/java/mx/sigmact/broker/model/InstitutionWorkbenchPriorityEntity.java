package mx.sigmact.broker.model;

import javax.persistence.*;

/**
 * Created on 01/11/16.
 */
@Entity
@Table(name = "INSTITUTION_WORKBENCH_PRIORITY", schema = "SIGMACT_BROKER")
public class InstitutionWorkbenchPriorityEntity {
    private int idInstitutionWorkbench;
    private int priority;
    private int fkIdWorkbenchInstitution;
    private int fkIdMainInstitution;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_institution_workbench", nullable = false)
    public int getIdInstitutionWorkbench() {
        return idInstitutionWorkbench;
    }

    public void setIdInstitutionWorkbench(int idInstitutionWorkbench) {
        this.idInstitutionWorkbench = idInstitutionWorkbench;
    }

    @Basic
    @Column(name = "priority", nullable = false)
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstitutionWorkbenchPriorityEntity that = (InstitutionWorkbenchPriorityEntity) o;

        if (idInstitutionWorkbench != that.idInstitutionWorkbench) return false;
        if (priority != that.priority) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idInstitutionWorkbench;
        result = 31 * result + priority;
        return result;
    }

    @Basic
    @Column(name = "fk_id_workbench_institution", nullable = false)
    public int getFkIdWorkbenchInstitution() {
        return fkIdWorkbenchInstitution;
    }

    public void setFkIdWorkbenchInstitution(int fkIdWorkbenchInstitution) {
        this.fkIdWorkbenchInstitution = fkIdWorkbenchInstitution;
    }

    @Basic
    @Column(name = "fk_id_main_institution", nullable = false)
    public int getFkIdMainInstitution() {
        return fkIdMainInstitution;
    }

    public void setFkIdMainInstitution(int fkIdMainInstitution) {
        this.fkIdMainInstitution = fkIdMainInstitution;
    }
}
