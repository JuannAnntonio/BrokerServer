package mx.sigmact.broker.services.rest;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.dao.FondeoDao;
import mx.sigmact.broker.pojo.fondeo.FondeoBancario;
import mx.sigmact.broker.pojo.fondeo.FondeoCetes;
import mx.sigmact.broker.pojo.fondeo.FondeoGubernamental;
/*Modificacion LF EYS */
import mx.sigmact.broker.pojo.fondeo.FondeoTiie;

/**
 * Created on 07/12/16.
 */

@RestController
@RequestMapping("fondeo/rest")
public class RESTFondeo {

	@Resource
	private DirtyPriceCalculator dpCalc;

	@Resource(name = "formatDate")
	private SimpleDateFormat sdf;

	@Resource
	private FondeoDao fondeoDao;

	FondeoBancario fondeoBancario = null;

	FondeoGubernamental fondeoGubernamental = null;

	FondeoCetes fondeoCetes = null;

	/* Modificacion LF EYS */
	FondeoTiie fondeoTiie = null;

	Logger log = Logger.getLogger(RESTFondeo.class);

	/**
	 * This method is for the main back office information returns the
	 * 
	 * @return A list with main information for back office
	 */
	@RequestMapping(value = "getFondeoLastRegister", method = RequestMethod.GET, produces = "application/json")
	public FondeoBancario doGetFondeoLastRegister() {

		log.info("[RESTFondeo][getFondeoLastRegister]");

		this.fondeoBancario = fondeoDao.getFondeoLastRegister();

		return this.fondeoBancario;
	}

	/* Modificacion LF EYS */
	@RequestMapping(value = "getFondeoTiieLastRegister", method = RequestMethod.GET, produces = "application/json")
	public FondeoTiie doGetFondeoTiieLastRegister() {

		log.info("[RESTFondeo][getFondeoTiieLastRegister]");

		this.fondeoTiie = fondeoDao.getFondeoTiieLastRegister();

		return this.fondeoTiie;
	}

	/**
	 * This method is for the main back office information returns the
	 * 
	 * @return A list with main information for back office
	 */
	@RequestMapping(value = "getFondeoGubernamentalLastRegister", method = RequestMethod.GET, produces = "application/json")
	public FondeoGubernamental doGetFondeoGubernamentalLastRegister() {

		log.info("[RESTFondeo][doGetFondeoGubernamentalLastRegister]");

		this.fondeoGubernamental = fondeoDao.getFondeoGubernamentalLastRegister();

		return this.fondeoGubernamental;
	}

	/**
	 * This method is for the main back office information returns the
	 * 
	 * @return A list with main information for back office
	 */
	@RequestMapping(value = "getFondeoCetesLastRegister", method = RequestMethod.GET, produces = "application/json")
	public FondeoCetes doGetFondeoCetesLastRegister() {

		log.info("[RESTFondeo][doGetFondeoCetesLastRegister]");

		this.fondeoCetes = fondeoDao.getFondeoCetesLastRegister();

		return this.fondeoCetes;
	}
}
