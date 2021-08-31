package mx.sigmact.broker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.pojo.backoffice.BackOfficeMainView;
import mx.sigmact.broker.pojo.backoffice.BackOfficeReporteCalculadoraPrecios;
import mx.sigmact.broker.pojo.backoffice.BackOfficeReporteCalculadoraPreciosDetalles_list;
import mx.sigmact.broker.pojo.backoffice.BackOfficeReporteCartaConfirmacion;
import mx.sigmact.broker.pojo.parameter.ValueParameter;
import mx.sigmact.broker.repositories.BrokerStandingRepository;

/**
 * Created by norberto on 09/09/2017.
 */
public class JdbcBackOfficeDao implements BackOfficeDao {

	Logger log = Logger.getLogger(JdbcBackOfficeDao.class);

	@Resource(name = "formatDate")
	private SimpleDateFormat sdf;

	@Resource
	DataSource dataSource;

    @Resource
    private BrokerStandingRepository stdRepo;
    
	@Resource
	DaoHelper daoHelper;

	@Value("${query.backoffice.main.decimales.horas.nuevoformato.sinCorreos.conInstituciones}")
	String backOfficeMain;

	@Value("${query.backoffice.main.decimales.horas.nuevoformato.sinCorreos.sinInstituciones}")
	String backOfficeMainSinInstituciones;

	@Value("${query.backoffice.reporte.carta.confirmacion.decimales.horas.nuevoformato.sinCorreos.conInstituciones}")
	String backOfficeReporteCartaConfirmacion;

	@Value("${query.backoffice.reporte.carta.confirmacion.decimales.horas.nuevoformato.sinCorreos.sinInstituciones}")
	String backOfficeReporteCartaConfirmacionSinInstitucion;

	@Value("${query.backoffice2.main.decimales.horas.nuevoformato.sinCorreos.conInstituciones}")
	String backOffice2Main;

	@Value("${query.backoffice2.main.decimales.horas.nuevoformato.sinCorreos.sinInstituciones}")
	String backOffice2MainSinInstituciones;

	@Value("${query.backoffice2.reporte.carta.confirmacion.decimales.horas.nuevoformato.sinCorreos.conInstituciones}")
	String backOffice2ReporteCartaConfirmacion;

	@Value("${query.backoffice2.reporte.carta.confirmacion.decimales.horas.nuevoformato.sinCorreos.sinInstituciones}")
	String backOffice2ReporteCartaConfirmacionSinInstitucion;

	@Value("${query.backoffice.reporte.calculadora.precios}")
	String backOfficeReporteCalculadoraPrecios;

	@Value("${query.backoffice.reporte.calculadora.precios.detalles}")
	String backOfficeReporteCalculadoraPreciosDetalles;

	@Resource
    private DirtyPriceCalculator dpCalc;
    
	@Resource
	private ParameterDao parameterDao;

