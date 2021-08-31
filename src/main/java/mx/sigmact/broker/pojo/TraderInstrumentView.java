package mx.sigmact.broker.pojo;

/**
 * Created on 08/12/16.
 */
public class TraderInstrumentView {
    private Integer idVPV;
    private Integer dxv;
    private String name;
    private Double rate;
    private Double nuRango;

    public TraderInstrumentView() {
    }

    public TraderInstrumentView(Integer idVPV, Integer dxv, String name) {
        this.idVPV = idVPV;
        this.dxv = dxv;
        this.name = name;
    }

    public TraderInstrumentView(Integer idVPV, Integer dxv, String name, Double rate, Double nuRango) {
        this.idVPV = idVPV;
        this.dxv = dxv;
        this.name = name;
        this.rate = rate;
        this.nuRango = nuRango;
    }
    public Integer getIdVPV() {
        return idVPV;
    }

    public void setIdVPV(Integer idVPV) {
        this.idVPV = idVPV;
    }

    public Integer getDxv() {
        return dxv;
    }

    public void setDxv(Integer dxv) {
        this.dxv = dxv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRate() { return rate; }

    public void setRate(Double rate) { this.rate = rate; }
    
    public Double getNuRango() {
		return nuRango;
	}

	public void setNuRango(Double nuRango) {
		this.nuRango = nuRango;
	}
}
