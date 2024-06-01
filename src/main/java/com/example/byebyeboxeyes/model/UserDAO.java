package com.example.byebyeboxeyes.model;

import java.sql.*;

/**
 * The Data Access Object for the User class.
 * Calls methods to:
 * create user tables in the connected DB, get the current loggedin user credentials, add new users to the DB if none exist,
 * deletes the users credentials from the DB,
 * and updates the current loggedin users password via the reset password functionality or the settings page to update a new password for the loggedin user
 *
 */
public class UserDAO implements IUserDAO{
    private Connection connection;
    //TODO:
    //  Use an interface instead of the SqliteConnection class in all DAO's.
    //  May need to use a dependancy injection framework if we want to keep the constructor private.
    private static UserDAO instance = new UserDAO(SqliteConnection.getInstance());
    private UserDAO(Connection connection) {
        this.connection = connection;
        createTable();
    }
    public static UserDAO getInstance() {
        return instance;
    }

    /**
     * Create a User table for the connected database
     * Uses the userID as a primary key that autoincrements
     * sets the username, email, and password fields.
     * No field can be set to NULL
     */
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

    /**
     * Method to get the current loggedin user in the DB
     * @param userName gets the username from the created table in the DB
     * @return the current loggedin user credentials from the DB
     */
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

    /**
     * Adds a user into the DB if none exists
     * @param userName adds the user's username text field to the DB
     * @param email adds the users email to the DB
     * @param password adds the users password to the DB
     */
    public void addUser(String userName, String email, String password) {
        String query =
                "INSERT INTO users (userName, email, password)" +
                "VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.executeUpdate();
            System.out.println(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to delete the user via the primary key - userID that is connected to the DB
     * @param userId the selected userID associated with the username, email, password will be deleted
     */
    public void deleteUser(int userId) {
        String query = "DELETE FROM users WHERE userID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the users password and changes it in the DB
     * this can be done via the ResetPassword controller which will send a verification link to the nominated email
     * or the Settings controller to reset the password in the application
     * @param password set the new password
     * @param userEmail input the users email to send the
     */
    public void updateUserPassword(String password, String userEmail) {
        String query = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, password);
            statement.setString(2, String.valueOf(userEmail));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
