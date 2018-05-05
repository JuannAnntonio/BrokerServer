package mx.sigmact.broker.pojo;

/**
 * Created on 02/01/2017.
 */
public interface Status {
    Integer getCode();

    void setCode(Integer code);

    String getStatus();

    void setStatus(String status);

    String getMessage();

    void setMessage(String message);
}


