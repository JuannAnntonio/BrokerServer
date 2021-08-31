package mx.sigmact.broker.pojo.udi;

public class Udi {

	private Integer udi;
	private String date;
	private double value;
	
	public Udi(Integer udi, String date, Double value) {
		this.udi = udi;
		this.date = date;
		this.value = value;
	}

	public Integer getUdi() {
		return udi;
	}

	public void setUdi(Integer udi) {
		this.udi = udi;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
