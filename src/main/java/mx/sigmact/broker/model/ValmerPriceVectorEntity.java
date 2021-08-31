package mx.sigmact.broker.model;

import javax.persistence.*;

import org.apache.log4j.Logger;

import mx.sigmact.broker.core.lib.DirtyPriceCalculator;

import java.util.Calendar;

/**
 * The valmer price vector.
 * Created on 15/10/16.
 */
@Entity
@Table(name = "VALMER_PRICE_VECTOR", schema = "SIGMACT_BROKER")
public class ValmerPriceVectorEntity {
    Logger log = Logger.getLogger(ValmerPriceVectorEntity.class);
    
    private int idValmerPriceVector;
    private Calendar date;
    private String issue;
    private String tv;
    private String issuingCompany;
    private String series;
    private String instrumentType;
    private double marketSurcharge;
    private double placementSurcharge;
    private double incomeTax;
    private double totalSurcharge;
    private double yield;
    private double couponRate;
    private int termDays;
    private Calendar placementDate;
    private Calendar expirationDate;
    private int daysToExpire;
    private String discountCurve;
    private String couponPeriod;
    private String fitch;
    private String sp;
    private String moodis;
    private String hr;
    private int cve;
    private Calendar couponStart;
    private Calendar couponEnd;
    private double dirtyPrice;
    private double cleanPrice;
    private double interest;
    private double adjustedTerm;
    private double convexity;
    private Double updatedNominalValue;
    private Long signedValue;
    private Double oustandingAmount;
    private String commonRepresentative;
    private String placementAgent;
    private int couponsDueToExpire;
    private int expiredCoupons;
    private int daysToContractClosing;
    private String sector;
    private String isin;
    private String verum;
    private int fkIdMarketType;
    
    public ValmerPriceVectorEntity() {
    	
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_valmer_price_vector", nullable = true)
    public int getIdValmerPriceVector() {

        log.info("[ValmerPriceVectorEntity][getIdValmerPriceVector]");
        
        return idValmerPriceVector;
    }

    public void setIdValmerPriceVector(int idValmerPriceVector) {
        this.idValmerPriceVector = idValmerPriceVector;
    }

    @Basic
    @Column(name = "date", nullable = true)
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Basic
    @Column(name = "issue", nullable = true, length = 25)
    public String getIssue() {
        return issue;
    }

    public void setIssue(String name) {
        this.issue = name;
    }

    @Basic
    @Column(name = "tv", nullable = true, length = 4)
    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }

    @Basic
    @Column(name = "issuing_company", nullable = true, length = 15)
    public String getIssuingCompany() {
        return issuingCompany;
    }

    public void setIssuingCompany(String issuingCompany) {
        this.issuingCompany = issuingCompany;
    }

    @Basic
    @Column(name = "series", nullable = true, length = 10)
    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    @Basic
    @Column(name = "instrument_type", nullable = true, length = 25)
    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    @Basic
    @Column(name = "market_surcharge", nullable = true, precision = 0)
    public double getMarketSurcharge() {
        return marketSurcharge;
    }

    public void setMarketSurcharge(double marketSurcharge) {
        this.marketSurcharge = marketSurcharge;
    }

    @Basic
    @Column(name = "placement_surcharge", nullable = true, precision = 0)
    public double getPlacementSurcharge() {
        return placementSurcharge;
    }

    public void setPlacementSurcharge(double placementSurcharge) {
        this.placementSurcharge = placementSurcharge;
    }

    @Basic
    @Column(name = "income_tax", nullable = true, precision = 0)
    public double getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(double incomeTax) {
        this.incomeTax = incomeTax;
    }

    @Basic
    @Column(name = "total_surcharge", nullable = true, precision = 0)
    public double getTotalSurcharge() {
        return totalSurcharge;
    }

    public void setTotalSurcharge(double totalSurcharge) {
        this.totalSurcharge = totalSurcharge;
    }

    @Basic
    @Column(name = "yield", nullable = true, precision = 0)
    public double getYield() {
        return yield;
    }

    public void setYield(double yield) {
        this.yield = yield;
    }

    @Basic
    @Column(name = "coupon_rate", nullable = true, precision = 0)
    public double getCouponRate() {
        return couponRate;
    }

    public void setCouponRate(double couponRate) {
        this.couponRate = couponRate;
    }

