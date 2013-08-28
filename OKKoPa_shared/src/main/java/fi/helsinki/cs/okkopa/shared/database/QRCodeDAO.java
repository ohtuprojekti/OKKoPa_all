package fi.helsinki.cs.okkopa.shared.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import fi.helsinki.cs.okkopa.shared.database.model.QRCodeDbModel;
import fi.helsinki.cs.okkopa.shared.exception.NotFoundException;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provides database connection to store generated QR-code strings
 * (anonymous labels).
 */
@Component
public class QRCodeDAO {

    private Dao<QRCodeDbModel, String> qrCodeDao;

    /**
     *
     * @param settings
     * @throws SQLException
     */
    @Autowired
    public QRCodeDAO(OkkopaDatabaseConnectionSource connectionSource) throws SQLException {
        // instantiate the dao
        qrCodeDao = DaoManager.createDao(connectionSource, QRCodeDbModel.class);

        // if you need to create the 'accounts' table make this call
        TableUtils.createTableIfNotExists(connectionSource, QRCodeDbModel.class);
    }

    /**
     * Returns username of given anonymous reference.
     * @param qrcodeString anonymous label.
     * @return Student username.
     * @throws SQLException
     * @throws NotFoundException
     */
    public String getUserID(String qrcodeString) throws SQLException, NotFoundException {
        QRCodeDbModel qrCode = qrCodeDao.queryForId(qrcodeString);
        if (qrCode == null) {
            throw new NotFoundException("No matching userID found for anonymous qr-code: "+qrcodeString);
        }
        return qrCode.getUserId();
    }

    /**
     * Adds new anonymous reference for database so it can be registered for students later.
     * @param qrCodeString newly generated qr-code.
     * @return return true or false if given code does not already exists in database.
     * @throws SQLException
     */
    public boolean addQRCode(String qrCodeString) throws SQLException {
        QRCodeDbModel qrCode = new QRCodeDbModel(qrCodeString, null);

        if (!qrCodeDao.idExists(qrCodeString)) {
            qrCodeDao.create(qrCode);
            return true;
        }
        return false;
    }

    /**
     *
     * @param qrCodeString
     * @param UserId
     * @return
     * @throws SQLException
     */
    public boolean addUser(String qrCodeString, String UserId) throws SQLException {
        QRCodeDbModel qrCode = qrCodeDao.queryForId(qrCodeString);

        if (qrCode.getUserId() == null) {
            qrCode = new QRCodeDbModel(qrCodeString, UserId);
            qrCodeDao.update(qrCode);
            return true;
        }
        return false;
    }
}