package fi.helsinki.cs.okkopa.main.stage;

import com.unboundid.ldap.sdk.LDAPException;
import fi.helsinki.cs.okkopa.database.OracleConnector;
import fi.helsinki.cs.okkopa.shared.exception.NotFoundException;
import fi.helsinki.cs.okkopa.ldap.LdapConnector;
import fi.helsinki.cs.okkopa.main.BatchDetails;
import fi.helsinki.cs.okkopa.main.ExceptionLogger;
import fi.helsinki.cs.okkopa.shared.Settings;
import fi.helsinki.cs.okkopa.model.CourseDbModel;
import fi.helsinki.cs.okkopa.model.ExamPaper;
import fi.helsinki.cs.okkopa.model.FeedbackDbModel;
import fi.helsinki.cs.okkopa.model.StudentDbModel;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveToTikliStage extends Stage<ExamPaper, ExamPaper> {

    private static final Logger LOGGER = Logger.getLogger(SaveToTikliStage.class.getName());
    private boolean tikliEnabled;
    private LdapConnector ldapConnector;
    private ExceptionLogger exceptionLogger;
    private OracleConnector oc;
    private Settings settings;
    private BatchDetails batch;

    @Autowired
    public SaveToTikliStage(LdapConnector ldapConnector, Settings settings,
            ExceptionLogger exceptionLogger, BatchDetails batch) {
        this.settings = settings;
        this.ldapConnector = ldapConnector;
        tikliEnabled = Boolean.parseBoolean(settings.getProperty("tikli.enable"));
        this.exceptionLogger = exceptionLogger;
        this.oc = new OracleConnector(settings);
        this.batch = batch;
    }

    @Override
    public void process(ExamPaper examPaper) {
        if (batch.getCourseCode() != null && tikliEnabled) {
            // Get student number from LDAP:
            try {
                ldapConnector.setStudentInfo(examPaper.getStudent());
                saveToTikli(examPaper);
                LOGGER.debug("Koepaperi tallennettu Tikliin.");
                batch.addSuccessfulTikliSave();
            } catch (NotFoundException | LDAPException | GeneralSecurityException ex) {
                exceptionLogger.logException(ex);
            }
        }
        processNextStages(examPaper);
    }

    private void saveToTikli(ExamPaper examPaper) {
        CourseDbModel course = new CourseDbModel(batch);
        FeedbackDbModel feedback = new FeedbackDbModel(settings, course, examPaper.getPdf(), examPaper.getStudent().getStudentNumber());
        StudentDbModel student = new StudentDbModel(examPaper.getStudent());
        try {
            oc.connect();
            if (oc.courseExists(course) && oc.studentExists(student)) {
                oc.insertFeedBackRow(feedback);
            }
            
        } catch (SQLException ex) {
            LOGGER.error(ex.toString());
            
        } finally {
            oc.disconnect();
        }
        LOGGER.debug("Tikliin tallennus päättyi!");
    }
}