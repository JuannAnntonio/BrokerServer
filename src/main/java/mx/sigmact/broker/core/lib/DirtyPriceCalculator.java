package mx.sigmact.broker.core.lib;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import mx.sigmact.broker.core.enums.ValueType;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.dao.Coupon_bondeDao;
import mx.sigmact.broker.dao.Coupon_bondeFDao;
import mx.sigmact.broker.dao.FondeoDao;
import mx.sigmact.broker.dao.ParameterDao;
import mx.sigmact.broker.dao.UdiDao;
import mx.sigmact.broker.dao.UtilDao;
import mx.sigmact.broker.model.CalendarEntity;
import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.model.ValmerPriceVectorEntity;
import mx.sigmact.broker.pojo.backoffice.BackOfficeReporteCalculadoraPreciosDetalles;
import mx.sigmact.broker.pojo.backoffice.BackOfficeReporteCalculadoraPreciosDetalles_list;
import mx.sigmact.broker.pojo.coupon_bonde.Coupon_rate;
import mx.sigmact.broker.pojo.coupon_bondeF.Coupon_rate;
import mx.sigmact.broker.pojo.fondeo.FondeoBancario;
/*Modificacion LF EYS */
import mx.sigmact.broker.pojo.fondeo.FondeoTiie;
import mx.sigmact.broker.pojo.fondeo.FondeoCetes;
import mx.sigmact.broker.pojo.fondeo.FondeoGubernamental;
import mx.sigmact.broker.pojo.udi.Udi;
import mx.sigmact.broker.repositories.BrokerCalendarRespository;
import mx.sigmact.broker.repositories.BrokerStandingRepository;
import mx.sigmact.broker.repositories.BrokerUdiValueRepository;
import mx.sigmact.broker.repositories.BrokerValmerPriceVectorRepository;
import mx.sigmact.broker.services.ExternalServices;

/**
 * Calculadora para el precio sucio dependiendo el tipo de valor
 * Created on 15/11/16.
 */
public class DirtyPriceCalculator {
    
    Logger log = Logger.getLogger(DirtyPriceCalculator.class);

    @Resource
    private ParameterDao parameterDao;

    @Resource
    BrokerCalendarRespository daoCalendar;

    @Resource
    BrokerValmerPriceVectorRepository dao;

    @Resource
    BrokerStandingRepository daoStanding;

    @Resource
    BrokerUdiValueRepository udiRepo;

    @Resource
    ExternalServices extSer;

    @Resource
    private FondeoDao fondeoDao;

    FondeoBancario fondeoBancario = null;

    /*Modificacion LF EYS */
    FondeoTiie fondeoTiie = null;

    FondeoGubernamental fondeoGubernamental = null;

    FondeoCetes fondeoCetes = null;
    
    private Udi udi;
    
    @Resource
    private UdiDao udiDao;

    @Resource
    private UtilDao utilDao;
    
    @Resource
    private Coupon_bondeDao coupon_bondeDao;

    /*Modificacion LF EYS */
    @Resource
    private Coupon_bondeFDao coupon_bondeFDao;

	Coupon_rate coupon_rate = null;

    public DirtyPriceCalculator() {
        super();
    }

    public Double calcDirtyPrice(StandingEntity standing, Calendar reportDate) {
        return calcDirtyPrice(standing, reportDate, null);
    }
    
    public Double calcDirtyPrice(Integer standingId, Calendar reportDate) {
        StandingEntity standing = daoStanding.findOne(standingId);
        return calcDirtyPrice(standing, reportDate);
    }
    
    public Double calcDirtyPrice_reporteCalcularPreciosDetalle(int id_valmer, double tasa_coupon, String tv,  String inicio_coupon, 
    		String periodo_coupon, String cupones_por_vencer, String dvx, Calendar reportDate, Double overrideRate, 
    		String instrumento, BackOfficeReporteCalculadoraPreciosDetalles_list retVal) {

        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle]");
        
        //backOfficeReporteCalculadoraPreciosDetalles.setCuponesxVencer("prueba");
		
