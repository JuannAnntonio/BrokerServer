package mx.sigmact.broker.pojo.fondeo;

public class FondeoCetes {

    private String date;
    private String rate;
    
    public FondeoCetes(String date, String rate) {

        this.date = date;
        this.rate = rate;
    }

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	
}
