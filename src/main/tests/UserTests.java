import com.example.byebyeboxeyes.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {
    private User user;
    private User newUser;
    @BeforeEach
    public void Setup() {
        user = new User("Username", "Email", "Password");
    }
    @Test
    public void TestGetUserName() {
        assertEquals("Username", user.getUserName());
    }
    @Test
    public void TestGetEmail() {
        // TODO: Update this when we sort email validation
        assertEquals("Email", user.getEmail());
    }
    @Test
    public void TestGetPassword() {
        assertEquals("Password", user.getPassword());
    }
    @Test
    public void testSetUserName() {
        user.setUserName("NewUsername");
        assertEquals("NewUsername", user.getUserName());
    }
    @Test
    public void testSetEmail() {
        // TODO: Update this when we sort email validation
        user.setEmail("newemail");
        assertEquals("newemail", user.getEmail());
    }
    @Test
    public void testSetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }
}
