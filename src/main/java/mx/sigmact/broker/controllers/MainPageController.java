package mx.sigmact.broker.controllers;

import mx.sigmact.broker.pojo.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

/**
 * Created on 02/11/16.
 */
@Controller
@RequestMapping("mainMenu")
public class MainPageController {
    Logger log = LoggerFactory.getLogger(MainPageController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView doGet() {
        ModelAndView mv = new ModelAndView();
        Collection<SimpleGrantedAuthority> authorities =
                (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        SimpleGrantedAuthority[] array = new SimpleGrantedAuthority[authorities.size()];
        authorities.toArray(array);
        SimpleGrantedAuthority simpleGrantedAuthority = array[0];
        String authority = simpleGrantedAuthority.getAuthority();
        log.info("AUTHORITY: " + authority);
        switch (authority) {
            case RoleType.SYSTEMADMIN:
                mv.setViewName("redirect:admin/dashboard");
                break;
            case RoleType.INSTITUTIONADMIN:
                mv.setViewName("redirect:instadmin/dashboard");
                break;
            case RoleType.BACKOFFICE:
                mv.setViewName("redirect:backoffice/dashboard");
                break;
            case RoleType.TRADE:
                mv.setViewName("redirect:trader/dashboard");
                break;
        }
        return mv;
    }


}
