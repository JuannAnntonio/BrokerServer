package mx.sigmact.broker.services.timers;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.pojo.StandingStatus;
import mx.sigmact.broker.repositories.BrokerStandingRepository;

/**
 * Created on 10/01/2017.
 */

public class TimerServicesHelper {

    @Resource
    BrokerStandingRepository stdRepo;
//    @Autowired
//    private StandingDAO standingDao;

    private final static Logger log = LoggerFactory.getLogger(TimerServicesHelper.class);

    public TimerServicesHelper() {
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void invalidateYesterdayPositions(){

    	log.info("[TimerServicesHelper][invalidateYesterdayPositions]");
    	
        List<StandingEntity> inMarket = stdRepo.findByFkIdStandingStatus(StandingStatus.INMARKET);
        inMarket.addAll(stdRepo.findByFkIdStandingStatus(StandingStatus.QUEUED));
        for(StandingEntity std: inMarket){

        	log.info("[TimerServicesHelper][invalidateYesterdayPositions] update cancell");

            //standingDao.updateStanding(StandingStatus.CANCELLED, std.getIdStanding());
        	
            std.setFkIdStandingStatus(StandingStatus.CANCELLED);
        }
        stdRepo.save(inMarket);
    }
}