        Double result = 0.0;
		
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] reportDate: " + reportDate.getTime().toString());
		
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] id_valmer: " + id_valmer);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] tasa_coupon: " + tasa_coupon);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] tv: " + tv);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] inicio_coupon: " + inicio_coupon);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] periodo_coupon: " + periodo_coupon);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] cupones_por_vencer: " + cupones_por_vencer);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] dvx: " + dvx);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] reportDate: " + reportDate);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] overrideRate: " + overrideRate);
		
        double rate = overrideRate;
        
        String cp = null;
        Integer couponPeriod = null;
        Integer PlazoRevisable = 0;
        Double tasaDeMercado = 0.0;
        int diaReportDate = 0;
        int mesReportDate = 0;
        int anioReportDate = 0;
        String fechaReportDate="";
        
        Calendar inicio_coupon_1 = null;
        
        if(!tv.contentEquals("BI")) {

            log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] no es BI");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		java.util.Date date_ = null;
            try {
    			date_ = sdf.parse(inicio_coupon);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] ERROR: " + e1.getMessage().toString());
    		}
    		inicio_coupon_1 = Calendar.getInstance();
    		inicio_coupon_1.setTime(date_);
    		
        }  else {

            log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] es BI");
        }
		
        switch (tv) {
            
        	case "LD":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Caso LD");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Fehca Valuacion: " + reportDate.getTime().toString());
                diaReportDate = reportDate.get(Calendar.DAY_OF_MONTH);
                mesReportDate = reportDate.get(Calendar.MONTH) + 1;
                anioReportDate = reportDate.get(Calendar.YEAR);

                fechaReportDate = anioReportDate + "-" + mesReportDate + "-" + diaReportDate;
                //ValueParameter valueParameter = parameterDao.getParameter("today");

                try {
                    //this.coupon_rate = coupon_bondeDao.getRate(valueParameter.getValue(), priceVectorData.getSeries());
                    this.coupon_rate = coupon_bondeDao.getRate(fechaReportDate, instrumento.substring(instrumento.length() - 6));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] ERROR coupon: " + e.getMessage().toString());
                }

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Tasa Cupon: " + Double.valueOf(this.coupon_rate.getRate()));
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceLDDetalles(inicio_coupon_1, reportDate, rate, couponPeriod,
                                100,
                                Double.valueOf(this.coupon_rate.getRate()), Integer.parseInt(cupones_por_vencer), 28, retVal);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Final del Calculo: " + couponPeriod);
                break;
            /*Modificacion LF EYS */
            case "LF":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Caso LF");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Fehca Valuacion: " + reportDate.getTime().toString());
                diaReportDate = reportDate.get(Calendar.DAY_OF_MONTH);
                mesReportDate = reportDate.get(Calendar.MONTH) + 1;
                anioReportDate = reportDate.get(Calendar.YEAR);

                fechaReportDate = anioReportDate + "-" + mesReportDate + "-" + diaReportDate;
                //ValueParameter valueParameter = parameterDao.getParameter("today");

                try {
                    this.coupon_rate = coupon_bondeFDao.getRate(fechaReportDate, instrumento.substring(instrumento.length() - 6));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] ERROR coupon: " + e.getMessage().toString());
                }

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Tasa Cupon: " + Double.valueOf(this.coupon_rate.getRate()));
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceLFDetalles(inicio_coupon_1, reportDate, rate, couponPeriod,
                                100,
                                Double.valueOf(this.coupon_rate.getRate()), Integer.parseInt(cupones_por_vencer), 28, retVal);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Final del Calculo: " + couponPeriod);
                break;
            /*Modificacion LF EYS */
              
            case "IM":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] IM Caso Revisable");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Inicio de Cupon: " + inicio_coupon);
                
                this.fondeoGubernamental = fondeoDao.getFondeoGubernamentalLastRegister();
                tasaDeMercado = Double.valueOf(this.fondeoGubernamental.getRate());
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] tasaDeMercado: " + tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Tasa Cupon: " + tasa_coupon);
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Periodo de Cupon: " + couponPeriod);
                
                PlazoRevisable = 28;
                result = dirtPriceRevDetalles(inicio_coupon_1, reportDate, rate, couponPeriod,
                                100, tasa_coupon,
                                Integer.parseInt(cupones_por_vencer), PlazoRevisable, tasaDeMercado, retVal);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Final del Calculo: ");
                break;
                
            case "IQ":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] IQ Caso Revisable");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Inicio de Cupon: " + inicio_coupon);
 
                this.fondeoGubernamental = fondeoDao.getFondeoGubernamentalLastRegister();
                tasaDeMercado = Double.valueOf(this.fondeoGubernamental.getRate());
               
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] tasaDeMercado: " + tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Tasa Cupon: " + tasa_coupon);
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Periodo de Cupon: " + couponPeriod);

                PlazoRevisable = 91;
                result = dirtPriceRevDetalles(inicio_coupon_1, reportDate, rate, couponPeriod,
                                100, tasa_coupon,
                                Integer.parseInt(cupones_por_vencer), PlazoRevisable, tasaDeMercado, retVal);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Final del Calculo: ");
                
                break;
                
            case "IS":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] IS Caso Revisable");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Inicio de Cupon: " + inicio_coupon);

                this.fondeoCetes = fondeoDao.getFondeoCetesLastRegister();
                tasaDeMercado = Double.valueOf(this.fondeoCetes.getRate());
  
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] tasaDeMercado: " + tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Tasa Cupon: " + tasa_coupon);
                 
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));
                PlazoRevisable = 182;

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceRevDetalles(inicio_coupon_1, reportDate, rate, couponPeriod,
                                100, tasa_coupon,
                                Integer.parseInt(cupones_por_vencer), PlazoRevisable, tasaDeMercado, retVal);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Final del Calculo: ");
        		
                break;
                
            case "BI":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Caso BI");                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Tasa: " + rate);
                
                List<BackOfficeReporteCalculadoraPreciosDetalles> backOfficeReporteCalculadoraPreciosDetalles_list =  new ArrayList<BackOfficeReporteCalculadoraPreciosDetalles>();
                BackOfficeReporteCalculadoraPreciosDetalles backOfficeReporteCalculadoraPreciosDetalles = new BackOfficeReporteCalculadoraPreciosDetalles();
                
                result =  (10.0 / (1 + rate * Double.valueOf(dvx) / 36000.0));
                
                backOfficeReporteCalculadoraPreciosDetalles.setFechaInicio(reportDate.getTime().toString());
                backOfficeReporteCalculadoraPreciosDetalles.setFechaFin(reportDate.getTime().toString());
                backOfficeReporteCalculadoraPreciosDetalles.setValorPresente(Double.toString(result));
                backOfficeReporteCalculadoraPreciosDetalles.setSumaValorPresente(Double.toString(result));
                backOfficeReporteCalculadoraPreciosDetalles.setDvx(dvx);
                backOfficeReporteCalculadoraPreciosDetalles.setPeriodoCoupon(periodo_coupon);
                backOfficeReporteCalculadoraPreciosDetalles.setIntereses(Double.toString(0.0));

                backOfficeReporteCalculadoraPreciosDetalles_list.add(backOfficeReporteCalculadoraPreciosDetalles);

        		retVal.setBackOfficeReporteCalculadoraPreciosDetalles(backOfficeReporteCalculadoraPreciosDetalles_list);
        		retVal.setIntereses(Double.toString(0.0));
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Final del Calculo: ");
                break;
                
            case "M":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Caso M");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Inicio de Cupon: " + inicio_coupon);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Tasa: " + rate);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Tasa Cupon: " + tasa_coupon);
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceMDetalles(inicio_coupon_1,
                        reportDate,
                        rate,
                        couponPeriod,
                        100,
                        tasa_coupon,
                        Integer.parseInt(cupones_por_vencer), retVal);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Final del Calculo: ");
                
                break;
                
            case "S":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Caso S");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Inicio de Cupon: " + inicio_coupon);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Tasa: " + rate);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Tasa Cupon: " + tasa_coupon);
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Periodo de Cupon: " + couponPeriod);
				
                result = dirtPriceMDetalles(inicio_coupon_1,
                        reportDate,
                        rate,
                        couponPeriod,
                        100,
                        tasa_coupon,
                        Integer.parseInt(cupones_por_vencer),
                        retVal);

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Valor Calculado Antes de UDI: " + result);

                int diaReportDate_ = reportDate.get(Calendar.DAY_OF_MONTH);
                int mesReportDate_ = reportDate.get(Calendar.MONTH) + 1;
                int anioReportDate_ = reportDate.get(Calendar.YEAR);
                String fechaReportDate_ = anioReportDate_ + "-" + mesReportDate_ + "-" + diaReportDate_;
                this.udi = udiDao.getUdi(fechaReportDate_);

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Obteniendo UDI: ");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] ID Tabla UDI: " + this.udi.getUdi());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Fecha de la UDI: " + this.udi.getDate());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Valor de la UDI: " + this.udi.getValue());
                 
                result *= this.udi.getValue();
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Precio Sucio: " + result);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPreciosDetalle] Final del Calculo");
                break;
        }
        
        return result;
        
    }

    public Double calcDirtyPrice_reporteCalcularPrecios(int id_valmer, double tasa_coupon, String tv,  String inicio_coupon, String periodo_coupon, 
    		String cupones_por_vencer, String dvx, Calendar reportDate, Double overrideRate, String instrumento) {

        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios]");
		
        Double result = 0.0;
		
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] reportDate: " + reportDate.getTime().toString());
		
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] id_valmer: " + id_valmer);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] tasa_coupon: " + tasa_coupon);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] tv: " + tv);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] inicio_coupon: " + inicio_coupon);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] periodo_coupon: " + periodo_coupon);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] cupones_por_vencer: " + cupones_por_vencer);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] dvx: " + dvx);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] reportDate: " + reportDate);
        log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] overrideRate: " + overrideRate);
		
        double rate = overrideRate;
        
        String cp = null;
        Integer couponPeriod = null;
        Integer PlazoRevisable = 0;
        Double tasaDeMercado = 0.0;
        int diaReportDate = 0;
        int mesReportDate = 0;
        int anioReportDate = 0;
        String fechaReportDate="";
        
        Calendar inicio_coupon_1 = null;
        
        if(!tv.contentEquals("BI")) {

            log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] no es BI");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		java.util.Date date_ = null;
            try {
    			date_ = sdf.parse(inicio_coupon);
    		} catch (ParseException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] ERROR: " + e1.getMessage().toString());
    		}
    		inicio_coupon_1 = Calendar.getInstance();
    		inicio_coupon_1.setTime(date_);
    		
        }  else {

            log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] es BI");
        }
		
        switch (tv) {
        
            case "LD":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Caso LD");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Fehca Valuacion: " + reportDate.getTime().toString());
                diaReportDate = reportDate.get(Calendar.DAY_OF_MONTH);
                mesReportDate = reportDate.get(Calendar.MONTH) + 1;
                anioReportDate = reportDate.get(Calendar.YEAR);

                fechaReportDate = anioReportDate + "-" + mesReportDate + "-" + diaReportDate;
                //ValueParameter valueParameter = parameterDao.getParameter("today");

                try {
                    //this.coupon_rate = coupon_bondeDao.getRate(valueParameter.getValue(), priceVectorData.getSeries());
                    this.coupon_rate = coupon_bondeDao.getRate(fechaReportDate, instrumento.substring(instrumento.length() - 6));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] ERROR coupon: " + e.getMessage().toString());
                }

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Tasa Cupon: " + Double.valueOf(this.coupon_rate.getRate()));
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceLD(inicio_coupon_1, reportDate, rate, couponPeriod,
                                100,
                                Double.valueOf(this.coupon_rate.getRate()), Integer.parseInt(cupones_por_vencer), 28);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Final del Calculo: " + couponPeriod);
                break;
            /*Modificacion LF EYS */    
            case "LF":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Caso LF");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Fehca Valuacion: " + reportDate.getTime().toString());
                diaReportDate = reportDate.get(Calendar.DAY_OF_MONTH);
                mesReportDate = reportDate.get(Calendar.MONTH) + 1;
                anioReportDate = reportDate.get(Calendar.YEAR);

                fechaReportDate = anioReportDate + "-" + mesReportDate + "-" + diaReportDate;
                //ValueParameter valueParameter = parameterDao.getParameter("today");

                try {
                    this.coupon_rate = coupon_bondeFDao.getRate(fechaReportDate, instrumento.substring(instrumento.length() - 6));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] ERROR coupon: " + e.getMessage().toString());
                }

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Tasa Cupon: " + Double.valueOf(this.coupon_rate.getRate()));
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceLF(inicio_coupon_1, reportDate, rate, couponPeriod,
                                100,
                                Double.valueOf(this.coupon_rate.getRate()), Integer.parseInt(cupones_por_vencer), 28);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Final del Calculo: " + couponPeriod);
                break;
                
            case "IM":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] IM Caso Revisable");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Inicio de Cupon: " + inicio_coupon);

                this.fondeoGubernamental = fondeoDao.getFondeoGubernamentalLastRegister();
                tasaDeMercado = Double.valueOf(this.fondeoGubernamental.getRate());
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] tasaDeMercado: " + tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Tasa Cupon: " + tasa_coupon);
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Periodo de Cupon: " + couponPeriod);
                
                PlazoRevisable = 28;
                result = dirtPriceRev(inicio_coupon_1, reportDate, rate, couponPeriod,
                                100, tasa_coupon,
                                Integer.parseInt(cupones_por_vencer), PlazoRevisable, tasaDeMercado);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Final del Calculo: ");
                break;
                
            case "IQ":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] IQ Caso Revisable");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Inicio de Cupon: " + inicio_coupon);
 
                this.fondeoGubernamental = fondeoDao.getFondeoGubernamentalLastRegister();
                tasaDeMercado = Double.valueOf(this.fondeoGubernamental.getRate());
               
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] tasaDeMercado: " + tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Tasa Cupon: " + tasa_coupon);
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Periodo de Cupon: " + couponPeriod);

                PlazoRevisable = 91;
                result = dirtPriceRev(inicio_coupon_1, reportDate, rate, couponPeriod,
                                100, tasa_coupon,
                                Integer.parseInt(cupones_por_vencer), PlazoRevisable, tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Final del Calculo: ");
                break;
                
            case "IS":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] IS Caso Revisable");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Inicio de Cupon: " + inicio_coupon);

                this.fondeoCetes = fondeoDao.getFondeoCetesLastRegister();
                tasaDeMercado = Double.valueOf(this.fondeoCetes.getRate());
  
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] tasaDeMercado: " + tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Tasa Cupon: " + tasa_coupon);
                 
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));
                PlazoRevisable = 182;

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceRev(inicio_coupon_1, reportDate, rate, couponPeriod,
                                100, tasa_coupon,
                                Integer.parseInt(cupones_por_vencer), PlazoRevisable, tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Final del Calculo: ");
                break;
            
            case "BI":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Caso BI");                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Tasa: " + rate);

                result =  (10.0 / (1 + rate * Double.valueOf(dvx) / 36000.0));
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Final del Calculo: ");
                break;
            
            case "M":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Caso M");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Inicio de Cupon: " + inicio_coupon);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Tasa: " + rate);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Tasa Cupon: " + tasa_coupon);
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceM(inicio_coupon_1,
                        reportDate,
                        rate,
                        couponPeriod,
                        100,
                        tasa_coupon,
                        Integer.parseInt(cupones_por_vencer));
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Final del Calculo: ");
                break;
            case "S":
            	
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Caso S");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Inicio de Cupon: " + inicio_coupon);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Tasa: " + rate);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Tasa Cupon: " + tasa_coupon);
                
                cp = periodo_coupon;
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Periodo de Cupon: " + couponPeriod);
				
                result = dirtPriceM(inicio_coupon_1,
                        reportDate,
                        rate,
                        couponPeriod,
                        100,
                        tasa_coupon,
                        Integer.parseInt(cupones_por_vencer));

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Valor Calculado Antes de UDI: " + result);

                int diaReportDate_ = reportDate.get(Calendar.DAY_OF_MONTH);
                int mesReportDate_ = reportDate.get(Calendar.MONTH) + 1;
                int anioReportDate_ = reportDate.get(Calendar.YEAR);
                String fechaReportDate_ = anioReportDate_ + "-" + mesReportDate_ + "-" + diaReportDate_;
                this.udi = udiDao.getUdi(fechaReportDate_);

                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Obteniendo UDI: ");
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] ID Tabla UDI: " + this.udi.getUdi());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Fecha de la UDI: " + this.udi.getDate());
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Valor de la UDI: " + this.udi.getValue());
                 
                result *= this.udi.getValue();
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Precio Sucio: " + result);
                log.info("[DirtyPriceCalculator][calcDirtyPrice_reporteCalcularPrecios] Final del Calculo");
                break;
        }
        
        return result;
        
    }
	
    public Double calcDirtyPrice(StandingEntity standing, Calendar reportDate, Double overrideRate) {
    	
        log.info("[DirtyPriceCalculator][calcDirtyPrice]");
		
        Double result = 0.0;
        int fkIdValmerPriceVector = standing.getFkIdValmerPriceVector();
		
        log.info("[DirtyPriceCalculator][calcDirtyPrice] reportDate: " + reportDate.getTime().toString());
		
        log.info("[DirtyPriceCalculator][calcDirtyPrice] fkIdValmerPriceVector: " + fkIdValmerPriceVector);
		
        ValmerPriceVectorEntity priceVectorData = null;
        
        try {
            priceVectorData = dao.findOne(fkIdValmerPriceVector);
        } catch(Exception e) {
            log.info("[DirtyPriceCalculator][calcDirtyPrice] Error: priceVectorData = dao.findOne(fkIdValmerPriceVector)" + e.getLocalizedMessage());
            log.info("[DirtyPriceCalculator][calcDirtyPrice] Error: priceVectorData = dao.findOne(fkIdValmerPriceVector)" + e.getMessage());
            log.info("[DirtyPriceCalculator][calcDirtyPrice] Error: priceVectorData = dao.findOne(fkIdValmerPriceVector)" + e.toString());
        }
        
        
        double rate = standing.getValue();
        if (overrideRate != null) {
            rate = overrideRate;
        }
        String tv = priceVectorData.getTv();
        String cp = null;
        Integer couponPeriod = null;
        Integer PlazoRevisable = 0;
        Double tasaDeMercado = 0.0;
        int diaReportDate = 0;
        int mesReportDate = 0;
        int anioReportDate = 0;
        String fechaReportDate="";
		
        switch (tv) {
            case "LD":
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Caso LD");
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Inicio de Cupon: " + priceVectorData.getCouponStart().getTime().toString());
                diaReportDate = reportDate.get(Calendar.DAY_OF_MONTH);
                mesReportDate = reportDate.get(Calendar.MONTH) + 1;
                anioReportDate = reportDate.get(Calendar.YEAR);

                fechaReportDate = anioReportDate + "-" + mesReportDate + "-" + diaReportDate;
                //ValueParameter valueParameter = parameterDao.getParameter("today");

                try {
                    //this.coupon_rate = coupon_bondeDao.getRate(valueParameter.getValue(), priceVectorData.getSeries());
                    this.coupon_rate = coupon_bondeDao.getRate(fechaReportDate, priceVectorData.getSeries());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                log.info("[DirtyPriceCalculator][calcDirtyPrice] Tasa Cupon: " + Double.valueOf(this.coupon_rate.getRate()));
                
                cp = priceVectorData.getCouponPeriod();
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceLD(priceVectorData.getCouponStart(), reportDate, rate, couponPeriod,
                                priceVectorData.getUpdatedNominalValue().intValue(),
                                Double.valueOf(this.coupon_rate.getRate()), priceVectorData.getCouponsDueToExpire(), 28);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Final del Calculo: " + couponPeriod);
                break;
             /*Inicia Modificacion LF EYS   */
            case "LF":
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Caso LF");
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Inicio de Cupon: " + priceVectorData.getCouponStart().getTime().toString());
                diaReportDate = reportDate.get(Calendar.DAY_OF_MONTH);
                mesReportDate = reportDate.get(Calendar.MONTH) + 1;
                anioReportDate = reportDate.get(Calendar.YEAR);

                fechaReportDate = anioReportDate + "-" + mesReportDate + "-" + diaReportDate;

                try {
                    this.coupon_rate = coupon_bondeFDao.getRate(fechaReportDate, priceVectorData.getSeries());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                log.info("[DirtyPriceCalculator][calcDirtyPrice] Tasa Cupon: " + Double.valueOf(this.coupon_rate.getRate()));
                
                cp = priceVectorData.getCouponPeriod();
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceLF(priceVectorData.getCouponStart(), reportDate, rate, couponPeriod,
                                priceVectorData.getUpdatedNominalValue().intValue(),
                                Double.valueOf(this.coupon_rate.getRate()), priceVectorData.getCouponsDueToExpire(), 28);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Final del Calculo: " + couponPeriod);
                break;
             /*Termina Modificacion LF EYS   */ 


            case "IM":
                log.info("[DirtyPriceCalculator][calcDirtyPrice] IM Caso Revisable");
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Inicio de Cupon: " + priceVectorData.getCouponStart().getTime().toString());

                this.fondeoGubernamental = fondeoDao.getFondeoGubernamentalLastRegister();
                tasaDeMercado = Double.valueOf(this.fondeoGubernamental.getRate());
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice] tasaDeMercado: " + tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Tasa Cupon: " + priceVectorData.getCouponRate());
                
                cp = priceVectorData.getCouponPeriod();
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Periodo de Cupon: " + couponPeriod);
                
                PlazoRevisable = 28;
                result = dirtPriceRev(priceVectorData.getCouponStart(), reportDate, rate, couponPeriod,
                                priceVectorData.getUpdatedNominalValue().intValue(), priceVectorData.getCouponRate(),
                                priceVectorData.getCouponsDueToExpire(), PlazoRevisable, tasaDeMercado);
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Final del Calculo: ");
                break;
            case "IQ":
                log.info("[DirtyPriceCalculator][calcDirtyPrice] IQ Caso Revisable");
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Inicio de Cupon: " + priceVectorData.getCouponStart().getTime().toString());
 
                this.fondeoGubernamental = fondeoDao.getFondeoGubernamentalLastRegister();
                tasaDeMercado = Double.valueOf(this.fondeoGubernamental.getRate());
               
                log.info("[DirtyPriceCalculator][calcDirtyPrice] tasaDeMercado: " + tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Tasa Cupon: " + priceVectorData.getCouponRate());
                
                cp = priceVectorData.getCouponPeriod();
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice] Periodo de Cupon: " + couponPeriod);

                PlazoRevisable = 91;
                result = dirtPriceRev(priceVectorData.getCouponStart(), reportDate, rate, couponPeriod,
                                priceVectorData.getUpdatedNominalValue().intValue(), priceVectorData.getCouponRate(),
                                priceVectorData.getCouponsDueToExpire(), PlazoRevisable, tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Final del Calculo: ");
                break;
            case "IS":
                log.info("[DirtyPriceCalculator][calcDirtyPrice] IS Caso Revisable");
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Inicio de Cupon: " + priceVectorData.getCouponStart().getTime().toString());

                this.fondeoCetes = fondeoDao.getFondeoCetesLastRegister();
                tasaDeMercado = Double.valueOf(this.fondeoCetes.getRate());
  
                log.info("[DirtyPriceCalculator][calcDirtyPrice] tasaDeMercado: " + tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Tasa Cupon: " + priceVectorData.getCouponRate());
                 
                cp = priceVectorData.getCouponPeriod();
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));
                PlazoRevisable = 182;

                log.info("[DirtyPriceCalculator][calcDirtyPrice] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceRev(priceVectorData.getCouponStart(), reportDate, rate, couponPeriod,
                                priceVectorData.getUpdatedNominalValue().intValue(), priceVectorData.getCouponRate(),
                                priceVectorData.getCouponsDueToExpire(), PlazoRevisable, tasaDeMercado);
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Final del Calculo: ");
                break;
            case "BI":
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Caso BI");                
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Tasa: " + rate);
                
                result = calcBi(rate, reportDate, priceVectorData.getExpirationDate());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Final del Calculo: ");
                break;
            case "M":
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Caso M");
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Inicio de Cupon: " + priceVectorData.getCouponStart().getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Tasa: " + rate);
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Tasa Cupon: " + priceVectorData.getCouponRate());
                
                cp = priceVectorData.getCouponPeriod();
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));
                
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Periodo de Cupon: " + couponPeriod);

                result = dirtPriceM(priceVectorData.getCouponStart(),
                        reportDate,
                        rate,
                        couponPeriod,
                        priceVectorData.getUpdatedNominalValue().intValue(),
                        priceVectorData.getCouponRate(),
                        priceVectorData.getCouponsDueToExpire());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Final del Calculo: ");
                break;
            case "S":
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Caso S");
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Fehca Valuacion: " + reportDate.getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Inicio de Cupon: " + priceVectorData.getCouponStart().getTime().toString());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Tasa: " + rate);
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Tasa Cupon: " + priceVectorData.getCouponRate());
                
                cp = priceVectorData.getCouponPeriod();
                couponPeriod = Integer.parseInt(cp.substring(0, cp.length() - 4));

                log.info("[DirtyPriceCalculator][calcDirtyPrice] Periodo de Cupon: " + couponPeriod);
				
                result = dirtPriceM(priceVectorData.getCouponStart(),
                        reportDate,
                        rate,
                        couponPeriod,
                        priceVectorData.getUpdatedNominalValue().intValue(),
                        priceVectorData.getCouponRate(),
                        priceVectorData.getCouponsDueToExpire());

                log.info("[DirtyPriceCalculator][calcDirtyPrice] Valor Calculado Antes de UDI: " + result);

                int diaReportDate_ = reportDate.get(Calendar.DAY_OF_MONTH);
                int mesReportDate_ = reportDate.get(Calendar.MONTH) + 1;
                int anioReportDate_ = reportDate.get(Calendar.YEAR);
                String fechaReportDate_ = anioReportDate_ + "-" + mesReportDate_ + "-" + diaReportDate_;
                this.udi = udiDao.getUdi(fechaReportDate_);

                log.info("[DirtyPriceCalculator][calcDirtyPrice] Obteniendo UDI: ");
                log.info("[DirtyPriceCalculator][calcDirtyPrice] ID Tabla UDI: " + this.udi.getUdi());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Fecha de la UDI: " + this.udi.getDate());
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Valor de la UDI: " + this.udi.getValue());
                 
                result *= this.udi.getValue();
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Precio Sucio: " + result);
                log.info("[DirtyPriceCalculator][calcDirtyPrice] Final del Calculo");
                break;
        }
        return result;
    }

    public Double dirtPriceRev(Calendar couponStrart,
                        Calendar valuationDay,
                        Double standingRate,
                        Integer couponPeriod,
                        Integer nominalValue,
                        Double couponRate,
                        Integer couponsDueToExpire,
                        Integer periodoCuponOriginal,
                        Double tasaDeMercado){

        log.info("[DirtyPriceCalculator][dirtPriceRev]");

        Calendar lCouponEnd = (Calendar)couponStrart.clone();
        Calendar lCouponStart = (Calendar)couponStrart.clone();
        Integer lCouponPeriod = couponPeriod;
        Double price = 0.0;
        Integer drag = 0;
        drag = couponPeriod - periodoCuponOriginal;
        double intereses = 0.0;
        Calendar lCouponEndIntereses = null;
        intereses = nominalValue * (couponRate * lCouponPeriod / 36000);
        double interesesDev = 0.0;
        
        log.info("[DirtyPriceCalculator][dirtPriceRev] Diferencia Periodo original menos Periodo Cupon: " + drag);
        log.info("[DirtyPriceCalculator][dirtPriceRev] Intereses Del Periodo: " + intereses);
        log.info("[DirtyPriceCalculator][dirtPriceRev] Inicia Iteracion");

        for(int i = 0;i < couponsDueToExpire;i++){

            log.info("[DirtyPriceCalculator][dirtPriceRev] Fecha de Valuacion: " + valuationDay.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceRev] Acumulado de Dias Inhabilies: " + drag);

            //lCouponEnd.add(Calendar.DAY_OF_YEAR, lCouponPeriod);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, periodoCuponOriginal);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, drag);
            
            int dialCouponEnd = lCouponEnd.get(Calendar.DAY_OF_MONTH);
            int meslCouponEnd = lCouponEnd.get(Calendar.MONTH) + 1;
            int aniolCouponEnd = lCouponEnd.get(Calendar.YEAR);
            
            String fechalCouponEnd = aniolCouponEnd + "-" + meslCouponEnd + "-" + dialCouponEnd;
            
            log.info("[DirtyPriceCalculator][dirtPriceRev] Nuevo Formato: " + fechalCouponEnd);
            
            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);
            if(utilDao.Dia_Habil(fechalCouponEnd)==1){
                drag = CalendarUtil.calcDiffDays(lCouponEnd, utilDao.Dia_Ant(fechalCouponEnd,1));
                //arreglar conversión
                lCouponEnd = utilDao.Dia_Ant(fechalCouponEnd,1);

                log.info("[DirtyPriceCalculator][dirtPriceRev] lCouponEnd utilDao: " + lCouponEnd.getTime().toString());
            }else {
                drag = 0;
            }    
            lCouponPeriod = CalendarUtil.calcDiffDays(lCouponEnd, lCouponStart);            
            //lCouponPeriod = daysDrag[0];
            //drag = daysDrag[1];
            Integer diffDays = CalendarUtil.calcDiffDays(lCouponEnd, valuationDay);
            if(i==0) {
                lCouponEndIntereses = (Calendar)lCouponEnd.clone();
                interesesDev = nominalValue * (couponRate * (lCouponPeriod-diffDays) / 36000);
                log.info("[DirtyPriceCalculator][dirtPriceRev] i=0 Fecha para el Calculo de Interese: " + lCouponEndIntereses.getTime().toString());
                log.info("[DirtyPriceCalculator][dirtPriceRev] i=0 Interes Devengado: " + interesesDev);
            }

            log.info("[DirtyPriceCalculator][dirtPriceRev] Fecha de Vencimiento de Cupon: " + lCouponEnd.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceRev] Periodo de Cupon: " + lCouponPeriod);
            log.info("[DirtyPriceCalculator][dirtPriceRev] Dias x Vencer: " + diffDays);

            final Double fi = calcFiRev(nominalValue, couponRate, lCouponPeriod, i+1, couponsDueToExpire, diffDays, tasaDeMercado);

            log.info("[DirtyPriceCalculator][dirtPriceRev] i=0 Interes : " + fi);

            Double tasaDescuento = standingRate + tasaDeMercado;

            log.info("[DirtyPriceCalculator][dirtPriceRev] Tasa de Descuento: " + tasaDescuento);
            log.info("[DirtyPriceCalculator][dirtPriceRev] Factor Descuento: " + Math.pow( 1.0+(tasaDescuento*Double.valueOf(periodoCuponOriginal)/36000.0),
                    (diffDays/Double.valueOf(periodoCuponOriginal))));
            log.info("[DirtyPriceCalculator][dirtPriceRev] Factor Descuento (parte superior): " +  1.0+(tasaDescuento*Double.valueOf(periodoCuponOriginal)/36000.0));
            log.info("[DirtyPriceCalculator][dirtPriceRev] Factor Descuento (parte inferior): " +  (diffDays/Double.valueOf(periodoCuponOriginal)));

            price += fi/
                        Math.pow(1.0+(tasaDescuento*Double.valueOf(periodoCuponOriginal)/36000.0),
                            (diffDays/Double.valueOf(periodoCuponOriginal)) );

            log.info("[DirtyPriceCalculator][dirtPriceRev] Interes en Valor Presente: " + price);

            if(periodoCuponOriginal>lCouponPeriod) {
                log.info("[DirtyPriceCalculator][dirtPriceRev] entra periodoCuponOriginal>lCouponPeriod ");
                drag = periodoCuponOriginal - lCouponPeriod;	
            }
//            lCouponPeriod = periodoCuponOriginal;
            lCouponStart = (Calendar)lCouponEnd.clone();
        } //fin for

        double priceclean = price - interesesDev;
        log.info("[DirtyPriceCalculator][dirtPriceRev] Precio Limpio: " + priceclean);

        if(CalendarUtil.calcDiffDays(lCouponEndIntereses, valuationDay)==0) {
            log.info("[DirtyPriceCalculator][dirtPriceRev] Fehca Valuacion es el Inicio de Cupon ");
            price = price - intereses;
        }

        log.info("[DirtyPriceCalculator][dirtPriceRev] Precio Sucio: " + price);
        log.info("[DirtyPriceCalculator][dirtPriceRev] Final de Iteracion: ");

        return price;
    }
    
    public Double dirtPriceRevDetalles(Calendar couponStrart,
                        Calendar valuationDay,
                        Double standingRate,
                        Integer couponPeriod,
                        Integer nominalValue,
                        Double couponRate,
                        Integer couponsDueToExpire,
                        Integer periodoCuponOriginal,
                        Double tasaDeMercado,
                		BackOfficeReporteCalculadoraPreciosDetalles_list retVal){

        log.info("[DirtyPriceCalculator][dirtPriceRevDetalles]");
        
        List<BackOfficeReporteCalculadoraPreciosDetalles> backOfficeReporteCalculadoraPreciosDetalles_list =  new ArrayList<BackOfficeReporteCalculadoraPreciosDetalles>();
        
        Calendar lCouponEnd = (Calendar)couponStrart.clone();
        Calendar lCouponStart = (Calendar)couponStrart.clone();
        Integer lCouponPeriod = couponPeriod;
        Double price = 0.0;
        Integer drag = 0;
        drag = couponPeriod - periodoCuponOriginal;
        double intereses = 0.0;
        Calendar lCouponEndIntereses = null;
        intereses = nominalValue * (couponRate * lCouponPeriod / 36000);
        double interesesDev = 0.0;
        
        log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Diferencia Periodo original menos Periodo Cupon: " + drag);
        log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Intereses Del Periodo: " + intereses);
        log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Inicia Iteracion");

        for(int i = 0;i < couponsDueToExpire;i++){

            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Fecha de Valuacion: " + valuationDay.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Acumulado de Dias Inhabilies: " + drag);
            
            BackOfficeReporteCalculadoraPreciosDetalles backOfficeReporteCalculadoraPreciosDetalles = new BackOfficeReporteCalculadoraPreciosDetalles();
    		if(i==0) {
                
                int dialCouponStart = couponStrart.get(Calendar.DAY_OF_MONTH);
                int meslCouponStart = couponStrart.get(Calendar.MONTH) + 1;
                int aniolCouponStart = couponStrart.get(Calendar.YEAR);
                
                String fechalCouponEnd  = aniolCouponStart + "-" + meslCouponStart + "-" + dialCouponStart;
    			
    			backOfficeReporteCalculadoraPreciosDetalles.setFechaInicio(fechalCouponEnd);
    			
    		} else {
    			backOfficeReporteCalculadoraPreciosDetalles.setFechaInicio(backOfficeReporteCalculadoraPreciosDetalles_list.get(i-1).getFechaFin());
    		}

            //lCouponEnd.add(Calendar.DAY_OF_YEAR, lCouponPeriod);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, periodoCuponOriginal);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, drag);
            
            int dialCouponEnd = lCouponEnd.get(Calendar.DAY_OF_MONTH);
            int meslCouponEnd = lCouponEnd.get(Calendar.MONTH) + 1;
            int aniolCouponEnd = lCouponEnd.get(Calendar.YEAR);
            
            String fechalCouponEnd = aniolCouponEnd + "-" + meslCouponEnd + "-" + dialCouponEnd;
            
            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Nuevo Formato: " + fechalCouponEnd);

            backOfficeReporteCalculadoraPreciosDetalles.setFechaFin(fechalCouponEnd.toString());
            
            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);
            if(utilDao.Dia_Habil(fechalCouponEnd)==1){
                drag = CalendarUtil.calcDiffDays(lCouponEnd, utilDao.Dia_Ant(fechalCouponEnd,1));
                //arreglar conversión
                lCouponEnd = utilDao.Dia_Ant(fechalCouponEnd,1);

                log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] lCouponEnd utilDao: " + lCouponEnd.getTime().toString());
            }else {
                drag = 0;
            }    

            lCouponPeriod = CalendarUtil.calcDiffDays(lCouponEnd, lCouponStart);  
            
            //lCouponPeriod = daysDrag[0];
            //drag = daysDrag[1];
            Integer diffDays = CalendarUtil.calcDiffDays(lCouponEnd, valuationDay);
            if(i==0) {
            	
                lCouponEndIntereses = (Calendar)lCouponEnd.clone();
                interesesDev = nominalValue * (couponRate * (lCouponPeriod-diffDays) / 36000);
                retVal.setIntereses(Double.toString(interesesDev));
                
                log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] i=0 Fecha para el Calculo de Interese: " + lCouponEndIntereses.getTime().toString());
                log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] i=0 Interes Devengado: " + interesesDev);
            }

            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Fecha de Vencimiento de Cupon: " + lCouponEnd.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Periodo de Cupon: " + lCouponPeriod);
            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Dias x Vencer: " + diffDays);
            
            backOfficeReporteCalculadoraPreciosDetalles.setDvx(Integer.toString(diffDays));

            backOfficeReporteCalculadoraPreciosDetalles.setPeriodoCoupon(Integer.toString(lCouponPeriod));

            final Double fi = calcFiRev(nominalValue, couponRate, lCouponPeriod, i+1, couponsDueToExpire, diffDays, tasaDeMercado);

			DecimalFormat df = new DecimalFormat("#####0.00000000");
			String fi2 =  df.format(fi).toString();
            backOfficeReporteCalculadoraPreciosDetalles.setIntereses(fi2);
            
            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] i=0 Interes : " + fi);

            Double tasaDescuento = standingRate + tasaDeMercado;

            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Tasa de Descuento: " + tasaDescuento);
            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Factor Descuento: " + Math.pow( 1.0+(tasaDescuento*Double.valueOf(periodoCuponOriginal)/36000.0),
                    (diffDays/Double.valueOf(periodoCuponOriginal))));
            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Factor Descuento (parte superior): " +  1.0+(tasaDescuento*Double.valueOf(periodoCuponOriginal)/36000.0));
            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Factor Descuento (parte inferior): " +  (diffDays/Double.valueOf(periodoCuponOriginal)));
            
            price += fi/
                        Math.pow(1.0+(tasaDescuento*Double.valueOf(periodoCuponOriginal)/36000.0),
                            (diffDays/Double.valueOf(periodoCuponOriginal)) );
            

			df = new DecimalFormat("#####0.00000000");
			String valorPresente =  df.format((fi/
                        Math.pow(1.0+(tasaDescuento*Double.valueOf(periodoCuponOriginal)/36000.0),
                            (diffDays/Double.valueOf(periodoCuponOriginal)) ))).toString();
            
            backOfficeReporteCalculadoraPreciosDetalles.setValorPresente(valorPresente);
            
			df = new DecimalFormat("#####0.00000000");
			String price2 =  df.format(price).toString();
            backOfficeReporteCalculadoraPreciosDetalles.setSumaValorPresente(price2.toString());

            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Interes en Valor Presente: " + price);

            if(periodoCuponOriginal>lCouponPeriod) {
                log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] entra periodoCuponOriginal>lCouponPeriod ");
                drag = periodoCuponOriginal - lCouponPeriod;	
            }
