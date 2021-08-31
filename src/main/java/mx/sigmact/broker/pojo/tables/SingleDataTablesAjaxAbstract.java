package mx.sigmact.broker.pojo.tables;

import java.util.List;

/**
 * Created on 10/11/2016.
 */
public abstract class SingleDataTablesAjaxAbstract {

    private Object data;


    public SingleDataTablesAjaxAbstract(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
