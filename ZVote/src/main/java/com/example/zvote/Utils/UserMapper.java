package com.example.zvote.Utils;

import com.example.zvote.Models.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public static UserModel mapResultSetToUser(ResultSet rs) throws SQLException {
        UserModel user = new UserModel(
                rs.getString("username"),
                rs.getString("user_email"),
                rs.getString("user_pass"),
                rs.getBytes("user_photoID"),
                rs.getString("phoneNb")
        );
        user.setUser_ID(rs.getInt("user_ID")); // Ensure user_ID is retrieved
        return user;
    }
}
