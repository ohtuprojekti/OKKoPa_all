package fi.helsinki.cs.okkopa.shared.database;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import fi.helsinki.cs.okkopa.shared.Settings;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Is the settings and properties for OKKoPas own database.
 */
@Component
public class OkkopaDatabaseConnectionSource extends JdbcConnectionSource {

    /**
     *
     * @param settings object where username and password are given.
     * @throws SQLException
     */
    @Autowired
    public OkkopaDatabaseConnectionSource(Settings settings) throws SQLException {
        super(settings.getProperty("database.h2.url"),
                settings.getProperty("database.h2.user"),
                settings.getProperty("database.h2.password"));
    }
}