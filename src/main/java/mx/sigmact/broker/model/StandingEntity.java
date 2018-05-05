package mx.sigmact.broker.model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * A standing created by a bidder.
 * Created on 15/10/16.
 */
@Entity
@Table(name = "STANDING", schema = "SIGMACT_BROKER")
public class StandingEntity implements Comparable<StandingEntity>, Cloneable {
    private int idStanding;
    private int fkIdStandingType;
    private int fkIdValmerPriceVector;
    private int fkIdUser;
    private int amount;
    private double value;
    private int fkIdStandingStatus;
    private Calendar datetime;
    private int currentAmount;
    private Double standingDirtyPrice;

    public StandingEntity() {
    }

    public StandingEntity(int fkIdStandingType, int fkIdValmerPriceVector, int fkIdUser, int amount, double value, int fkIdStandingStatus, Calendar datetime, int currentAmount, Double standingDirtyPrice) {
        this.fkIdStandingType = fkIdStandingType;
        this.fkIdValmerPriceVector = fkIdValmerPriceVector;
        this.fkIdUser = fkIdUser;
        this.amount = amount;
        this.value = value;
        this.fkIdStandingStatus = fkIdStandingStatus;
        this.datetime = datetime;
        this.currentAmount = currentAmount;
        this.standingDirtyPrice = standingDirtyPrice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_standing", nullable = false)
    public int getIdStanding() {
        return idStanding;
    }

    public void setIdStanding(int idStanding) {
        this.idStanding = idStanding;
    }

    @Basic
    @Column(name = "fk_id_standing_type", nullable = false)
    public int getFkIdStandingType() {
        return fkIdStandingType;
    }

    public void setFkIdStandingType(int fkIdStandingType) {
        this.fkIdStandingType = fkIdStandingType;
    }

    @Basic
    @Column(name = "fk_id_valmer_price_vector", nullable = false)
    public int getFkIdValmerPriceVector() {
        return fkIdValmerPriceVector;
    }

    public void setFkIdValmerPriceVector(int fkIdValmerPriceVector) {
        this.fkIdValmerPriceVector = fkIdValmerPriceVector;
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
    @Column(name = "amount", nullable = false)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "value", nullable = false, precision = 0)
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Basic
    @Column(name = "fk_id_standing_status", nullable = false)
    public int getFkIdStandingStatus() {
        return fkIdStandingStatus;
    }

    public void setFkIdStandingStatus(int fkIdStandingStatus) {
        this.fkIdStandingStatus = fkIdStandingStatus;
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

        StandingEntity that = (StandingEntity) o;

        if (idStanding != that.idStanding) return false;
        if (fkIdStandingType != that.fkIdStandingType) return false;
        if (fkIdValmerPriceVector != that.fkIdValmerPriceVector) return false;
        if (fkIdUser != that.fkIdUser) return false;
        if (amount != that.amount) return false;
        if (Double.compare(that.value, value) != 0) return false;
        if (fkIdStandingStatus != that.fkIdStandingStatus) return false;
        if (datetime != null ? !datetime.equals(that.datetime) : that.datetime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = idStanding;
        result = 31 * result + fkIdStandingType;
        result = 31 * result + fkIdValmerPriceVector;
        result = 31 * result + fkIdUser;
        result = 31 * result + amount;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + fkIdStandingStatus;
        result = 31 * result + (datetime != null ? datetime.hashCode() : 0);
        return result;
    }

    @Override //TODO apply rules
    public int compareTo(StandingEntity o) {
        if (this.equals(o)) {
            return 0;
        } else if (this.getValue() > o.getValue()) {
            return 1;
        } else if (this.getValue() < o.getValue()) {
            return -1;
        } else {
            int dateCompare = this.getDatetime().compareTo(o.getDatetime());
            if (dateCompare != 0) {
                return dateCompare;
            } else {
                return this.getAmount() - o.getAmount();//returns the one with the smallest amount first in sort
            }
        }
    }

    /**
     * Returns a copy of this standing minus the database id
     * @return
     */
    @Override
    public StandingEntity clone(){
        return new StandingEntity(this.fkIdStandingType,
                this.fkIdValmerPriceVector,
                this.fkIdUser,
                this.amount,
                this.value,
                this.fkIdStandingStatus,
                this.datetime,
                this.currentAmount,
                this.standingDirtyPrice);
    }


    @Basic
    @Column(name = "current_amount", nullable = false)
    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    @Basic
    @Column(name = "standing_dirty_price", nullable = true, precision = 0)
    public Double getStandingDirtyPrice() {
        return standingDirtyPrice;
    }

    public void setStandingDirtyPrice(Double standingDirtyPrice) {
        this.standingDirtyPrice = standingDirtyPrice;
    }
}
