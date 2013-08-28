package fi.helsinki.cs.okkopa.shared.database.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;


/**
 * OKKoPa's own database's model for miseed exam info for anonymous references processed before students registration.
 */
@DatabaseTable(tableName = "MissedExam")
public class MissedExamDbModel {
    @DatabaseField(columnName = "qrCode", foreign = true, canBeNull = false)
    private QRCodeDbModel qrCode;
    @DatabaseField(columnName = "date", canBeNull = false)
    private Date date;
    
    public MissedExamDbModel() {
        this.date = new Date();
    }
    
    
    /**
     * Creates new model with given qr-code datqabase model.
     * @param qRCode
     */
    public MissedExamDbModel(QRCodeDbModel qRCode) {
        this.qrCode = qRCode;
        this.date = new Date();
    }

    public QRCodeDbModel getQrCode() {
        return qrCode;
    }

    public void setQrCode(QRCodeDbModel qrCode) {
        this.qrCode = qrCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}