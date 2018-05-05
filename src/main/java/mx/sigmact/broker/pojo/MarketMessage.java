package mx.sigmact.broker.pojo;

/**
 * Created by norberto on 30/01/17.
 */
public class MarketMessage extends ExecutionStatus {

    private Object data;

    public MarketMessage() {
    }

    public MarketMessage(Integer code, String status, String message, Object data) {
        super(code, status, message);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
