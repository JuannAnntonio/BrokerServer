package mx.sigmact.broker.services.rest;

import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.dao.BackOfficeDao;
import mx.sigmact.broker.dao.ParameterDao;
import mx.sigmact.broker.model.InstitutionEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.pojo.backoffice.DTABackOfficeDashboard;
import mx.sigmact.broker.pojo.parameter.ValueParameter;

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
@RequestMapping("parameter/rest")
public class RESTParameter {

    @Resource
    private DirtyPriceCalculator dpCalc;

    @Resource(name = "formatDate")
    private SimpleDateFormat sdf;
    
    @Resource
    private ParameterDao parameterDao;

	ValueParameter valueParameter = null;
	
    Logger log = Logger.getLogger(RESTParameter.class);

    /**
     * This method is for the main back office information returns the
     * @return A list with main information for back office
     */
    @RequestMapping(value = "getParameter", method = RequestMethod.GET, produces = "application/json")
    public ValueParameter doGetParameter(@RequestParam("id_parameter_value") String id_parameter_value) {
    	
        log.info("[RESTParameter][doGetParameter]");
        log.info("[RESTParameter][doGetParameter] id_parameter_value: " + id_parameter_value);

        this.valueParameter = parameterDao.getParameter(id_parameter_value);
        
        return this.valueParameter;
    }
}
