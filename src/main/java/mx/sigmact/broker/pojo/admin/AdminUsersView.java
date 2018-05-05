package mx.sigmact.broker.pojo.admin;

/**
 * Class for the users view
 * Created on 21/11/2016.
 */
public class AdminUsersView {
    private String name;
    private String institution;
    private String profile;
    private String availableInstruments;
    private String active;
    private String viewDetails;

    private static final String ANCHOR ="<a href='";
    private static final String ENDANCHOROPENING="'>";
    private static final String CLOSEANCHOR="</a>";
    private static final String CLOSEANCHORWITHBARICON = "'><i class='fa fa-bars fa-fw'></i></a>";
    public AdminUsersView() {
        super();
    }

    public AdminUsersView(String name, String institution, String profile, String availableInstruments, String active, String viewDetails) {
        this.setName(name);
        this.setInstitution(institution);
        this.setProfile(profile);
        this.setAvailableInstruments(availableInstruments);
        this.setActive(active);
        this.setViewDetails(viewDetails);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(ANCHOR).append("user?username=").append(name)
                .append(ENDANCHOROPENING).append(name).append(CLOSEANCHOR);
        this.name = sb.toString();
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAvailableInstruments() {
        return availableInstruments;
    }

    public void setAvailableInstruments(String availableInstruments) {
        this.availableInstruments = availableInstruments;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getViewDetails() {
        return viewDetails;
    }

    public void setViewDetails(String viewDetails) {
        StringBuilder sb = new StringBuilder();
        sb.append(ANCHOR).append("user?username=").append(viewDetails)
                .append(CLOSEANCHORWITHBARICON);
        this.viewDetails = sb.toString();
    }


}