//            lCouponPeriod = periodoCuponOriginal;
            lCouponStart = (Calendar)lCouponEnd.clone();
            
            backOfficeReporteCalculadoraPreciosDetalles_list.add(backOfficeReporteCalculadoraPreciosDetalles);
            
        } //fin for

        double priceclean = price - interesesDev;
        log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Precio Limpio: " + priceclean);

        if(CalendarUtil.calcDiffDays(lCouponEndIntereses, valuationDay)==0) {
            log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Fehca Valuacion es el Inicio de Cupon ");
            price = price - intereses;
        }

        log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Precio Sucio: " + price);
        log.info("[DirtyPriceCalculator][dirtPriceRevDetalles] Final de Iteracion: ");

		retVal.setBackOfficeReporteCalculadoraPreciosDetalles(backOfficeReporteCalculadoraPreciosDetalles_list);

        return price;
    }

    /*Modificacion LF EYS */
    public Double dirtPriceLF(Calendar couponStrart, Calendar valuationDay, Double standingRate, Integer couponPeriod,
                    Integer nominalValue, Double couponRate, Integer couponsDueToExpire, Integer periodoCuponOriginal) {
        log.info("[DirtyPriceCalculator][dirtPriceLF]");

        Calendar lCouponEnd = (Calendar) couponStrart.clone();
        Calendar lCouponStart = (Calendar) couponStrart.clone();
        Integer lCouponPeriod = couponPeriod;
        Double price = 0.0;
        Integer drag = 0;
        drag = couponPeriod - periodoCuponOriginal;
        this.fondeoTiie = fondeoDao.getFondeoTiieLastRegister();
        Double tasaDeMercado = Double.valueOf(this.fondeoTiie.getRate());
        double intereses = 0.0;
        double interesesDev = 0.0;
        Calendar lCouponEndIntereses = null;
        intereses = nominalValue * (couponRate * lCouponPeriod / 36000);

        log.info("[DirtyPriceCalculator][dirtPriceLF] Tasa de Mercado: " + this.fondeoTiie.getRate());
        log.info("[DirtyPriceCalculator][dirtPriceLF] Diferencia Periodo original menos Periodo Cupon: " + drag);
        log.info("[DirtyPriceCalculator][dirtPriceLF] Intereses Del Periodo: " + intereses);
        log.info("[DirtyPriceCalculator][dirtPriceLF] Inicia Iteracion");

        for (int i = 0; i < couponsDueToExpire; i++) {

            log.info("[DirtyPriceCalculator][dirtPriceLF] Fecha de Valuacion: " + valuationDay.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceLF] Acumulado de Dias Inhabilies: " + drag);

            lCouponEnd.add(Calendar.DAY_OF_YEAR, periodoCuponOriginal);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, drag);
            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);

            int dialCouponEnd = lCouponEnd.get(Calendar.DAY_OF_MONTH);
            int meslCouponEnd = lCouponEnd.get(Calendar.MONTH) + 1;
            int aniolCouponEnd = lCouponEnd.get(Calendar.YEAR);
            
            String fechalCouponEnd = aniolCouponEnd + "-" + meslCouponEnd + "-" + dialCouponEnd;
            
            log.info("[DirtyPriceCalculator][dirtPriceLF] Nuevo Formato: " + fechalCouponEnd);
            
            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);
            if(utilDao.Dia_Habil(fechalCouponEnd)==1){
                drag = CalendarUtil.calcDiffDays(lCouponEnd, utilDao.Dia_Ant(fechalCouponEnd,1));
                //arreglar conversión
                lCouponEnd = utilDao.Dia_Ant(fechalCouponEnd,1);

                log.info("[DirtyPriceCalculator][dirtPriceLF] lCouponEnd: " + lCouponEnd);
            }else {
                drag = 0;
            }    
            
            lCouponPeriod = CalendarUtil.calcDiffDays(lCouponEnd, lCouponStart);            
