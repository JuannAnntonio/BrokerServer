package mx.sigmact.broker.pojo.backoffice;

public class BackOfficeReporteCartaConfirmacion {

	private String folio;
	private String fecha;
	private String hora;
	private String instrumeto;
	private String tipoOperacion;
	private String contraParte;
	private String montoNom;
	private String tasaNeg;
	private String tasaCom;
	private String tasaLiq;
	private String precio;
	private String monto;
	private String fechaLiq;
	private String dvx;
	private String titulos;
	private String operador;

	public BackOfficeReporteCartaConfirmacion(String folio, String fecha, String hora, String instrumeto, String tipoOperacion,
			String contraParte, String montoNom, String tasaNeg, String tasaCom, String tasaLiq, String precio, String monto,
			String fechaLiq, String dvx, String titulos, String operador) {
		
		this.folio = folio;
		this.fecha = fecha;
		this.hora = hora;
		this.instrumeto = instrumeto;
		this.tipoOperacion = tipoOperacion;
		this.contraParte = contraParte;
		this.montoNom = montoNom;
		this.tasaNeg = tasaNeg;
		this.tasaCom = tasaCom;
		this.tasaLiq = tasaLiq;
		this.precio = precio;
		this.monto = monto;
		this.fechaLiq = fechaLiq;
		this.dvx = dvx;
		this.titulos = titulos;
		this.operador = operador;

	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getInstrumento() {
		return instrumeto;
	}

	public void setInstrumento(String instrumeto) {
		this.instrumeto = instrumeto;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public String getContraParte() {
		return contraParte;
	}

	public void setContraParte(String contraParte) {
		this.contraParte = contraParte;
	}

	public String getMontoNom() {
		return montoNom;
	}

	public void setMontoNom(String montoNom) {
		this.montoNom = montoNom;
	}

	public String getTasaNeg() {
		return tasaNeg;
	}

	public void setTasaNeg(String tasaNeg) {
		this.tasaNeg = tasaNeg;
	}

	public String getTasaCom() {
		return tasaCom;
	}

	public void setTasaCom(String tasaCom) {
		this.tasaCom = tasaCom;
	}

	public String getTasaLiq() {
		return tasaLiq;
	}

	public void setTasaLiq(String tasaLiq) {
		this.tasaLiq = tasaLiq;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getFechaLiq() {
		return fechaLiq;
	}

	public void setFechaLiq(String fechaLiq) {
		this.fechaLiq = fechaLiq;
	}

	public String getDvx() {
		return dvx;
	}

	public void setDvx(String dvx) {
		this.dvx = dvx;
	}

	public String getTitulos() {
		return titulos;
	}

	public void setTitulos(String titulos) {
		this.titulos = titulos;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

}
