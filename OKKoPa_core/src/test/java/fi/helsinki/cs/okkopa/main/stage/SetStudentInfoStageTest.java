package fi.helsinki.cs.okkopa.main.stage;

import fi.helsinki.cs.okkopa.shared.database.MissedExamDao;
import fi.helsinki.cs.okkopa.shared.database.QRCodeDAO;
import fi.helsinki.cs.okkopa.shared.exception.NotFoundException;
import fi.helsinki.cs.okkopa.main.ExceptionLogger;
import fi.helsinki.cs.okkopa.shared.Settings;
import fi.helsinki.cs.okkopa.model.ExamPaper;
import fi.helsinki.cs.okkopa.model.Student;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SetStudentInfoStageTest {

    private ExceptionLogger exceptionLoggerMock;
    private QRCodeDAO qrCodeDatabaseMock;
    private MissedExamDao missedExamDatabaseMock;
    private SetStudentInfoStage setStudentInfoStage;
    private Stage nextStage;
    private ExamPaper mockPaper;
    private Settings settingsMock;

    public SetStudentInfoStageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        exceptionLoggerMock = mock(ExceptionLogger.class);
        qrCodeDatabaseMock = mock(QRCodeDAO.class);
        missedExamDatabaseMock = mock(MissedExamDao.class);
        settingsMock = mock(Settings.class);
        when(settingsMock.getProperty("mail.receiver.atdomain")).thenReturn("@tes.ti");
        setStudentInfoStage = new SetStudentInfoStage(qrCodeDatabaseMock, missedExamDatabaseMock, exceptionLoggerMock, settingsMock);
        nextStage = mock(Stage.class);
        setStudentInfoStage.setNext(nextStage);
        mockPaper = mock(ExamPaper.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testWithNormalId() throws SQLException, NotFoundException {
        when(mockPaper.getQRCodeString()).thenReturn("tunnus");
        setStudentInfoStage.process(mockPaper);
        verify(qrCodeDatabaseMock, never()).getUserID(anyString());
        verify(mockPaper, times(1)).setStudent(any(Student.class));
        verify(nextStage, times(1)).process(mockPaper);
    }

    @Test
    public void testWithGeneratedId() throws SQLException, NotFoundException {
        when(mockPaper.getQRCodeString()).thenReturn("4GeneratedBecauseStartsWithNumber");
        when(qrCodeDatabaseMock.getUserID("4GeneratedBecauseStartsWithNumber")).thenReturn("jeejee");
        setStudentInfoStage.process(mockPaper);
        verify(qrCodeDatabaseMock, times(1)).getUserID("4GeneratedBecauseStartsWithNumber");
        verify(mockPaper, times(1)).setStudent(any(Student.class));
        verify(nextStage, times(1)).process(mockPaper);
    }
    
    @Test
    public void testWithGeneratedIdReturningNull() throws SQLException, NotFoundException {
        when(mockPaper.getQRCodeString()).thenReturn("4GeneratedBecauseStartsWithNumber");
        setStudentInfoStage.process(mockPaper);
        verify(qrCodeDatabaseMock, times(1)).getUserID("4GeneratedBecauseStartsWithNumber");
        verify(mockPaper, never()).setStudent(any(Student.class));
        verify(nextStage, never()).process(mockPaper);
    }

    @Test
    public void testExamPapersStudent() throws SQLException, NotFoundException {
        ExamPaper examPaper = new ExamPaper();
        examPaper.setQRCodeString("tunnus");
        setStudentInfoStage.process(examPaper);
        assertEquals("tunnus", examPaper.getStudent().getUsername());
        assertEquals("tunnus@tes.ti", examPaper.getStudent().getEmail());
    }

    @Test
    public void testWithUserIdError() throws SQLException, NotFoundException {
        when(mockPaper.getQRCodeString()).thenReturn("0tunnus");
        doThrow(new NotFoundException()).when(qrCodeDatabaseMock).getUserID("0tunnus");
        setStudentInfoStage.process(mockPaper);
        verify(qrCodeDatabaseMock, times(1)).getUserID("0tunnus");
        verify(mockPaper, never()).setStudent(any(Student.class));
        verify(nextStage, never()).process(mockPaper);
    }
}