package mx.sigmact.broker.services.timers;

import mx.sigmact.broker.model.StandingEntity;
import mx.sigmact.broker.pojo.StandingStatus;
import mx.sigmact.broker.repositories.BrokerStandingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created on 10/01/2017.
 */

public class TimerServicesHelper {

    @Resource
    BrokerStandingRepository stdRepo;

    private final static Logger log = LoggerFactory.getLogger(TimerServicesHelper.class);

    public TimerServicesHelper() {
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void invalidateYesterdayPositions(){
        List<StandingEntity> inMarket = stdRepo.findByFkIdStandingStatus(StandingStatus.INMARKET);
        inMarket.addAll(stdRepo.findByFkIdStandingStatus(StandingStatus.QUEUED));
        for(StandingEntity std: inMarket){
            std.setFkIdStandingStatus(StandingStatus.CANCELLED);
        }
        stdRepo.save(inMarket);
    }
}
