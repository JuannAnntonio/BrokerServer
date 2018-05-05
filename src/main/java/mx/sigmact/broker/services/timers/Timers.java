package mx.sigmact.broker.services.timers;

import mx.sigmact.broker.core.lib.UdiClient;
import mx.sigmact.broker.core.service.ValmerServiceReader;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.model.ValmerPriceVectorEntity;
import mx.sigmact.broker.repositories.BrokerUdiValueRepository;
import mx.sigmact.broker.repositories.BrokerValmerPriceVectorRepository;
import mx.sigmact.broker.services.ExternalServices;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created on 27/11/2016.
 */
@Service
public class Timers {

    @Resource
    ValmerServiceReader vsr;

    @Resource
    UdiClient udiClient;

    @Resource
    BrokerValmerPriceVectorRepository bvpvr;

    @Resource
    BrokerUdiValueRepository udiRepo;

    @Resource
    SimpleDateFormat formatDate;

    @Resource
    ExternalServices extSer;

    @Resource
    TimerServicesHelper helper;


    private static final Logger log = LoggerFactory.getLogger(Timers.class);

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public synchronized void loadValmerVector() {
        log.info("Loading valmer vector");
        LocalDate cal = LocalDate.now();
        try {
            Calendar lCal = Calendar.getInstance();
            CalendarUtil.zeroTimeCalendar(lCal);
            lCal.setTime(cal.toDate());
            lCal.add(Calendar.DAY_OF_YEAR, -1);
            String responseBody = vsr.getValmerPlain(lCal);
            List<ValmerPriceVectorEntity> topId = bvpvr.findTop1ByOrderByIdValmerPriceVectorDesc();
            int topIdInt = topId.get(0).getIdValmerPriceVector();
            List<ValmerPriceVectorEntity> valmerPriceVectorEntities =
                    vsr.readCSV(new ByteArrayInputStream(responseBody.getBytes(StandardCharsets.UTF_8)), topIdInt);
            vsr.setMarketToVector(valmerPriceVectorEntities, 1);
            bvpvr.save(valmerPriceVectorEntities);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(e.getLocalizedMessage());
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public synchronized void loadUdiToday() {
        extSer.loadUdi();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public synchronized void loadFunding() {
        extSer.loadAndSoreFunding();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized void invalidateYesterdayPositions(){
       helper.invalidateYesterdayPositions();
    }

}
