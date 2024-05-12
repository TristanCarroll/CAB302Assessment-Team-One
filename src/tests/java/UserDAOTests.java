import com.example.byebyeboxeyes.model.User;
import com.example.byebyeboxeyes.model.UserDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTests {

    private static Connection mockConnection;
    private static UserDAO userDAO;
    private String username = "testUser";
    private String email = "test@example.com";
    private String password = "password";


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
        userDAO.addUser(username, email, password);
        User retrievedUser = userDAO.getUser("testUser");
        assertEquals(username, retrievedUser.getUserName());
        assertEquals(email, retrievedUser.getEmail());
        assertEquals(password, retrievedUser.getPassword());
    }
    @Test
    public void testDeleteUser() throws Exception {
        userDAO.deleteUser(username);
        assertNull(userDAO.getUser("testUser"));
    }
}
