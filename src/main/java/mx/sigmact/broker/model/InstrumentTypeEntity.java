package mx.sigmact.broker.model;

import javax.persistence.*;

/**
 * The instrument type is used to create the instrument type string by using the issuing company and the type of
 * value (tv).
 * Created on 15/10/16.
 */
@Entity
@Table(name = "INSTRUMENT_TYPE", schema = "SIGMACT_BROKER")
public class InstrumentTypeEntity {
    private int idInstrument;
    private String issuingCompany;
    private String tv;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_instrument", nullable = false)
    public int getIdInstrument() {
        return idInstrument;
    }

    public void setIdInstrument(int idInstrument) {
        this.idInstrument = idInstrument;
    }

    @Basic
    @Column(name = "issuing_company", nullable = false, length = 25)
    public String getIssuingCompany() {
        return issuingCompany;
    }

    public void setIssuingCompany(String issuingCompany) {
        this.issuingCompany = issuingCompany;
    }

    @Basic
    @Column(name = "tv", nullable = false, length = 10)
    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InstrumentTypeEntity that = (InstrumentTypeEntity) o;

        if (idInstrument != that.idInstrument) return false;
        if (issuingCompany != null ? !issuingCompany.equals(that.issuingCompany) : that.issuingCompany != null)
            return false;
        if (tv != null ? !tv.equals(that.tv) : that.tv != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idInstrument;
        result = 31 * result + (issuingCompany != null ? issuingCompany.hashCode() : 0);
        result = 31 * result + (tv != null ? tv.hashCode() : 0);
        return result;
    }
}
