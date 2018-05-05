package mx.sigmact.broker.services.rest;

import mx.sigmact.broker.dao.JdbcAdminDao;
import mx.sigmact.broker.model.*;
import mx.sigmact.broker.pojo.ExecutionStatus;
import mx.sigmact.broker.pojo.admin.AdminDashboard;
import mx.sigmact.broker.pojo.admin.AdminUsersView;
import mx.sigmact.broker.pojo.graphs.MorrisGraph;
import mx.sigmact.broker.pojo.tables.DTAAdminInstitution;
import mx.sigmact.broker.pojo.tables.DTGeneralTable;
import mx.sigmact.broker.repositories.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * Created on 02/11/16.
 */
@RestController
@RequestMapping("admin/rest/")
public class RESTAdmin {

    private static final int DAYSTRADED = 30;

    @Resource
    JdbcAdminDao dao;

    @Resource
    BrokerUserRepository userRepo;

    @Resource
    BrokerInstitutionRepository instRepo;

    @Resource
    BrokerRolesEntity roleRepo;

    @Resource
    BrokerUserxInstrumentRepository uxiRepo;

    @Resource
    BrokerInstitutionWorkbenchPriorityRepository iwpRepo;

    /**
     * Pages the Institution list starting with page 1.
     *
     * @param page
     * @param pageSize
     * @return
     */
    //TODO CREATE a CACHE to return the id form the catalogs *********
    @RequestMapping(value = "getInstitutionInfo", method = RequestMethod.GET, produces = "application/json")
    public DTAAdminInstitution doGet(@RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "page_size", required = false) Integer pageSize) {
        DTAAdminInstitution data = new DTAAdminInstitution(dao.findByDate(Calendar.getInstance()));
        return data;
    }

    @RequestMapping(value = "getDashboardInfo", method = RequestMethod.GET, produces = "application/json")
    public AdminDashboard doGet() {
        MorrisGraph graph = new MorrisGraph();

        return dao.findDashBoardData();
    }

    @RequestMapping(value = "getUsers", method = RequestMethod.GET, produces = "application/json")
    public DTGeneralTable<AdminUsersView> doGetUsers(@RequestParam(value = "page", required = false) Integer page,
                                                     @RequestParam(value = "page_size", required = false) Integer pageSize,
                                                     @RequestParam(value = "institution", required = false) String institution) {
        DTGeneralTable<AdminUsersView> result = null;
        if (institution == null) {
            result = new DTGeneralTable<>(dao.findUsersView());
        } else {
            result = new DTGeneralTable<>(dao.findUsersView(institution));
        }
        return result;
    }

    @RequestMapping(value = "saveUserInfo", method = RequestMethod.POST, produces = "application/json")
    public ExecutionStatus doPostSaveUserInfo(
            @RequestParam("previous_name") String previousName,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("phone_number") String phoneNumber,
            @RequestParam("institution") Integer institution,
            @RequestParam("profile") String profile,
            @RequestParam("active") Boolean active,
            @RequestParam(value = "instruments[]", required = false) Integer[] instruments,
            @RequestParam(value = "password", required = false) String password
    ) {
        ExecutionStatus status = new ExecutionStatus();
        UserEntity user;
        RolesEntity role;
        RolesEntity previousRole = null;
        UserEntity savedUser;
        if (!previousName.isEmpty()) {
            user = userRepo.findOneByUsername(previousName);
            role = roleRepo.findOneByFkIdUser(user.getIdUser());
            if (hasDuplicates(status, name, email, user)) {
                    return status;
            }
            previousRole = roleRepo.findOneByFkIdUser(user.getIdUser());
        } else {
            if (hasDuplicates(status, name, email)) {
                return status;
            }
            user = new UserEntity();
            role = new RolesEntity();
        }
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        user.setUsername(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setFkIdInstitution(institution);
        user.setEnabled(active ? new Integer(1).shortValue() : new Integer(0).shortValue());
        userRepo.save(user);
        if (previousName.isEmpty()) {
            savedUser = userRepo.findOneByUsername(user.getUsername());
            role.setFkIdUser(savedUser.getIdUser());
        }
        role.setRole(profile);
        if (previousRole != null) {
            roleRepo.delete(previousRole);
        }
        roleRepo.save(role);
        List<UserxinstrumentEntity> currentUserInstruments = uxiRepo.findByFkIdUser(user.getIdUser());
        for (UserxinstrumentEntity uxi : currentUserInstruments) {
            if (notFound(uxi, instruments)) {
                uxiRepo.delete(uxi);
            }
        }
        if (instruments != null) {
            int priority = 1;
            for (Integer idInstrument : instruments) {
                UserxinstrumentEntity val = getFromCurrentInstruments(idInstrument, currentUserInstruments);
                if (val != null) {
                    val.setPriority(priority);
                    uxiRepo.save(val);//TODO change to list save
                } else {
                    val = new UserxinstrumentEntity(); //TODO assign id
                    val.setFkIdInstrument(idInstrument);
                    val.setFkIdUser(user.getIdUser());
                    val.setPriority(priority);
                    uxiRepo.save(val);
                }
                priority++;
            }
        }
        status.setCode(200);
        status.setStatus("Success");
        status.setMessage("El usuario ha sido añadido con exito");
        return status;
    }

    private boolean hasDuplicates(ExecutionStatus status, String name, String email, UserEntity previousUser) {
        UserEntity oneByUsername = userRepo.findOneByUsername(name);
        if (oneByUsername != null && !name.equals(previousUser.getUsername())) {
            status.setCode(501);
            status.setStatus("Error");
            status.setMessage("El nombre de usuario ya se encuentra registrado");
            return true;
        }
        UserEntity oneByEmail = userRepo.findOneByEmail(email);
        if (oneByEmail != null && !email.equals(previousUser.getEmail())) {
            status.setCode(502);
            status.setStatus("Error");
            status.setMessage("El correo ya se encuentra registrado");
            return true;
        }
        return false;
    }

    private boolean hasDuplicates(ExecutionStatus status, String name, String email) {
        UserEntity oneByUsername = userRepo.findOneByUsername(name);
        if (oneByUsername != null) {
            status.setCode(501);
            status.setStatus("Error");
            status.setMessage("El nombre de usuario ya se encuentra registrado");
            return true;
        }
        UserEntity oneByEmail = userRepo.findOneByEmail(email);
        if (oneByEmail != null) {
            status.setCode(502);
            status.setStatus("Error");
            status.setMessage("El correo ya se encuentra registrado");
            return true;
        }
        return false;
    }

    @RequestMapping(value = "updatePassword", method = RequestMethod.POST, produces = "application/json")
    public ExecutionStatus doPostUpdatePassword(
            @RequestParam("username") String username,
            @RequestParam("old_password") String oldPassword,
            @RequestParam("new_password") String new_password
    ) {
        ExecutionStatus result = new ExecutionStatus();
        UserEntity user = userRepo.findOneByUsername(username);
        if (new_password.length() < 8 ||
                !(new_password.matches(".*[A-Z].*") && new_password.matches(".*[a-z].*"))) {
            result.setCode(502);
            result.setStatus("error");
            result.setMessage("The password should be at least 8 characters in length and contain upper and lower case letters");
        } else if (user == null) {
            result.setCode(501);
            result.setStatus("error");
            result.setMessage("user not found on the database");
        } else if (checkPassword(user.getPassword(), oldPassword)) {
            user.setPassword(new_password);
            userRepo.save(user);
            result.setCode(200);
            result.setStatus("success");
            result.setMessage("password changed");
        } else {
            result.setCode(500);
            result.setStatus("error");
            result.setMessage("the old password did not matched");
        }
        return result;
    }

    private Boolean checkPassword(String password, String oldPassword) {
        Boolean result = false;
        if (password.equals(oldPassword)) { //TODO Change to hash
            result = true;
        }
        return result;
    }


    @RequestMapping(value = "saveInstitutionInfo", method = RequestMethod.POST, produces = "application/json")
    public Boolean doPostSaveInstrumentInfo(
            @RequestParam("previous_name") String previousName,
            @RequestParam("name") String name,
            @RequestParam("phone_number") String phoneNumber,
            @RequestParam("key") String key,
            @RequestParam("system_commission") Double systemCommission,
            @RequestParam("active") Boolean active,
            @RequestParam("workbenches[]") Integer[] workbenches
    ) {
        Boolean isNewInstitution = false;
        InstitutionEntity institution = null;
        if (previousName != null && !previousName.isEmpty()) {
            institution = instRepo.findOneByName(previousName);
        } else {
            institution = new InstitutionEntity();
            isNewInstitution = true;
        }
        institution.setName(name);
        institution.setPhoneNumber(phoneNumber);
        institution.setKey(key);
        institution.setSystemCommission(systemCommission);
        institution.setEnabled(active ? new Integer(1).shortValue() : new Integer(0).shortValue());
        instRepo.save(institution);
        if (isNewInstitution) {
            institution = instRepo.findOneByName(institution.getName());
        }
        List<InstitutionWorkbenchPriorityEntity> currentWorkbenchs = iwpRepo.findByFkIdMainInstitution(institution.getIdInstitution());
        int priority = 1;
        for (InstitutionWorkbenchPriorityEntity iwp : currentWorkbenchs) {
            if (notFound(iwp, workbenches)) {
                iwpRepo.delete(iwp);
            }
        }

        for (Integer idWorkbench : workbenches) {
            InstitutionWorkbenchPriorityEntity val = getFromCurrentWorkbenches(idWorkbench, currentWorkbenchs);
            if (val != null) {
                val.setPriority(priority);
                iwpRepo.save(val);//TODO change to list save
            } else {
                val = new InstitutionWorkbenchPriorityEntity(); //TODO assign id
                val.setFkIdMainInstitution(institution.getIdInstitution());
                val.setFkIdWorkbenchInstitution(idWorkbench);
                val.setPriority(priority);
                iwpRepo.save(val);
            }
            priority++;
        }

        Boolean status = true;
        return status;
    }

    private InstitutionWorkbenchPriorityEntity getFromCurrentWorkbenches(Integer idWorkbench, List<InstitutionWorkbenchPriorityEntity> currentWorkbenchs) {
        for (InstitutionWorkbenchPriorityEntity iwp : currentWorkbenchs) {
            if (idWorkbench == iwp.getFkIdWorkbenchInstitution()) {
                return iwp;
            }
        }
        return null;
    }

    private UserxinstrumentEntity getFromCurrentInstruments(Integer idInstrument, List<UserxinstrumentEntity> list) {
        for (UserxinstrumentEntity uxi : list) {
            if (idInstrument == uxi.getFkIdInstrument()) {
                return uxi;
            }
        }
        return null;
    }

    private Boolean notFound(UserxinstrumentEntity uxi, Integer[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        for (Integer i : array) {
            if (uxi.getFkIdInstrument() == i) {
                return false;
            }
        }
        return true;
    }

    private Boolean notFound(InstitutionWorkbenchPriorityEntity iwp, Integer[] array) {
        for (Integer i : array) {
            if (iwp.getFkIdWorkbenchInstitution() == i) {
                return false;
            }
        }
        return true;
    }


}