//            lCouponPeriod = daysDrag[0];
//            drag = daysDrag[1];
            Integer diffDays = CalendarUtil.calcDiffDays(lCouponEnd, valuationDay);
            if(i==0) {
                lCouponEndIntereses = (Calendar)lCouponEnd.clone();
                interesesDev = nominalValue * (couponRate * (lCouponPeriod-diffDays) / 36000);
                log.info("[DirtyPriceCalculator][dirtPriceLF] i=0 Fecha para el Calculo de Interese: " + lCouponEndIntereses.getTime().toString());
                log.info("[DirtyPriceCalculator][dirtPriceLF] i=0 Interes Devengado: " + interesesDev);
            }

            log.info("[DirtyPriceCalculator][dirtPriceLF] Fecha de Vencimiento de Cupon: " + lCouponEnd.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceLF] Periodo de Cupon: " + lCouponPeriod);
            log.info("[DirtyPriceCalculator][dirtPriceLF] Dias x Vencer: " + diffDays);

            final Double fi = calcFiLF(nominalValue, couponRate, lCouponPeriod, i + 1, couponsDueToExpire, diffDays,
                            tasaDeMercado);

            log.info("[DirtyPriceCalculator][dirtPriceLF] i=0 Interes : " + fi);

            Double tasaDescuento = standingRate + tasaDeMercado;

            log.info("[DirtyPriceCalculator][dirtPriceLF] Tasa de Descuento: " + tasaDescuento);
            log.info("[DirtyPriceCalculator][dirtPriceLF] Factor Descuento: " + Math.pow(1.0 + (tasaDescuento / 36000.0), diffDays));
            log.info("[DirtyPriceCalculator][dirtPriceLF] Factor Descuento (parte superior): " +  (1.0 + (tasaDescuento / 36000.0)));
            log.info("[DirtyPriceCalculator][dirtPriceLF] Factor Descuento (parte inferior): " +  (diffDays));

            price += fi / Math.pow(1.0 + (tasaDescuento / 36000.0), diffDays);

            log.info("[DirtyPriceCalculator][dirtPriceLF] Interes en Valor Presente: " + price);

            if (periodoCuponOriginal > lCouponPeriod) {
                drag = periodoCuponOriginal - lCouponPeriod;
                log.info("[DirtyPriceCalculator][dirtPriceLF] entra periodoCuponOriginal>lCouponPeriod ");
            }