    @Basic
    @Column(name = "term_days", nullable = true)
    public int getTermDays() {
        return termDays;
    }

    public void setTermDays(int termDays) {
        this.termDays = termDays;
    }

    @Basic
    @Column(name = "placement_date", nullable = true)
    public Calendar getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(Calendar palcementDate) {
        this.placementDate = palcementDate;
    }

    @Basic
    @Column(name = "expiration_date", nullable = true)
    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Basic
    @Column(name = "days_to_expire", nullable = true)
    public int getDaysToExpire() {
        return daysToExpire;
    }

    public void setDaysToExpire(int daysToExpire) {
        this.daysToExpire = daysToExpire;
    }

    @Basic
    @Column(name = "discount_curve", nullable = true, length = 35)
    public String getDiscountCurve() {
        return discountCurve;
    }

    public void setDiscountCurve(String discountCurve) {
        this.discountCurve = discountCurve;
    }

    @Basic
    @Column(name = "coupon_period", nullable = true, length = 10)
    public String getCouponPeriod() {
        return couponPeriod;
    }

    public void setCouponPeriod(String couponPeriod) {
        this.couponPeriod = couponPeriod;
    }

    @Basic
    @Column(name = "fitch", nullable = true, length = 15)
    public String getFitch() {
        return fitch;
    }

    public void setFitch(String fitch) {
        this.fitch = fitch;
    }

    @Basic
    @Column(name = "sp", nullable = true, length = 15)
    public String getSp() {
        return sp;
    }

    public void setSp(String sp) {
        this.sp = sp;
    }

    @Basic
    @Column(name = "moodis", nullable = true, length = 15)
    public String getMoodis() {
        return moodis;
    }

    public void setMoodis(String moodis) {
        this.moodis = moodis;
    }

    @Basic
    @Column(name = "hr", nullable = true, length = 20)
    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    @Basic
    @Column(name = "cve", nullable = true)
    public int getCve() {
        return cve;
    }

    public void setCve(int cve) {
        this.cve = cve;
    }

    @Basic
    @Column(name = "coupon_start", nullable = true)
    public Calendar getCouponStart() {
        return couponStart;
    }

    public void setCouponStart(Calendar couponStart) {
        this.couponStart = couponStart;
    }

    @Basic
    @Column(name = "coupon_end", nullable = true)
    public Calendar getCouponEnd() {
        return couponEnd;
    }

    public void setCouponEnd(Calendar couponEnd) {
        this.couponEnd = couponEnd;
    }

    @Basic
    @Column(name = "dirty_price", nullable = true, precision = 0)
    public double getDirtyPrice() {
        return dirtyPrice;
    }

    public void setDirtyPrice(double dirtyPrice) {
        this.dirtyPrice = dirtyPrice;
    }

    @Basic
    @Column(name = "clean_price", nullable = true, precision = 0)
    public double getCleanPrice() {
        return cleanPrice;
    }

    public void setCleanPrice(double cleanPrice) {
        this.cleanPrice = cleanPrice;
    }

    @Basic
    @Column(name = "interest", nullable = true, precision = 0)
    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    @Basic
    @Column(name = "adjusted_term", nullable = true, precision = 0)
    public double getAdjustedTerm() {
        return adjustedTerm;
    }

    public void setAdjustedTerm(double adjustedTerm) {
        this.adjustedTerm = adjustedTerm;
    }

    @Basic
    @Column(name = "convexity", nullable = true, precision = 0)
    public double getConvexity() {
        return convexity;
    }

    public void setConvexity(double convexity) {
        this.convexity = convexity;
    }

    @Basic
    @Column(name = "updated_nominal_value", nullable = true, precision = 0)
    public Double getUpdatedNominalValue() {
        return updatedNominalValue;
    }

    public void setUpdatedNominalValue(Double updatedNominalValue) {
        this.updatedNominalValue = updatedNominalValue;
    }

    @Basic
    @Column(name = "signed_value", nullable = true)
    public Long getSignedValue() {
        return signedValue;
    }

    public void setSignedValue(Long signedValue) {
        this.signedValue = signedValue;
    }

    @Basic
    @Column(name = "oustanding_amount", nullable = true, precision = 0)
    public Double getOustandingAmount() {
        return oustandingAmount;
    }

