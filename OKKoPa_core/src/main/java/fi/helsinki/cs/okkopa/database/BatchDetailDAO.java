package fi.helsinki.cs.okkopa.database;

import fi.helsinki.cs.okkopa.shared.database.OkkopaDatabaseConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import fi.helsinki.cs.okkopa.shared.exception.NotFoundException;
import fi.helsinki.cs.okkopa.model.BatchDbModel;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Provides database connection to store Batch Details about an attachment.
 * Such as Report email and e-mail template.
 */
@Component
public class BatchDetailDAO {

    private Dao<BatchDbModel, String> batchDbDao;

    @Autowired
    public BatchDetailDAO(OkkopaDatabaseConnectionSource connectionSource) throws SQLException {
        batchDbDao = DaoManager.createDao(connectionSource, BatchDbModel.class);
        TableUtils.createTableIfNotExists(connectionSource, BatchDbModel.class);

    }

    
    /**
     * Adds a batch detail to the database.
     * @param batchInfo BatchInfo to be added.
     * @throws SQLException If database error occours
     */
    public void addBatchDetails(BatchDbModel batchInfo) throws SQLException {
        batchDbDao.createIfNotExists(batchInfo);
    }
    
    
    /**
     * Finds a batch detail referring to a given ID.
     * @param id ID of the batch detail. This is in the font page.
     * @return Found BatchDetail
     * @throws SQLException if database error occours
     * @throws NotFoundException if no batch detail matches the given ID.
     */
    public BatchDbModel getBatchDetails(String id) throws SQLException, NotFoundException {
        BatchDbModel batchDb = batchDbDao.queryForId(id);
        if (batchDb == null) {
            throw new NotFoundException("No matching report e-mail address or e-mail content found for id: "+id);
        }
        return batchDb;
    }
}
