package mx.sigmact.broker.pojo.backoffice;

public class BackOfficeReporteCalculadoraPreciosDetalles {

	private String fechaInicio;
	private String fechaFin;
	private String dvx;
	private String periodoCoupon;
	private String intereses;
	private String valorPresente;
	private String sumaValorPresente;
	
	public BackOfficeReporteCalculadoraPreciosDetalles() {
		
	}

	public BackOfficeReporteCalculadoraPreciosDetalles(String fechaInicio, String fechaFin, String dvx, 
													   String periodoCoupon, String intereses, String valorPresente, 
													   String sumaValorPresente) {
		
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.dvx = dvx;
		this.periodoCoupon = periodoCoupon;
		this.intereses = intereses;
		this.valorPresente = valorPresente;
		this.sumaValorPresente = sumaValorPresente;
		
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getDvx() {
		return dvx;
	}

	public void setDvx(String dvx) {
		this.dvx = dvx;
	}

	public String getPeriodoCoupon() {
		return periodoCoupon;
	}

	public void setPeriodoCoupon(String periodoCoupon) {
		this.periodoCoupon = periodoCoupon;
	}

	public String getIntereses() {
		return intereses;
	}

	public void setIntereses(String intereses) {
		this.intereses = intereses;
	}

	public String getValorPresente() {
		return valorPresente;
	}

	public void setValorPresente(String valorPresente) {
		this.valorPresente = valorPresente;
	}

	public String getSumaValorPresente() {
		return sumaValorPresente;
	}

	public void setSumaValorPresente(String sumaValorPresente) {
		this.sumaValorPresente = sumaValorPresente;
	}
	
	
	
}