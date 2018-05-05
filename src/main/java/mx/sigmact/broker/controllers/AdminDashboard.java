package mx.sigmact.broker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created on 28/10/16.
 */
@RequestMapping("/admin")
@Controller
public class AdminDashboard {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView doGet() {
    /*    ModelAndView mv = new ModelAndView();
        mv.setViewName("admin_dashboard");

        double agressions[] = new double[100];
        double standings[] = new double[100];
        for (int i = 0; i < 100; i++) {
            agressions[99-i] = i;
            standings[i] = i;
        }
        mv.addObject("agressions", agressions);
        mv.addObject("standings", standings);

        return mv;*/
        return null;
    }
}
