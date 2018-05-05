package mx.sigmact.broker.pojo.trader;

import mx.sigmact.broker.pojo.Status;

/**
 * Returns a cancellation status.
 * The code gives a description of the status:
 * 2xx means OK
 * 4xx position not found
 * 5xx means some kind of error, like the transaction was closed before you could cancel it.
 */
public class CancelledStatus extends Bidding implements Status{
    private Integer code;
    private String status;
    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return  message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
