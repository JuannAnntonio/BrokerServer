package mx.sigmact.broker.services.rest;

import mx.sigmact.broker.core.lib.UdiClient;
import mx.sigmact.broker.pojo.UdisBanxicoInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created on 16/11/16.
 */

@RestController
public class RESTGetUdi {

    @Resource
    UdiClient udiClient;

    @RequestMapping(value = "services/getbanxicoudi", produces = "application/json")
    public UdisBanxicoInfo getBanxicoUdis(){
        return udiClient.getUdiTimeAndObsevedValue();

    }
}
