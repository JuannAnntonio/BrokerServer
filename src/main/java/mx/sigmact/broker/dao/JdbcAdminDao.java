package mx.sigmact.broker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import mx.sigmact.broker.core.lib.DaoHelper;
import mx.sigmact.broker.core.util.CalendarUtil;
import mx.sigmact.broker.model.InstitutionEntity;
import mx.sigmact.broker.model.InstrumentTypeEntity;
import mx.sigmact.broker.pojo.admin.AdminDashboard;
import mx.sigmact.broker.pojo.admin.AdminInstitutions;
import mx.sigmact.broker.pojo.admin.AdminMatrix;
import mx.sigmact.broker.pojo.admin.AdminMatrix2;
import mx.sigmact.broker.pojo.admin.AdminUsersView;
import mx.sigmact.broker.pojo.admin.InstitutionWorkbenches;
import mx.sigmact.broker.pojo.admin.UserInstruments;
import mx.sigmact.broker.pojo.graphs.LineGraphTwoLineElements;

/**
 * Created on 14/11/2016.
 */
@Service
public class JdbcAdminDao implements AdminDao {

    @Resource
    DataSource dataSource;
    
    @Resource
	DaoHelper daoHelper;

    @Value("${query.madrix.edit}")
    String sqlMatrixEdit;
    
    @Value("${query.admin.matrix}")
    String sqlMatrix;
    
    @Value("${query.admin.matrix2}")
    String sqlMatrix2;
    
    @Value("${query.admin.institution}")
    String sqlStatement;

    @Value("${query.admin.dashboard}")
    String sqlAdminDashboard;

    @Value("${query.admin.users}")
    private String sqlAdminUsers;

    @Value("${query.admin.instruments.existing.users}")
    private String sqlAdminGetUserInstruments;

    @Value("${query.admin.institution.workbenches}")
    private String sqlAdminGetInstitutionWorkbenches;

    @Value("${query.admin.users.view.by.institution}")
    private String sqlAdminUsersByInstitution;

    private static final Logger log = LoggerFactory.getLogger(JdbcAdminDao.class);

    private static final String LINKPREFIX = "<a href=\"";
    private static final String LINKMIDDLE = "\">";
    private static final String LINKSUFFIX = "</a>";
    private static final String LINKSUFFIXICON = "\"><i class=\"fa fa-bars fa-fw\"></i></a>";

    @Override
    public List<AdminInstitutions> findByDate(Calendar calendar) {
        List<AdminInstitutions> result = null;
        CalendarUtil.zeroTimeCalendar(calendar);
        calendar.add(Calendar.DAY_OF_YEAR, -89);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sDate = sdf.format(calendar.getTime());
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = createPreparedStatement(con, sDate);
             ResultSet resultSet = ps.executeQuery()) {
            if (resultSet != null) {
                result = new ArrayList<>();
                while (resultSet.next()) {
                    String institution = resultSet.getString(1);
                    String activeUsers = resultSet.getString(2);
                    String activeBids = resultSet.getString(3);
                    String tradedAmout = resultSet.getString(4);
                    String isActive = resultSet.getString(5);
                    Boolean bIsActive = true;
                    if (institution.equals("DEFAULT")) {
                        continue;
                    }
                    if (isActive.equals("0")) {
                        bIsActive = false;
                    }
                    if (activeUsers == null) {
                        activeUsers = "0";
                    }
                    if (activeBids == null) {
                        activeBids = "0";
                    }
                    if (tradedAmout == null) {
                        tradedAmout = "0";

                    }

                    AdminInstitutions item = new AdminInstitutions(
                            buildLinkInstitution(institution),
                            buildLinkActiveUsers(activeUsers, institution),
                            activeBids,
                            tradedAmout,
                            buildCheckButtonInactive(bIsActive),
                            buildLinkActiveUsersIcon(institution),
                            buildLinkInstitutionMatrixIcon(institution));
                    result.add(item);
                }
            }

			con.close();
			
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
        }

