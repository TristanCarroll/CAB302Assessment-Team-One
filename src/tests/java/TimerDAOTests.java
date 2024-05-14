import com.example.byebyeboxeyes.timer.Timer;
import com.example.byebyeboxeyes.model.TimerDAO;
import com.example.byebyeboxeyes.model.SqliteConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TimerDAOTests {
    public static Boolean timersLoaded;
    private class Callback implements TimerDAO.TimersLoadCallback {

        @Override
        public void onTimersLoaded(ArrayList<Timer> timers) {
            TimerDAOTests.timersLoaded = true;
        }
    }
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
    public void testSaveTimer() {
        try {
            Timer1.timerID = timerDAO.saveTimer(Timer1.userID, Timer1.hours, Timer1.minutes, Timer1.seconds, 0);
            Timer2.timerID = timerDAO.saveTimer(Timer2.userID, Timer2.hours, Timer2.minutes, Timer2.seconds, 0);
            assertTrue(true);
        } catch (Exception e) {
            fail("Saving timers failed: " + e.getMessage());
        }
    }
    @Test
    public void testUpdateTimer() {
        try {
            timerDAO.updateTimer(Timer1.timerID, 0, 0,59, 0);
            assertTrue(true);
        } catch (Exception e) {
            fail("Updating timers failed: " + e.getMessage());
        }
    }
    @Test
    public void testIsTimerFavourite() {
        assertEquals(1, timerDAO.isTimerFavourite(Timer1.timerID));
    }
    @Test
    public void testSetTimerFavourite() {
        try {
            timerDAO.setTimerFavourite(Timer1.timerID, 1);
        } catch (Exception e) {
            fail("Setting favourite failed: " + e.getMessage());
        }
    }
    @Test
    public void testDeleteTimer() {
        try {
            timerDAO.deleteTimer(Timer1.timerID);
            timerDAO.deleteTimer(Timer2.timerID);
            assertTrue(true);
        } catch (Exception e) {
            fail("Saving timers failed: " + e.getMessage());
        }
    }
}
