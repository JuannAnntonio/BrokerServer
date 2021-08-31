package mx.sigmact.broker.pojo.backoffice;

/**
 * Created on 08/12/16.
 */
public class BackOfficeMainView {

	private String folio;
	private String fecha;
	private String hora;
	private String instrumeto;
	private String dvx;
	private String comprador;
	private String vendedor;
	private String tasa;
	private String monto;
	private String comision;
	private String precio;
	private String titulos;
	private String fv;

	public BackOfficeMainView(String folio, String fecha, String hora, String instrumeto, String dvx, String comprador,
			String vendedor, String tasa, String monto, String comision, String precio, String titulos, String fv) {

		this.folio = folio;
		this.fecha = fecha;
		this.hora = hora;
		this.instrumeto = instrumeto;
		this.dvx = dvx;
		this.comprador = comprador;
		this.vendedor = vendedor;
		this.tasa = tasa;
		this.monto = monto;
		this.comision = comision;
		this.precio = precio;
		this.titulos = titulos;
		this.fv = fv;
		
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

	public String getInstrumeto() {
		return instrumeto;
	}

	public void setInstrumeto(String instrumeto) {
		this.instrumeto = instrumeto;
	}

	public String getDvx() {
		return dvx;
	}

	public void setDvx(String dvx) {
		this.dvx = dvx;
	}

	public String getComprador() {
		return comprador;
	}

	public void setComprador(String comprador) {
		this.comprador = comprador;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getTasa() {
		return tasa;
	}

	public void setTasa(String tasa) {
		this.tasa = tasa;
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getComision() {
		return comision;
	}

	public void setComision(String comision) {
		this.comision = comision;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getTitulos() {
		return titulos;
	}

	public void setTitulos(String titulos) {
		this.titulos = titulos;
	}

	public String getFv() {
		return fv;
	}

	public void setFv(String fv) {
		this.fv = fv;
	}

}
