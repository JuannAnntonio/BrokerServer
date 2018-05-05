package mx.sigmact.broker.dao;

import mx.sigmact.broker.pojo.backoffice.BackOfficeMainView;

import java.util.Date;
import java.util.List;
/**
 * Dao for accessing information about the BackOffice
 * Created by norberto on 09/09/2017.
 */
public interface BackOfficeDao {
    List<BackOfficeMainView> getBackOfficeMainView(Integer workbench, String date);
}
