package mx.sigmact.broker.model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * This entityrepresents an aggresion initiated in a trade environment.
 *
 * Created on 15/10/16.
 */
@Entity
@Table(name = "AGGRESSION", schema = "SIGMACT_BROKER", catalog = "")
public class AggressionEntity {
    private int idAggression;
    private int fkIdStanding;
    private int fkIdTransactionStatus;
    private int fkIdUser;
    private int fkIdInstitution;
    private Calendar datetime;
    private Integer amount;
    private Double aggressionDirtyPrice;

    public AggressionEntity() {
    }

    public AggressionEntity(int fkIdStanding, int fkIdTransactionStatus, int fkIdUser, int fkIdInstitution, Calendar datetime, Integer amount, Double aggressionDirtyPrice) {
        this.fkIdStanding = fkIdStanding;
        this.fkIdTransactionStatus = fkIdTransactionStatus;
        this.fkIdUser = fkIdUser;
        this.fkIdInstitution = fkIdInstitution;
        this.datetime = datetime;
        this.amount = amount;
        this.aggressionDirtyPrice = aggressionDirtyPrice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_aggression", nullable = false)
    public int getIdAggression() {
        return idAggression;
    }

    public void setIdAggression(int idAgression) {
        this.idAggression = idAgression;
    }

    @Basic
    @Column(name = "fk_id_standing", nullable = false)
    public int getFkIdStanding() {
        return fkIdStanding;
    }

    public void setFkIdStanding(int fkIdStanding) {
        this.fkIdStanding = fkIdStanding;
    }

    @Basic
    @Column(name = "fk_id_transaction_status", nullable = false)
    public int getFkIdTransactionStatus() {
        return fkIdTransactionStatus;
    }

    public void setFkIdTransactionStatus(int fkIdTransactionStatus) {
        this.fkIdTransactionStatus = fkIdTransactionStatus;
    }

    @Basic
    @Column(name = "fk_id_user", nullable = false)
    public int getFkIdUser() {
        return fkIdUser;
    }

    public void setFkIdUser(int fkIdUser) {
        this.fkIdUser = fkIdUser;
    }

    @Basic
    @Column(name = "fk_id_institution", nullable = false)
    public int getFkIdInstitution() {
        return fkIdInstitution;
    }

    public void setFkIdInstitution(int fkIdInstitution) {
        this.fkIdInstitution = fkIdInstitution;
    }

    @Basic
    @Column(name = "datetime", nullable = true)
    public Calendar getDatetime() {
        return datetime;
    }

    public void setDatetime(Calendar datetime) {
        this.datetime = datetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AggressionEntity that = (AggressionEntity) o;

        if (idAggression != that.idAggression) return false;
        if (fkIdStanding != that.fkIdStanding) return false;
        if (fkIdTransactionStatus != that.fkIdTransactionStatus) return false;
        if (fkIdUser != that.fkIdUser) return false;
        if (fkIdInstitution != that.fkIdInstitution) return false;
        if (datetime != null ? !datetime.equals(that.datetime) : that.datetime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idAggression;
        result = 31 * result + fkIdStanding;
        result = 31 * result + fkIdTransactionStatus;
        result = 31 * result + fkIdUser;
        result = 31 * result + fkIdInstitution;
        result = 31 * result + (datetime != null ? datetime.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "amount", nullable = true)
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "aggression_dirty_price", nullable = true, precision = 0)
    public Double getAggressionDirtyPrice() {
        return aggressionDirtyPrice;
    }

    public void setAggressionDirtyPrice(Double aggressionDirtyPrice) {
        this.aggressionDirtyPrice = aggressionDirtyPrice;
    }
}
