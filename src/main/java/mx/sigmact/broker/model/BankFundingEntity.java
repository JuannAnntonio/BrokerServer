package mx.sigmact.broker.model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by norberto on 28/02/2017.
 */
@Entity
@Table(name = "BANK_FUNDING", schema = "SIGMACT_BROKER", catalog = "")
public class BankFundingEntity {
    private Calendar date;
    private Double rate;
    private int idFondeoBancario;

    @Basic
    @Column(name = "date")
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Basic
    @Column(name = "rate")
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Id
    @Column(name = "id_fondeo_bancario")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getIdFondeoBancario() {
        return idFondeoBancario;
    }

    public void setIdFondeoBancario(int idFondeoBancario) {
        this.idFondeoBancario = idFondeoBancario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BankFundingEntity that = (BankFundingEntity) o;

        if (idFondeoBancario != that.idFondeoBancario) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (rate != null ? !rate.equals(that.rate) : that.rate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (rate != null ? rate.hashCode() : 0);
        result = 31 * result + idFondeoBancario;
        return result;
    }
}
