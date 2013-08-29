package fi.helsinki.cs.okkopa.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import fi.helsinki.cs.okkopa.shared.Settings;
import fi.helsinki.cs.okkopa.shared.database.model.BatchDbModel;
import fi.helsinki.cs.okkopa.shared.database.model.MissedExamDbModel;
import fi.helsinki.cs.okkopa.shared.database.model.QRCodeDbModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Controls OKKoPa's own database inputs and outputs with simple api.
 */
public class OkkopaDatabase {

    private static Dao<QRCodeDbModel, String> qrcodeDao;
    private static ConnectionSource connectionSource;
    private static boolean open = false;
    private static Dao<MissedExamDbModel, Void> missedExamDao;
    private static Dao<BatchDbModel, String> batchDbDao;

    /**
     * Checks if database-connection is opened.
     * @return true if open else false
     */
    public static boolean isOpen() {
        return open;
    }

    /**
     * Connects to OKKoPa's own database and creates it if no database found from given url.
     * @param settings
     * @throws SQLException
     */
    public OkkopaDatabase(Settings settings) throws SQLException {
        String databaseUrl = settings.getProperty("database.h2.url");
        String username = settings.getProperty("database.h2.user");
        String password = settings.getProperty("database.h2.password");

        // create a connection source to our database
        connectionSource = new JdbcConnectionSource(databaseUrl, username, password);

        // instantiate the dao
        qrcodeDao = DaoManager.createDao(connectionSource, QRCodeDbModel.class);
        TableUtils.createTableIfNotExists(connectionSource, QRCodeDbModel.class);
        
        missedExamDao = DaoManager.createDao(connectionSource, MissedExamDbModel.class);
        TableUtils.createTableIfNotExists(connectionSource, MissedExamDbModel.class);
        
        batchDbDao = DaoManager.createDao(connectionSource, BatchDbModel.class);
        TableUtils.createTableIfNotExists(connectionSource, BatchDbModel.class);
        open = true;
    }

    /**
     * Returns student username from given QR-code.
     * @param qrcodeString QR-code
     * @return student username, null if given QR-code is not found on database.
     * @throws SQLException
     */
    public static String getUserID(String qrcodeString) throws SQLException {
        QRCodeDbModel qrCode = qrcodeDao.queryForId(qrcodeString);
        if (qrCode == null) {
            return null;
        }
        return qrCode.getUserId();
    }

    /**
     * Adds generated QR-code to database to be later registered by user.
     * @param QRCode generated QR-code.
     * @return true if given QR-code does not already exists in database, else false
     * @throws SQLException
     */
    public static boolean addQRCode(String QRCode) throws SQLException {
        QRCodeDbModel qrCode = new QRCodeDbModel(QRCode, null);

        if (qrcodeDao.idExists(QRCode) == false) {
            qrcodeDao.createIfNotExists(qrCode);
            return true;
        }
        return false;
    }
    
    /**
     * Checks if given QR-code exists in database.
     * @param QRCode
     * @return true if exists else false
     * @throws SQLException
     */
    public static boolean QRCodeExists(String QRCode) throws SQLException {
        if(qrcodeDao.queryForId(QRCode) != null) {
            return true;
        }
        return false;
    }

    /**
     * Registers user to given QR-code.
     * @param QRCode
     * @param UserId student username to register given QR-code.
     * @return true if wasn't already registered to someone else, else false.
     * @throws SQLException
     */
    public static boolean addUSer(String QRCode, String UserId) throws SQLException {
        QRCodeDbModel qrCode = qrcodeDao.queryForId(QRCode);
        
        if (qrCode.getUserId() == null) {
            qrCode = new QRCodeDbModel(QRCode, UserId);
            qrcodeDao.update(qrCode);
            return true;
        }
        return false;
    }
    
    /**
     * Adds missed anonymous QR-code to database for to show info when it is registered too late.
     * @param anonymousCode
     * @throws SQLException
     */
    public static void addMissedExam(String anonymousCode) throws SQLException {
        QRCodeDbModel qrCode = new QRCodeDbModel(anonymousCode, null);
        MissedExamDbModel missedExam = new MissedExamDbModel(qrCode);
        missedExamDao.create(missedExam);
    }
    
    /**
     * Retursn a list of dates when given anonymous QR-code is scanned but not yet registered.
     * @param anonymousCode
     * @return
     * @throws SQLException
     */
    public static List<Date> getMissedExams(String anonymousCode) throws SQLException {      
        QueryBuilder<MissedExamDbModel, Void> queryBuilder = missedExamDao.queryBuilder();
        PreparedQuery prepQuery = queryBuilder.where().eq("qrCode", anonymousCode).prepare();
        
        List<MissedExamDbModel> missedExams = missedExamDao.query(prepQuery);
        List<Date> result = new ArrayList<Date>();
        
        for (MissedExamDbModel model : missedExams) {
            result.add(model.getDate());
        }
        return result;
    }
    
    /**
     * Adds info and assistants email from front-page generation form.
     * @param batchInfo Database model
     * @throws SQLException
     */
    public static void addBatchDetails(BatchDbModel batchInfo) throws SQLException {
        batchDbDao.createIfNotExists(batchInfo);
    }
    
    /**
     * Checks if info or email exists in database with given reference-code.
     * @param id reference-code from front-page.
     * @return true if exists, else false.
     * @throws SQLException
     */
    public static boolean batchDetailsExists(String id) throws SQLException {
        if(batchDbDao.queryForId(id) != null) {
            return true;
        }
        return false;
    }

    /**
     * Closes OKKoPa's own database. Can be done "too many times", because it is open automatically if not open.
     * @throws SQLException
     */
    public static void closeConnectionSource() throws SQLException {
        connectionSource.close();
    }
}