package mx.sigmact.broker.pojo.admin;

/**
 * Class for the institution view
 * Created on 02/11/16.
 */
public class AdminMatrix {
	private String idComision;
	private String idInstitution1;
    private String institutionName;
    private String instrument;
    private String comision;
    private String idInstitution2;
    private String institutionName2;
    private String boton;

    public AdminMatrix(String idComision,
    						 String idInstitution1,
    						 String institutionName,
                             String instrument,
                             String comision,
                             String idInstitution2,
                             String institutionName2,
                             String boton) {
        this.idComision =  idComision;
        this.idInstitution1 = idInstitution1;
    	this.institutionName = institutionName;
        this.instrument = instrument;
        this.comision = comision;
        this.idInstitution2 = idInstitution2;
        this.institutionName2 = institutionName2;
        this.boton = boton;
    }
    
    public String getIdComision() {
        return idComision;
    }
    
    public void setIdComision(String idComision) {
        this.idComision = idComision;
    }
    
    public String getIdIntitution1() {
        return idInstitution1;
    }

    public void setIdInstitution1(String idInstitution1) {
        this.idInstitution1 = idInstitution1;
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
    
    public String getIdIntitution2() {
        return idInstitution2;
    }

    public void setIdInstitution2(String idInstitution2) {
        this.idInstitution2 = idInstitution2;
    }

    public String getInstitutionName2() {
        return institutionName2;
    }

    public void setInstitutionName2(String institutionName2) {
        this.institutionName2 = institutionName2;
    }
    public String getBoton() {
        return boton;
    }

    public void setBoton(String boton) {
        this.boton = boton;
    }
}