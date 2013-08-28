package fi.helsinki.cs.okkopa.shared.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.TableUtils;
import fi.helsinki.cs.okkopa.shared.database.model.MissedExamDbModel;
import fi.helsinki.cs.okkopa.shared.database.model.QRCodeDbModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Works as interface for gettings things out of database and setting new rows for it.
 */
@Component
public class MissedExamDao {
    
    private Dao<MissedExamDbModel, Void> missedExamDao;
    
    /**
     *
     * @param connectionSource settings and properties for database connection.
     * @throws SQLException
     */
    @Autowired
    public MissedExamDao(OkkopaDatabaseConnectionSource connectionSource) throws SQLException {
        missedExamDao = DaoManager.createDao(connectionSource, MissedExamDbModel.class);
        TableUtils.createTableIfNotExists(connectionSource, MissedExamDbModel.class);
    }
    
    
    /**
     * Adds missed exam for given code, so that when it will be registered user knows that he/she has missed one-to-many examPapers.
     * @param anonymousCode Anonymous QR-code given in examPaper.
     * @throws SQLException
     */
    public void addMissedExam(String anonymousCode) throws SQLException {
        QRCodeDbModel qrCode = new QRCodeDbModel(anonymousCode, null);
        MissedExamDbModel missedExam = new MissedExamDbModel(qrCode);
        missedExamDao.create(missedExam);
    }
    
    /**
     * Return a list of dates when GQ-code was read but not registered yet for any username.
     * @param anonymousCode Anonymous QR-code given in examPaper.
     * @return A list of Dates.
     * @throws SQLException
     */
    public List<Date> getMissedExams(String anonymousCode) throws SQLException {
        QueryBuilder<MissedExamDbModel, Void> queryBuilder = missedExamDao.queryBuilder();
        PreparedQuery prepQuery = queryBuilder.where().eq("qrCode", anonymousCode).prepare();
        
        List<MissedExamDbModel> missedExams = missedExamDao.query(prepQuery);
        List<Date> result = new ArrayList<Date>();
        
        for (MissedExamDbModel model : missedExams) {
            result.add(model.getDate());
        }
        return result;
    }
}