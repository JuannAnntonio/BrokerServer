package mx.sigmact.broker.services.rest;

import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.dao.BackOfficeDao;
import mx.sigmact.broker.dao.Coupon_bondeDao;
import mx.sigmact.broker.dao.FondeoDao;
import mx.sigmact.broker.dao.ParameterDao;
import mx.sigmact.broker.model.InstitutionEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.pojo.backoffice.DTABackOfficeDashboard;
import mx.sigmact.broker.pojo.coupon_bonde.Coupon_rate;
import mx.sigmact.broker.pojo.fondeo.FondeoBancario;
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
@RequestMapping("coupon_bonde/rest")
public class RESTCoupon_bonde {

    @Resource
    private DirtyPriceCalculator dpCalc;

    @Resource(name = "formatDate")
    private SimpleDateFormat sdf;
    
    @Resource
    private Coupon_bondeDao coupon_bondeDao;

    Coupon_rate coupon_rate = null;
	
    Logger log = Logger.getLogger(RESTCoupon_bonde.class);

    /**
     * This method is for the main back office information returns the
     * @return A list with main information for back office
     */
    @RequestMapping(value = "getRate", method = RequestMethod.GET, produces = "application/json")
    public Coupon_rate doGetFondeoLastRegister(@RequestParam("date") String date, @RequestParam("series") String series) {
    	
        log.info("[RESTCoupon_bonde][getRate]");

        this.coupon_rate = coupon_bondeDao.getRate(date, series);
        
        return this.coupon_rate;
    }
}
