package fi.helsinki.cs.okkopa.shared.database.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import fi.helsinki.cs.okkopa.shared.Settings;
import java.util.Calendar;
import java.util.Date;

/**
 * Kurki database model for feedback info for student in given cource.
 */
@DatabaseTable(tableName = "SUORITUSPALAUTE")
public class FeedbackDbModel {

    @DatabaseField(columnName = "SUORITUSPALAUTE_ID", generatedIdSequence = "SUORITUSPALAUTE_SEQUENCE")
    private int id;
    @DatabaseField(columnName = "OPISKELIJANUMERO")
    private String studentNumber;
    @DatabaseField(columnName = "KURSSIKOODI")
    private String courseCode;
    @DatabaseField(columnName = "LUKUKAUSI")
    private String period;
    @DatabaseField(columnName = "LUKUVUOSI")
    private int year;
    @DatabaseField(columnName = "TYYPPI")
    private String courseType;
    @DatabaseField(columnName = "KURSSI_NRO")
    private int courseNumber;
    @DatabaseField(columnName = "SUORITUSPALAUTE_KOHDE")
    private String feedbackType;
    @DatabaseField(columnName = "SUORITUSPALAUTE_PVM")
    private Date date;
    @DatabaseField(columnName = "SUORITUSPALAUTE_ANTAJA")
    private String feedbackAuthor;
    @DatabaseField(columnName = "SUORITUSPALAUTE_TEKSTI")
    private String feedbackText;
    @DatabaseField(columnName = "SUORITUSPALAUTE_URL")
    private String feedbackUrl;
    @DatabaseField(columnName = "SUORITUSPALAUTE_DATA", dataType = DataType.BYTE_ARRAY)
    private byte[] data;
    @DatabaseField(columnName = "SUORITUSPALAUTE_DATA_MIME")
    private String mimeType;
    @DatabaseField(columnName = "SUORITUSPALAUTE_DATA_NIMI")
    private String fileName;

    public FeedbackDbModel() {
    }

    /**
     * Initializes settings, cource database model, file-blob and student number. 
     * @param settings
     * @param course
     * @param blob
     * @param studentNumber
     */
    public FeedbackDbModel(Settings settings, CourseDbModel course, byte[] blob, String studentNumber) {
        this.studentNumber = studentNumber;
        this.date = Calendar.getInstance().getTime();
       
        setCourceData(course);
        
        setBlob(blob, settings);
        
        //Check these before release:
        setTikliSettings(settings);
    }
    
    public String getStudentNumber() {
        return studentNumber;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getPeriod() {
        return period;
    }

    public int getYear() {
        return year;
    }

    public String getCourseType() {
        return courseType;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public Date getDate() {
        return date;
    }

    public String getFeedbackAuthor() {
        return feedbackAuthor;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public byte[] getData() {
        return data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFeedbackAuthor(String feedbackAuthor) {
        this.feedbackAuthor = feedbackAuthor;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public void setFeedbackUrl(String feedbackUrl) {
        this.feedbackUrl = feedbackUrl;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private void setCourceData(CourseDbModel course) {
        this.courseCode = course.getCourseCode();
        this.courseNumber = course.getCourseNumber();
        this.courseType = course.getType();
        this.period = course.getPeriod();
        this.year = course.getYear();
    }

    private void setBlob(byte[] blob, Settings settings) {
        this.data = blob;
        this.mimeType = "application/pdf";
        this.fileName = settings.getProperty("exampaper.attachmentname");
    }

    private void setTikliSettings(Settings settings) {
        this.feedbackAuthor = settings.getProperty("tikli.authorid");
        this.feedbackText = settings.getProperty("tikli.description");
        this.feedbackType = "Koe";
        this.feedbackUrl = null;
    }
}
