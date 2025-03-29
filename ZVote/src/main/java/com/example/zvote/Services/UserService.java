package com.example.zvote.Services;

import com.example.zvote.Connection.DBHandler;
import com.example.zvote.Controllers.SignInController;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {
    public static void addUser(String username, String user_email, String user_pass, byte[] user_photoID, String phoneNb) {
        if (username.isEmpty() || user_email.isEmpty() || user_pass.isEmpty() || user_photoID.length == 0 || phoneNb.isEmpty()) {
            SignInController.showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
            return;
        }

        DBHandler db = null;
        try {
            db = new DBHandler();
            Connection conn = db.getConnection();

            // Check if username already exists
            String checkQuery = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement checkStatement = conn.prepareStatement(checkQuery)) {
                checkStatement.setString(1, username);
                var resultSet = checkStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    SignInController.showAlert(Alert.AlertType.ERROR, "Error", "Username already exists. Please choose a different one.");
                    return; // Stop execution if username already exists
                }
            }

            String query = "INSERT INTO users (username, user_email, user_pass, user_photoID, phoneNb) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, username);
                statement.setString(2, user_email);
                statement.setString(3, user_pass);
                statement.setBytes(4, user_photoID); // Proper binary data handling
                statement.setString(5, phoneNb);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    SignInController.showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully!");
                }
            } catch (SQLException exe) {
                SignInController.showAlert(Alert.AlertType.ERROR, "Error", "Error adding user: " + exe.getMessage());
                exe.printStackTrace();
            }
        } catch (Exception e) {
            SignInController.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding user.");
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.closeConnection(); // Ensure the database connection is closed
            }
        }
    }

    public static void deleteUser(String username) {
        if (username == null || username.isEmpty()) {
            SignInController.showAlert(Alert.AlertType.ERROR, "Error", "Please specify a username.");
            return;
        }

        DBHandler db = null;
        try {
            db = new DBHandler();
            Connection conn = db.getConnection();

            String query = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, username);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    SignInController.showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully!");
                } else {
                    SignInController.showAlert(Alert.AlertType.ERROR, "Error", "No user found with the specified username.");
                }
            } catch (SQLException exe) {
                SignInController.showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + exe.getMessage());
                exe.printStackTrace();
            }
        } catch (Exception e) {
            SignInController.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting user.");
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.closeConnection(); // Ensure the database connection is closed
            }
        }
    }
}