package com.example.zvote.Services;  // Package declaration, specifies the namespace


// Importing necessary classes and utilities
import com.example.zvote.Models.UserModel;
import com.example.zvote.Connection.DBHandler;
import com.example.zvote.Utils.UserMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.zvote.Models.UserModel.hashPassword;


public class UserService {
    private Connection connection;  // Database connection instance


    // Constructor to initialize the database connection
    public UserService() throws Exception {
        DBHandler dbHandler = new DBHandler();
        connection = dbHandler.getConnection();
    }


    // Method to add a new user to the database
    public void addUser(UserModel user) throws SQLException {

        // Query to check if a username already exists in the database
        String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, user.getUsername());
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                throw new IllegalArgumentException("Username already exists. Please choose a different one.");
            }
        }

        // Query to insert a new user into the database
        String insertQuery = "INSERT INTO users (username, user_email, user_pass, user_photoID, phoneNb, role) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getUser_email());
            statement.setString(3, user.getUser_pass());
            statement.setBytes(4, user.getUser_photoID());
            statement.setString(5, user.getPhoneNb());
            statement.setString(6, user.getRole());
            statement.executeUpdate();
        }
    }


    // Method to fetch all users from the database
    public List<UserModel> getAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        List<UserModel> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(UserMapper.mapResultSetToUser(resultSet));  // Map resultSet to UserModel objects
            }
        }
        return users;
    }


    // Method to fetch a user by username
    public UserModel getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);  // Set username parameter
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return UserMapper.mapResultSetToUser(resultSet);  // Return the mapped UserModel object
            }
        }
        return null;  // Return null if no user is found
    }


    // Method to update an existing user's information in the database
    public void updateUser(UserModel updatedUser) throws SQLException {
        String query = "UPDATE users SET user_email = ?, user_pass = ?, user_photoID = ?, phoneNb = ?, role = ? WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, updatedUser.getUser_email());
            statement.setString(2, updatedUser.getUser_pass());
            statement.setBytes(3, updatedUser.getUser_photoID());
            statement.setString(4, updatedUser.getPhoneNb());
            statement.setString(5, updatedUser.getRole());
            statement.setString(6, updatedUser.getUsername());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new IllegalArgumentException("User not found.");  // Throw exception if no user is updated
            }
        }
    }


    // Method to delete a user from the database by username
    public void deleteUser(String username) throws SQLException {
        String deleteQuery = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setString(1, username);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new IllegalArgumentException("No user found with the specified username.");  // Exception for non-existent user
            }
        }
    }


    // Method to check login credentials (username and password)
    public boolean checkLogin(String username, String password) throws SQLException {

        // Safeguard against null database connection
        if (connection == null) {
            System.out.println("Database connection is null!");
            return false;
        }

        String query = "SELECT user_pass FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);  // Set username parameter
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("user_pass");
                System.out.println("Stored Hash: " + storedPassword);
                System.out.println("Computed Hash: " + hashPassword(password));
                return storedPassword.equals(hashPassword(password));  // Compare hashed passwords
            }
        }
        return false;  // Return false if credentials are invalid
    }
}