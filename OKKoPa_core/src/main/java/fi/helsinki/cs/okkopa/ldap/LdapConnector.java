package fi.helsinki.cs.okkopa.ldap;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.util.ssl.KeyStoreKeyManager;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;
import fi.helsinki.cs.okkopa.shared.Settings;
import fi.helsinki.cs.okkopa.shared.exception.NotFoundException;
import fi.helsinki.cs.okkopa.model.Student;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * LDAP-connector. Connects to LDAP and fetches student number matching given
 * user id.
 *
 */
@Component
public class LdapConnector {

    private Settings settings;
    private static final Logger LOGGER = Logger.getLogger(LdapConnector.class.getName());
    private String searchFilter;
    private String baseOU;
    private String bindDN;
    private String bindPWD;

    /**
     * Creates a new LdapConnector with settings given as a parameter.
     *
     * @param settings The settings file that contains credentials LDAP host
     * name, port, bindDN (user name), password, key store file location and key
     * store secret.
     *
     */
    @Autowired
    public LdapConnector(Settings settings) {
        this.settings = settings;
        this.searchFilter = "(uid=%s)";
        this.baseOU = "dc=helsinki,dc=fi";
        this.bindDN = settings.getProperty("ldap.user");
        this.bindPWD = settings.getProperty("ldap.password");
    }

    /**
     * Fetches student number matching username from LDAP. Sets the student
     * number to a Student object and returns it if a number is found. Throws an
     * exception if LDAP connection / search fails, or if more than one or no
     * results are found.
     *
     * @param student a container for username, e-mail address and student
     * number for a single student. Contains just a user name when given as a
     * parameter, should contain also student number when returned.
     *
     */
    public Student setStudentInfo(Student student) throws NotFoundException, GeneralSecurityException, LDAPException {
        LDAPConnection ldc = null;

        try {
            ldc = getLDAPConnetion();

            SearchResult result = getStudentNumber(ldc, student);
            parseAndSetStudentNumber(result, student);
            
            ldc.close();
            return student;

        } catch (LDAPException | GeneralSecurityException | NotFoundException ex) {
            if (ldc != null) {
                ldc.close();
            }
            throw ex;
        }
    }

    private SSLUtil getSSLUtil() throws KeyStoreException {
        return new SSLUtil(new KeyStoreKeyManager(settings.getProperty("ldap.keystore.file"),
                settings.getProperty("ldap.keystore.secret").toCharArray()),
                new TrustAllTrustManager(true));
    }

    private LDAPConnection getLDAPConnetion() throws GeneralSecurityException, NumberFormatException, LDAPException {
        SSLUtil sslUtil = getSSLUtil();
        LDAPConnection ldc = new LDAPConnection(sslUtil.createSSLSocketFactory(), settings.getProperty("ldap.server.address"), Integer.parseInt(settings.getProperty("ldap.server.port")));
        authenticateToLPAP(ldc);
        return ldc;
    }

    private SearchResult getStudentNumber(LDAPConnection ldc, Student student) throws LDAPSearchException, NotFoundException {
        SearchResult result = ldc.search(baseOU, SearchScope.SUBORDINATE_SUBTREE, String.format(searchFilter, student.getUsername()), "schacPersonalUniqueCode");

        if (result.getEntryCount() > 1) {
            throw new NotFoundException("Too many results from LDAP-query with username " + student.getUsername() + ".");
        }

        if (result.getEntryCount() < 1) {
            throw new NotFoundException("No student information returned from LDAP.");
        }
        return result;
    }

    private void authenticateToLPAP(LDAPConnection ldc) throws LDAPException {
        SimpleBindRequest bindReq = new SimpleBindRequest(bindDN, bindPWD);
        bindReq.setResponseTimeoutMillis(TimeUnit.SECONDS.toMillis(1));
        ldc.bind(bindReq);
    }

    private void parseAndSetStudentNumber(SearchResult result, Student student) {
        SearchResultEntry entry = result.getSearchEntries().get(0);
        String[] strArr = entry.getAttributeValue("schacPersonalUniqueCode").split(":");
        String studentNumber = strArr[strArr.length - 1];
        student.setStudentNumber(studentNumber);
        
        LOGGER.info("Found student with student number: " + studentNumber);
    }
}