package mx.sigmact.broker.services.rest;

import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.dao.BackOfficeDao;
import mx.sigmact.broker.dao.FondeoDao;
import mx.sigmact.broker.dao.ParameterDao;
import mx.sigmact.broker.dao.UdiDao;
import mx.sigmact.broker.model.InstitutionEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.pojo.backoffice.DTABackOfficeDashboard;
import mx.sigmact.broker.pojo.fondeo.FondeoBancario;
import mx.sigmact.broker.pojo.fondeo.FondeoCetes;
import mx.sigmact.broker.pojo.fondeo.FondeoGubernamental;
import mx.sigmact.broker.pojo.parameter.ValueParameter;
import mx.sigmact.broker.pojo.udi.Udi;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created on 07/12/16.
 */

@RestController
@RequestMapping("udi/rest")
public class RESTUdi {


    @Resource(name = "formatDate")
    private SimpleDateFormat sdf;
    
    private Udi udi = null;
    
    @Resource
    private UdiDao udiDao;
	
    Logger log = Logger.getLogger(RESTUdi.class);

    /**
     * This method is for the main back office information returns the
     * @return A list with main information for back office
     */
    @RequestMapping(value = "getUdi", method = RequestMethod.GET, produces = "application/json")
    public Udi doGetUdi(@RequestParam("date") String date) {
    	
        log.info("[RESTUdi][doGetUdi]");
        
        log.info("[RESTUdi][doGetUdi] date: " + date);

        this.udi = udiDao.getUdi(date);
        
        return this.udi;
    }
    
}