    public void setOustandingAmount(Double oustandingAmount) {
        this.oustandingAmount = oustandingAmount;
    }

    @Basic
    @Column(name = "common_representative", nullable = true, length = 15)
    public String getCommonRepresentative() {
        return commonRepresentative;
    }

    public void setCommonRepresentative(String commonRepresentative) {
        this.commonRepresentative = commonRepresentative;
    }

    @Basic
    @Column(name = "placement_agent", nullable = true, length = 15)
    public String getPlacementAgent() {
        return placementAgent;
    }

    public void setPlacementAgent(String placementAgent) {
        this.placementAgent = placementAgent;
    }

    @Basic
    @Column(name = "coupons_due_to_expire", nullable = true)
    public int getCouponsDueToExpire() {
        return couponsDueToExpire;
    }

    public void setCouponsDueToExpire(int couponsDueToExpire) {
        this.couponsDueToExpire = couponsDueToExpire;
    }

    @Basic
    @Column(name = "expired_coupons", nullable = true)
    public int getExpiredCoupons() {
        return expiredCoupons;
    }

    public void setExpiredCoupons(int expiredCoupons) {
        this.expiredCoupons = expiredCoupons;
    }

    @Basic
    @Column(name = "days_to_contract_closing", nullable = true)
    public int getDaysToContractClosing() {
        return daysToContractClosing;
    }

    public void setDaysToContractClosing(int daysToContractClosing) {
        this.daysToContractClosing = daysToContractClosing;
    }

    @Basic
    @Column(name = "sector", nullable = true, length = 10)
    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    @Basic
    @Column(name = "ISIN", nullable = true, length = 12)
    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    @Basic
    @Column(name = "VERUM", nullable = true, length = 15)
    public String getVerum() {
        return verum;
    }

    public void setVerum(String verum) {
        this.verum = verum;
    }

    @Basic
    @Column(name = "fk_id_market_type", nullable = true)
    public int getFkIdMarketType() {
        return fkIdMarketType;
    }

    public void setFkIdMarketType(int fkIdMarketType) {
        this.fkIdMarketType = fkIdMarketType;
    }

