package fi.helsinki.cs.okkopa.main.stage;

import fi.helsinki.cs.okkopa.file.delete.ErrorPDFRemover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Deletes expired PDF files that hasn't been able to be read. The file expiration time must be set in the settings file.
 */

@Component
public class DeleteOldErrorPDFsStage extends Stage {

    private ErrorPDFRemover errorPDFRemover;
    
    /**
     *
     * @param errorPDFRemover
     */
    @Autowired
    public DeleteOldErrorPDFsStage(ErrorPDFRemover errorPDFRemover) {
        this.errorPDFRemover = errorPDFRemover;
    }
    
    @Override
    public void process(Object in) {
        errorPDFRemover.deleteOldMessages();
        processNextStages(null);
    }  
}
