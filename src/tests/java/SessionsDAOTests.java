import com.example.byebyeboxeyes.model.SessionsDAO;
import com.example.byebyeboxeyes.model.UserDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SessionsDAOTests {
    private static Connection mockConnection;
    private static SessionsDAO sessionsDAO;
    private static int USER_ID = 1;
    private static int TIMER_ID = 1;
    private static int SESSION_ID;
    private static long UNIX_START_TIME = 1000;
    private static long UNIX_END_TIME = 2000;
    @BeforeAll
    public static void setUp() throws Exception {
        mockConnection = MockSqliteConnection.getInstance();
        Constructor<SessionsDAO> constructor = SessionsDAO.class.getDeclaredConstructor(Connection.class);
        constructor.setAccessible(true);
        sessionsDAO = constructor.newInstance(mockConnection);
    }
    @Test
    public void testStartSession() {
        try {
            SESSION_ID = sessionsDAO.startSession(USER_ID, TIMER_ID, UNIX_START_TIME);
            assertTrue(true);
        } catch (Exception e) {
            fail("Adding user failed: " + e.getMessage());
        }
    }
    @Test
    public void testEndSession() {
        try{
            sessionsDAO.endSession(SESSION_ID, UNIX_END_TIME);
        } catch (Exception e) {
            fail("Ending session failed: " + e.getMessage());
        }
    }
    @Test
    public void testGetTotalSessionTimePerDay() {
        try {
            sessionsDAO.getTotalSessionTimePerDay(USER_ID);
            assertTrue(true);
        } catch (Exception e) {
            fail("Getting total session time per day failed: " + e.getMessage());
        }
    }
    @Test
    public void testGetTotalUsageToday() {
        try {
            sessionsDAO.getTotalUsageToday(USER_ID);
            assertTrue(true);
        } catch (Exception e) {
            fail("Getting total session time today failed: " + e.getMessage());
        }
    }
    @Test
    public void testGetTotalUsageOverall() {
        try {
            sessionsDAO.getTotalUsageOverall(USER_ID);
            assertTrue(true);
        } catch (Exception e) {
            fail("Getting total session time overall failed: " + e.getMessage());
        }
    }
    @Test
    public void getNumberOfSessionsToday() {
        try {
            sessionsDAO.getNumberOfSessionsToday(USER_ID);
            assertTrue(true);
        } catch (Exception e) {
            fail("Getting number of sessions today failed: " + e.getMessage());
        }
    }
    @Test
    public void testGetNumberOfSessionsOverall() {
        try {
            sessionsDAO.getNumberOfSessionsOverall(USER_ID);
            assertTrue(true);
        } catch (Exception e) {
            fail("Getting number of sessions overall failed: " + e.getMessage());
        }
    }
}
