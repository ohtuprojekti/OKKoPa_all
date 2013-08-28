package fi.helsinki.cs.okkopa.shared.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import fi.helsinki.cs.okkopa.shared.Settings;
import fi.helsinki.cs.okkopa.shared.database.model.CourseDbModel;
import fi.helsinki.cs.okkopa.shared.database.model.FeedbackDbModel;
import fi.helsinki.cs.okkopa.shared.database.model.StudentDbModel;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provides a connection to Kurki Oracle database.
 */
@Component
public class OracleConnector {

    private static final Logger LOGGER = Logger.getLogger(OracleConnector.class.getName());
    private ConnectionSource connectionSource;
    private String url;
    private String pwd;
    private String user;
    private String host;
    private String port;
    private String instance;
    private int yearOffset;
    private Dao<CourseDbModel, Object> courseDbModel;
    private Dao<FeedbackDbModel, String> feedbackDbModel;
    private Dao<StudentDbModel, Object> studentDbModel;

    /**
     *
     * @param settings
     */
    @Autowired
    public OracleConnector(Settings settings) {
        this.pwd = settings.getProperty("database.oracle.password");
        this.user = settings.getProperty("database.oracle.user");
        this.host = settings.getProperty("database.oracle.host");
        this.port = settings.getProperty("database.oracle.port");
        this.instance = settings.getProperty("database.oracle.instance");
        this.yearOffset = Integer.parseInt(settings.getProperty("database.oracle.showcoursesforyears"));
        this.url = "jdbc:oracle:thin:@" + this.host + ":" + this.port + ":" + this.instance;
    }

    /**
     * Connects to Oracle database.
     * @throws SQLException
     */
    public void connect() throws SQLException {
        this.connectionSource = new JdbcConnectionSource(url, user, pwd);
    }

    /**
     * Disconnects from the Oracle database.
     */
    public void disconnect() {
        try {
            this.connectionSource.close();
        } catch (SQLException ex) {
            LOGGER.error("Could not disconnect from Oracle DB (Kurki).");
        }
    }
    
    /**
     * Returs list of database models (=rows) where one model-object has info of one line in database. In this instance it has info of different cources.
     * Limited by "how many years old cources" in settings.xml.
     * @return List of datadase models of the cources.
     * @throws SQLException
     */
    public List<CourseDbModel> getCourseList() throws SQLException {
        this.courseDbModel = DaoManager.createDao(connectionSource, CourseDbModel.class);
        int startYear, endYear;
        endYear = Calendar.getInstance().get(Calendar.YEAR);
        startYear = endYear - this.yearOffset;
        QueryBuilder<CourseDbModel, Object> queryBuilder = this.courseDbModel.queryBuilder();
        PreparedQuery prepQuery = queryBuilder.where().between("LUKUVUOSI", (Integer) startYear, (Integer) endYear).prepare();
        return this.courseDbModel.query(prepQuery);
    }

    /**
     * Checks if given cource exists in Kurki databse.
     * @param course Database model which is to be checked.
     * @return true or false if exists.
     * @throws SQLException
     */
    public boolean courseExists(CourseDbModel course) throws SQLException {
        this.courseDbModel = DaoManager.createDao(connectionSource, CourseDbModel.class);
        List<CourseDbModel> result;
        result = this.courseDbModel.queryForMatching(course);
        if (result.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if student exists in Kurki database.
     * @param student database model of wanted student.
     * @return true or false if exists.
     * @throws SQLException
     */
    public boolean studentExists(StudentDbModel student) throws SQLException {
        this.studentDbModel = DaoManager.createDao(connectionSource, StudentDbModel.class);
        List<StudentDbModel> result;
        result = this.studentDbModel.queryForMatching(student);
        if (result.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Iserts new Feedback for some cource with given pdf-attachment.
     * @param newRow database model of new feedback.
     * @throws SQLException
     */
    public void insertFeedBackRow(FeedbackDbModel newRow) throws SQLException {
        this.feedbackDbModel = DaoManager.createDao(connectionSource, FeedbackDbModel.class);
        if (this.feedbackDbModel.create(newRow) != 1) {
            throw new SQLException("Rows inserted <> 1");
        } LOGGER.debug("Palaute rivi lisätty tikliin");
    }
}