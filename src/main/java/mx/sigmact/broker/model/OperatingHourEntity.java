package mx.sigmact.broker.model;

import javax.persistence.*;

/**
 * To limit the operating hours of the system. This is also linked to the market in which the transactions are
 * done.
 * Created on 15/10/16.
 */
@Entity
@Table(name = "OPERATING_HOUR", schema = "SIGMACT_BROKER")
public class OperatingHourEntity {
    private int idOperatingHours;
    private String opening;
    private String closing;
    private int fkIdMarketType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_operating_hours", nullable = false)
    public int getIdOperatingHours() {
        return idOperatingHours;
    }

    public void setIdOperatingHours(int idOperatingHours) {
        this.idOperatingHours = idOperatingHours;
    }

    @Basic
    @Column(name = "opening", nullable = false, length = 45)
    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    @Basic
    @Column(name = "closing", nullable = false, length = 45)
    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }

    @Basic
    @Column(name = "fk_id_market_type", nullable = false)
    public int getFkIdMarketType() {
        return fkIdMarketType;
    }

    public void setFkIdMarketType(int fkIdMarketType) {
        this.fkIdMarketType = fkIdMarketType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperatingHourEntity that = (OperatingHourEntity) o;

        if (idOperatingHours != that.idOperatingHours) return false;
        if (fkIdMarketType != that.fkIdMarketType) return false;
        if (opening != null ? !opening.equals(that.opening) : that.opening != null) return false;
        if (closing != null ? !closing.equals(that.closing) : that.closing != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idOperatingHours;
        result = 31 * result + (opening != null ? opening.hashCode() : 0);
        result = 31 * result + (closing != null ? closing.hashCode() : 0);
        result = 31 * result + fkIdMarketType;
        return result;
    }
}
