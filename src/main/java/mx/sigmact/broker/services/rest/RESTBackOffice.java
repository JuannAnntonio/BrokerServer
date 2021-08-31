package mx.sigmact.broker.services.rest;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.dao.BackOfficeDao;
import mx.sigmact.broker.dao.ParameterDao;
import mx.sigmact.broker.model.InstitutionEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.pojo.backoffice.DTABackOfficeDashboard;
import mx.sigmact.broker.pojo.backoffice.DTABackOfficeReporteCalculadoraPrecios;
import mx.sigmact.broker.pojo.backoffice.DTABackOfficeReporteCalculadoraPreciosDetalles;
import mx.sigmact.broker.pojo.backoffice.DTABackOfficeReporteCartaConfirmacion;
import mx.sigmact.broker.pojo.parameter.ValueParameter;
import mx.sigmact.broker.repositories.BrokerInstitutionRepository;
import mx.sigmact.broker.repositories.BrokerUserRepository;

/**
 * Created on 07/12/16.
 */

@RestController
@RequestMapping("backoffice/rest")
public class RESTBackOffice {

	@Resource
	private DirtyPriceCalculator dpCalc;

	@Resource
	private BackOfficeDao backOfficeDao;

	@Resource
	private ParameterDao parameterDao;

	@Resource
	private BrokerUserRepository userRepo;

	@Resource
	private BrokerInstitutionRepository instRepo;

	@Resource(name = "formatDate")
	private SimpleDateFormat sdf;

	Logger log = Logger.getLogger(RESTBackOffice.class);

	/**
	 * This method is for the main back office information returns the
	 * 
	 * @return A list with main information for back office
	 * 
	 * 
	 */
	@RequestMapping(value = "getDashboard", method = RequestMethod.GET, produces = "application/json")
	public DTABackOfficeDashboard getDashboard(@RequestParam("start") String start, @RequestParam("end") String end) {

		log.info("[RESTBackOffice][getDashboard]");

		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		UserEntity user = userRepo.findOneByUsername(name);
		InstitutionEntity userInstitution = instRepo.findOne(user.getFkIdInstitution());

		/*
		 * Fecha de hoy date Calendar cal = Calendar.getInstance(); cal =
		 * CalendarUtil.zeroTimeCalendar(cal); String date = sdf.format(cal.getTime());
		 */

		ValueParameter valueParameter = parameterDao.getParameter("today");

		DTABackOfficeDashboard retVal = new DTABackOfficeDashboard(backOfficeDao
				.getBackOfficeMainView(userInstitution.getIdInstitution(), valueParameter.getValue(), start, end));

		return retVal;
	}

	/**
	 * This method is for the main back office information returns the
	 * 
	 * @return A list with main information for back office
	 */
	@RequestMapping(value = "getReporteCartaConfirmacion", method = RequestMethod.GET, produces = "application/json")
	public DTABackOfficeReporteCartaConfirmacion getReporteCartaConfirmacion(@RequestParam("start") String start,
			@RequestParam("end") String end) {

		log.info("[RESTBackOffice][getReporteCartaConfirmacion]");

		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		UserEntity user = userRepo.findOneByUsername(name);
		InstitutionEntity userInstitution = instRepo.findOne(user.getFkIdInstitution());

		/*
		 * Fecha de hoy date Calendar cal = Calendar.getInstance(); cal =
		 * CalendarUtil.zeroTimeCalendar(cal); String date = sdf.format(cal.getTime());
		 */

		ValueParameter valueParameter = parameterDao.getParameter("today");

		DTABackOfficeReporteCartaConfirmacion retVal = new DTABackOfficeReporteCartaConfirmacion(
				backOfficeDao.getBackOfficeReporteCartaConfirmacion(userInstitution.getIdInstitution(),
						valueParameter.getValue(), start, end));

		return retVal;
	}

	@RequestMapping(value = "getReporteCalculadoraPrecios", method = RequestMethod.GET, produces = "application/json")
	public DTABackOfficeReporteCalculadoraPrecios getReporteCalculadoraPrecios(
			@RequestParam(value = "tv", required = true) String tv) {

		log.info("[RESTBackOffice][getReporteCalculadoraPrecios]");

		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		UserEntity user = userRepo.findOneByUsername(name);
		InstitutionEntity userInstitution = instRepo.findOne(user.getFkIdInstitution());

		/*
		 * Fecha de hoy date Calendar cal = Calendar.getInstance(); cal =
		 * CalendarUtil.zeroTimeCalendar(cal); String date = sdf.format(cal.getTime());
		 */

		ValueParameter valueParameter = parameterDao.getParameter("today");

		DTABackOfficeReporteCalculadoraPrecios retVal = new DTABackOfficeReporteCalculadoraPrecios(
				backOfficeDao.getBackOfficeReporteCalculadoraPrecios(userInstitution.getIdInstitution(),
						valueParameter.getValue(), tv));

		return retVal;
	}

	@RequestMapping(value = "getReporteCalculadoraPreciosDetalles", method = RequestMethod.GET, produces = "application/json")
	public DTABackOfficeReporteCalculadoraPreciosDetalles getReporteCalculadoraPreciosDetalles(
			@RequestParam(value = "id_valmer", required = true) String id_valmer) {

		log.info("[RESTBackOffice][getReporteCalculadoraPrecios]");

		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		UserEntity user = userRepo.findOneByUsername(name);
		InstitutionEntity userInstitution = instRepo.findOne(user.getFkIdInstitution());

		/*
		 * Fecha de hoy date Calendar cal = Calendar.getInstance(); cal =
		 * CalendarUtil.zeroTimeCalendar(cal); String date = sdf.format(cal.getTime());
		 */

		ValueParameter valueParameter = parameterDao.getParameter("today");

		DTABackOfficeReporteCalculadoraPreciosDetalles retVal = new DTABackOfficeReporteCalculadoraPreciosDetalles(
				backOfficeDao.getBackOfficeReporteCalculadoraPreciosDetalles(userInstitution.getIdInstitution(),
						valueParameter.getValue(), id_valmer));

		return retVal;
	}
}