        return result;
    }
    
    @Override
    public List<AdminMatrix> findAdminMatrix(String institucion) {
    	
    	List<AdminMatrix> mi = new ArrayList<>();
    	
    	log.info("[JdbcAdminDao][findAdminMatrix]");
    	log.info("[JdbcAdminDao][findAdminMatrix] institucion: " + institucion);
    	
    	try {
			try(Connection con = dataSource.getConnection();
					PreparedStatement ps = daoHelper.createPreparedStatementVar(con, sqlMatrix,   			
							institucion);
		             ResultSet rs = ps.executeQuery();){
				log.info("[JdbcAdminDao][findAdminMatrix][Try]");
				
				int i = 0;
				while (rs.next()) {
					
					String id= rs.getString(1);
					log.info("[JdbcAdminDao][findAdminMatrix] exec query Res: " + i + " idComision: " + id);
					log.info("[JdbcAdminDao][findAdminMatrix] exec query Res: " + i + " idInstitucion1: " + rs.getString(2));
					log.info("[JdbcAdminDao][findAdminMatrix] exec query Res: " + i + " Institucion1: " + rs.getString(3));
					log.info("[JdbcAdminDao][findAdminMatrix] exec query Res: " + i + " Instrumento: " + rs.getString(4));
					log.info("[JdbcAdminDao][findAdminMatrix] exec query Res: " + i + " Comision: " + rs.getString(5));
					log.info("[JdbcAdminDao][findAdminMatrix] exec query Res: " + i + " idInstitucion2: " + rs.getString(6));
					log.info("[JdbcAdminDao][findAdminMatrix] exec query Res: " + i + " Institucion2: " + rs.getString(7));
					
					
					AdminMatrix row = new AdminMatrix(rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6),
							rs.getString(7),
							editMatrix(id));
							
					mi.add(row);
					
					i++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				log.info("[JdbcAdminDao][findAdminMatrix] ERROR: " + e.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			log.info("[JdbcAdminDao][findAdminMatrix] EROR: " + e.toString());
		}
    	
    	return mi;
    }
    
    @Override
    public List<AdminMatrix2> findAdminMatrix2(String id) {
    	
    	List<AdminMatrix2> mi = new ArrayList<>();
    	
    	log.info("[JdbcAdminDao][findAdminMatrix2]");
    	log.info("[JdbcAdminDao][findAdminMatrix2] id_comision: " + id);
    	
    	try {
			try(Connection con = dataSource.getConnection();
					PreparedStatement ps = daoHelper.createPreparedStatementVar(con, sqlMatrix2,   			
							id);
		             ResultSet rs = ps.executeQuery();){
				log.info("[JdbcAdminDao][findAdminMatrix2][Try]");
				
				int i = 0;
				while (rs.next()) {
					
					log.info("[JdbcAdminDao][findAdminMatrix2] exec query Res: " + i + " idComision: " + rs.getString(1));
					log.info("[JdbcAdminDao][findAdminMatrix2] exec query Res: " + i + " Institucion1: " + rs.getString(2));
					log.info("[JdbcAdminDao][findAdminMatrix2] exec query Res: " + i + " Instrumento: " + rs.getString(3));
					log.info("[JdbcAdminDao][findAdminMatrix2] exec query Res: " + i + " Comision: " + rs.getString(4));
					log.info("[JdbcAdminDao][findAdminMatrix2] exec query Res: " + i + " Institucion2: " + rs.getString(5));
					
					
					AdminMatrix2 row = new AdminMatrix2(rs.getString(1),rs.getString(2),rs.getString(3),
							rs.getString(4),rs.getString(5));
					
					mi.add(row);
					
					i++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				log.info("[JdbcAdminDao][findAdminMatrix2] ERROR: " + e.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			log.info("[JdbcAdminDao][findAdminMatrix2] ERROR: " + e.toString());
		}
    	
    	return mi;
    }

    public int findEditMatrix (String id, String comision) {
    	
    	
    	log.info("[JdbcAdminDao][findEditMatrix]");
    	log.info("[JdbcAdminDao][findEditMatrix] id_comision: " + id);
    	log.info("[JdbcAdminDao][findEditMatrix] comision: " + comision);
    	
    	try(Connection con = dataSource.getConnection();
				PreparedStatement ps = daoHelper.createPreparedStatementVar(con, sqlMatrixEdit,	
						comision, 
						id);){
			log.info("[JdbcAdminDao][findEditMatrix][Try]");
			log.info("Query: " + ps);
			int cont= ps.executeUpdate();
			if(cont>0) {
				//Insertar alertas que si se hizo el cambio 
				return 1;
			}else{
				//Insertar alertas que no se hizo el cambio
				return 0;
			}
    	}catch (SQLException e) {
    		log.info("[JdbcAdminDao][findEditMatrix] Error: "+ e);
    		return 0;
    	}  	
    }
    
    @Override
    public AdminDashboard findDashBoardData() {
        AdminDashboard ad = new AdminDashboard();
        List<LineGraphTwoLineElements> g1 = new ArrayList<>();
        List<LineGraphTwoLineElements> g2 = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = createPreparedStatementVar(con, sqlAdminDashboard);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String y = rs.getString(1);
                String a1 = rs.getString(2);
                String a2 = rs.getString(3);
                String b1 = rs.getString(4);
                String b2 = rs.getString(5);
                LineGraphTwoLineElements a = new LineGraphTwoLineElements(y, a1, a2);
                LineGraphTwoLineElements b = new LineGraphTwoLineElements(y, b1, b2);
                g1.add(a);
                g2.add(b);
            }
            ad.setLastQuarterActivityStandings(g1);
            ad.setLastQuarterTradedAmount(g2);

			con.close();
			
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

        }

        return ad;
    }

    @Override
    public List<AdminUsersView> findUsersView() {
        List<AdminUsersView> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = createPreparedStatementVar(con, sqlAdminUsers);
             ResultSet rs = ps.executeQuery()) {
           handleUserView(rs,list);
			con.close();
			
        } catch (SQLException e) {
            log.error("Genral error: " + e.getMessage());
            log.error("Localized error: " + e.getLocalizedMessage());
        }
        return list;
    }

    @Override
    public List<AdminUsersView> findUsersView(String institution) {
        List<AdminUsersView> list = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = createPreparedStatementVar(con, sqlAdminUsersByInstitution, institution);
             ResultSet rs = ps.executeQuery()) {
            handleUserView(rs, list);
			con.close();
        } catch (SQLException e) {
            log.error("Genral error: " + e.getMessage());
            log.error("Localized error: " + e.getLocalizedMessage());
        }
        return list;
    }

    private void handleUserView(ResultSet rs, List<AdminUsersView> list) throws SQLException {
        while (rs.next()) {
            String lName = rs.getString(1);
            String lInstitution = rs.getString(2);
            String lProfile = rs.getString(3);
            String lInstruments = rs.getString(4);
            String lEnabled = rs.getString(5);
            AdminUsersView item = new AdminUsersView(lName, lInstitution, lProfile, lInstruments, lEnabled, lName);
            list.add(item);
        }
    }

    @Override
    public UserInstruments findCurrentUserInstruments(Integer idUser) {
        UserInstruments userInstruments = new UserInstruments();
        List<InstrumentTypeEntity> active = new ArrayList<>();
        List<InstrumentTypeEntity> inactive = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = createPreparedStatementVarInt(con, sqlAdminGetUserInstruments, idUser);
             ResultSet rs = ps.executeQuery()) {
            Integer priority;
            while (rs.next()) {
                InstrumentTypeEntity instrumentTypeEntity = new InstrumentTypeEntity();
                instrumentTypeEntity.setIdInstrument(rs.getInt(1));
                instrumentTypeEntity.setTv(rs.getString(2));
                priority = rs.getInt(3);
                if (priority >= 0) {
                    active.add(instrumentTypeEntity);
                } else {
                    inactive.add(instrumentTypeEntity);
                }
            }
			con.close();
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
            log.error("Localized error: " + e.getLocalizedMessage());
        }
        userInstruments.setActiveInstruments(active);
        userInstruments.setInactiveInstruments(inactive);
        return userInstruments;
    }

    @Override
    public InstitutionWorkbenches findInstitutionWorkbenches(Integer idInstitution) {
        InstitutionWorkbenches result = null;
        List<InstitutionEntity> active = new ArrayList<>();
        List<InstitutionEntity> inactive = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = createPreparedStatementVarInt(con, sqlAdminGetInstitutionWorkbenches, idInstitution);
             ResultSet rs = ps.executeQuery()) {
            Integer priority;
            while (rs.next()) {
                InstitutionEntity institution = new InstitutionEntity();
                institution.setIdInstitution(rs.getInt(1));
                institution.setName(rs.getString(2));
                if (institution.getName().equals("DEFAULT")) {
                    continue;
                }
                priority = rs.getInt(3);
                if (priority >= 0) {
                    active.add(institution);
                } else {
                    inactive.add(institution);
                }
            }
			con.close();
        } catch (SQLException e) {
            log.error("General error: " + e.getMessage());
            log.error("Localized error: " + e.getLocalizedMessage());
        }
        result = new InstitutionWorkbenches(active, inactive);
        return result;
    }


    private String buildLinkInstitutionMatrixIcon(String name) {
        StringBuilder result = new StringBuilder();
        result.append(LINKPREFIX).append("institutionmatrix?institution=")
                .append(name)
                .append(LINKSUFFIXICON);
        return result.toString();
    }

    private String buildLinkActiveUsersIcon(String name) {
        StringBuilder result = new StringBuilder();
        result.append(LINKPREFIX).append("users?institution=")
                .append(name)
                .append(LINKSUFFIXICON);
        return result.toString();
    }

    private String buildCheckButtonInactive(boolean lIsActive) {
        if (lIsActive) {
            return "<input type=\"checkbox\" checked disabled>";
        } else {
            return "<input type=\"checkbox\" disabled>";
        }
    }

    private String buildLinkActiveUsers(String lActiveUsersInt, String name) {
        StringBuilder result = new StringBuilder();
        result.append(LINKPREFIX).append("users?institution=")
                .append(name)
                .append(LINKMIDDLE)
                .append(lActiveUsersInt)
                .append(LINKSUFFIX);
        return result.toString();
    }

    private String buildLinkInstitution(String name) {
        StringBuilder result = new StringBuilder();
        result.append(LINKPREFIX).append("institution?institution=")
                .append(name)
                .append(LINKMIDDLE)
                .append(name)
                .append(LINKSUFFIX);
        return result.toString();
    }
    
    private String editMatrix(String id) {
        StringBuilder result = new StringBuilder();
        result.append(LINKPREFIX).append("matrix_detail?id=")
		        .append(id)
		        .append(LINKSUFFIXICON);
		return result.toString();
	}

    private PreparedStatement createPreparedStatementVar(Connection con, String sql, String... vars) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        if (vars != null) {
            for (int i = 0; i < vars.length; i++) {
                ps.setString(i + 1, vars[i]);
            }
        }
        return ps;
    }

    private PreparedStatement createPreparedStatementVarInt(Connection con, String sql, Integer... vars) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        if (vars != null) {
            for (int i = 0; i < vars.length; i++) {
                ps.setInt(i + 1, vars[i]);
            }
        }
        return ps;
    }

    private PreparedStatement createPreparedStatement(Connection con, String date) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sqlStatement);
        ps.setString(1, date);
        return ps;
    }

}
