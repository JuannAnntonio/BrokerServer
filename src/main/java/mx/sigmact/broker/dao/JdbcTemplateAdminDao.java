package mx.sigmact.broker.dao;

import org.springframework.stereotype.Service;

/**
 * JDBC Template Admin DAO uses JDBC Template
 * Created on 09/11/16.
 */
@Deprecated
@Service
public class JdbcTemplateAdminDao //implements AdminDao
{


  /*  @Resource(name = "dataSource")
    DataSource dataSource;

    @Value("${query.admin.institution}")
    String sqlAdminInstitution;

    private static final String LINKPREFIX = "<a href=\"";
    private static final String LINKMIDDLE = "\">";
    private static final String LINKSUFFIX = "</a>";
    private static final String LINKSUFFIXICON = "<\"><i class=\"fa fa-bars fa-fw\"></i></a>";

    public JdbcTemplateAdminDao() {
        super();
    }

    @Override
    @Transactional
    public List<AdminInstitutions> findByDate(Calendar calendar) {
        try {
            String schema = dataSource.getConnection().getSchema();//TODO delete
        } catch (SQLException e) {
            e.printStackTrace();
        }
        CalendarUtil.zeroTimeCalendar(calendar);
        calendar.add(Calendar.DAY_OF_YEAR, -89);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sDate = sdf.format(calendar.getTime());
        List<AdminInstitutions> result = new ArrayList<>();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlAdminInstitution, sDate);
        jdbcTemplate = null;
        for(Map<String, Object> row: rows){
            String lInstitutionName = ((String) row.get("Institution"));
            Long lActiveUsersInt = ((Long) row.get("Active_Users"));
            Long lActiveBidsInt = ((Long) row.get("Active_Bids_Offers"));
            BigDecimal lAmountTraded = ((BigDecimal) row.get("Traded_Amount"));
            Boolean lIsActive = false;
            if(((Integer)row.get("Active"))!= 0){
                lIsActive = true;
            }
            if(lActiveUsersInt == null){
                lActiveUsersInt = 0L;
            }
            if(lActiveBidsInt == null){
                lActiveBidsInt = 0L;
            }
            if(lAmountTraded == null){
                lAmountTraded = new BigDecimal(0);
            }
            AdminInstitutions item = new AdminInstitutions(
                    buildLinkInstitution(lInstitutionName),
                    buildLinkActiveUsers(lActiveUsersInt.intValue(), lInstitutionName),
                    String.valueOf(lActiveBidsInt),
                    String.valueOf(lAmountTraded),
                    buildCheckButtonInactive(lIsActive),
                    buildLinkActiveUsersIcon(lInstitutionName),
                    buildLinkInstitutionMatrixIcon(lInstitutionName)
            );
            result.add(item);
        }
        return result;
    }

    @Override
    public AdminDashboard findDashBoardData() {
        return null;
    }

    private String buildLinkInstitutionMatrixIcon(String name) {
        StringBuilder result = new StringBuilder();
        result.append(LINKPREFIX).append("admin/institutionmatrix?institution=")
                .append(name)
                .append(LINKSUFFIXICON);
        return result.toString();
    }

    private String buildLinkActiveUsersIcon(String name) {
        StringBuilder result = new StringBuilder();
        result.append(LINKPREFIX).append("admin/users?institution=")
                .append(name)
                .append(LINKSUFFIXICON);
        return result.toString();
    }

    private String buildCheckButtonInactive(boolean lIsActive) {
        if(lIsActive){
            return "<input type=\"checkbox\" checked disabled>";
        }else{
            return "<input type=\"checkbox\" disabled>";
        }
    }

    private String buildLinkActiveUsers(int lActiveUsersInt, String name) {
        StringBuilder result = new StringBuilder();
        result.append(LINKPREFIX).append("admin/users?institution=")
                .append(name)
                .append(LINKMIDDLE)
                .append(lActiveUsersInt)
                .append(LINKSUFFIX);
        return result.toString();
    }

    private String buildLinkInstitution(String name) {
        StringBuilder result = new StringBuilder();
        result.append(LINKPREFIX).append("admin/institution?institution=")
                .append(name)
                .append(LINKMIDDLE)
                .append(name)
                .append(LINKSUFFIX);
        return result.toString();
    }*/
}
