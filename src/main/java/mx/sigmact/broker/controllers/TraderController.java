package mx.sigmact.broker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created on 05/12/16.
 */
@Controller
@RequestMapping("/trader")
public class TraderController {
    @RequestMapping(value = "dashboard")
    public ModelAndView doGetTraderDashboard(){
        ModelAndView mv = new ModelAndView("trader/dashboard");
        return mv;
    }
}
