package mx.sigmact.broker.model;

import javax.persistence.*;

/**
 * The type of bidding done in the application it is either BID or OFFER;
 * Created on 15/10/16.
 */
@Entity
@Table(name = "STANDING_TYPE", schema = "SIGMACT_BROKER")
public class StandingTypeEntity {

    private int idStandingType;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_standing_type", nullable = false)
    public int getIdStandingType() {
        return idStandingType;
    }

    public void setIdStandingType(int idStandingType) {
        this.idStandingType = idStandingType;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 20)
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

        StandingTypeEntity that = (StandingTypeEntity) o;

        if (idStandingType != that.idStandingType) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idStandingType;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
