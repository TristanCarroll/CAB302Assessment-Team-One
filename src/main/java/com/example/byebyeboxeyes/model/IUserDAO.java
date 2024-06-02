package com.example.byebyeboxeyes.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface IUserDAO {
    /**
     * Creates a user object from values in a record of the user table
     *
     * @param userName The username to create the object from
     * @return A user object created from values in the user table
     */
    User getUser(String userName);

    /**
     * Adds a record to the user table. The primary key (userId) is auto-incremented
     *
     * @param userName The username to insert
     * @param email The email address to insert
     * @param password The password to insert
     */
    void addUser(String userName, String email, String password);

    /**
     * Deletes a record from the user table based on the provided primary key
     *
     * @param userId The identification number of the record to delete.
     */
    void deleteUser(int userId);
}
