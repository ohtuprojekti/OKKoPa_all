package fi.helsinki.cs.okkopa.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import fi.helsinki.cs.okkopa.model.BatchDbModel;
import fi.helsinki.cs.okkopa.model.MissedExamDbModel;
import fi.helsinki.cs.okkopa.model.QRCodeDbModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OkkopaDatabase {

    private static Dao<QRCodeDbModel, String> qrcodeDao;
    private static ConnectionSource connectionSource;
    private static boolean open = false;
    private static Dao<MissedExamDbModel, Void> missedExamDao;
    private static Dao<BatchDbModel, String> batchDbDao;

    public static boolean isOpen() {
        return open;
    }

    public OkkopaDatabase() throws SQLException {
        String databaseUrl = Settings.instance.getProperty("database.h2.url");
        String username = Settings.instance.getProperty("database.h2.user");
        String password = Settings.instance.getProperty("database.h2.password");

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

    public static String getUserID(String qrcodeString) throws SQLException {
        QRCodeDbModel qrCode = qrcodeDao.queryForId(qrcodeString);
        if (qrCode == null) {
            return null;
        }
        return qrCode.getUserId();
    }

    public static boolean addQRCode(String QRCode) throws SQLException {
        QRCodeDbModel qrCode = new QRCodeDbModel(QRCode, null);

        if (qrcodeDao.idExists(QRCode) == false) {
            qrcodeDao.createIfNotExists(qrCode);
            return true;
        }
        return false;
    }
    
    public static boolean QRCodeExists(String QRCode) throws SQLException {
        if(qrcodeDao.queryForId(QRCode) != null) {
            return true;
        }
        return false;
    }

    public static boolean addUSer(String QRCode, String UserId) throws SQLException {
        QRCodeDbModel qrCode = qrcodeDao.queryForId(QRCode);
        
        if (qrCode.getUserId() == null) {
            qrCode = new QRCodeDbModel(QRCode, UserId);
            qrcodeDao.update(qrCode);
            return true;
        }
        return false;
    }
    
    public static void addMissedExam(String anonymousCode) throws SQLException {
        QRCodeDbModel qrCode = new QRCodeDbModel(anonymousCode, null);
        MissedExamDbModel missedExam = new MissedExamDbModel(qrCode);
        missedExamDao.create(missedExam);
    }
    
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
    
    public static void addBatchDetails(BatchDbModel batchInfo) throws SQLException {
        batchDbDao.createIfNotExists(batchInfo);
    }

    public static void closeConnectionSource() throws SQLException {
        connectionSource.close();
    }
}