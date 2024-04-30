import com.example.byebyeboxeyes.model.User;
import com.example.byebyeboxeyes.model.UserDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTests {

    private static Connection mockConnection;
    private static UserDAO userDAO;
    private User user = new User("testUser", "test@example.com", "password");


    // Accessing the private constructor with reflection
    @BeforeAll
    public static void setUp() throws Exception {
        mockConnection = MockSqliteConnection.getInstance();
        Constructor<UserDAO> constructor = UserDAO.class.getDeclaredConstructor(Connection.class);
        constructor.setAccessible(true);
        userDAO = constructor.newInstance(mockConnection);
    }
    @Test
    public void testAddAndGetUser() throws Exception {
        userDAO.addUser(user);
        User retrievedUser = userDAO.getUser("testUser");
        assertEquals(user.getUserName(), retrievedUser.getUserName());
        assertEquals(user.getEmail(), retrievedUser.getEmail());
        assertEquals(user.getPassword(), retrievedUser.getPassword());
    }

    @Test
    public void testDeleteUser() throws Exception {
        userDAO.deleteUser(user);
        assertNull(userDAO.getUser("testUser"));
    }
}
