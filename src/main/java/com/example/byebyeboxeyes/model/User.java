package com.example.byebyeboxeyes.model;

/**
 * User class that Constructs a new User with specified username, email, and password.
 * Stored userid in the connected DB is used as the primary key
 */
public class User {
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    private int userID;
    private String userName;
    private String email;
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     Constructs a new User with specified username, email, and password. Stored id as primary key
     * @param userID ID of the contact - stored in database as primary key
     * @param userName The name of the user
     * @param email the email of the user
     * @param password the password of the user
     */
    public User(int userID, String userName, String email, String password) {
        this.userID = userID;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