//                lCouponPeriod = periodoCuponOriginal;
                lCouponStart = (Calendar) lCouponEnd.clone();
        }

        double priceclean = price - interesesDev;
        log.info("[DirtyPriceCalculator][dirtPriceLF] Precio Limpio: " + priceclean);

        if(CalendarUtil.calcDiffDays(lCouponEndIntereses, valuationDay)==0) {
            log.info("[DirtyPriceCalculator][dirtPriceLF] Fehca Valuacion es el Inicio de Cupon ");
            price = price - intereses;
        }
        log.info("[DirtyPriceCalculator][dirtPriceLF] Precio Sucio: " + price);
        log.info("[DirtyPriceCalculator][dirtPriceLF] Final de Iteracion: ");

        return price;
    }


    
    public Double dirtPriceLD(Calendar couponStrart, Calendar valuationDay, Double standingRate, Integer couponPeriod,
                    Integer nominalValue, Double couponRate, Integer couponsDueToExpire, Integer periodoCuponOriginal) {
        log.info("[DirtyPriceCalculator][dirtPriceLD]");

        Calendar lCouponEnd = (Calendar) couponStrart.clone();
        Calendar lCouponStart = (Calendar) couponStrart.clone();
        Integer lCouponPeriod = couponPeriod;
        Double price = 0.0;
        Integer drag = 0;
        drag = couponPeriod - periodoCuponOriginal;
        this.fondeoBancario = fondeoDao.getFondeoLastRegister();
        Double tasaDeMercado = Double.valueOf(this.fondeoBancario.getRate());
        double intereses = 0.0;
        double interesesDev = 0.0;
        Calendar lCouponEndIntereses = null;
        intereses = nominalValue * (couponRate * lCouponPeriod / 36000);

        log.info("[DirtyPriceCalculator][dirtPriceLD] Tasa de Mercado: " + this.fondeoBancario.getRate());
        log.info("[DirtyPriceCalculator][dirtPriceLD] Diferencia Periodo original menos Periodo Cupon: " + drag);
        log.info("[DirtyPriceCalculator][dirtPriceLD] Intereses Del Periodo: " + intereses);
        log.info("[DirtyPriceCalculator][dirtPriceLD] Inicia Iteracion");

        for (int i = 0; i < couponsDueToExpire; i++) {

            log.info("[DirtyPriceCalculator][dirtPriceLD] Fecha de Valuacion: " + valuationDay.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceLD] Acumulado de Dias Inhabilies: " + drag);

            lCouponEnd.add(Calendar.DAY_OF_YEAR, periodoCuponOriginal);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, drag);
            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);

            int dialCouponEnd = lCouponEnd.get(Calendar.DAY_OF_MONTH);
            int meslCouponEnd = lCouponEnd.get(Calendar.MONTH) + 1;
            int aniolCouponEnd = lCouponEnd.get(Calendar.YEAR);
            
            String fechalCouponEnd = aniolCouponEnd + "-" + meslCouponEnd + "-" + dialCouponEnd;
            
            log.info("[DirtyPriceCalculator][dirtPriceLD] Nuevo Formato: " + fechalCouponEnd);
            
            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);
            if(utilDao.Dia_Habil(fechalCouponEnd)==1){
                drag = CalendarUtil.calcDiffDays(lCouponEnd, utilDao.Dia_Ant(fechalCouponEnd,1));
                //arreglar conversión
                lCouponEnd = utilDao.Dia_Ant(fechalCouponEnd,1);

                log.info("[DirtyPriceCalculator][dirtPriceLD] lCouponEnd: " + lCouponEnd);
            }else {
                drag = 0;
            }    
            
            lCouponPeriod = CalendarUtil.calcDiffDays(lCouponEnd, lCouponStart);            
//            lCouponPeriod = daysDrag[0];
//            drag = daysDrag[1];
            Integer diffDays = CalendarUtil.calcDiffDays(lCouponEnd, valuationDay);
            if(i==0) {
                lCouponEndIntereses = (Calendar)lCouponEnd.clone();
                interesesDev = nominalValue * (couponRate * (lCouponPeriod-diffDays) / 36000);
                log.info("[DirtyPriceCalculator][dirtPriceLD] i=0 Fecha para el Calculo de Interese: " + lCouponEndIntereses.getTime().toString());
                log.info("[DirtyPriceCalculator][dirtPriceLD] i=0 Interes Devengado: " + interesesDev);
            }

            log.info("[DirtyPriceCalculator][dirtPriceLD] Fecha de Vencimiento de Cupon: " + lCouponEnd.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceLD] Periodo de Cupon: " + lCouponPeriod);
            log.info("[DirtyPriceCalculator][dirtPriceLD] Dias x Vencer: " + diffDays);

            final Double fi = calcFiLD(nominalValue, couponRate, lCouponPeriod, i + 1, couponsDueToExpire, diffDays,
                            tasaDeMercado);

            log.info("[DirtyPriceCalculator][dirtPriceLD] i=0 Interes : " + fi);

            Double tasaDescuento = standingRate + tasaDeMercado;

            log.info("[DirtyPriceCalculator][dirtPriceLD] Tasa de Descuento: " + tasaDescuento);
            log.info("[DirtyPriceCalculator][dirtPriceLD] Factor Descuento: " + Math.pow(1.0 + (tasaDescuento / 36000.0), diffDays));
            log.info("[DirtyPriceCalculator][dirtPriceLD] Factor Descuento (parte superior): " +  (1.0 + (tasaDescuento / 36000.0)));
            log.info("[DirtyPriceCalculator][dirtPriceLD] Factor Descuento (parte inferior): " +  (diffDays));

            price += fi / Math.pow(1.0 + (tasaDescuento / 36000.0), diffDays);

            log.info("[DirtyPriceCalculator][dirtPriceLD] Interes en Valor Presente: " + price);

            if (periodoCuponOriginal > lCouponPeriod) {
                drag = periodoCuponOriginal - lCouponPeriod;
                log.info("[DirtyPriceCalculator][dirtPriceLD] entra periodoCuponOriginal>lCouponPeriod ");
            }
