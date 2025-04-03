package com.example.zvote.Controllers;

import com.example.zvote.Models.UserModel;
import com.example.zvote.Services.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class UserController {
    private static UserModel currentUser; // Holds the logged-in user info

    public void showUserProfile(Stage primaryStage, UserModel user) {
        currentUser = user;

        // Root layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #FFFFFF;");

        // Title
        Label title = new Label("User Profile");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        BorderPane.setAlignment(title, Pos.CENTER);
        layout.setTop(title);
        BorderPane.setMargin(title, new Insets(20, 0, 20, 0));

        // User Info Section
        VBox userInfoSection = new VBox(20);
        userInfoSection.setPadding(new Insets(20));
        userInfoSection.setAlignment(Pos.TOP_CENTER);

        // Username
        Label usernameLabel = new Label("Username: " + user.getUsername());
        usernameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #555555;");

        // Email
        Label emailLabel = new Label("Email: " + user.getUser_email());
        emailLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #555555;");

        // Phone Number
        Label phoneLabel = new Label("Phone Number: " + user.getPhoneNb());
        phoneLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #555555;");

        // Role
        Label roleLabel = new Label("Role: " + user.getRole());
        roleLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #555555;");

        // Photo
        ImageView photoView = new ImageView();
        photoView.setFitWidth(150);
        photoView.setFitHeight(150);
        photoView.setStyle("-fx-border-color: #C8F0FF; -fx-border-width: 3px; -fx-border-radius: 10px;");
        if (user.getUser_photoID() != null && user.getUser_photoID().length > 0) {
            photoView.setImage(new Image(new ByteArrayInputStream(user.getUser_photoID())));
        } else {
            // Fallback to default image
            photoView.setImage(new Image("/images/default-profile.png")); // Replace with actual default path
        }

        // Add user info to section
        userInfoSection.getChildren().addAll(photoView, usernameLabel, emailLabel, phoneLabel, roleLabel);

        layout.setCenter(userInfoSection);

        // Buttons Section
        HBox buttonSection = new HBox(20);
        buttonSection.setPadding(new Insets(20));
        buttonSection.setAlignment(Pos.CENTER);

        // Update Info Button
        Button updateButton = new Button("Update Info");
        updateButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold; -fx-border-radius: 10px;");
        updateButton.setOnAction(event -> showUpdateForm(layout, userInfoSection));

        buttonSection.getChildren().add(updateButton);

        layout.setBottom(buttonSection);

        // Scene settings
        Scene scene = new Scene(layout, 600, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void showUpdateForm(BorderPane layout, VBox userInfoSection) {
        VBox updateForm = new VBox(20);
        updateForm.setPadding(new Insets(20));
        updateForm.setAlignment(Pos.TOP_CENTER);

        Label updateTitle = new Label("Update Info");
        updateTitle.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        // Editable fields
        TextField emailField = new TextField(currentUser.getUser_email());
        emailField.setPromptText("Enter your new email");
        emailField.setStyle("-fx-border-radius: 10px; -fx-border-color: #C8F0FF; -fx-padding: 10px;");

        TextField phoneField = new TextField(currentUser.getPhoneNb());
        phoneField.setPromptText("Enter your new phone number");
        phoneField.setStyle("-fx-border-radius: 10px; -fx-border-color: #C8F0FF; -fx-padding: 10px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your new password");
        passwordField.setStyle("-fx-border-radius: 10px; -fx-border-color: #C8F0FF; -fx-padding: 10px;");

        Button submitButton = new Button("Save Changes");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold; -fx-border-radius: 10px;");
        submitButton.setOnAction(event -> {
            currentUser.setUser_email(emailField.getText());
            currentUser.setPhoneNb(phoneField.getText());
            currentUser.setUser_pass(passwordField.getText());

            try {
                UserService userService = new UserService();
                userService.updateUser(currentUser);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Your information has been updated.");
                alert.showAndWait();

                // Refresh user info section
                layout.setCenter(userInfoSection);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Unable to update information.");
                alert.showAndWait();
            }
        });

        updateForm.getChildren().addAll(updateTitle, emailField, phoneField, passwordField, submitButton);
        layout.setCenter(updateForm);
    }
}