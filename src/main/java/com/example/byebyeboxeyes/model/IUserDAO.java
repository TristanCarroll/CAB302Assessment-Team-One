package com.example.byebyeboxeyes.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface IUserDAO {
    User getUser(String userName);
    void addUser(String userName, String email, String password);
    void deleteUser(int userId);
}