    @Override
    public boolean equals(Object o) {
    	

        log.info("[ValmerPriceVectorEntity][equals]");
    	
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValmerPriceVectorEntity that = (ValmerPriceVectorEntity) o;

        if (idValmerPriceVector != that.idValmerPriceVector) return false;
        if (Double.compare(that.marketSurcharge, marketSurcharge) != 0) return false;
        if (Double.compare(that.placementSurcharge, placementSurcharge) != 0) return false;
        if (Double.compare(that.incomeTax, incomeTax) != 0) return false;
        if (Double.compare(that.totalSurcharge, totalSurcharge) != 0) return false;
        if (Double.compare(that.yield, yield) != 0) return false;
        if (Double.compare(that.couponRate, couponRate) != 0) return false;
        if (termDays != that.termDays) return false;
        if (daysToExpire != that.daysToExpire) return false;
        if (cve != that.cve) return false;
        if (Double.compare(that.dirtyPrice, dirtyPrice) != 0) return false;
        if (Double.compare(that.cleanPrice, cleanPrice) != 0) return false;
        if (Double.compare(that.interest, interest) != 0) return false;
        if (Double.compare(that.adjustedTerm, adjustedTerm) != 0) return false;
        if (Double.compare(that.convexity, convexity) != 0) return false;
        if (couponsDueToExpire != that.couponsDueToExpire) return false;
        if (expiredCoupons != that.expiredCoupons) return false;
        if (daysToContractClosing != that.daysToContractClosing) return false;
        if (fkIdMarketType != that.fkIdMarketType) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (issue != null ? !issue.equals(that.issue) : that.issue != null) return false;
        if (tv != null ? !tv.equals(that.tv) : that.tv != null) return false;
        if (issuingCompany != null ? !issuingCompany.equals(that.issuingCompany) : that.issuingCompany != null)
            return false;
        if (series != null ? !series.equals(that.series) : that.series != null) return false;
        if (instrumentType != null ? !instrumentType.equals(that.instrumentType) : that.instrumentType != null)
            return false;
        if (placementDate != null ? !placementDate.equals(that.placementDate) : that.placementDate != null)
            return false;
        if (expirationDate != null ? !expirationDate.equals(that.expirationDate) : that.expirationDate != null)
            return false;
        if (discountCurve != null ? !discountCurve.equals(that.discountCurve) : that.discountCurve != null)
            return false;
        if (couponPeriod != null ? !couponPeriod.equals(that.couponPeriod) : that.couponPeriod != null) return false;
        if (fitch != null ? !fitch.equals(that.fitch) : that.fitch != null) return false;
        if (sp != null ? !sp.equals(that.sp) : that.sp != null) return false;
        if (moodis != null ? !moodis.equals(that.moodis) : that.moodis != null) return false;
        if (hr != null ? !hr.equals(that.hr) : that.hr != null) return false;
        if (couponStart != null ? !couponStart.equals(that.couponStart) : that.couponStart != null) return false;
        if (couponEnd != null ? !couponEnd.equals(that.couponEnd) : that.couponEnd != null) return false;
        if (updatedNominalValue != null ? !updatedNominalValue.equals(that.updatedNominalValue) : that.updatedNominalValue != null)
            return false;
        if (signedValue != null ? !signedValue.equals(that.signedValue) : that.signedValue != null) return false;
        if (oustandingAmount != null ? !oustandingAmount.equals(that.oustandingAmount) : that.oustandingAmount != null)
            return false;
        if (commonRepresentative != null ? !commonRepresentative.equals(that.commonRepresentative) : that.commonRepresentative != null)
            return false;
        if (placementAgent != null ? !placementAgent.equals(that.placementAgent) : that.placementAgent != null)
            return false;
        if (sector != null ? !sector.equals(that.sector) : that.sector != null) return false;
        if (isin != null ? !isin.equals(that.isin) : that.isin != null) return false;
        if (verum != null ? !verum.equals(that.verum) : that.verum != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
    	

        log.info("[ValmerPriceVectorEntity][hashCode]");
    	
        int result;
        long temp;
        result = idValmerPriceVector;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (issue != null ? issue.hashCode() : 0);
        result = 31 * result + (tv != null ? tv.hashCode() : 0);
        result = 31 * result + (issuingCompany != null ? issuingCompany.hashCode() : 0);
        result = 31 * result + (series != null ? series.hashCode() : 0);
        result = 31 * result + (instrumentType != null ? instrumentType.hashCode() : 0);
        temp = Double.doubleToLongBits(marketSurcharge);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(placementSurcharge);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(incomeTax);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(totalSurcharge);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(yield);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(couponRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + termDays;
        result = 31 * result + (placementDate != null ? placementDate.hashCode() : 0);
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
        result = 31 * result + daysToExpire;
        result = 31 * result + (discountCurve != null ? discountCurve.hashCode() : 0);
        result = 31 * result + (couponPeriod != null ? couponPeriod.hashCode() : 0);
        result = 31 * result + (fitch != null ? fitch.hashCode() : 0);
        result = 31 * result + (sp != null ? sp.hashCode() : 0);
        result = 31 * result + (moodis != null ? moodis.hashCode() : 0);
        result = 31 * result + (hr != null ? hr.hashCode() : 0);
        result = 31 * result + cve;
        result = 31 * result + (couponStart != null ? couponStart.hashCode() : 0);
        result = 31 * result + (couponEnd != null ? couponEnd.hashCode() : 0);
        temp = Double.doubleToLongBits(dirtyPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(cleanPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(interest);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(adjustedTerm);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(convexity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (updatedNominalValue != null ? updatedNominalValue.hashCode() : 0);
        result = 31 * result + (signedValue != null ? signedValue.hashCode() : 0);
        result = 31 * result + (oustandingAmount != null ? oustandingAmount.hashCode() : 0);
        result = 31 * result + (commonRepresentative != null ? commonRepresentative.hashCode() : 0);
        result = 31 * result + (placementAgent != null ? placementAgent.hashCode() : 0);
        result = 31 * result + couponsDueToExpire;
        result = 31 * result + expiredCoupons;
        result = 31 * result + daysToContractClosing;
        result = 31 * result + (sector != null ? sector.hashCode() : 0);
        result = 31 * result + (isin != null ? isin.hashCode() : 0);
        result = 31 * result + (verum != null ? verum.hashCode() : 0);
        result = 31 * result + fkIdMarketType;
        return result;
    }
}
