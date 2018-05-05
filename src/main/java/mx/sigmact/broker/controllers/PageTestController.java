package mx.sigmact.broker.controllers;

import mx.sigmact.broker.core.lib.DirtyPriceCalculator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created on 28/10/16.
 */
@Controller
public class PageTestController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ModelAndView doGet(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/dashboard");

        double agressions[] = new double[100];
        double standings[] = new double[100];
        for (int i = 0; i < 100; i++) {
            agressions[99-i] = i;
            standings[i] = i;
        }
        mv.addObject("agressions", agressions);
        mv.addObject("standings", standings);

        return mv;
    }

    @Resource
    DirtyPriceCalculator dpc;


    @RequestMapping(value = "/test/dirtyPrice", method = RequestMethod.GET )
    public String doGetDirtyPrice(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = null;
        try {
            parse = sdf.parse("2016-10-03 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(parse);
        dpc.calcDirtyPrice(7,calendario );
        return ("login");

    }

}
