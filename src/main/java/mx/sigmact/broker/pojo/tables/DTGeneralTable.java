package mx.sigmact.broker.pojo.tables;

import java.util.List;

/**
 * Created on 10/11/2016.
 */
public class DTGeneralTable<T> extends DataTablesAjaxAbstract<T> {

    public DTGeneralTable(List<T> data) {
        super(data);
    }
}
