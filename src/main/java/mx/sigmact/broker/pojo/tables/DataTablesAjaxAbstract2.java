package mx.sigmact.broker.pojo.tables;

import java.util.List;

/**
 * Created on 10/11/2016.
 */
public abstract class DataTablesAjaxAbstract2 {

    private int data;


    public DataTablesAjaxAbstract2(int data) {
        this.data = data;
    }
    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
