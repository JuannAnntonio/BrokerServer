package mx.sigmact.broker.pojo.instrument;

public class Instrument {

    private Integer id_instrument;
    private String issuing_company;
    private String tv;
    
    public Instrument(Integer id_instrument, String issuing_company, String tv) {

        this.id_instrument = id_instrument;
        this.issuing_company = issuing_company;
        this.tv = tv;
    }

	public Integer getId_instrument() {
		return id_instrument;
	}

	public void setId_instrument(Integer id_instrument) {
		this.id_instrument = id_instrument;
	}

	public String getIssuing_company() {
		return issuing_company;
	}

	public void setIssuing_company(String issuing_company) {
		this.issuing_company = issuing_company;
	}

	public String getTv() {
		return tv;
	}

	public void setTv(String tv) {
		this.tv = tv;
	}
	
}
