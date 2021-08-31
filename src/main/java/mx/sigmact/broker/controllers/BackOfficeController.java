package mx.sigmact.broker.controllers;

import mx.sigmact.broker.dao.JdbcAdminDao;
import mx.sigmact.broker.model.InstitutionEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.repositories.BrokerInstitutionRepository;
import mx.sigmact.broker.repositories.BrokerInstrumentTypeRepository;
import mx.sigmact.broker.repositories.BrokerRolesEntity;
import mx.sigmact.broker.repositories.BrokerUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;
import javax.annotation.Resource;

/**
 * This class maps all the request for the admin mvc
 * Created on 16/11/16.
 */
@Controller
@RequestMapping("/backoffice")
public class BackOfficeController {
    Logger log = Logger.getLogger(BackOfficeController.class);

    @Resource
    BrokerUserRepository userRepo;
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

    /*Reporte Especial*/
    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public ModelAndView adminDashBoardDoGet() {
    	
        log.info("[BackOfficeController][adminDashBoardDoGet]");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findOneByUsername(username);
        InstitutionEntity institutionEntity = instRepo.findByIdInstitution(user.getFkIdInstitution());
    	
        log.info("[BackOfficeController][adminDashBoardDoGet] institutionEntity.getIdInstitution(): " + institutionEntity.getIdInstitution());
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("backoffice/dashboard");
        mv.addObject("idInstitution", institutionEntity.getIdInstitution());
        
        return mv;
    }

    /*Reporte Carta Confirmacion*/
    
    @RequestMapping(value = "reporteCartaConfirmacion", method = RequestMethod.GET/*, produces = "application/json"*/)
    public ModelAndView reporteCartaConfirmacionDoGet() {
    	
        log.info("[BackOfficeController][reporteCartaConfirmacionDoGet]");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findOneByUsername(username);
        InstitutionEntity institutionEntity = instRepo.findByIdInstitution(user.getFkIdInstitution());
    	
        log.info("[BackOfficeController][adminDashBoardDoGet] institutionEntity.getIdInstitution(): " + institutionEntity.getIdInstitution());
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("backoffice/reporteCartaConfirmacion");
        mv.addObject("idInstitution", institutionEntity.getIdInstitution());
        return mv;
        
    }

    /*Reporte Calculadora de Precios*/
    
    @RequestMapping(value = "reporteCalculadoraPrecios", method = RequestMethod.GET/*, produces = "application/json"*/)
    public ModelAndView reporteCalculadoraPreciosDoGet() {
    	
        log.info("[BackOfficeController][reporteCalculadoraPreciosDoGet]");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findOneByUsername(username);
        InstitutionEntity institutionEntity = instRepo.findByIdInstitution(user.getFkIdInstitution());
    	
        log.info("[BackOfficeController][adminDashBoardDoGet] institutionEntity.getIdInstitution(): " + institutionEntity.getIdInstitution());
        
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("backoffice/reporteCalculadoraPrecios");
        mv.addObject("idInstitution", institutionEntity.getIdInstitution());
        return mv;
        
    }

    /*Reporte Calculadora de Precios Detalles*/
    
    @RequestMapping(value = "reporteCalculadoraPreciosDetalles", method = RequestMethod.GET/*, produces = "application/json"*/)
    public ModelAndView reporteCalculadoraPreciosDetallesDoGet() {
    	
        log.info("[BackOfficeController][reporteCalculadoraPreciosDetallesDoGet]");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findOneByUsername(username);
        InstitutionEntity institutionEntity = instRepo.findByIdInstitution(user.getFkIdInstitution());
    	
        log.info("[BackOfficeController][adminDashBoardDoGet] institutionEntity.getIdInstitution(): " + institutionEntity.getIdInstitution());
        
        
        ModelAndView mv = new ModelAndView();
        mv.setViewName("backoffice/reporteCalculadoraPreciosDetalles");
        mv.addObject("idInstitution", institutionEntity.getIdInstitution());
        return mv;
        
    }

}