	@Override
	public List<BackOfficeMainView> getBackOfficeMainView(Integer institucion, String date, String start, String end) {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression,
		 * ticket_sent" tienen que empatar con la misma fecha.
		 */

		log.info("[JdbcBackOfficeDao][getBackOfficeMainView]");

		log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query: query.backoffice.main");

		log.info("[JdbcBackOfficeDao][getBackOfficeMainView] query params institucion: " + institucion + " date: "
				+ date);

		// date = "2020-03-19";
		
		

		List<BackOfficeMainView> retVal = new ArrayList<>();
		try (Connection con = dataSource.getConnection();) {

			// PreparedStatement ps = createPreparedStatementVar(con, backOfficeMain,
			// sdf.format(date.getTime()), workbench);
			// createPreparedStatementVar(con, backOfficeMain, date, workbench);
			
			//si institucion es == 5 hay que cambiar el query.
			//en el query quitar institucion, buyerid y sellerid.

			PreparedStatement ps = null;
			
			ValueParameter fecha = parameterDao.getParameter("today");
			
			log.info("[JdbcBackOfficeDao][getBackOfficeMainView] start: " + start);
			log.info("[JdbcBackOfficeDao][getBackOfficeMainView] end: " + end);
			log.info("[JdbcBackOfficeDao][getBackOfficeMainView] fecha: " + fecha.getValue());
			
			if(start.equals(end) && start.equals(fecha.getValue()) && end.equals(fecha.getValue())) {

				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] fechas iguales");

				if(institucion!=5) {
	
					log.info("[JdbcBackOfficeDao][getBackOfficeMainView] Institucion es Diferente a 5");
					
					log.info("[JdbcBackOfficeDao][getBackOfficeMainView] Institucion es Diferente a 5 start: " + start);
					
					log.info("[JdbcBackOfficeDao][getBackOfficeMainView] Institucion es Diferente a 5 end: " + end);
					
					ps = daoHelper.createPreparedStatementVar(con, backOffice2Main, 
							start, 
							end,
							institucion, 
							start, 
							end, 
							institucion, 
							start, 
							end, 
							institucion);
				} else {
	
					log.info("[JdbcBackOfficeDao][getBackOfficeMainView] Institucion es 5");
					
					ps = daoHelper.createPreparedStatementVar(con, backOffice2MainSinInstituciones, 
							start, 
							end);
					
				}
				
				
			} else {

				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] fechas no iguales");

				if(institucion!=5) {
	
					log.info("[JdbcBackOfficeDao][getBackOfficeMainView] Institucion es Diferente a 5");
					
					log.info("[JdbcBackOfficeDao][getBackOfficeMainView] Institucion es Diferente a 5 start: " + start);
					
					log.info("[JdbcBackOfficeDao][getBackOfficeMainView] Institucion es Diferente a 5 end: " + end);
					
					ps = daoHelper.createPreparedStatementVar(con, backOfficeMain, 
							start, 
							end,
							institucion, 
							start, 
							end, 
							institucion, 
							start, 
							end, 
							institucion);
				} else {
	
					log.info("[JdbcBackOfficeDao][getBackOfficeMainView] Institucion es 5");
					
					ps = daoHelper.createPreparedStatementVar(con, backOfficeMainSinInstituciones, 
							start, 
							end);
					
				}
				
			}
			
			ResultSet rs = ps.executeQuery();
					
