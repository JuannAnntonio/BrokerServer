package mx.sigmact.broker.pojo;

public class MarketPositionUser {

	private Integer idStanding;
	private Integer idStandingType;
	private Integer idValmerPriceVector;
	private Integer idUser;
	private Integer idInstitution;
	private String nameInstitution;

	public Integer getIdStanding() {
		return idStanding;
	}

	public void setIdStanding(Integer idStanding) {
		this.idStanding = idStanding;
	}

	public Integer getIdStandingType() {
		return idStandingType;
	}

	public void setIdStandingType(Integer idStandingType) {
		this.idStandingType = idStandingType;
	}

	public Integer getIdValmerPriceVector() {
		return idValmerPriceVector;
	}

	public void setIdValmerPriceVector(Integer idValmerPriceVector) {
		this.idValmerPriceVector = idValmerPriceVector;
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public Integer getIdInstitution() {
		return idInstitution;
	}

	public void setIdInstitution(Integer idInstitution) {
		this.idInstitution = idInstitution;
	}

	public String getNameInstitution() {
		return nameInstitution;
	}

	public void setNameInstitution(String nameInstitution) {
		this.nameInstitution = nameInstitution;
	}

}