//                lCouponPeriod = periodoCuponOriginal;
                lCouponStart = (Calendar) lCouponEnd.clone();
        }

        double priceclean = price - interesesDev;
        log.info("[DirtyPriceCalculator][dirtPriceLD] Precio Limpio: " + priceclean);

        if(CalendarUtil.calcDiffDays(lCouponEndIntereses, valuationDay)==0) {
            log.info("[DirtyPriceCalculator][dirtPriceLD] Fehca Valuacion es el Inicio de Cupon ");
            price = price - intereses;
        }
        log.info("[DirtyPriceCalculator][dirtPriceLD] Precio Sucio: " + price);
        log.info("[DirtyPriceCalculator][dirtPriceLD] Final de Iteracion: ");

        return price;
    }

    /*Modificacion LF EYS */

    public Double dirtPriceLFDetalles(Calendar couponStrart, Calendar valuationDay, Double standingRate, Integer couponPeriod,
                    Integer nominalValue, Double couponRate, Integer couponsDueToExpire, Integer periodoCuponOriginal,
                    BackOfficeReporteCalculadoraPreciosDetalles_list retVal) {
    	
        log.info("[DirtyPriceCalculator][dirtPriceLFDetalles]");
        
        List<BackOfficeReporteCalculadoraPreciosDetalles> backOfficeReporteCalculadoraPreciosDetalles_list =  new ArrayList<BackOfficeReporteCalculadoraPreciosDetalles>();

        Calendar lCouponEnd = (Calendar) couponStrart.clone();
        Calendar lCouponStart = (Calendar) couponStrart.clone();
        Integer lCouponPeriod = couponPeriod;
        Double price = 0.0;
        Integer drag = 0;
        drag = couponPeriod - periodoCuponOriginal;
        this.fondeoTiie = fondeo.getFondeoTiieLastRegister();
        Double tasaDeMercado = Double.valueOf(this.fondeoTiie.getRate());
        double intereses = 0.0;
        double interesesDev = 0.0;
        Calendar lCouponEndIntereses = null;
        intereses = nominalValue * (couponRate * lCouponPeriod / 36000);

        log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Tasa de Mercado: " + this.fondeoTiie.getRate());
        log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Diferencia Periodo original menos Periodo Cupon: " + drag);
        log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Intereses Del Periodo: " + intereses);
        log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Inicia Iteracion");

        for (int i = 0; i < couponsDueToExpire; i++) {

            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Fecha de Valuacion: " + valuationDay.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Acumulado de Dias Inhabilies: " + drag);
            
            BackOfficeReporteCalculadoraPreciosDetalles backOfficeReporteCalculadoraPreciosDetalles = new BackOfficeReporteCalculadoraPreciosDetalles();
            if(i==0) {
                
                int dialCouponStart = couponStrart.get(Calendar.DAY_OF_MONTH);
                int meslCouponStart = couponStrart.get(Calendar.MONTH) + 1;
                int aniolCouponStart = couponStrart.get(Calendar.YEAR);
                
                String fechalCouponEnd  = aniolCouponStart + "-" + meslCouponStart + "-" + dialCouponStart;
    			
    			backOfficeReporteCalculadoraPreciosDetalles.setFechaInicio(fechalCouponEnd);
    		} else {
    			backOfficeReporteCalculadoraPreciosDetalles.setFechaInicio(backOfficeReporteCalculadoraPreciosDetalles_list.get(i-1).getFechaFin());
    		}

            lCouponEnd.add(Calendar.DAY_OF_YEAR, periodoCuponOriginal);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, drag);
            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);

            int dialCouponEnd = lCouponEnd.get(Calendar.DAY_OF_MONTH);
            int meslCouponEnd = lCouponEnd.get(Calendar.MONTH) + 1;
            int aniolCouponEnd = lCouponEnd.get(Calendar.YEAR);
            
            String fechalCouponEnd = aniolCouponEnd + "-" + meslCouponEnd + "-" + dialCouponEnd;
            
            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Nuevo Formato: " + fechalCouponEnd);

            backOfficeReporteCalculadoraPreciosDetalles.setFechaFin(fechalCouponEnd.toString());
            
            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);
            if(utilDao.Dia_Habil(fechalCouponEnd)==1){
                drag = CalendarUtil.calcDiffDays(lCouponEnd, utilDao.Dia_Ant(fechalCouponEnd,1));
                //arreglar conversión
                lCouponEnd = utilDao.Dia_Ant(fechalCouponEnd,1);

                log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] lCouponEnd: " + lCouponEnd);
            }else {
                drag = 0;
            }    
            
            lCouponPeriod = CalendarUtil.calcDiffDays(lCouponEnd, lCouponStart);     
            
//            lCouponPeriod = daysDrag[0];
//            drag = daysDrag[1];
            Integer diffDays = CalendarUtil.calcDiffDays(lCouponEnd, valuationDay);
            if(i==0) {
                lCouponEndIntereses = (Calendar)lCouponEnd.clone();
                interesesDev = nominalValue * (couponRate * (lCouponPeriod-diffDays) / 36000);
                retVal.setIntereses(Double.toString(interesesDev));
                log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] i=0 Fecha para el Calculo de Interese: " + lCouponEndIntereses.getTime().toString());
                log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] i=0 Interes Devengado: " + interesesDev);
            }

            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Fecha de Vencimiento de Cupon: " + lCouponEnd.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Periodo de Cupon: " + lCouponPeriod);
            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Dias x Vencer: " + diffDays);
            
            backOfficeReporteCalculadoraPreciosDetalles.setDvx(Integer.toString(diffDays));
            
            backOfficeReporteCalculadoraPreciosDetalles.setPeriodoCoupon(Integer.toString(lCouponPeriod));

            final Double fi = calcFiLF(nominalValue, couponRate, lCouponPeriod, i + 1, couponsDueToExpire, diffDays,
                            tasaDeMercado);

			DecimalFormat df = new DecimalFormat("#####0.00000000");
			String fi2 =  df.format(fi).toString();
			
            backOfficeReporteCalculadoraPreciosDetalles.setIntereses(fi2);

            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] i=0 Interes : " + fi);

            Double tasaDescuento = standingRate + tasaDeMercado;

            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Tasa de Descuento: " + tasaDescuento);
            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Factor Descuento: " + Math.pow(1.0 + (tasaDescuento / 36000.0), diffDays));
            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Factor Descuento (parte superior): " +  (1.0 + (tasaDescuento / 36000.0)));
            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Factor Descuento (parte inferior): " +  (diffDays));

            price += fi / Math.pow(1.0 + (tasaDescuento / 36000.0), diffDays);

			df = new DecimalFormat("#####0.00000000");
			String valorPresente =  df.format(fi / Math.pow(1.0 + (tasaDescuento / 36000.0), diffDays)).toString();
            
            backOfficeReporteCalculadoraPreciosDetalles.setValorPresente(valorPresente);
            
			df = new DecimalFormat("#####0.00000000");
			String price2 =  df.format(price).toString();

            backOfficeReporteCalculadoraPreciosDetalles.setSumaValorPresente(price2);

            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Interes en Valor Presente: " + price);

            if (periodoCuponOriginal > lCouponPeriod) {
                drag = periodoCuponOriginal - lCouponPeriod;
                log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] entra periodoCuponOriginal>lCouponPeriod ");
            }
            //lCouponPeriod = periodoCuponOriginal;
            lCouponStart = (Calendar) lCouponEnd.clone();
            
            backOfficeReporteCalculadoraPreciosDetalles_list.add(backOfficeReporteCalculadoraPreciosDetalles);
        }

        double priceclean = price - interesesDev;
        log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Precio Limpio: " + priceclean);

        if(CalendarUtil.calcDiffDays(lCouponEndIntereses, valuationDay)==0) {
            log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Fehca Valuacion es el Inicio de Cupon ");
            price = price - intereses;
        }
        log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Precio Sucio: " + price);
        log.info("[DirtyPriceCalculator][dirtPriceLFDetalles] Final de Iteracion: ");

		retVal.setBackOfficeReporteCalculadoraPreciosDetalles(backOfficeReporteCalculadoraPreciosDetalles_list);

        return price;
    }

    public Double dirtPriceLDDetalles(Calendar couponStrart, Calendar valuationDay, Double standingRate, Integer couponPeriod,
                    Integer nominalValue, Double couponRate, Integer couponsDueToExpire, Integer periodoCuponOriginal,
                    BackOfficeReporteCalculadoraPreciosDetalles_list retVal) {
    	
        log.info("[DirtyPriceCalculator][dirtPriceLDDetalles]");
        
        List<BackOfficeReporteCalculadoraPreciosDetalles> backOfficeReporteCalculadoraPreciosDetalles_list =  new ArrayList<BackOfficeReporteCalculadoraPreciosDetalles>();

        Calendar lCouponEnd = (Calendar) couponStrart.clone();
        Calendar lCouponStart = (Calendar) couponStrart.clone();
        Integer lCouponPeriod = couponPeriod;
        Double price = 0.0;
        Integer drag = 0;
        drag = couponPeriod - periodoCuponOriginal;
        this.fondeoBancario = fondeoDao.getFondeoLastRegister();
        Double tasaDeMercado = Double.valueOf(this.fondeoBancario.getRate());
        double intereses = 0.0;
        double interesesDev = 0.0;
        Calendar lCouponEndIntereses = null;
        intereses = nominalValue * (couponRate * lCouponPeriod / 36000);

        log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Tasa de Mercado: " + this.fondeoBancario.getRate());
        log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Diferencia Periodo original menos Periodo Cupon: " + drag);
        log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Intereses Del Periodo: " + intereses);
        log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Inicia Iteracion");

        for (int i = 0; i < couponsDueToExpire; i++) {

            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Fecha de Valuacion: " + valuationDay.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Acumulado de Dias Inhabilies: " + drag);
            
            BackOfficeReporteCalculadoraPreciosDetalles backOfficeReporteCalculadoraPreciosDetalles = new BackOfficeReporteCalculadoraPreciosDetalles();
            if(i==0) {
                
                int dialCouponStart = couponStrart.get(Calendar.DAY_OF_MONTH);
                int meslCouponStart = couponStrart.get(Calendar.MONTH) + 1;
                int aniolCouponStart = couponStrart.get(Calendar.YEAR);
                
                String fechalCouponEnd  = aniolCouponStart + "-" + meslCouponStart + "-" + dialCouponStart;
    			
    			backOfficeReporteCalculadoraPreciosDetalles.setFechaInicio(fechalCouponEnd);
    		} else {
    			backOfficeReporteCalculadoraPreciosDetalles.setFechaInicio(backOfficeReporteCalculadoraPreciosDetalles_list.get(i-1).getFechaFin());
    		}

            lCouponEnd.add(Calendar.DAY_OF_YEAR, periodoCuponOriginal);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, drag);
            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);

            int dialCouponEnd = lCouponEnd.get(Calendar.DAY_OF_MONTH);
            int meslCouponEnd = lCouponEnd.get(Calendar.MONTH) + 1;
            int aniolCouponEnd = lCouponEnd.get(Calendar.YEAR);
            
            String fechalCouponEnd = aniolCouponEnd + "-" + meslCouponEnd + "-" + dialCouponEnd;
            
            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Nuevo Formato: " + fechalCouponEnd);

            backOfficeReporteCalculadoraPreciosDetalles.setFechaFin(fechalCouponEnd.toString());
            
            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);
            if(utilDao.Dia_Habil(fechalCouponEnd)==1){
                drag = CalendarUtil.calcDiffDays(lCouponEnd, utilDao.Dia_Ant(fechalCouponEnd,1));
                //arreglar conversión
                lCouponEnd = utilDao.Dia_Ant(fechalCouponEnd,1);

                log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] lCouponEnd: " + lCouponEnd);
            }else {
                drag = 0;
            }    
            
            lCouponPeriod = CalendarUtil.calcDiffDays(lCouponEnd, lCouponStart);     
            
