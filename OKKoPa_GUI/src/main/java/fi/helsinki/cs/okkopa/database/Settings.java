package fi.helsinki.cs.okkopa.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages and reads XML files.
 */
public class Settings extends Properties {

    /**
     *
     */
    public static Settings instance = readSettingXML2("settings.xml");
    
    public Settings(String fileName) throws FileNotFoundException, IOException {
        Properties props = readSettingXML(fileName);

        try {
            props.putAll(readSettingXML("passwords.xml"));
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        this.putAll(props);
    }

    private static Settings readSettingXML2(String fileName) {
        try {
            return new Settings(fileName);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private Properties readSettingXML(String fileName) throws FileNotFoundException, IOException {
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
