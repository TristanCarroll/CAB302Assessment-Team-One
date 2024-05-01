package com.example.byebyeboxeyes.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserDAO {
    private Connection connection;
    private static UserDAO instance = new UserDAO(SqliteConnection.getInstance());
    private UserDAO(Connection connection) {
        this.connection = connection;
        createTable();
    }
    public static UserDAO getInstance() {
        return instance;
    }

    private void createTable() {
        try (Statement statement = connection.createStatement()) {
            String query =
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "userID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userName VARCHAR UNIQUE," +
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
                        resultSet.getInt("userID"),
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
    public void addUser(String userName, String email, String password) throws Exception {
        String query =
                "INSERT INTO users (userName, email, password)" +
                "VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.executeUpdate();
            // TODO: Debugging - Remove this
            System.out.println(statement);
        }
    }

    public void deleteUser(String username) throws Exception {
        String query = "DELETE FROM users WHERE userName = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }
    }
}