//            lCouponPeriod = daysDrag[0];
//            drag = daysDrag[1];
            Integer diffDays = CalendarUtil.calcDiffDays(lCouponEnd, valuationDay);
            if(i==0) {
                lCouponEndIntereses = (Calendar)lCouponEnd.clone();
                interesesDev = nominalValue * (couponRate * (lCouponPeriod-diffDays) / 36000);
                retVal.setIntereses(Double.toString(interesesDev));
                log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] i=0 Fecha para el Calculo de Interese: " + lCouponEndIntereses.getTime().toString());
                log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] i=0 Interes Devengado: " + interesesDev);
            }

            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Fecha de Vencimiento de Cupon: " + lCouponEnd.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Periodo de Cupon: " + lCouponPeriod);
            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Dias x Vencer: " + diffDays);
            
            backOfficeReporteCalculadoraPreciosDetalles.setDvx(Integer.toString(diffDays));
            
            backOfficeReporteCalculadoraPreciosDetalles.setPeriodoCoupon(Integer.toString(lCouponPeriod));

            final Double fi = calcFiLD(nominalValue, couponRate, lCouponPeriod, i + 1, couponsDueToExpire, diffDays,
                            tasaDeMercado);

			DecimalFormat df = new DecimalFormat("#####0.00000000");
			String fi2 =  df.format(fi).toString();
			
            backOfficeReporteCalculadoraPreciosDetalles.setIntereses(fi2);

            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] i=0 Interes : " + fi);

            Double tasaDescuento = standingRate + tasaDeMercado;

            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Tasa de Descuento: " + tasaDescuento);
            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Factor Descuento: " + Math.pow(1.0 + (tasaDescuento / 36000.0), diffDays));
            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Factor Descuento (parte superior): " +  (1.0 + (tasaDescuento / 36000.0)));
            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Factor Descuento (parte inferior): " +  (diffDays));

            price += fi / Math.pow(1.0 + (tasaDescuento / 36000.0), diffDays);

			df = new DecimalFormat("#####0.00000000");
			String valorPresente =  df.format(fi / Math.pow(1.0 + (tasaDescuento / 36000.0), diffDays)).toString();
            
            backOfficeReporteCalculadoraPreciosDetalles.setValorPresente(valorPresente);
            
			df = new DecimalFormat("#####0.00000000");
			String price2 =  df.format(price).toString();

            backOfficeReporteCalculadoraPreciosDetalles.setSumaValorPresente(price2);

            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Interes en Valor Presente: " + price);

            if (periodoCuponOriginal > lCouponPeriod) {
                drag = periodoCuponOriginal - lCouponPeriod;
                log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] entra periodoCuponOriginal>lCouponPeriod ");
            }
            //lCouponPeriod = periodoCuponOriginal;
            lCouponStart = (Calendar) lCouponEnd.clone();
            
            backOfficeReporteCalculadoraPreciosDetalles_list.add(backOfficeReporteCalculadoraPreciosDetalles);
        }

        double priceclean = price - interesesDev;
        log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Precio Limpio: " + priceclean);

        if(CalendarUtil.calcDiffDays(lCouponEndIntereses, valuationDay)==0) {
            log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Fehca Valuacion es el Inicio de Cupon ");
            price = price - intereses;
        }
        log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Precio Sucio: " + price);
        log.info("[DirtyPriceCalculator][dirtPriceLDDetalles] Final de Iteracion: ");

		retVal.setBackOfficeReporteCalculadoraPreciosDetalles(backOfficeReporteCalculadoraPreciosDetalles_list);

        return price;
    }

    public Double dirtPriceM(Calendar couponStrart,
                            Calendar valuationDay,
                            Double standingRate,
                            Integer couponPeriod,
                            Integer nominalValue,
                            Double couponRate,
                            Integer couponsDueToExpire){
        Calendar lCouponEnd = (Calendar)couponStrart.clone();
        Calendar lCouponStart = (Calendar)couponStrart.clone();
        Integer lCouponPeriod = 182;//couponPeriod;
        Integer plazo = couponPeriod -lCouponPeriod;
        Double price = 0.0;
        Integer drag = 0;
        drag += plazo;
        double intereses = 0.0;
        Calendar lCouponEndIntereses = null;
        intereses = nominalValue * (couponRate * lCouponPeriod / 36000);
        double interesesDev = 0.0;
        
        log.info("[DirtyPriceCalculator][dirtPriceRev] Diferencia Periodo original menos Periodo Cupon: " + drag);
        log.info("[DirtyPriceCalculator][dirtPriceRev] Intereses Del Periodo: " + intereses);
        log.info("[DirtyPriceCalculator][dirtPriceRev] Inicia Iteracion");
         
         for(int i = 0;i < couponsDueToExpire;i++){
            log.info("[DirtyPriceCalculator][dirtPriceRev] Fecha de Valuacion: " + valuationDay.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceRev] Acumulado de Dias Inhabilies: " + drag);

            lCouponEnd.add(Calendar.DAY_OF_YEAR, lCouponPeriod);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, drag);
//            Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);

            int dialCouponEnd = lCouponEnd.get(Calendar.DAY_OF_MONTH);
            int meslCouponEnd = lCouponEnd.get(Calendar.MONTH) + 1;
            int aniolCouponEnd = lCouponEnd.get(Calendar.YEAR);
            
            String fechalCouponEnd = aniolCouponEnd + "-" + meslCouponEnd + "-" + dialCouponEnd;
            
            log.info("[DirtyPriceCalculator][dirtPriceRev] Nuevo Formato: " + fechalCouponEnd);
            

            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);
            if(utilDao.Dia_Habil(fechalCouponEnd)==1){
                drag = CalendarUtil.calcDiffDays(lCouponEnd, utilDao.Dia_Ant(fechalCouponEnd,1));
                //arreglar conversión
                lCouponEnd = utilDao.Dia_Ant(fechalCouponEnd,1);

                log.info("[DirtyPriceCalculator][dirtPriceRev] lCouponEnd: " + lCouponEnd);
            }else {
                drag = 0;
            }      
            Integer lPeriodoCupon = CalendarUtil.calcDiffDays(lCouponEnd, lCouponStart);            
