package mx.sigmact.broker.model;

import javax.persistence.*;

/**
 * Status of the Entity
 * Created on 15/10/16.
 */
@Entity
@Table(name = "STANDING_STATUS", schema = "SIGMACT_BROKER")
public class StandingStatusEntity {
    private int idStandingStatus;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_standing_status", nullable = false)
    public int getIdStandingStatus() {
        return idStandingStatus;
    }

    public void setIdStandingStatus(int idStandingStatus) {
        this.idStandingStatus = idStandingStatus;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StandingStatusEntity that = (StandingStatusEntity) o;

        if (idStandingStatus != that.idStandingStatus) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idStandingStatus;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
