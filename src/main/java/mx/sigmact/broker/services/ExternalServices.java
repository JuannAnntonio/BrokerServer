package mx.sigmact.broker.services;

import mx.sigmact.broker.core.lib.FundingRateClient;
import mx.sigmact.broker.core.lib.UdiClient;
import mx.sigmact.broker.model.BankFundingEntity;
import mx.sigmact.broker.model.UdiValueEntity;
import mx.sigmact.broker.pojo.UdisBanxicoInfo;
import mx.sigmact.broker.repositories.BrokerBankFundingRepository;
import mx.sigmact.broker.repositories.BrokerUdiValueRepository;
import mx.sigmact.broker.services.restPojo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

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
    @Value("${token_bmx}")
    String tokenBmx ;
    @Value("${banco_de_mexico_rest}")
    String bancoDeMexicoRest;
    private final static Logger log = LoggerFactory.getLogger(ExternalServices.class);


    public ExternalServices() {
    }

    public UdiValueEntity loadUdiRest(java.util.Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String uri = bancoDeMexicoRest + "/"+sdf.format(date)+"/"+sdf.format(date);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Bmx-Token",tokenBmx);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        ResponseEntity<Response> result = new ResponseEntity<Response>(HttpStatus.OK);
        try {
            result = restTemplate.exchange(uri, HttpMethod.GET, entity, Response.class);
        }catch(RestClientException ex){
            log.error(uri);
            ex.printStackTrace();
        }
        UdiValueEntity udi = new UdiValueEntity();
        loadAndStoreUdi(result.getBody(),udi);
        return udi;
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
    @Transactional
    public UdiValueEntity loadAndStoreUdi(Response response, UdiValueEntity udi){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date(sdf.parse(response.getBmx().getSeries().get(0).getDatos().get(0).getFecha()).getTime());
            udi.setUdiDate(date);
            udi.setUdiValue(Double.parseDouble(response.getBmx().getSeries().get(0).getDatos().get(0).getDato()));
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
