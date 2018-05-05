package mx.sigmact.broker.pojo.admin;

import mx.sigmact.broker.model.InstitutionEntity;

import java.util.List;

/**
 * Created on 29/11/2016.
 */
public class InstitutionWorkbenches {
    private List<InstitutionEntity> activeWorkbenchs;
    private List<InstitutionEntity> inactiveWorkbenchs;

    public InstitutionWorkbenches(List<InstitutionEntity> activeWorkbenchs, List<InstitutionEntity> inactiveWorkbenchs) {
        this.activeWorkbenchs = activeWorkbenchs;
        this.inactiveWorkbenchs = inactiveWorkbenchs;
    }

    public List<InstitutionEntity> getActiveWorkbenchs() {
        return activeWorkbenchs;
    }

    public void setActiveWorkbenchs(List<InstitutionEntity> activeWorkbenchs) {
        this.activeWorkbenchs = activeWorkbenchs;
    }

    public List<InstitutionEntity> getInactiveWorkbenchs() {
        return inactiveWorkbenchs;
    }

    public void setInactiveWorkbenchs(List<InstitutionEntity> inactiveWorkbenchs) {
        this.inactiveWorkbenchs = inactiveWorkbenchs;
    }
}
