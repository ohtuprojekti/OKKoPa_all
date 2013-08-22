package fi.helsinki.cs.okkopa.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import fi.helsinki.cs.okkopa.model.CourseDbModel;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class OracleConnector {

    private ConnectionSource connectionSource;
    private String url;
    private String pwd;
    private String user;
    private String host;
    private String port;
    private String instance;
    private int yearOffset;
    Dao<CourseDbModel, Object> courseDbModel;

    @Autowired
    public OracleConnector(Settings settings) {
        this.pwd = settings.getProperty("database.oracle.password");
        this.user = settings.getProperty("database.oracle.user");
        this.host = settings.getProperty("database.oracle.host");
        this.port = settings.getProperty("database.oracle.port");
        this.instance = settings.getProperty("database.oracle.instance");
        this.yearOffset = Integer.parseInt(settings.getProperty("database.oracle.showcoursesforyears"));
        this.url = "jdbc:oracle:thin:@" + this.host + ":" + this.port + ":" + this.instance;
        System.out.println(url);
    }

    public void connect() throws SQLException {
        this.connectionSource = new JdbcConnectionSource(url, user, pwd);
    }

    public List<CourseDbModel> getCourseList() throws SQLException {
        this.courseDbModel = DaoManager.createDao(connectionSource, CourseDbModel.class);
        int startYear, endYear;
        endYear = Calendar.getInstance().get(Calendar.YEAR);
        startYear = endYear-this.yearOffset;
        QueryBuilder<CourseDbModel, Object> queryBuilder = this.courseDbModel.queryBuilder();
        PreparedQuery prepQuery = queryBuilder.where().between("LUKUVUOSI", (Integer) startYear, (Integer) endYear).prepare();
        return this.courseDbModel.query(prepQuery);
    }
    
    public boolean courseExists(CourseDbModel course) throws SQLException {
        try {
            this.connect();
            this.courseDbModel = DaoManager.createDao(connectionSource, CourseDbModel.class);
            List<CourseDbModel> result;
            result = this.courseDbModel.queryForMatching(course);
            if (result.size() == 1) {
                System.out.println(result.get(0).getName());
                this.connectionSource.close();
                return true;
            } else {
                this.connectionSource.close();
                return false;
            }

        } catch (SQLException ex) {
            this.connectionSource.close();
            throw ex;
        }
    }
}
