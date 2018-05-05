package mx.sigmact.broker.pojo;

/**
 * Created on 02/12/16.
 */
public class UserProfile {
    public static final String BACK_OFFICE = RoleType.BACKOFFICE;
    public static final String INSTITUTIONAL_ADMIN = RoleType.INSTITUTIONADMIN;
    public static final String SYSTEM_ADMIN = RoleType.SYSTEMADMIN;
    public static final String TRADER = RoleType.TRADE;

    public static String[] getProfiles(){
        return new String[]{BACK_OFFICE, INSTITUTIONAL_ADMIN, SYSTEM_ADMIN, TRADER};
    }
}
