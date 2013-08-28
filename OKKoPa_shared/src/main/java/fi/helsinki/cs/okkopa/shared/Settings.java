package fi.helsinki.cs.okkopa.shared;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.springframework.stereotype.Component;

/**
 * Manages and reads XML files.
 */
@Component
public class Settings extends Properties {

    public Settings() throws IOException {
        this(System.getenv("OKKOPA_SETTINGS_FILEPATH"));
    }

    /**
     *
     * @param fileName to where xml-file is.
     * @throws IOException
     */
    public Settings(String fileName) throws IOException {
        Properties props = readSettingXML(fileName);
        this.putAll(props);
    }

    private Properties readSettingXML(String fileName) throws IOException {
        Properties currentProps = new Properties();

        try {
            InputStream currentStream = getClass().getResourceAsStream("/" + fileName);
            currentProps.loadFromXML(currentStream);
            currentStream.close();
            return currentProps;
        } catch (Exception e) {
            FileInputStream fis = new FileInputStream(fileName);
            currentProps.loadFromXML(fis);
            fis.close();
            return currentProps;
        }
    }
}
