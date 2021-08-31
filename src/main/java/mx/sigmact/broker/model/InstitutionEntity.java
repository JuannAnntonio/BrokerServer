package mx.sigmact.broker.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created on 30/11/16.
 */
@Entity
@Table(name = "INSTITUTION", schema = "SIGMACT_BROKER")
public class InstitutionEntity implements Serializable {

	private static final long serialVersionUID = -2716053459082375889L;

	private int idInstitution;
	private String key;
	private String name;
	private String phoneNumber;
	private double systemCommission;
	private short enabled;
	private Integer idFlat;
	private Integer idPPrueba;
	private Double rango;

	@Id
	@Column(name = "id_institution", nullable = false)
	public int getIdInstitution() {
		return idInstitution;
	}

	public void setIdInstitution(int idInstitution) {
		this.idInstitution = idInstitution;
	}

	@Basic
	@Column(name = "institution_key", nullable = false, length = 20)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Basic
	@Column(name = "name", nullable = false, length = 20)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "phone_number", nullable = false, length = 45)
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Basic
	@Column(name = "system_commission", nullable = false, precision = 0)
	public double getSystemCommission() {
		return systemCommission;
	}

	public void setSystemCommission(double systemCommission) {
		this.systemCommission = systemCommission;
	}

	@Basic
	@Column(name = "enabled", nullable = false)
	public short getEnabled() {
		return enabled;
	}

	public void setEnabled(short enabled) {
		this.enabled = enabled;
	}

	@Column(name = "Id_Flat")
	public Integer getIdFlat() {
		return idFlat;
	}

	public void setIdFlat(Integer idFlat) {
		this.idFlat = idFlat;
	}

	@Column(name = "Id_PPrueba")
	public Integer getIdPPrueba() {
		return idPPrueba;
	}

	public void setIdPPrueba(Integer idPPrueba) {
		this.idPPrueba = idPPrueba;
	}

	@Column(name = "Nu_Rango")
	public Double getRango() {
		return rango;
	}

	public void setRango(Double rango) {
		this.rango = rango;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		InstitutionEntity that = (InstitutionEntity) o;

		if (idInstitution != that.idInstitution)
			return false;
		if (Double.compare(that.systemCommission, systemCommission) != 0)
			return false;
		if (enabled != that.enabled)
			return false;
		if (key != null ? !key.equals(that.key) : that.key != null)
			return false;
		if (name != null ? !name.equals(that.name) : that.name != null)
			return false;
		if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null)
			return false;
		if (idFlat != null ? !idFlat.equals(that.idFlat) : that.idFlat != null)
			return false;
		if (idPPrueba != null ? !idPPrueba.equals(that.idPPrueba) : that.idPPrueba != null)
			return false;
		if (rango != null ? !rango.equals(that.rango) : that.rango != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = idInstitution;
		result = 31 * result + (key != null ? key.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
		result = 31 * result + (idFlat != null ? idFlat.hashCode() : 0);
		result = 31 * result + (idPPrueba != null ? idPPrueba.hashCode() : 0);
		result = 31 * result + (rango != null ? rango.hashCode() : 0);
		temp = Double.doubleToLongBits(systemCommission);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (int) enabled;
		return result;
	}

}
