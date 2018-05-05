package mx.sigmact.broker.pojo;

/**
 * Created on 08/12/16.
 */
public class TraderInstrumentView {
    private Integer idVPV;
    private Integer dxv;
    private String name;

    public TraderInstrumentView() {
    }

    public TraderInstrumentView(Integer idVPV, Integer dxv, String name) {
        this.idVPV = idVPV;
        this.dxv = dxv;
        this.name = name;
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
}
