package mx.sigmact.broker.pojo.admin;

import mx.sigmact.broker.model.InstrumentTypeEntity;

import java.util.List;

/**
 * Created on 25/11/2016.
 */
public class UserInstruments {
    private List<InstrumentTypeEntity> activeInstruments;
    private List<InstrumentTypeEntity> inactiveInstruments;

    public UserInstruments(List<InstrumentTypeEntity> activeInstruments, List<InstrumentTypeEntity> inactiveInstruments) {
        this.activeInstruments = activeInstruments;
        this.inactiveInstruments = inactiveInstruments;
    }

    public UserInstruments() {
    }

    public List<InstrumentTypeEntity> getActiveInstruments() {
        return activeInstruments;
    }

    public void setActiveInstruments(List<InstrumentTypeEntity> activeInstruments) {
        this.activeInstruments = activeInstruments;
    }

    public List<InstrumentTypeEntity> getInactiveInstruments() {
        return inactiveInstruments;
    }

    public void setInactiveInstruments(List<InstrumentTypeEntity> inactiveInstruments) {
        this.inactiveInstruments = inactiveInstruments;
    }
}
