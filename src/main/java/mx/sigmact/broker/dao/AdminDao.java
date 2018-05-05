package mx.sigmact.broker.dao;

import mx.sigmact.broker.pojo.admin.*;

import java.util.Calendar;
import java.util.List;

/**
 * Interface to implement for all the view in the admin
 * Created on 09/11/16.
 */
public interface AdminDao {
    List<AdminInstitutions> findByDate(Calendar calendar);
    AdminDashboard findDashBoardData();
    List<AdminUsersView> findUsersView();
    List<AdminUsersView> findUsersView(String institution);
    UserInstruments findCurrentUserInstruments(Integer idUser);
    InstitutionWorkbenches findInstitutionWorkbenches(Integer idInstitution);
}
