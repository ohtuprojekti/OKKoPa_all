/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.helsinki.cs.okkopa.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author hannahir
 */
@DatabaseTable(tableName = "batchInfo")
public class BatchDbModel {

    @DatabaseField(id = true, columnName = "id")
    private String id;
    
    @DatabaseField(columnName = "emailContent")
    private String emailContent;
    
    @DatabaseField(columnName = "reportEmailAddress")
    private String reportEmailAddress;
    
    /**
     *
     */
    public BatchDbModel() {
        
    }
    
    /**
     *
     * @param id
     * @param emailContent
     * @param reportEmailAddress
     */
    public BatchDbModel(String id, String emailContent, String reportEmailAddress) {
        this.id = id;
        this.emailContent = emailContent;
        this.reportEmailAddress = reportEmailAddress;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getEmailContent() {
        return emailContent;
    }

    /**
     *
     * @param emailContent
     */
    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    /**
     *
     * @return
     */
    public String getReportEmailAddress() {
        return reportEmailAddress;
    }

    /**
     *
     * @param reportEmailAddress
     */
    public void setReportEmailAddress(String reportEmailAddress) {
        this.reportEmailAddress = reportEmailAddress;
    }
    
    
}
