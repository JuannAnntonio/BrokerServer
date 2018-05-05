package mx.sigmact.broker.pojo.tables;

import mx.sigmact.broker.pojo.admin.AdminInstitutions;

import java.util.List;

/**
 * Created on 10/11/2016.
 */
public class DTAAdminInstitution extends DataTablesAjaxAbstract<AdminInstitutions> {

    public DTAAdminInstitution(List<AdminInstitutions> data) {
        super(data);
    }
}
