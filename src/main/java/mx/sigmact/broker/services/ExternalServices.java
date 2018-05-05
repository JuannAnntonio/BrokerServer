package mx.sigmact.broker.services;

import mx.sigmact.broker.core.lib.FundingRateClient;
import mx.sigmact.broker.core.lib.UdiClient;
import mx.sigmact.broker.model.BankFundingEntity;
import mx.sigmact.broker.model.UdiValueEntity;
import mx.sigmact.broker.pojo.UdisBanxicoInfo;
import mx.sigmact.broker.repositories.BrokerBankFundingRepository;
import mx.sigmact.broker.repositories.BrokerUdiValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created on 10/01/2017.
 */
public class ExternalServices {

    @Resource
    FundingRateClient fundingRateClient;

    @Resource
    BrokerBankFundingRepository fundRepo;

    @Resource
    SimpleDateFormat formatDate;

    @Resource
    UdiClient udiClient;

    @Resource
    BrokerUdiValueRepository udiRepo;

    private final static Logger log = LoggerFactory.getLogger(ExternalServices.class);


    public ExternalServices() {
    }

    public UdiValueEntity loadUdi() {
        UdisBanxicoInfo udiTimeAndObsevedValue = udiClient.getUdiTimeAndObsevedValue();
        UdiValueEntity udi = new UdiValueEntity();
        loadAndStoreUdi(udiTimeAndObsevedValue, udi);
        return udi;
    }

    @Transactional
    public UdiValueEntity loadAndStoreUdi(UdisBanxicoInfo udiTimeAndObsevedValue, UdiValueEntity udi){
        try {
            Date date = new Date(formatDate.parse(udiTimeAndObsevedValue.getDate()).getTime());
            date.setTime(formatDate.parse(udiTimeAndObsevedValue.getDate()).getTime());
            udi.setUdiDate(date);
            udi.setUdiValue(Double.parseDouble(udiTimeAndObsevedValue.getUdiValue()));
        } catch (ParseException e) {
            log.error("Bad udi data format");
        }
        udiRepo.save(udi);
        return udi;
    }

    public BankFundingEntity loadAndSoreFunding(){
        BankFundingEntity fundingTimeAndObsevedValue = fundingRateClient.getFundingTimeAndObsevedValue();
        fundRepo.save(fundingTimeAndObsevedValue);
        return fundingTimeAndObsevedValue;
    }
}
