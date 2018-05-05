package mx.sigmact.broker.pojo.admin;

/**
 * Class for the isntittution view
 * Created on 02/11/16.
 */
public class AdminInstitutions {
    private String institutionName;
    private String activeUsers;
    private String activeBid;
    private String tradedLastDays;
    private String isActive;
    private String viewUsers;
    private String viewMatrix;

    public AdminInstitutions(String institutionName,
                             String activeUsers,
                             String activeBid,
                             String tradedLastDays,
                             String isActive,
                             String viewUsers,
                             String viewMatrix) {
        this.institutionName = institutionName;
        this.activeUsers = activeUsers;
        this.activeBid = activeBid;
        this.tradedLastDays = tradedLastDays;
        this.isActive = isActive;
        this.viewUsers = viewUsers;
        this.viewMatrix = viewMatrix;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(String activeUsers) {
        this.activeUsers = activeUsers;
    }

    public String getActiveBid() {
        return activeBid;
    }

    public void setActiveBid(String activeBid) {
        this.activeBid = activeBid;
    }

    public String getTradedLastDays() {
        return tradedLastDays;
    }

    public void setTradedLastDays(String tradedLastDays) {
        this.tradedLastDays = tradedLastDays;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getViewUsers() {
        return viewUsers;
    }

    public void setViewUsers(String viewUsers) {
        this.viewUsers = viewUsers;
    }

    public String getViewMatrix() {
        return viewMatrix;
    }

    public void setViewMatrix(String viewMatrix) {
        this.viewMatrix = viewMatrix;
    }
}
