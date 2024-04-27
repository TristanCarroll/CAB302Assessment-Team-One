package com.example.boxeyesproto.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserDAO {
    private Connection connection;
    private static UserDAO instance = new UserDAO();
    private UserDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }

    public static UserDAO getInstance() {
        return instance;
    }
    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            String query =
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "userName VARCHAR PRIMARY KEY," +
                    "email VARCHAR NOT NULL," +
                    "password VARCHAR NOT NULL" +
                    ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public User getUser(String userName) {
        String query =
                "SELECT * FROM users\n" +
                "WHERE userName LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("userName"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void addUser(User user) throws Exception {
        String query =
                "INSERT INTO users (userName, email, password)" +
                "VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            // TODO: Debugging - Remove this
            System.out.println(statement);
        }
    }
}
