package fi.helsinki.cs.okkopa.main.stage;

import fi.helsinki.cs.okkopa.mail.read.EmailRead;
import fi.helsinki.cs.okkopa.main.ExceptionLogger;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.mail.Message;
import javax.mail.MessagingException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Retrieves new emails and processes the attachments one by one by calling the next stage repeatedly.
 */

@Component
public class GetEmailStage extends Stage<Object, InputStream> {

    private static final Logger LOGGER = Logger.getLogger(GetEmailStage.class.getName());
    private EmailRead emailReader;
    private ExceptionLogger exceptionLogger;

    /**
     *
     * @param server
     * @param exceptionLogger
     */
    @Autowired
    public GetEmailStage(EmailRead server, ExceptionLogger exceptionLogger) {
        this.exceptionLogger = exceptionLogger;
        this.emailReader = server;
    }

    @Override
    public void process(Object in) {
        try {
            emailReader.connect();
            LOGGER.debug("Kirjauduttu sisään.");
            
            loopMessagesAsLongAsThereAreNew();
            
            LOGGER.debug("Ei lisää viestejä.");
            
        } catch (MessagingException | IOException ex) {
            exceptionLogger.logException(ex);
        } finally {
            emailReader.closeQuietly();
        }
    }

    private void processAttachments(Message nextMessage) throws MessagingException, IOException {
        List<InputStream> messagesAttachments = emailReader.getMessagesAttachments(nextMessage);
        for (InputStream attachment : messagesAttachments) {
            LOGGER.debug("Käsitellään liitettä.");
            processNextStages(attachment);
            IOUtils.closeQuietly(attachment);
        }
    }

    private void loopMessagesAsLongAsThereAreNew() throws MessagingException, IOException {
        Message nextMessage = emailReader.getNextMessage();
        while (nextMessage != null) {
            LOGGER.debug("Sähköposti haettu.");
            
            processAttachments(nextMessage);
            
            emailReader.cleanUpMessage(nextMessage);
            nextMessage = emailReader.getNextMessage();
        }
    }
}
