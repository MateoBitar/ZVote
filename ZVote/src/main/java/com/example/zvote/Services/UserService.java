package com.example.zvote.Services;

import com.example.zvote.Models.UserModel;
import com.example.zvote.Connection.DBHandler;
import com.example.zvote.Utils.UserMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static Connection connection;

    public UserService() throws Exception {
        DBHandler dbHandler = new DBHandler();
        this.connection = dbHandler.getConnection();
    }

    // Save a user
    public static void addUser(UserModel user) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, user.getUsername());
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                throw new IllegalArgumentException("Username already exists. Please choose a different one.");
            }
        }

        String insertQuery = "INSERT INTO users (username, user_email, user_pass, user_photoID, phoneNb, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getUser_email());
            statement.setString(3, user.getUser_pass()); // Password is already hashed in the model
            statement.setBytes(4, user.getUser_photoID());
            statement.setString(5, user.getPhoneNb());
            statement.setString(6, user.getRole());
            statement.executeUpdate();
        }
    }

    // Fetch all users
    public List<UserModel> getAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        List<UserModel> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(UserMapper.mapResultSetToUser(resultSet));
            }
        }
        return users;
    }

    // Update a user
    public void updateUser(int userId, UserModel updatedUser) throws SQLException {
        String query = "UPDATE users SET username = ?, user_email = ?, user_pass = ?, user_photoID = ?, phoneNb = ?, role = ? WHERE user_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, updatedUser.getUsername());
            statement.setString(2, updatedUser.getUser_email());
            statement.setString(3, updatedUser.getUser_pass()); // Already hashed in UserModel
            statement.setBytes(4, updatedUser.getUser_photoID());
            statement.setString(5, updatedUser.getPhoneNb());
            statement.setString(6, updatedUser.getRole());
            statement.setInt(7, userId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new IllegalArgumentException("User not found.");
            }
        }
    }

    // Delete a user
    public void deleteUser(String username) throws SQLException {
        String deleteQuery = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setString(1, username);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new IllegalArgumentException("No user found with the specified username.");
            }
        }
    }
}
