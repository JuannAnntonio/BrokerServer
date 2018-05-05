package mx.sigmact.broker.model;

import javax.persistence.*;

/**
 * Created on 28/11/16.
 */
@Entity
@Table(name = "INSTITUTION_INSTRUMENTS", schema = "SIGMACT_BROKER")
public class InstitutionInstrumentsEntity {
    private int idCommision;
    private double surcharge;
    private String enabled;

    @Id
    @Column(name = "id_commision", nullable = false)
    public int getIdCommision() {
        return idCommision;
    }

    public void setIdCommision(int idCommision) {
        this.idCommision = idCommision;
    }

    @Basic
    @Column(name = "surcharge", nullable = false, precision = 0)
    public double getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(double surcharge) {
        this.surcharge = surcharge;
    }

    @Basic
    @Column(name = "enabled", nullable = true, length = 45)
    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstitutionInstrumentsEntity that = (InstitutionInstrumentsEntity) o;

        if (idCommision != that.idCommision) return false;
        if (Double.compare(that.surcharge, surcharge) != 0) return false;
        if (enabled != null ? !enabled.equals(that.enabled) : that.enabled != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = idCommision;
        temp = Double.doubleToLongBits(surcharge);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        return result;
    }
}
