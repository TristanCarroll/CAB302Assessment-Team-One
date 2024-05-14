//import com.example.byebyeboxeyes.controller.StatisticsController;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.lang.reflect.Method;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import static org.junit.jupiter.api.Assertions.fail;
//
//public class StatisticsControllerTests {
//    private static StatisticsController statisticsController;
//    private URL url = new URL("file:/C:/Users/TJ/IdeaProjects/ByeByeBoxEyes/target/classes/com/example/byebyeboxeyes/statistics-view.fxml");
//
//    public StatisticsControllerTests() throws MalformedURLException {
//    }
//
//    @BeforeAll
//    public static void setUp() { statisticsController = new StatisticsController(); }
//    @Test
//    public void testSetupTotalPerDayChart() {
//        try {
//            Method method = StatisticsController.class.getDeclaredMethod("setupTotalPerDayChart");
//            method.setAccessible(true);
//            method.invoke(statisticsController);
//        } catch (Exception e) {
//            fail("Init failed: " + e.getMessage());
//        }
//    }
//
//}
