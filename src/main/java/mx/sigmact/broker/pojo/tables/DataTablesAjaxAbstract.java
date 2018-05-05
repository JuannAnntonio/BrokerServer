package mx.sigmact.broker.pojo.tables;

import java.util.List;

/**
 * Created on 10/11/2016.
 */
public abstract class DataTablesAjaxAbstract<T> {

    private List<T> data;


    public DataTablesAjaxAbstract(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
