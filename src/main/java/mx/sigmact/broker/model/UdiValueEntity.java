package mx.sigmact.broker.model;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created on 28/11/16.
 */
@Entity
@Table(name = "UDI_VALUE", schema = "SIGMACT_BROKER")
public class UdiValueEntity {
    private int idUdi;
    private Date udiDate;
    private double udiValue;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_udi", nullable = false)
    public int getIdUdi() {
        return idUdi;
    }

    public void setIdUdi(int idUdi) {
        this.idUdi = idUdi;
    }

    @Basic
    @Column(name = "udi_date", nullable = false)
    public Date getUdiDate() {
        return udiDate;
    }

    public void setUdiDate(Date udiDate) {
        this.udiDate = udiDate;
    }

    @Basic
    @Column(name = "UDI_VALUE", nullable = false, precision = 0)
    public double getUdiValue() {
        return udiValue;
    }

    public void setUdiValue(double udiValue) {
        this.udiValue = udiValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UdiValueEntity that = (UdiValueEntity) o;

        if (idUdi != that.idUdi) return false;
        if (Double.compare(that.udiValue, udiValue) != 0) return false;
        if (udiDate != null ? !udiDate.equals(that.udiDate) : that.udiDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = idUdi;
        result = 31 * result + (udiDate != null ? udiDate.hashCode() : 0);
        temp = Double.doubleToLongBits(udiValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
