package mx.sigmact.broker.dao;

import mx.sigmact.broker.pojo.backoffice.BackOfficeMainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by norberto on 09/09/2017.
 */
public class JdbcBackOfficeDao implements BackOfficeDao {

    private static Logger log = LoggerFactory.getLogger(JdbcBackOfficeDao.class);

    @Resource(name = "formatDate")
    private SimpleDateFormat sdf;

    @Resource
    private DataSource dataSource;

    @Value("${query.backoffice.main}")
    String backOfficeMain;

    @Override
    public List<BackOfficeMainView> getBackOfficeMainView(Integer workbench, String date) {
        List<BackOfficeMainView> retVal = new ArrayList<>();
        try (Connection con = dataSource.getConnection();
             //PreparedStatement ps = createPreparedStatementVar(con, backOfficeMain, sdf.format(date.getTime()), workbench);
             PreparedStatement ps = createPreparedStatementVar(con, backOfficeMain, date, workbench);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BackOfficeMainView row = new BackOfficeMainView(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10),
                        rs.getString(11),
                        rs.getString(12),
                        rs.getString(13),
                        rs.getString(14)
                );
                retVal.add(row);
            }
        } catch (SQLException e) {
            log.error("Error al cargar la tabla principal el backoffice:\n" + e.getMessage());
        }
        return retVal;
    }

    private PreparedStatement createPreparedStatementVar(Connection con, String sql,
                                                         String date, int workbench) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, date);
        ps.setInt(2, workbench);
        return ps;
    }

}
