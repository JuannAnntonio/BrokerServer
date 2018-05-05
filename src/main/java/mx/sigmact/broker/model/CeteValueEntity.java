package mx.sigmact.broker.model;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created on 28/11/16.
 */
@Entity
@Table(name = "CETE_VALUE", schema = "SIGMACT_BROKER")
public class CeteValueEntity {
    private int idCt;
    private Date ctDate;
    private Double ctRate;

    @Id
    @Column(name = "id_ct", nullable = false)
    public int getIdCt() {
        return idCt;
    }

    public void setIdCt(int idCt) {
        this.idCt = idCt;
    }

    @Basic
    @Column(name = "ct_date", nullable = true)
    public Date getCtDate() {
        return ctDate;
    }

    public void setCtDate(Date ctDate) {
        this.ctDate = ctDate;
    }

    @Basic
    @Column(name = "ct_rate", nullable = true, precision = 0)
    public Double getCtRate() {
        return ctRate;
    }

    public void setCtRate(Double ctRate) {
        this.ctRate = ctRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CeteValueEntity that = (CeteValueEntity) o;

        if (idCt != that.idCt) return false;
        if (ctDate != null ? !ctDate.equals(that.ctDate) : that.ctDate != null) return false;
        if (ctRate != null ? !ctRate.equals(that.ctRate) : that.ctRate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idCt;
        result = 31 * result + (ctDate != null ? ctDate.hashCode() : 0);
        result = 31 * result + (ctRate != null ? ctRate.hashCode() : 0);
        return result;
    }
}
