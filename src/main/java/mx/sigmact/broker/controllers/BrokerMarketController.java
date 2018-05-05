package mx.sigmact.broker.controllers;

import mx.sigmact.broker.pojo.trader.MarketPosition;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created on 18/12/16.
 */
@RestController
public class BrokerMarketController {
    @MessageMapping("/tradersMessages")
    @SendTo("/marketPositions")
    public MarketPosition sendMarketPositionToCLients(){

        return null;

    }

    @RequestMapping(value = "/tradersMessages/info", method = RequestMethod.GET)
    public String infoTradersMessages(){
        return "V0.1";
    }

}
