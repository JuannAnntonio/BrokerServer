package mx.sigmact.broker.controllers;

import mx.sigmact.broker.model.InstitutionEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.repositories.BrokerInstitutionRepository;
import mx.sigmact.broker.repositories.BrokerUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * Created on 05/12/16.
 */
@Controller
@RequestMapping("/trader")
public class TraderController {
    @Resource
    BrokerUserRepository userRepo;

    @Resource
    BrokerInstitutionRepository instRepo;

    @RequestMapping(value = "dashboard")
    public ModelAndView doGetTraderDashboard(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findOneByUsername(username);
        InstitutionEntity institutionEntity = instRepo.findByIdInstitution(user.getFkIdInstitution());

        ModelAndView mv = new ModelAndView("trader/dashboard");
        mv.addObject("institution", institutionEntity.getName());
        return mv;
    }
}