			int i = 0;
			while (rs.next()) {

				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " Folio: " + rs.getString(1));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " Fecha: " + rs.getString(2));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " Hora: " + rs.getString(3));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " Instrumento: " + rs.getString(4));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " DVX: " + rs.getString(5));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " Comprador: " + rs.getString(6));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " Vendedor: " + rs.getString(7));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " Tasa: " + rs.getString(8));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " Monto String: " + rs.getString(9));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " Comision: " + rs.getString(10));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " Precio: " + rs.getString(11));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " Titulos: " + rs.getString(12));
				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] exec query Res: " + i + " FV: " + rs.getString(13));

				BackOfficeMainView row = new BackOfficeMainView(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
						rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
				
				retVal.add(row);

				i++;

			}
		} catch (SQLException e) {
			log.error(
					"[JdbcBackOfficeDao][getBackOfficeMainView] ERROR: Error al cargar la tabla principal el backoffice: "
							+ e.getMessage());
		}
		return retVal;
	}

	@Override
	public List<BackOfficeReporteCartaConfirmacion> getBackOfficeReporteCartaConfirmacion(Integer institucion, String date, String start, String end) {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression,
		 * ticket_sent" tienen que empatar con la misma fecha.
		 */

		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion]");

		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query: query.backoffice.reporte.carta.confirmacion.decimales");

		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] query params institucion: " + institucion + " date: "
				+ date);

		// date = "2020-03-19";

		List<BackOfficeReporteCartaConfirmacion> retVal = new ArrayList<>();
		
		// PreparedStatement ps = createPreparedStatementVar(con, backOfficeMain,
		// sdf.format(date.getTime()), workbench);
		// createPreparedStatementVar(con, backOfficeMain, date, workbench);

		//si institucion es == 5 hay que cambiar el query.
		//en el query quitar institucion.
		//en el query quitar institucion, buyerid y sellerid.

		ValueParameter fecha = parameterDao.getParameter("today");
		
		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] start: " + start);
		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] end: " + end);
		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] fecha: " + fecha.getId_parameter());

		try (Connection con = dataSource.getConnection()) {
			
			PreparedStatement ps = null;
			

			
			if(start.equals(end) && start.equals(fecha.getValue()) && end.equals(fecha.getValue())) {

				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] fechas iguales");
				

				if(institucion!=5) {
					
					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] Institucion es Diferente a 5");
					
					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] Institucion es Diferente a 5 start: " + start);
					
					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] Institucion es Diferente a 5 end: " + end);
					
					ps = daoHelper.createPreparedStatementVar(con, backOffice2ReporteCartaConfirmacion, 
							institucion, 
							institucion,
							institucion,
							start, 
							end);
				} else {
					
					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] Institucion es 5");
					ps = daoHelper.createPreparedStatementVar(con, backOffice2ReporteCartaConfirmacionSinInstitucion, 
							start, 
							end);
					
				}
				
			} else {

				log.info("[JdbcBackOfficeDao][getBackOfficeMainView] fechas no iguales");
				

				if(institucion!=5) {
					
					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] Institucion es Diferente a 5");
					
					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] Institucion es Diferente a 5 start: " + start);
					
					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] Institucion es Diferente a 5 end: " + end);
					
					ps = daoHelper.createPreparedStatementVar(con, backOfficeReporteCartaConfirmacion, 
							institucion, 
							institucion,
							institucion,
							start, 
							end);
				} else {
					
					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] Institucion es 5");
					ps = daoHelper.createPreparedStatementVar(con, backOfficeReporteCartaConfirmacionSinInstitucion, 
							start, 
							end);
					
				}
				
			}
			
			ResultSet rs = ps.executeQuery();
			
			int i = 0;
			while (rs.next()) {

				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " Folio: " + rs.getString(1));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " Fecha: " + rs.getString(2));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " Hora: " + rs.getString(3));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " Instrumento: " + rs.getString(4));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " tipoOperacion: " + rs.getString(5));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " contraParte: " + rs.getString(6));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " montoNom: " + rs.getString(7));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " tasaNeg: " + rs.getString(8));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " tasaCom: " + rs.getString(9));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " tasaNeg2: " + rs.getString(10));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " Precio: " + rs.getString(11));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " monto: " + rs.getString(12));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " fechaLiq: " + rs.getString(13));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " dvx: " + rs.getString(14));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " titulos: " + rs.getString(15));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] exec query Res: " + i + " operador: " + rs.getString(16));

				BackOfficeReporteCartaConfirmacion row = new BackOfficeReporteCartaConfirmacion(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
						rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16));
				retVal.add(row);

				i++;

			}
		} catch (SQLException e) {
			log.error(
					"[JdbcBackOfficeDao][getBackOfficeReporteCartaConfirmacion] ERROR: Error al cargar la tabla principal el backoffice: "
							+ e.getMessage());
		}
		return retVal;
	}

	@Override
	public List<BackOfficeReporteCalculadoraPrecios> getBackOfficeReporteCalculadoraPrecios(Integer institucion, String date, String tv) {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression,
		 * ticket_sent" tienen que empatar con la misma fecha.
		 */

		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios]");

		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query: query.backoffice.reporte.calculadora.precios");

		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] query params institucion: " + institucion + " date: "
				+ date);
		
		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] query params tv: " + tv);

		// date = "2020-03-19";

		List<BackOfficeReporteCalculadoraPrecios> retVal = new ArrayList<>();
		
		// PreparedStatement ps = createPreparedStatementVar(con, backOfficeMain,
		// sdf.format(date.getTime()), workbench);
		// createPreparedStatementVar(con, backOfficeMain, date, workbench);

		//si institucion es == 5 hay que cambiar el query.
		//en el query quitar institucion.
		//en el query quitar institucion, buyerid y sellerid.

		//ValueParameter fecha = parameterDao.getParameter("today");

		try (Connection con = dataSource.getConnection()) {
			
			PreparedStatement ps = null;
			
			ps = daoHelper.createPreparedStatementVar(con, backOfficeReporteCalculadoraPrecios, tv);
		
			ResultSet rs = ps.executeQuery();
			
			int i = 0;
			while (rs.next()) {

				
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " instrumento: " + rs.getString(1));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " FechaValuacion: " + rs.getString(2));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " DxV: " + rs.getString(3));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " TasaCupon: " + rs.getString(4));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " TasaValuacion: " + rs.getString(5));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " TasaMercado: " + rs.getString(6));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " ValorUdi: " + rs.getString(7));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " InicioCupon: " + rs.getString(8));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " FinCupon: " + rs.getString(9));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " PeriodoCupon: " + rs.getString(10));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " CuponesxVencer: " + rs.getString(11));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " id_valmer_Price_Vector: " + rs.getString(12));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] exec query Res: " + i + " tv: " + rs.getString(13));

				//id_valmer_Price_Vector ocupar para
				//StandingEntity standing = stdRepo.findOne(rs.getInt(12));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date_ = null;
				Double dirtyPrice = 0.0;
				String dirtyPrice_sring = "";
				try {
					date_ = sdf.parse(rs.getString(2));
					Calendar fechaValuacion = Calendar.getInstance();
					fechaValuacion.setTime(date_);

					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] fechaValuacion: " + fechaValuacion.getTime());

					dirtyPrice = dpCalc.calcDirtyPrice_reporteCalcularPrecios(
							rs.getInt(12),
							rs.getDouble(4),
							rs.getString(13),
							rs.getString(8),
							rs.getString(10),
							rs.getString(11),
							rs.getString(3), 
							fechaValuacion,
							rs.getDouble(5),
							rs.getString(1)
		            );
					
					DecimalFormat df = new DecimalFormat("#####0.000000");
					dirtyPrice_sring =  df.format(dirtyPrice).toString();
					
					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] dirtyPrice: " + dirtyPrice_sring);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] ERROR: " + e.getMessage().toString());
				}
	            
				
				//mandar double dirtyPrice en BackOfficeReporteCalculadoraPrecios
				BackOfficeReporteCalculadoraPrecios row = new BackOfficeReporteCalculadoraPrecios(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
						rs.getString(9), rs.getString(10), rs.getString(11), dirtyPrice_sring, rs.getString(12));
				
				retVal.add(row);

				i++;

			}
		} catch (SQLException e) {
			log.error(
					"[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPrecios] ERROR: Error al cargar la tabla principal del backoffice: "
							+ e.getMessage());
		}
		return retVal;
	}

	@Override
	public BackOfficeReporteCalculadoraPreciosDetalles_list getBackOfficeReporteCalculadoraPreciosDetalles(Integer institucion, String date, String id_valmer) {

		/*
		 * Para mostrar datos el parametro date tiene que venir de la tabla parameter.
		 * Para que muestre datos al igual date y las tablas standing, aggression,
		 * ticket_sent" tienen que empatar con la misma fecha.
		 */

		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles]");

		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query: query.backoffice.reporte.calculadora.precios");

		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] query params institucion: " + institucion + " date: "
				+ date);
		
		log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] query params id_valmer: " + id_valmer);

		// date = "2020-03-19";

		BackOfficeReporteCalculadoraPreciosDetalles_list retVal = new BackOfficeReporteCalculadoraPreciosDetalles_list();
				
		// PreparedStatement ps = createPreparedStatementVar(con, backOfficeMain,
		// sdf.format(date.getTime()), workbench);
		// createPreparedStatementVar(con, backOfficeMain, date, workbench);

		//si institucion es == 5 hay que cambiar el query.
		//en el query quitar institucion.
		//en el query quitar institucion, buyerid y sellerid.

		//ValueParameter fecha = parameterDao.getParameter("today");

		try (Connection con = dataSource.getConnection()) {
			
			PreparedStatement ps = null;
			
			ps = daoHelper.createPreparedStatementVar(con, backOfficeReporteCalculadoraPreciosDetalles, id_valmer);
		
			ResultSet rs = ps.executeQuery();
			
			int i = 0;
			while (rs.next()) {

				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " instrumento: " + rs.getString(1));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " FechaValuacion: " + rs.getString(2));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " DxV: " + rs.getString(3));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " TasaCupon: " + rs.getString(4));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " TasaValuacion: " + rs.getString(5));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " TasaMercado: " + rs.getString(6));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " ValorUdi: " + rs.getString(7));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " InicioCupon: " + rs.getString(8));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " FinCupon: " + rs.getString(9));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " PeriodoCupon: " + rs.getString(10));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " CuponesxVencer: " + rs.getString(11));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " id_valmer_Price_Vector: " + rs.getString(12));
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] exec query Res: " + i + " tv: " + rs.getString(13));

				//id_valmer_Price_Vector ocupar para
				//StandingEntity standing = stdRepo.findOne(rs.getInt(12));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date_ = null;
				Double dirtyPrice = 0.0;
				String dirtyPrice_sring = "";
				
				try {
					date_ = sdf.parse(rs.getString(2));
					Calendar fechaValuacion = Calendar.getInstance();
					fechaValuacion.setTime(date_);

					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] fechaValuacion: " + fechaValuacion.getTime());

					dirtyPrice = dpCalc.calcDirtyPrice_reporteCalcularPreciosDetalle(
							rs.getInt(12),
							rs.getDouble(4),
							rs.getString(13),
							rs.getString(8),
							rs.getString(10),
							rs.getString(11),
							rs.getString(3), 
							fechaValuacion,
							rs.getDouble(5),
							rs.getString(1),
							retVal
		            );
					
					if(rs.getString(13)=="BI" || rs.getString(13)=="LD") {

						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] formatos a 6 digitos");
						
						DecimalFormat df = new DecimalFormat("#####0.000000");
						Double sd  = dirtyPrice - Double.valueOf(retVal.getIntereses());
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] sd: " + sd);
						String price2 =  df.format(sd).toString();
						retVal.setPrecioLimpio(price2);
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] price2: " + price2);
						
						df = new DecimalFormat("#####0.000000");
						dirtyPrice_sring =  df.format(dirtyPrice).toString();
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] dirtyPrice: " + dirtyPrice_sring);
						retVal.setPrecioSucio(dirtyPrice_sring);
						
						df = new DecimalFormat("#####0.000000");
						String intereses2 =  df.format(Double.valueOf(retVal.getIntereses())).toString();
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] intereses2: " + intereses2);
						retVal.setIntereses(intereses2);
					/*Inicia Modificacion EYS LF */
					} else if(rs.getString(13)=="BI" || rs.getString(13)=="LD") {

						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] formatos a 6 digitos");
						
						DecimalFormat df = new DecimalFormat("#####0.000000");
						Double sd  = dirtyPrice - Double.valueOf(retVal.getIntereses());
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] sd: " + sd);
						String price2 =  df.format(sd).toString();
						retVal.setPrecioLimpio(price2);
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] price2: " + price2);
						
						df = new DecimalFormat("#####0.000000");
						dirtyPrice_sring =  df.format(dirtyPrice).toString();
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] dirtyPrice: " + dirtyPrice_sring);
						retVal.setPrecioSucio(dirtyPrice_sring);
						
						df = new DecimalFormat("#####0.000000");
						String intereses2 =  df.format(Double.valueOf(retVal.getIntereses())).toString();
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] intereses2: " + intereses2);
						retVal.setIntereses(intereses2);
						
					/*Termina Modificacion EYS LF */
					} else {

						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] formatos a 8 digitos");
						
						DecimalFormat df = new DecimalFormat("#####0.00000000");
						Double sd  = dirtyPrice - Double.valueOf(retVal.getIntereses());
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] sd: " + sd);
						String price2 =  df.format(sd).toString();
						retVal.setPrecioLimpio(price2);
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] price2: " + price2);
						
						df = new DecimalFormat("#####0.00000000");
						dirtyPrice_sring =  df.format(dirtyPrice).toString();
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] dirtyPrice: " + dirtyPrice_sring);
						retVal.setPrecioSucio(dirtyPrice_sring);
						
						df = new DecimalFormat("#####0.00000000");
						String intereses2 =  df.format(Double.valueOf(retVal.getIntereses())).toString();
						log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] intereses2: " + intereses2);
						retVal.setIntereses(intereses2);
						
					}
					
					retVal.setInstrumento(rs.getString(1));
					retVal.setFechaValuacion(rs.getString(2));
					//retVal.setIntereses("");
					retVal.setValorUdi(rs.getString(7));

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] ERROR: " + e.getMessage().toString());
				}
				
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] instrumento: " + retVal.getInstrumento());
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] fechaValuacion: " + retVal.getFechaValuacion());
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] PrecioSucio: " + retVal.getPrecioSucio());
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] precioLimpio: " + retVal.getPrecioLimpio());
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] intereses: " + retVal.getIntereses());
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] valorUdi: " + retVal.getValorUdi());
				log.info("+++++++++++++++++++++++++++++++++++++++++++++++ArrayList++++++++++++++++++++++++++++++++++++++++++++++++++++");
				log.info("[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] fechaFin: " + retVal.getBackOfficeReporteCalculadoraPreciosDetalles().get(0).getFechaFin());

				i++;

			}
		} catch (SQLException e) {
			log.error(
					"[JdbcBackOfficeDao][getBackOfficeReporteCalculadoraPreciosDetalles] ERROR: Error al cargar la tabla principal del backoffice: "
							+ e.getMessage());
		}
		
		return retVal;
		
	}

}
