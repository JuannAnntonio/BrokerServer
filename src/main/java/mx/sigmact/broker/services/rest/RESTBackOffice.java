package mx.sigmact.broker.services.rest;

import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.dao.BackOfficeDao;
import mx.sigmact.broker.model.InstitutionEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.pojo.backoffice.BackOfficeMainView;
import mx.sigmact.broker.pojo.backoffice.DTABackOfficeDashboard;
import mx.sigmact.broker.repositories.BrokerInstitutionRepository;
import mx.sigmact.broker.repositories.BrokerUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    private BrokerUserRepository userRepo;

    @Resource
    private BrokerInstitutionRepository instRepo;

    @Resource(name = "formatDate")
    private SimpleDateFormat sdf;

    private static Logger log = LoggerFactory.getLogger(RESTBackOffice.class);

    /* ********************* Trader inital set up ************************************/

    /**
     * This method is for the main back office information returns the
     * @return A list with main information for back office
     */
    @RequestMapping(value = "getDashboard", method = RequestMethod.GET, produces = "application/json")
    public DTABackOfficeDashboard doGetTraderInstruments() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findOneByUsername(name);
        InstitutionEntity userInstitution = instRepo.findOne(user.getFkIdInstitution());
        Calendar cal = Calendar.getInstance();
        cal = CalendarUtil.zeroTimeCalendar(cal);
        String format = sdf.format(cal.getTime());
        DTABackOfficeDashboard retVal =
                new DTABackOfficeDashboard(
                        backOfficeDao.getBackOfficeMainView(userInstitution.getIdInstitution(), format));
        return retVal;
    }
}
