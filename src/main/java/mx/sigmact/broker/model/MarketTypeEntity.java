package mx.sigmact.broker.model;

import javax.persistence.*;

/**
 * Market entity is used as reference
 * Created on 15/10/16.
 */
@Entity
@Table(name = "MARKET_TYPE", schema = "SIGMACT_BROKER")
public class MarketTypeEntity {
    private int idMarketType;
    private String name;
    private String country;
    private int timeZone;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_market_type", nullable = false)
    public int getIdMarketType() {
        return idMarketType;
    }

    public void setIdMarketType(int idMarketType) {
        this.idMarketType = idMarketType;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "country", nullable = false, length = 45)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "time_zone", nullable = false)
    public int getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarketTypeEntity that = (MarketTypeEntity) o;

        if (idMarketType != that.idMarketType) return false;
        if (timeZone != that.timeZone) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idMarketType;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + timeZone;
        return result;
    }
}
