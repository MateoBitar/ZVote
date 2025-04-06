package com.example.zvote.Utils;  // Package declaration, specifies the namespace


// Importing necessary classes
import com.example.zvote.Models.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserMapper {

    // Static method to map a ResultSet to a UserModel instance
    public static UserModel mapResultSetToUser(ResultSet rs) throws SQLException {

        // Create a UserModel object using data from the ResultSet
        UserModel user = new UserModel(
                rs.getString("username"),        // Retrieve username from ResultSet
                rs.getString("user_email"),      // Retrieve user email from ResultSet
                rs.getString("user_pass"),       // Retrieve user password from ResultSet
                rs.getBytes("user_photoID"),     // Retrieve user photo ID (binary data) from ResultSet
                rs.getString("phoneNb")          // Retrieve phone number from ResultSet
        );

        // Set user_ID retrieved from the ResultSet to the UserModel object
        user.setUser_ID(rs.getInt("user_ID"));

        // Set role retrieved from the ResultSet to the UserModel object
        user.setRole(rs.getString("role"));

        // Return the mapped UserModel object
        return user;
    }
}