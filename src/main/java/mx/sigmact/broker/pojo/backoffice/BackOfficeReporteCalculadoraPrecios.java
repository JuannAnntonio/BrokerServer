package mx.sigmact.broker.pojo.backoffice;

public class BackOfficeReporteCalculadoraPrecios {

	private String instrumento;
	private String fechaValuacion;
	private String dxV;
	private String tasaCupon;
	private String tasaValuacion;
	private String tasaMercado;
	private String valorUdi;
	private String inicioCupon;
	private String finCupon;
	private String periodoCupon;
	private String cuponesxVencer;
	private String precioSucio;
	private String detalle;
	private String id_valmer_Price_Vector;
	private String tv;

	public BackOfficeReporteCalculadoraPrecios(String instrumento, String fechaValuacion, String dxV,
												String tasaCupon, String tasaValuacion, String tasaMercado, 
												String valorUdi, String inicioCupon, String finCupon, 
												String periodoCupon, String cuponesxVencer, String precioSucio, String detalle,
												String id_valmer_Price_Vector, String tv) {
		
		this.instrumento = instrumento;
		this.fechaValuacion = fechaValuacion;
		this.dxV = dxV;
		this.tasaCupon = tasaCupon;
		this.tasaValuacion = tasaValuacion;
		this.tasaMercado = tasaMercado;
		this.valorUdi = valorUdi;
		this.inicioCupon = inicioCupon;
		this.finCupon = finCupon;
		this.periodoCupon = periodoCupon;
		this.cuponesxVencer = cuponesxVencer;
		this.precioSucio = precioSucio;
		this.detalle = detalle;
		this.id_valmer_Price_Vector = id_valmer_Price_Vector;
		this.tv = tv;
		
	}

	public BackOfficeReporteCalculadoraPrecios(String instrumento, String fechaValuacion, String dxV,
												String tasaCupon, String tasaValuacion, String tasaMercado, 
												String valorUdi, String inicioCupon, String finCupon, 
												String periodoCupon, String cuponesxVencer, String precioSucio,
												String id_valmer_Price_Vector) {
		
		this.instrumento = instrumento;
		this.fechaValuacion = fechaValuacion;
		this.dxV = dxV;
		this.tasaCupon = tasaCupon;
		this.tasaValuacion = tasaValuacion;
		this.tasaMercado = tasaMercado;
		this.valorUdi = valorUdi;
		this.inicioCupon = inicioCupon;
		this.finCupon = finCupon;
		this.periodoCupon = periodoCupon;
		this.cuponesxVencer = cuponesxVencer;
		this.precioSucio = precioSucio;
		this.id_valmer_Price_Vector = id_valmer_Price_Vector;
		
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

	public String getDxV() {
		return dxV;
	}

	public void setDxV(String dxV) {
		this.dxV = dxV;
	}

	public String getTasaCupon() {
		return tasaCupon;
	}

	public void setTasaCupon(String tasaCupon) {
		this.tasaCupon = tasaCupon;
	}

	public String getTasaValuacion() {
		return tasaValuacion;
	}

	public void setTasaValuacion(String tasaValuacion) {
		this.tasaValuacion = tasaValuacion;
	}

	public String getTasaMercado() {
		return tasaMercado;
	}

	public void setTasaMercado(String tasaMercado) {
		this.tasaMercado = tasaMercado;
	}

	public String getValorUdi() {
		return valorUdi;
	}

	public void setValorUdi(String valorUdi) {
		this.valorUdi = valorUdi;
	}

	public String getInicioCupon() {
		return inicioCupon;
	}

	public void setInicioCupon(String inicioCupon) {
		this.inicioCupon = inicioCupon;
	}

	public String getFinCupon() {
		return finCupon;
	}

	public void setFinCupon(String finCupon) {
		this.finCupon = finCupon;
	}

	public String getPeriodoCupon() {
		return periodoCupon;
	}

	public void setPeriodoCupon(String periodoCupon) {
		this.periodoCupon = periodoCupon;
	}

	public String getCuponesxVencer() {
		return cuponesxVencer;
	}

	public void setCuponesxVencer(String cuponesxVencer) {
		this.cuponesxVencer = cuponesxVencer;
	}

	public String getPrecioSucio() {
		return precioSucio;
	}

	public void setPrecioSucio(String precioSucio) {
		this.precioSucio = precioSucio;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public String getId_valmer_Price_Vector() {
		return id_valmer_Price_Vector;
	}

	public void setId_valmer_Price_Vector(String id_valmer_Price_Vector) {
		this.id_valmer_Price_Vector = id_valmer_Price_Vector;
	}

	public String getTv() {
		return tv;
	}

	public void setTv(String tv) {
		this.tv = tv;
	}
	
	
}