import com.example.byebyeboxeyes.model.ISqliteConnection;
import com.example.byebyeboxeyes.model.SqliteConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MockSqliteConnection implements ISqliteConnection {
    private static Connection instance = null;

    private MockSqliteConnection() {
        String url = "jdbc:sqlite:Mock.db";

        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new MockSqliteConnection();
        }
        return instance;
    }
}
