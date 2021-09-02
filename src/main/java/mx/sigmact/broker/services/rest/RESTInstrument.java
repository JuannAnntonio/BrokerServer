package mx.sigmact.broker.services.rest;

import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.dao.BackOfficeDao;
import mx.sigmact.broker.dao.FondeoDao;
import mx.sigmact.broker.dao.InstrumentDao;
import mx.sigmact.broker.dao.ParameterDao;
import mx.sigmact.broker.dao.UdiDao;
import mx.sigmact.broker.model.InstitutionEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.pojo.backoffice.DTABackOfficeDashboard;
import mx.sigmact.broker.pojo.fondeo.FondeoBancario;
import mx.sigmact.broker.pojo.fondeo.FondeoTiie;
import mx.sigmact.broker.pojo.fondeo.FondeoCetes;
import mx.sigmact.broker.pojo.fondeo.FondeoGubernamental;
import mx.sigmact.broker.pojo.instrument.Instrument;
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
@RequestMapping("instrument/rest")
public class RESTInstrument {


    @Resource(name = "formatDate")
    private SimpleDateFormat sdf;
    
    private Instrument instrument = null;
    
    @Resource
    private InstrumentDao instrumentDao;
	
    Logger log = Logger.getLogger(RESTInstrument.class);

    /**
     * This method is for the main back office information returns the
     * @return A list with main information for back office
     */
    @RequestMapping(value = "getInstrument", method = RequestMethod.GET, produces = "application/json")
    public Instrument doGetInstrument(@RequestParam("tv") String tv) {
    	
        log.info("[RESTUdi][doGetInstrument]");

        this.instrument = instrumentDao.getInstrument(tv);
        
        return this.instrument;
    }
    
}
