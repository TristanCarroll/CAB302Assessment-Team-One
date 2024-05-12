import com.example.byebyeboxeyes.timer.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TimerTests {

    private Timer timer;
    private int userID = 1;
    private static int timerID = 1;

    @BeforeEach
    public void setUp() {
        timer = new Timer(timerID,userID, 1, 30, 0);
        timerID++;
    }

    @Test
    public void testGetHours() {
        assertEquals(1, timer.getHours());
    }

    @Test
    public void testGetMinutes() {
        assertEquals(30, timer.getMinutes());
    }

    @Test
    public void testGetSeconds() {
        assertEquals(0, timer.getSeconds());
    }

    @Test
    public void testSetHours() {
        timer.setHours(2);
        assertEquals(2, timer.getHours());
    }

    @Test
    public void testSetMinutes() {
        timer.setMinutes(45);
        assertEquals(45, timer.getMinutes());
    }

    @Test
    public void testSetSeconds() {
        timer.setSeconds(15);
        assertEquals(15, timer.getSeconds());
    }

    @Test
    public void testDecrementTime() {
        timer.decrementTime();
        assertEquals(59, timer.getSeconds());

        for (int i = 0; i < 60; i++) {
            timer.decrementTime();
        }
        assertEquals(28, timer.getMinutes());
        assertEquals(59, timer.getSeconds());

        for (int i = 0; i < 88 * 60 + 59; i++) {
            timer.decrementTime();
        }
        assertEquals(0, timer.getHours());
        assertEquals(0, timer.getMinutes());
        assertEquals(0, timer.getSeconds());
    }

    @Test
    public void testIsFinished() {
        assertFalse(timer.isFinished());

        // Decrementing 1 hour 30 minutes
        for (int i = 0; i < 90 * 60; i++) {
            timer.decrementTime();
        }
        assertTrue(timer.isFinished());
    }

    @Test
    public void testToString() {
        assertEquals("01:30:00", timer.toString());
    }
}
