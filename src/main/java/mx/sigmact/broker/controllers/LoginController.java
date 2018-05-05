package mx.sigmact.broker.controllers;

import mx.sigmact.broker.pojo.RoleType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

/**
 * Created on 14/10/16.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView doGet() {
        return sendToInitialPage();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView doGetLogin() {
        return sendToInitialPage();
    }

    private ModelAndView sendToInitialPage() {
        Collection<SimpleGrantedAuthority> authorities =
                (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        SimpleGrantedAuthority[] array = new SimpleGrantedAuthority[authorities.size()];
        authorities.toArray(array);
        SimpleGrantedAuthority simpleGrantedAuthority = array[0];
        String authority = simpleGrantedAuthority.getAuthority();
        ModelAndView mv = new ModelAndView();
        Boolean isPageSet = false;
        switch (authority) {
            case RoleType.SYSTEMADMIN:
                mv.setViewName("redirect:admin/dashboard");
                isPageSet = true;
                break;
            case RoleType.INSTITUTIONADMIN:
                mv.setViewName("redirect:instadmin/dashboard");
                isPageSet = true;
                break;
            case RoleType.BACKOFFICE:
                mv.setViewName("redirect:backoffice/dashboard");
                isPageSet = true;
                break;
            case RoleType.TRADE:
                mv.setViewName("redirect:trader/dashboard");
                isPageSet = true;
                break;
        }
        if (!isPageSet) {
            mv.setViewName("login");
        }
        return mv;
    }

}
