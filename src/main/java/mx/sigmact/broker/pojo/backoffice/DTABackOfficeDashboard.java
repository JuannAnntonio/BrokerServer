package mx.sigmact.broker.pojo.backoffice;

import mx.sigmact.broker.pojo.tables.DataTablesAjaxAbstract;

import java.util.List;

/**
 * Created by norberto on 10/09/2017.
 */
public class DTABackOfficeDashboard extends DataTablesAjaxAbstract<BackOfficeMainView> {
    public DTABackOfficeDashboard(List<BackOfficeMainView> data) {
        super(data);
    }
}
