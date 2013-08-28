package fi.helsinki.cs.okkopa.shared.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * OKKoPa's own database's model for created and registered anonymous QR-codes.
 * If userID (= student username) field is null, then its anonymous reference code is not registered.
 */
@DatabaseTable(tableName = "qrCodes")
public class QRCodeDbModel {

    @DatabaseField(id = true, columnName = "qrCode")
    private String qrCodeString;
    @DatabaseField(columnName = "userId")
    private String userId;

    public QRCodeDbModel() {
    }

    /**
     * Creates ne model based of given username and anonymous label.
     * @param qrCodeString anonymous reference code.
     * @param userId Student username.
     */
    public QRCodeDbModel(String qrCodeString, String userId) {
        this.qrCodeString = qrCodeString;
        this.userId = userId;
    }

    public String getQRCodeString() {
        return qrCodeString;
    }

    public void setQRCodeString(String qrCodeString) {
        this.qrCodeString = qrCodeString;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}