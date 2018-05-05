package mx.sigmact.broker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created on 02/12/16.
 */
@Controller
public class ExceptionHandle {

    public ModelAndView handleError404(HttpServletRequest request, Exception e){
        ModelAndView mv = new ModelAndView();
        mv.addObject("exception",e);
        mv.setViewName("error");
        return mv;
    }
}
