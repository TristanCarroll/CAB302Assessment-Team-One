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
    private static class Timer1 {
        public static int timerID;
        public static final int userID = 1;
        public static final int hours = 1;
        public static final int minutes = 30;
        public static final int seconds = 0;
    }
    private static class Timer2 {
        public static int timerID;
        public static final int userID = 1;
        public static final int hours = 0;
        public static final int minutes = 45;
        public static final int seconds = 10;
    }
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
        Timer1.timerID = timerDAO.saveTimer(Timer1.userID, Timer1.hours, Timer1.minutes, Timer1.seconds);
        Timer2.timerID = timerDAO.saveTimer(Timer2.userID, Timer2.hours, Timer2.minutes, Timer2.seconds);
        List<Timer> loadedTimers = timerDAO.loadTimers(Timer1.userID);

        //TODO: Should be using primary keys but the current Timer implementation doesn't allow for it
        assertEquals(2, loadedTimers.size());
        assertEquals(loadedTimers.get(0).getHours(),   Timer1.hours);
        assertEquals(loadedTimers.get(0).getMinutes(), Timer1.minutes);
        assertEquals(loadedTimers.get(0).getSeconds(), Timer1.seconds);
        assertEquals(loadedTimers.get(1).getHours(),   Timer2.hours);
        assertEquals(loadedTimers.get(1).getMinutes(), Timer2.minutes);
        assertEquals(loadedTimers.get(1).getSeconds(), Timer2.seconds);
    }
    @Test
    public void testDeleteTimers() throws Exception {
        //TODO: Timer primary keys again
        timerDAO.deleteTimer(1);
        timerDAO.deleteTimer(2);
        assertTrue(timerDAO.loadTimers(Timer1.userID).isEmpty());
    }
}
