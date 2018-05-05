package mx.sigmact.broker.controllers;

import mx.sigmact.broker.dao.JdbcAdminDao;
import mx.sigmact.broker.repositories.BrokerInstitutionRepository;
import mx.sigmact.broker.repositories.BrokerInstrumentTypeRepository;
import mx.sigmact.broker.repositories.BrokerRolesEntity;
import mx.sigmact.broker.repositories.BrokerUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * This class maps all the request for the admin mvc
 * Created on 16/11/16.
 */
@Controller
@RequestMapping("/backoffice")
public class BackOfficeController {

    @Resource
    BrokerUserRepository repo;
    @Resource
    BrokerInstitutionRepository instRepo;
    @Resource
    BrokerRolesEntity rolesRepo;
    @Resource
    BrokerInstrumentTypeRepository instTypeRepo;
    @Resource
    JdbcAdminDao adminDao;


    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public ModelAndView adminDashBoardDoGet() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("backoffice/dashboard");
        return mv;
    }


}