//            drag = daysDrag[1];
            Integer diffDays = CalendarUtil.calcDiffDays(lCouponEnd, valuationDay);
            if(i==0) {
        	lCouponEndIntereses = (Calendar)lCouponEnd.clone();
                interesesDev = nominalValue * (couponRate * (lCouponPeriod-diffDays) / 36000);
                log.info("[DirtyPriceCalculator][dirtPriceRev] i=0 Fecha para el Calculo de Interese: " + lCouponEndIntereses.getTime().toString());
                log.info("[DirtyPriceCalculator][dirtPriceRev] i=0 Interes Devengado: " + interesesDev);
            }
            log.info("[DirtyPriceCalculator][dirtPriceRev] Fecha de Vencimiento de Cupon: " + lCouponEnd.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceRev] Periodo de Cupon: " + lPeriodoCupon);
            log.info("[DirtyPriceCalculator][dirtPriceRev] Dias x Vencer: " + diffDays);

            final Double fi = calcFi(nominalValue, couponRate, lPeriodoCupon, i+1, couponsDueToExpire);

            log.info("[DirtyPriceCalculator][dirtPriceRev] i=0 Interes : " + fi);
            
            log.info("[DirtyPriceCalculator][dirtPriceRev] Tasa de Rendimiento: " + standingRate);
            log.info("[DirtyPriceCalculator][dirtPriceRev] Factor Descuento: " + Math.pow(1.0+(standingRate*(182.0/36000.0)), diffDays/182.0));
            log.info("[DirtyPriceCalculator][dirtPriceRev] Factor Descuento (parte superior): " + ( 1.0+(standingRate*(182.0/36000.0))));
            log.info("[DirtyPriceCalculator][dirtPriceRev] Factor Descuento (parte inferior): " +  (diffDays/182.0));

            price += fi/ Math.pow(1.0+(standingRate*(182.0/36000.0)), diffDays/182.0);

            log.info("[DirtyPriceCalculator][dirtPriceRev] Interes en Valor Presente: " + price);

            lCouponStart = (Calendar)lCouponEnd.clone();
        }
        
        double priceclean = price - interesesDev;
        log.info("[DirtyPriceCalculator][dirtPriceRev] Precio Limpio: " + priceclean);

        if(CalendarUtil.calcDiffDays(lCouponEndIntereses, valuationDay)==0) {
            log.info("[DirtyPriceCalculator][dirtPriceRev] Fehca Valuacion es el Inicio de Cupon ");
            price = price - intereses;
        }
        log.info("[DirtyPriceCalculator][dirtPriceRev] Precio Sucio: " + price);
        log.info("[DirtyPriceCalculator][dirtPriceRev] Final de Iteracion: ");
        
        return price;
    }

    public Double dirtPriceMDetalles(Calendar couponStrart,
                            Calendar valuationDay,
                            Double standingRate,
                            Integer couponPeriod,
                            Integer nominalValue,
                            Double couponRate,
                            Integer couponsDueToExpire,
                            BackOfficeReporteCalculadoraPreciosDetalles_list retVal){

        log.info("[DirtyPriceCalculator][dirtPriceMDetalles]");
        
        List<BackOfficeReporteCalculadoraPreciosDetalles> backOfficeReporteCalculadoraPreciosDetalles_list =  new ArrayList<BackOfficeReporteCalculadoraPreciosDetalles>();

        Calendar lCouponEnd = (Calendar)couponStrart.clone();
        Calendar lCouponStart = (Calendar)couponStrart.clone();
        Integer lCouponPeriod = 182;//couponPeriod;
        Integer plazo = couponPeriod -lCouponPeriod;
        Double price = 0.0;
        Integer drag = 0;
        drag += plazo;
        double intereses = 0.0;
        Calendar lCouponEndIntereses = null;
        intereses = nominalValue * (couponRate * lCouponPeriod / 36000);
        double interesesDev = 0.0;
        
        log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Diferencia Periodo original menos Periodo Cupon: " + drag);
        log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Intereses Del Periodo: " + intereses);
        log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Inicia Iteracion");
         
         for(int i = 0;i < couponsDueToExpire;i++){
        	 
            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Fecha de Valuacion: " + valuationDay.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Acumulado de Dias Inhabilies: " + drag);
            
            BackOfficeReporteCalculadoraPreciosDetalles backOfficeReporteCalculadoraPreciosDetalles = new BackOfficeReporteCalculadoraPreciosDetalles();
            if(i==0) {
                
                int dialCouponStart = couponStrart.get(Calendar.DAY_OF_MONTH);
                int meslCouponStart = couponStrart.get(Calendar.MONTH) + 1;
                int aniolCouponStart = couponStrart.get(Calendar.YEAR);
                
                String fechalCouponEnd  = aniolCouponStart + "-" + meslCouponStart + "-" + dialCouponStart;
    			
    			backOfficeReporteCalculadoraPreciosDetalles.setFechaInicio(fechalCouponEnd);
    		} else {
    			backOfficeReporteCalculadoraPreciosDetalles.setFechaInicio(backOfficeReporteCalculadoraPreciosDetalles_list.get(i-1).getFechaFin());
    		}

            lCouponEnd.add(Calendar.DAY_OF_YEAR, lCouponPeriod);
            lCouponEnd.add(Calendar.DAY_OF_YEAR, drag);
            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);
            
            int dialCouponEnd = lCouponEnd.get(Calendar.DAY_OF_MONTH);
            int meslCouponEnd = lCouponEnd.get(Calendar.MONTH) + 1;
            int aniolCouponEnd = lCouponEnd.get(Calendar.YEAR);
            
            String fechalCouponEnd = aniolCouponEnd + "-" + meslCouponEnd + "-" + dialCouponEnd;
            
            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Nuevo Formato: " + fechalCouponEnd);

            backOfficeReporteCalculadoraPreciosDetalles.setFechaFin(fechalCouponEnd.toString());

            //Integer[] daysDrag = calcDays(lCouponStart, lCouponEnd);
            if(utilDao.Dia_Habil(fechalCouponEnd)==1){
            	
                drag = CalendarUtil.calcDiffDays(lCouponEnd, utilDao.Dia_Ant(fechalCouponEnd,1));
                //arreglar conversión
                lCouponEnd = utilDao.Dia_Ant(fechalCouponEnd,1);

                log.info("[DirtyPriceCalculator][dirtPriceMDetalles] lCouponEnd: " + lCouponEnd);
                
            } else {
            	
                drag = 0;
                
            }      
            Integer lPeriodoCupon = CalendarUtil.calcDiffDays(lCouponEnd, lCouponStart);     
            
            //drag = daysDrag[1];
            Integer diffDays = CalendarUtil.calcDiffDays(lCouponEnd, valuationDay);
            
            if(i==0) {
            	
            	lCouponEndIntereses = (Calendar)lCouponEnd.clone();
                
        		interesesDev = nominalValue * (couponRate * (lCouponPeriod-diffDays) / 36000);
                log.info("[DirtyPriceCalculator][dirtPriceMDetalles] i=0 Fecha para el Calculo de Interese: " + lCouponEndIntereses.getTime().toString());
                log.info("[DirtyPriceCalculator][dirtPriceMDetalles] i=0 Interes Devengado: " + interesesDev);
                retVal.setIntereses(Double.toString(interesesDev));
            }
            
            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Fecha de Vencimiento de Cupon: " + lCouponEnd.getTime().toString());
            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Periodo de Cupon: " + lPeriodoCupon);
            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Dias x Vencer: " + diffDays);
            
            backOfficeReporteCalculadoraPreciosDetalles.setDvx(Integer.toString(diffDays));
            
            backOfficeReporteCalculadoraPreciosDetalles.setPeriodoCoupon(Integer.toString(lCouponPeriod));

            final Double fi = calcFi(nominalValue, couponRate, lPeriodoCupon, i+1, couponsDueToExpire);
            
			DecimalFormat df = new DecimalFormat("#####0.00000000");
			String fi2 =  df.format(fi).toString();

            backOfficeReporteCalculadoraPreciosDetalles.setIntereses(fi2);

            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] i=0 Interes : " + fi);
            
            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Tasa de Rendimiento: " + standingRate);
            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Factor Descuento: " + Math.pow(1.0+(standingRate*(182.0/36000.0)), diffDays/182.0));
            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Factor Descuento (parte superior): " + ( 1.0+(standingRate*(182.0/36000.0))));
            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Factor Descuento (parte inferior): " +  (diffDays/182.0));

            price += fi/ Math.pow(1.0+(standingRate*(182.0/36000.0)), diffDays/182.0);

			df = new DecimalFormat("#####0.00000000");
			String valorPresente =  df.format(fi/ Math.pow(1.0+(standingRate*(182.0/36000.0)), diffDays/182.0)).toString();
            
            backOfficeReporteCalculadoraPreciosDetalles.setValorPresente(valorPresente);
            
			df = new DecimalFormat("#####0.00000000");
			String price2 =  df.format(price).toString();

            backOfficeReporteCalculadoraPreciosDetalles.setSumaValorPresente(price2.toString());

            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Interes en Valor Presente: " + price);

            lCouponStart = (Calendar)lCouponEnd.clone();
            
            backOfficeReporteCalculadoraPreciosDetalles_list.add(backOfficeReporteCalculadoraPreciosDetalles);
        }
        
        double priceclean = price - interesesDev;
        log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Precio Limpio: " + priceclean);

        if(CalendarUtil.calcDiffDays(lCouponEndIntereses, valuationDay)==0) {
            log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Fehca Valuacion es el Inicio de Cupon ");
            price = price - intereses;
        }
        log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Precio Sucio: " + price);
        log.info("[DirtyPriceCalculator][dirtPriceMDetalles] Final de Iteracion: ");

		retVal.setBackOfficeReporteCalculadoraPreciosDetalles(backOfficeReporteCalculadoraPreciosDetalles_list);
        
        return price;
    }
    
    public Double calcFiRev(Integer nominalValue, Double couponRate, Integer couponDays, Integer cycle,
                    Integer totalCouponsToPay, Integer daysTransc, Double tasaMercado) {
        log.info("[DirtyPriceCalculator][calcFiRev]");
        Double result = null;
        
        if (cycle == 1) {
            if (cycle == totalCouponsToPay) {
                result = nominalValue + nominalValue * (couponRate * couponDays / 36000);
                log.info("[DirtyPriceCalculator][calcFiRev] 3er IF Inereses del cupon: " + result);
               }else { result = nominalValue * (couponRate * couponDays / 36000);
               log.info("[DirtyPriceCalculator][calcFiRev] 1er IF Inereses del cupon: " + result);
            }
        } else if (cycle == totalCouponsToPay) {
                result = nominalValue + nominalValue * (tasaMercado * couponDays / 36000);
                log.info("[DirtyPriceCalculator][calcFiRev] 3er IF Inereses del cupon: " + result);
        } else {
                result = nominalValue * (tasaMercado * couponDays / 36000);
                log.info("[DirtyPriceCalculator][calcFiRev] 2do IF Inereses del cupon: " + result);
        }
        return result;
    }

    /*Modificacion LF EYS*/

    public Double calcFiLF(Integer nominalValue, Double couponRate, Integer couponDays, Integer cycle,
			Integer totalCouponsToPay, Integer daysTransc, Double tasaMercado) {
        log.info("[DirtyPriceCalculator][dirtPriceLF]");
        Double result = null;
        
        if (cycle == 1) {
           if (cycle == totalCouponsToPay) {
                result = nominalValue + nominalValue * (1 + couponRate * (couponDays - daysTransc) / 36000)
                            * (Math.pow((1 + tasaMercado / 36000), daysTransc)) - 100;
                log.info("[DirtyPriceCalculator][dirtPriceLF] 1er IF Inereses del cupon: " + result);
            } else {
            result = nominalValue * (1 + couponRate * (couponDays - daysTransc) / 36000)
                            * (Math.pow((1 + tasaMercado / 36000), daysTransc)) - 100;
            log.info("[DirtyPriceCalculator][dirtPriceLF] 1er IF Inereses del cupon: " + result);
        }
        } else if (cycle == totalCouponsToPay) {
            result = nominalValue + (nominalValue * (Math.pow((1 + tasaMercado / 36000), couponDays)) - 100);
            log.info("[DirtyPriceCalculator][dirtPriceLF] 3er IF Inereses del cupon: " + result);
        } else {
                result = nominalValue * (Math.pow((1 + tasaMercado / 36000), couponDays)) - 100;
                log.info("[DirtyPriceCalculator][dirtPriceLF] 2do if (else) Inereses del cupon: " + result);
        }
        return result;
    }


    public Double calcFiLD(Integer nominalValue, Double couponRate, Integer couponDays, Integer cycle,
			Integer totalCouponsToPay, Integer daysTransc, Double tasaMercado) {
        log.info("[DirtyPriceCalculator][dirtPriceLD]");
        Double result = null;
        
        if (cycle == 1) {
           if (cycle == totalCouponsToPay) {
                result = nominalValue + nominalValue * (1 + couponRate * (couponDays - daysTransc) / 36000)
                            * (Math.pow((1 + tasaMercado / 36000), daysTransc)) - 100;
                log.info("[DirtyPriceCalculator][dirtPriceLD] 1er IF Inereses del cupon: " + result);
            } else {
            result = nominalValue * (1 + couponRate * (couponDays - daysTransc) / 36000)
                            * (Math.pow((1 + tasaMercado / 36000), daysTransc)) - 100;
            log.info("[DirtyPriceCalculator][dirtPriceLD] 1er IF Inereses del cupon: " + result);
        }
        } else if (cycle == totalCouponsToPay) {
            result = nominalValue + (nominalValue * (Math.pow((1 + tasaMercado / 36000), couponDays)) - 100);
            log.info("[DirtyPriceCalculator][dirtPriceLD] 3er IF Inereses del cupon: " + result);
        } else {
                result = nominalValue * (Math.pow((1 + tasaMercado / 36000), couponDays)) - 100;
                log.info("[DirtyPriceCalculator][dirtPriceLD] 2do if (else) Inereses del cupon: " + result);
        }
        return result;
    }
   
    public Double calcFi (Integer nominalValue, Double couponRate, Integer couponDays, Integer cycle, Integer
            totalCouponsToPay){
        Double result = null;
        if (cycle == totalCouponsToPay) {
            result = nominalValue + (nominalValue * couponDays * couponRate) / (36000);
            log.info("[DirtyPriceCalculator][dirtPriceLD] 2do IF Inereses del cupon: " + result);
        } else {
            result = (nominalValue * couponDays * couponRate) / (36000);
            log.info("[DirtyPriceCalculator][dirtPriceLD] 1er IF Inereses del cupon: " + result);
        }
        return result;
    }
     
    public Double calcBi(Double standingRate, Calendar reportDate, Calendar expirationDate) {
        return 10.0 / (1 + standingRate * (CalendarUtil.calcDiffDays(expirationDate, reportDate)) / 36000.0);
    }

    public Double interst(ValueType vt, Double standingRate, Double couponRate, Calendar couponStart, Calendar couponEnd, Calendar expirationDate, Calendar reportDate, String couponPeriod, Double yield, Double marketSurcharge, Integer updatedNominalValue) {
        CalendarUtil.zeroTimeCalendar(reportDate);
        CalendarUtil.zeroTimeCalendar(couponEnd);
        Integer couponPeriodInt = Integer.parseInt(couponPeriod.substring(0, couponPeriod.length() - 4));
        Integer elapsedDays = couponPeriodInt - ((Long) TimeUnit.DAYS.convert(couponEnd.getTime().getTime() - reportDate.getTime().getTime(), TimeUnit.MILLISECONDS)).intValue();
        Double interest = elapsedDays * couponRate * updatedNominalValue / 36000.0;
        return null;
    }

    public Integer[] calcDays(Calendar startCoupon, Calendar endCoupon) {
        Calendar origianlDate;
        Calendar lEndCoupon;
        Integer result[] = new Integer[2];
        origianlDate = (Calendar) endCoupon.clone();
        List<CalendarEntity> byDateEquals = daoCalendar.findByDateEqualsAndFkIdMarketTypeEquals(endCoupon, 1);
        boolean isHolidayIn = false;
        if (endCoupon.get(Calendar.DAY_OF_WEEK) == 7 || endCoupon.get(Calendar.DAY_OF_WEEK) == 1) {
            isHolidayIn = true;
            if (endCoupon.get(Calendar.DAY_OF_WEEK) == 7) {
                result[0] = CalendarUtil.calcDiffDays(endCoupon, startCoupon) - 1;
                result[1] = 1;
            } else if (endCoupon.get(Calendar.DAY_OF_WEEK) == 1) {
                result[0] = CalendarUtil.calcDiffDays(endCoupon, startCoupon) - 2;
                result[1] = 2;
            }
            return result;
        } else if (byDateEquals.size() > 0 && byDateEquals.get(0) != null) {
            CalendarEntity calendarEntity = byDateEquals.get(0);
            isHolidayIn = calendarEntity.getIsHoliday() == 0 ? false : true;
        } else {
            result[0] = CalendarUtil.calcDiffDays(endCoupon, startCoupon);
            result[1] = 0;
            return result;
        }
        if (isHolidayIn) {
            lEndCoupon = (daoCalendar.findTop1ByDateLessThanEqualAndIsHolidayEqualsOrderByDateDesc(endCoupon, (byte) 1))
                    .get(0).getDate();
            result[1] = CalendarUtil.calcDiffDays(origianlDate, lEndCoupon);
        } else {
            result[1] = 0;
        }
        result[0] = CalendarUtil.calcDiffDays(endCoupon, startCoupon);
        return result;
    }

}