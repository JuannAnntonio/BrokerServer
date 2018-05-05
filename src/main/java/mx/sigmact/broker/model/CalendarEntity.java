package mx.sigmact.broker.model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * The calendar entity is meant to be used to mark holidays and other important dates in
 * tha application and apply a behavior depending on the situation.
 *
 * Created on 15/10/16.
 */
@Entity
@Table(name = "CALENDAR", schema = "SIGMACT_BROKER")
public class CalendarEntity {
    private int idHolidays;
    private Calendar date;
    private String description;
    private int fkIdMarketType;
    private byte isHoliday;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_holidays", nullable = false)
    public int getIdHolidays() {
        return idHolidays;
    }

    public void setIdHolidays(int idHolidays) {
        this.idHolidays = idHolidays;
    }

    @Basic
    @Column(name = "date", nullable = false)
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "fk_id_market_type", nullable = false)
    public int getFkIdMarketType() {
        return fkIdMarketType;
    }

    public void setFkIdMarketType(int fkIdMarketType) {
        this.fkIdMarketType = fkIdMarketType;
    }

    @Basic
    @Column(name = "is_holiday", nullable = false)
    public byte getIsHoliday() {
        return isHoliday;
    }

    public void setIsHoliday(byte isHoliday) {
        this.isHoliday = isHoliday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarEntity that = (CalendarEntity) o;

        if (idHolidays != that.idHolidays) return false;
        if (fkIdMarketType != that.fkIdMarketType) return false;
        if (isHoliday != that.isHoliday) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idHolidays;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + fkIdMarketType;
        result = 31 * result + (int) isHoliday;
        return result;
    }
}
