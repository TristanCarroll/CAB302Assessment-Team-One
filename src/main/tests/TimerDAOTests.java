import com.example.byebyeboxeyes.timer.Timer;
import com.example.byebyeboxeyes.model.TimerDAO;
import com.example.byebyeboxeyes.model.SqliteConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TimerDAOTests {
    private final int userID = 1;
    private final Timer timer1 = new Timer(userID, 1, 30, 0);
    private final Timer timer2 = new Timer(userID, 0, 45, 10);
    private static Connection mockConnection;
    private static TimerDAO timerDAO;

    // Accessing the private constructor with reflection
    @BeforeAll
    public static void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        mockConnection = MockSqliteConnection.getInstance();
        Constructor<TimerDAO> constructor = TimerDAO.class.getDeclaredConstructor(Connection.class);
        constructor.setAccessible(true);
        timerDAO = constructor.newInstance(mockConnection);
    }
    @Test
    public void testSaveAndLoadTimers() {
        timerDAO.saveTimer(timer1);
        timerDAO.saveTimer(timer2);
        List<Timer> loadedTimers = timerDAO.loadTimers(userID);

        //TODO: Should be using primary keys but the current Timer implementation doesn't allow for it
        assertEquals(2, loadedTimers.size());
        assertEquals(loadedTimers.get(0).getHours(), timer1.getHours());
        assertEquals(loadedTimers.get(0).getMinutes(), timer1.getMinutes());
        assertEquals(loadedTimers.get(0).getSeconds(), timer1.getSeconds());
        assertEquals(loadedTimers.get(1).getHours(), timer2.getHours());
        assertEquals(loadedTimers.get(1).getMinutes(), timer2.getMinutes());
        assertEquals(loadedTimers.get(1).getSeconds(), timer2.getSeconds());
    }
    @Test
    public void testDeleteTimers() throws Exception {
        //TODO: Timer primary keys again
        timerDAO.deleteTimer("1");
        timerDAO.deleteTimer("2");
        assertTrue(timerDAO.loadTimers(userID).isEmpty());
    }
}
