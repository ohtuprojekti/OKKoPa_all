package fi.helsinki.cs.okkopa.main.stage;

import fi.helsinki.cs.okkopa.shared.database.MissedExamDao;
import fi.helsinki.cs.okkopa.shared.database.QRCodeDAO;
import fi.helsinki.cs.okkopa.shared.exception.NotFoundException;
import fi.helsinki.cs.okkopa.main.ExceptionLogger;
import fi.helsinki.cs.okkopa.shared.Settings;
import fi.helsinki.cs.okkopa.model.ExamPaper;
import fi.helsinki.cs.okkopa.model.Student;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetStudentInfoStage extends Stage<ExamPaper, ExamPaper> {

    private static final Logger LOGGER = Logger.getLogger(SetStudentInfoStage.class.getName());
    private ExceptionLogger exceptionLogger;
    private QRCodeDAO qRCodeDatabase;
    private MissedExamDao missedExamDAO;
    private String atDomain;

    /**
     *
     * @param qRCodeDatabase
     * @param missedExamDAO
     * @param exceptionLogger
     * @param settings
     */
    @Autowired
    public SetStudentInfoStage(QRCodeDAO qRCodeDatabase, MissedExamDao missedExamDAO, ExceptionLogger exceptionLogger, Settings settings) {
        this.qRCodeDatabase = qRCodeDatabase;
        this.exceptionLogger = exceptionLogger;
        this.missedExamDAO = missedExamDAO;
        this.atDomain = settings.getProperty("mail.receiver.atdomain");
    }

    @Override
    public void process(ExamPaper examPaper) {
        try {
            LOGGER.debug("QR code: " + examPaper.getQRCodeString());
            String userId = fetchUserId(examPaper.getQRCodeString());
            
            if (userId == null) {
                //Rekisteröimätön anonyymikoodi.
                missedExamDAO.addMissedExam(examPaper.getQRCodeString());
                return;
            }
            Student student = getNewStudent(examPaper, userId);
            LOGGER.debug("Sähköpostiosoite: " + student.getEmail());
        
        } catch (NotFoundException | SQLException ex) {
            exceptionLogger.logException(ex);
            LOGGER.debug("Luettu QR-koodi ei ollut käyttäjätunnus eikä generoitu koodi.");
            // QR code isn't an user id and doesn't match any database entries.
            return;
        }
        processNextStages(examPaper);
    }
    
    

    private String fetchUserId(String qrcode) throws SQLException, NotFoundException {
        filterTooShorts(qrcode);
        
        // Check database
        if (Character.isDigit(qrcode.charAt(0))) {
            return qRCodeDatabase.getUserID(qrcode);
        }
        
        filterIfUsernameHasDigits(qrcode);
        
        return qrcode;
    }

    private Student getNewStudent(ExamPaper examPaper, String userId) {
        Student student = new Student();
        
        examPaper.setStudent(student);
        student.setUsername(userId);
        student.setEmail(userId + atDomain);
        
        return student;
    }

    private void filterTooShorts(String qrcode) throws NotFoundException {
        if (qrcode.length() == 0) {
            throw new NotFoundException("fetchUserId received empty string.");
        }
    }

    private void filterIfUsernameHasDigits(String qrcode) throws NotFoundException {
        for (char c : qrcode.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new NotFoundException("Found a number in user name. QR-code: "+qrcode);
            }
        }
    }
}