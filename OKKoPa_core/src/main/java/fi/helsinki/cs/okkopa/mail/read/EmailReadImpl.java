package fi.helsinki.cs.okkopa.mail.read;

import fi.helsinki.cs.okkopa.shared.Settings;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

@Component
public class EmailReadImpl implements EmailRead {

    private static final Logger LOGGER = Logger.getLogger(EmailReadImpl.class.getName());
    private Store store;
    private Folder inbox;
    private final String host;
    private final String username;
    private final String password;
    private final int port;
    private final String inboxFolderName;
    private final boolean deleteAfterProcessing;
    private final String processedFolderName;
    private final int maximumAttachmentSizeInMegabytes;
    
    private final int BYTESINMEGABYTE = 1024 * 1024;

    /**
     * Constructor, gets settings from the given properties object.
     *
     * @param settings
     */
    @Autowired
    public EmailReadImpl(Settings settings) {
        host = settings.getProperty("mail.imap.host");
        username = settings.getProperty("mail.imap.user");
        password = settings.getProperty("mail.imap.password");
        port = Integer.parseInt(settings.getProperty("mail.imap.port"));
        inboxFolderName = settings.getProperty("mail.imap.inboxfoldername");
        deleteAfterProcessing = Boolean.parseBoolean(settings.getProperty("mail.imap.deleteafterprocessing"));
        processedFolderName = settings.getProperty("mail.imap.processedfoldername");
        maximumAttachmentSizeInMegabytes = Integer.parseInt(settings.getProperty("mail.attachment.maximumsizeinmegabytes"));
    }

    @Override
    public void close() throws MessagingException {
        inbox.close(false);
        store.close();
    }

    @Override
    public void connect() throws MessagingException {
        Properties properties = System.getProperties();
        Session session = Session.getDefaultInstance(properties);
        
        store = session.getStore("imaps");
        store.connect(host, port, username, password);
        inbox = store.getFolder(inboxFolderName);
        inbox.open(Folder.READ_WRITE);
    }

    @Override
    public List<InputStream> getMessagesAttachments(Message message) throws MessagingException, IOException {
        List<InputStream> attachments = new ArrayList<>();
        Multipart multipart = (Multipart) message.getContent();
        
        goThroughMultipartsInSearchOfPDFs(multipart, attachments);
        
        return attachments;
    }

    @Override
    public Message getNextMessage() throws MessagingException {
        Message[] messages = inbox.getMessages();
        if (messages.length == 0) {
            return null;
        }
        return messages[0];
    }

    @Override
    public void cleanUpMessage(Message message) throws MessagingException {
        // if not deleting, move to processed folder
        if (!deleteAfterProcessing) {
            Folder processed = getFolder();
            copyMessage(message, processed);
        }
        // delete in any case from inbox
        message.setFlag(Flags.Flag.DELETED, true);
        inbox.expunge();
    }

    @Override
    public void closeQuietly() {
        try {
            close();
        } catch (Exception ex) {
            // Ignore
        }
    }

    private void goThroughMultipartsInSearchOfPDFs(Multipart multipart, List<InputStream> attachments) throws MessagingException, IOException {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            
            // filter by type, filename and attachment size
            if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) && 
                    !StringUtils.isNotBlank(bodyPart.getFileName()) && 
                    ((double) bodyPart.getSize() / BYTESINMEGABYTE) <= maximumAttachmentSizeInMegabytes) {
                continue;
            }
            InputStream inputStream = bodyPart.getInputStream();
            attachments.add(inputStream);
        }
    }

    private Folder getFolder() throws MessagingException {
        Folder processed = store.getFolder(processedFolderName);
        if (!processed.exists()) {
            LOGGER.info("Sähköpostikansiota " + processedFolderName + " ei löytynyt. Luodaan kansio.");
            processed.create(Folder.HOLDS_MESSAGES);
        }
        processed.open(Folder.READ_WRITE);
        
        return processed;
    }

    private void copyMessage(Message message, Folder processed) throws MessagingException {
        Message[] messages = {message};
        inbox.copyMessages(messages, processed);
    }
}