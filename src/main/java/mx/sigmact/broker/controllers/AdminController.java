package mx.sigmact.broker.controllers;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import mx.sigmact.broker.dao.JdbcAdminDao;
import mx.sigmact.broker.model.InstitutionEntity;
import mx.sigmact.broker.model.InstrumentTypeEntity;
import mx.sigmact.broker.model.RolesEntity;
import mx.sigmact.broker.model.UserEntity;
import mx.sigmact.broker.pojo.UserProfile;
import mx.sigmact.broker.pojo.admin.InstitutionWorkbenches;
import mx.sigmact.broker.pojo.admin.UserInstruments;
import mx.sigmact.broker.repositories.BrokerInstitutionRepository;
import mx.sigmact.broker.repositories.BrokerInstrumentTypeRepository;
import mx.sigmact.broker.repositories.BrokerRolesEntity;
import mx.sigmact.broker.repositories.BrokerUserRepository;

/**
 * This class maps all the request for the admin mvc
 * Created on 16/11/16.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	Logger log = Logger.getLogger(AdminController.class);

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
    @Resource
    BrokerUserRepository userRepo;

    @RequestMapping(value = "dashboard", method = RequestMethod.GET)
    public ModelAndView adminDashBoardDoGet() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepo.findOneByUsername(username);
        InstitutionEntity institutionEntity = instRepo.findByIdInstitution(user.getFkIdInstitution());
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/dashboard");
        mv.addObject("institution", institutionEntity.getName());
        return mv;
    }

    @RequestMapping(value = "institutions", method = RequestMethod.GET)
    public ModelAndView adminInstitutionsDoGet() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/institutions");
        return mv;
    }

    @RequestMapping(value = "priceprovider", method = RequestMethod.GET)
    public ModelAndView adminPriceproviderDoGet() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/priceprovider");
        return mv;
    }

   @RequestMapping(value = "institutionmatrix", method = RequestMethod.GET)
    public ModelAndView adminMatrixDoGet(@RequestParam("institution") String institutionName) {
    	
    	log.info("[AdminController][institutionmatrix]");
    	
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/institutionmatrix");
        
        return mv;
    }
   
   @RequestMapping(value = "matrix_detail", method = RequestMethod.GET)
   public ModelAndView adminMatrix2DoGet(@RequestParam("id") String id) {
   	
   	log.info("[AdminController][matrix_details]");
   	
       ModelAndView mv = new ModelAndView();
       mv.setViewName("admin/matrix_details");
       
       return mv;
   }
   
    @RequestMapping(value = "users", method = RequestMethod.GET)
    public ModelAndView adminUsersDoGet() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/users");
        return mv;
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public ModelAndView adminUserDoGet(@RequestParam("username") String username) {
        ModelAndView mv = new ModelAndView();
        UserEntity user = repo.findOneByUsername(username);
        Iterable<InstitutionEntity> all = instRepo.findAll();
        Iterator iter = all.iterator();
        while (iter.hasNext()) {
            InstitutionEntity o = (InstitutionEntity) iter.next();
            if (o.getName().equals("DEFAULT")) {
                iter.remove();
            }
        }
        UserInstruments instruments = adminDao.findCurrentUserInstruments(user.getIdUser());
        InstitutionEntity institutionEntity = instRepo.findOne(user.getFkIdInstitution());
        RolesEntity role = rolesRepo.findOneByFkIdUser(user.getIdUser());
        String [] roles = UserProfile.getProfiles();
        mv.setViewName("admin/user_details");
        mv.addObject("institutions", all);
        mv.addObject("current_institution", institutionEntity.getIdInstitution());
        mv.addObject("role", role.getRole());
        mv.addObject("roles", roles);
        mv.addObject("user", user);
        mv.addObject("instruments", instruments);
        return mv;
    }

    @RequestMapping(value = "addUser", method = RequestMethod.GET)
    public ModelAndView adminAddUserDoGet() {
        ModelAndView mv = new ModelAndView();
        Iterable<InstitutionEntity> all = instRepo.findAll();
        Iterator iter = all.iterator();
        while (iter.hasNext()) {
            InstitutionEntity o = (InstitutionEntity) iter.next();
            if (o.getName().equals("DEFAULT")) {
                iter.remove();
            }
        }
        final String [] roles = UserProfile.getProfiles();
        Iterable<InstrumentTypeEntity> instruments = instTypeRepo.findAll();
        mv.setViewName("admin/user_add");
        mv.addObject("institutions", all);
        mv.addObject("roles", roles);
        mv.addObject("instruments", instruments);
        return mv;
    }

    @RequestMapping(value = "institution", method = RequestMethod.GET)
    public ModelAndView adminInstitutionDoGet(@RequestParam("institution") String institutionName) {
        ModelAndView mv = new ModelAndView();
        InstitutionEntity institutionEntity = instRepo.findOneByName(institutionName);
        InstitutionWorkbenches workbenches = adminDao.findInstitutionWorkbenches(institutionEntity.getIdInstitution());

        mv.setViewName("admin/institution_details");
        mv.addObject("institution", institutionEntity);
        mv.addObject("workbenches", workbenches);
        return mv;
    }

    @RequestMapping(value = "addInstitution", method = RequestMethod.GET)
    public ModelAndView adminInstitutionAddDoGet() {
        ModelAndView mv = new ModelAndView();
        List<InstitutionEntity> all = instRepo.findAll();
        Iterator iter = all.iterator();
        while (iter.hasNext()) {
            InstitutionEntity o = (InstitutionEntity) iter.next();
            if (o.getName().equals("DEFAULT")) {
                iter.remove();
            }
        }
        InstitutionWorkbenches workbenches = new InstitutionWorkbenches(null,all);
        mv.setViewName("admin/institution_add");
        mv.addObject("workbenches", workbenches);
        return mv;
    }
}
