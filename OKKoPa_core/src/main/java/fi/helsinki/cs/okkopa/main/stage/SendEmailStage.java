package fi.helsinki.cs.okkopa.main.stage;

import fi.helsinki.cs.okkopa.database.FailedEmailDAO;
import fi.helsinki.cs.okkopa.mail.send.EmailSender;
import fi.helsinki.cs.okkopa.file.save.Saver;
import fi.helsinki.cs.okkopa.main.ExceptionLogger;
import fi.helsinki.cs.okkopa.shared.Settings;
import fi.helsinki.cs.okkopa.model.ExamPaper;
import fi.helsinki.cs.okkopa.model.FailedEmailDbModel;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.sql.SQLException;
import java.util.Date;
import javax.mail.MessagingException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Sends the evaluated exam to a student. If sending fails the exam paper is saved to be sent later.
 */

@Component
public class SendEmailStage extends Stage<ExamPaper, ExamPaper> {

    private static final Logger LOGGER = Logger.getLogger(SendEmailStage.class.getName());
    private ExceptionLogger exceptionLogger;
    private EmailSender emailSender;
    private String saveRetryFolder;
    private Saver fileSaver;
    private FailedEmailDAO failedEmailDatabase;

    /**
     *
     * @param emailSender
     * @param exceptionLogger
     * @param settings
     * @param fileSaver
     * @param failedEmailDatabase
     */
    @Autowired
    public SendEmailStage(EmailSender emailSender, ExceptionLogger exceptionLogger,
            Settings settings, Saver fileSaver, FailedEmailDAO failedEmailDatabase) {
        this.emailSender = emailSender;
        this.exceptionLogger = exceptionLogger;
        saveRetryFolder = settings.getProperty("mail.send.retrysavefolder");
        this.fileSaver = fileSaver;
        this.failedEmailDatabase = failedEmailDatabase;
        //        retryExpirationMinutes = Integer.parseInt(settings.getSettings().getProperty("mail.send.retryexpirationminutes"));
    }

    @Override
    public void process(ExamPaper examPaper) {
        sendEmail(examPaper);
        processNextStages(examPaper);
    }

    private void sendEmail(ExamPaper examPaper) {
        try {
            LOGGER.debug("Lähetetään sähköposti.");
            sendEmail2(examPaper);
            
        } catch (MessagingException ex) {
            LOGGER.debug("Sähköpostin lähetys epäonnistui.");
            
            LOGGER.debug("Tallennetaan PDF-liite levylle.");
            saveFailedEmail(examPaper);
            exceptionLogger.logException(ex);
        }
    }

    private void saveFailedEmail(ExamPaper examPaper) {
        try {
            String filename = saveFailedPdf(examPaper);
            
            addFailedEmailToDb(filename, examPaper);
            
        } catch (FileAlreadyExistsException | SQLException ex) {
            exceptionLogger.logException(ex);
            // TODO if one fails what then?
        }
    }

    private void sendEmail2(ExamPaper examPaper) throws MessagingException {
        InputStream is = new ByteArrayInputStream(examPaper.getPdf());
        emailSender.send(examPaper.getStudent().getEmail(), is);
        IOUtils.closeQuietly(is);
    }

    private String saveFailedPdf(ExamPaper examPaper) throws FileAlreadyExistsException {
        String filename = System.currentTimeMillis() + ".pdf";
        InputStream is = new ByteArrayInputStream(examPaper.getPdf());
        fileSaver.saveInputStream(is, saveRetryFolder, filename);
        IOUtils.closeQuietly(is);
        return filename;
    }

    private void addFailedEmailToDb(String filename, ExamPaper examPaper) throws SQLException {
        FailedEmailDbModel failedEmail = new FailedEmailDbModel();
        
        failedEmail.setFilename(filename);
        failedEmail.setReceiverEmail(examPaper.getStudent().getEmail());
        failedEmail.setFailTime(new Date());
        
        failedEmailDatabase.addFailedEmail(failedEmail);
    }
}