package mx.sigmact.broker.pojo.parameter;

public class ValueParameter {

    private String id_parameter;
    private String value;
    
    public ValueParameter(String id_parameter, String value) {

        this.id_parameter = id_parameter;
        this.value = value;
    }

	public String getId_parameter() {
		return id_parameter;
	}

	public void setId_parameter(String id_parameter) {
		this.id_parameter = id_parameter;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
