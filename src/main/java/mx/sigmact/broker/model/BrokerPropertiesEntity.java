package mx.sigmact.broker.model;

import javax.persistence.*;

/**
 * Created on 01/11/16.
 */
@Entity
@Table(name = "BROKER_PROPERTIES", schema = "SIGMACT_BROKER")
public class BrokerPropertiesEntity {
    private int idBrokerProperties;
    private int waitingSecondsAfterAggresion;
    private int waitingSecondsAfterBiddingIncrease;
    private int waitingSecondsAfterBidding;
    private int activeValueTypeCount;
    private int bidAliveTime;
    private String valmerPriceVectorService;
    private String bancoDeMexicoWsdl;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_broker_properties", nullable = false)
    public int getIdBrokerProperties() {
        return idBrokerProperties;
    }

    public void setIdBrokerProperties(int idBrokerProperties) {
        this.idBrokerProperties = idBrokerProperties;
    }

    @Basic
    @Column(name = "waiting_seconds_after_aggresion", nullable = false)
    public int getWaitingSecondsAfterAggresion() {
        return waitingSecondsAfterAggresion;
    }

    public void setWaitingSecondsAfterAggresion(int waitingSecondsAfterAggresion) {
        this.waitingSecondsAfterAggresion = waitingSecondsAfterAggresion;
    }

    @Basic
    @Column(name = "waiting_seconds_after_bidding_increase", nullable = false)
    public int getWaitingSecondsAfterBiddingIncrease() {
        return waitingSecondsAfterBiddingIncrease;
    }

    public void setWaitingSecondsAfterBiddingIncrease(int waitingSecondsAfterBiddingIncrease) {
        this.waitingSecondsAfterBiddingIncrease = waitingSecondsAfterBiddingIncrease;
    }

    @Basic
    @Column(name = "waiting_seconds_after_bidding", nullable = false)
    public int getWaitingSecondsAfterBidding() {
        return waitingSecondsAfterBidding;
    }

    public void setWaitingSecondsAfterBidding(int waitingSecondsAfterBidding) {
        this.waitingSecondsAfterBidding = waitingSecondsAfterBidding;
    }

    @Basic
    @Column(name = "active_value_type_count", nullable = false)
    public int getActiveValueTypeCount() {
        return activeValueTypeCount;
    }

    public void setActiveValueTypeCount(int activeValueTypeCount) {
        this.activeValueTypeCount = activeValueTypeCount;
    }

    @Basic
    @Column(name = "bid_alive_time", nullable = false)
    public int getBidAliveTime() {
        return bidAliveTime;
    }

    public void setBidAliveTime(int bidAliveTime) {
        this.bidAliveTime = bidAliveTime;
    }

    @Basic
    @Column(name = "valmer_price_vector_service", nullable = false, length = 255)
    public String getValmerPriceVectorService() {
        return valmerPriceVectorService;
    }

    public void setValmerPriceVectorService(String valmerPriceVectorService) {
        this.valmerPriceVectorService = valmerPriceVectorService;
    }

    @Basic
    @Column(name = "banco_de_mexico_wsdl", nullable = false, length = 255)
    public String getBancoDeMexicoWsdl() {
        return bancoDeMexicoWsdl;
    }

    public void setBancoDeMexicoWsdl(String bancoDeMexicoWsdl) {
        this.bancoDeMexicoWsdl = bancoDeMexicoWsdl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BrokerPropertiesEntity that = (BrokerPropertiesEntity) o;

        if (idBrokerProperties != that.idBrokerProperties) return false;
        if (waitingSecondsAfterAggresion != that.waitingSecondsAfterAggresion) return false;
        if (waitingSecondsAfterBiddingIncrease != that.waitingSecondsAfterBiddingIncrease) return false;
        if (waitingSecondsAfterBidding != that.waitingSecondsAfterBidding) return false;
        if (activeValueTypeCount != that.activeValueTypeCount) return false;
        if (bidAliveTime != that.bidAliveTime) return false;
        if (valmerPriceVectorService != null ? !valmerPriceVectorService.equals(that.valmerPriceVectorService) : that.valmerPriceVectorService != null)
            return false;
        if (bancoDeMexicoWsdl != null ? !bancoDeMexicoWsdl.equals(that.bancoDeMexicoWsdl) : that.bancoDeMexicoWsdl != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idBrokerProperties;
        result = 31 * result + waitingSecondsAfterAggresion;
        result = 31 * result + waitingSecondsAfterBiddingIncrease;
        result = 31 * result + waitingSecondsAfterBidding;
        result = 31 * result + activeValueTypeCount;
        result = 31 * result + bidAliveTime;
        result = 31 * result + (valmerPriceVectorService != null ? valmerPriceVectorService.hashCode() : 0);
        result = 31 * result + (bancoDeMexicoWsdl != null ? bancoDeMexicoWsdl.hashCode() : 0);
        return result;
    }
}
