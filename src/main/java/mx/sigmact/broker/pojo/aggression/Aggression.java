package mx.sigmact.broker.pojo.aggression;

import java.util.Calendar;

public class Aggression {

    private int idAggression;
    private int fkIdStanding;
    private int fkIdTransactionStatus;
    private int fkIdUser;
    private int fkIdInstitution;
    private Calendar datetime;
    private Integer amount;
    private Double aggressionDirtyPrice;

    public Aggression() {
    }

    public Aggression(int fkIdStanding, int fkIdTransactionStatus, int fkIdUser, int fkIdInstitution, Calendar datetime, Integer amount, Double aggressionDirtyPrice) {
        this.fkIdStanding = fkIdStanding;
        this.fkIdTransactionStatus = fkIdTransactionStatus;
        this.fkIdUser = fkIdUser;
        this.fkIdInstitution = fkIdInstitution;
        this.datetime = datetime;
        this.amount = amount;
        this.aggressionDirtyPrice = aggressionDirtyPrice;
    }

	public int getIdAggression() {
		return idAggression;
	}

	public void setIdAggression(int idAggression) {
		this.idAggression = idAggression;
	}

	public int getFkIdStanding() {
		return fkIdStanding;
	}

	public void setFkIdStanding(int fkIdStanding) {
		this.fkIdStanding = fkIdStanding;
	}

	public int getFkIdTransactionStatus() {
		return fkIdTransactionStatus;
	}

	public void setFkIdTransactionStatus(int fkIdTransactionStatus) {
		this.fkIdTransactionStatus = fkIdTransactionStatus;
	}

	public int getFkIdUser() {
		return fkIdUser;
	}

	public void setFkIdUser(int fkIdUser) {
		this.fkIdUser = fkIdUser;
	}

	public int getFkIdInstitution() {
		return fkIdInstitution;
	}

	public void setFkIdInstitution(int fkIdInstitution) {
		this.fkIdInstitution = fkIdInstitution;
	}

	public Calendar getDatetime() {
		return datetime;
	}

	public void setDatetime(Calendar datetime) {
		this.datetime = datetime;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Double getAggressionDirtyPrice() {
		return aggressionDirtyPrice;
	}

	public void setAggressionDirtyPrice(Double aggressionDirtyPrice) {
		this.aggressionDirtyPrice = aggressionDirtyPrice;
	}
    
    
	
}
