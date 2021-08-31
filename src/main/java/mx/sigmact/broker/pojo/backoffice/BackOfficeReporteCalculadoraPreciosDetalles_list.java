package mx.sigmact.broker.pojo.backoffice;

import java.util.List;

public class BackOfficeReporteCalculadoraPreciosDetalles_list {

	private String instrumento;
	private String fechaValuacion;
	private String precioSucio;
	private String precioLimpio;
	private String intereses;
	private String valorUdi;
	private List<BackOfficeReporteCalculadoraPreciosDetalles> backOfficeReporteCalculadoraPreciosDetalles;
	
	public BackOfficeReporteCalculadoraPreciosDetalles_list() {
		
	}

	public BackOfficeReporteCalculadoraPreciosDetalles_list(String instrumento, String fechaValuacion, String precioSucio,
												String precioLimpio, String intereses, String valorUdi) {
		
		this.instrumento = instrumento;
		this.fechaValuacion = fechaValuacion;
		this.precioSucio = precioSucio;
		this.precioLimpio = precioLimpio;
		this.intereses = intereses;
		this.valorUdi = valorUdi;
		
	}

	

	public String getInstrumento() {
		return instrumento;
	}

	public void setInstrumento(String instrumento) {
		this.instrumento = instrumento;
	}

	public String getFechaValuacion() {
		return fechaValuacion;
	}

	public void setFechaValuacion(String fechaValuacion) {
		this.fechaValuacion = fechaValuacion;
	}

	public String getPrecioSucio() {
		return precioSucio;
	}

	public void setPrecioSucio(String precioSucio) {
		this.precioSucio = precioSucio;
	}

	public String getPrecioLimpio() {
		return precioLimpio;
	}

	public void setPrecioLimpio(String precioLimpio) {
		this.precioLimpio = precioLimpio;
	}

	public String getIntereses() {
		return intereses;
	}

	public void setIntereses(String intereses) {
		this.intereses = intereses;
	}

	public String getValorUdi() {
		return valorUdi;
	}

	public void setValorUdi(String valorUdi) {
		this.valorUdi = valorUdi;
	}

	public List<BackOfficeReporteCalculadoraPreciosDetalles> getBackOfficeReporteCalculadoraPreciosDetalles() {
		return backOfficeReporteCalculadoraPreciosDetalles;
	}

	public void setBackOfficeReporteCalculadoraPreciosDetalles(
			List<BackOfficeReporteCalculadoraPreciosDetalles> backOfficeReporteCalculadoraPreciosDetalles) {
		this.backOfficeReporteCalculadoraPreciosDetalles = backOfficeReporteCalculadoraPreciosDetalles;
	}
	
}