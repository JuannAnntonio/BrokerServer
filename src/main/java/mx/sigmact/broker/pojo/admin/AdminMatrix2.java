package mx.sigmact.broker.pojo.admin;

/**
 * Class for the institution view
 * Created on 02/11/16.
 */
public class AdminMatrix2 {
	private String idComision;
    private String institutionName;
    private String instrument;
    private String comision;
    private String institutionName2;

    public AdminMatrix2(String idComision,
    						 String institutionName,
                             String instrument,
                             String comision,
                             String institutionName2) {
        this.idComision =  idComision;
    	this.institutionName = institutionName;
        this.instrument = instrument;
        this.comision = comision;
        this.institutionName2 = institutionName2;
    }
    
    public String getIdComision() {
        return idComision;
    }
    
    public void setIdComision(String idComision) {
        this.idComision = idComision;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
    
    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getComision() {
        return comision;
    }

    public void setComision(String comision) {
        this.comision = comision;
    }
    
    public String getInstitutionName2() {
        return institutionName2;
    }

    public void setInstitutionName2(String institutionName2) {
        this.institutionName2 = institutionName2;
    }
}