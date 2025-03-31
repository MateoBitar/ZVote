package com.example.zvote.Controllers;

import com.example.zvote.Models.UserModel;
import com.example.zvote.Services.UserService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class SignInController {
    private static Map<String, Object> userSession = new HashMap<>(); // Holds session details

    public static void showSignInWindow(Stage primaryStage) {
        Stage signInStage = new Stage();
        signInStage.initModality(Modality.APPLICATION_MODAL);
        signInStage.setTitle("Sign In - ZVote");
        signInStage.setResizable(false);
        signInStage.initStyle(StageStyle.UNDECORATED);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C8F0FF; -fx-border-width: 3px;" +
                " -fx-border-radius: 10px");

        Label title = new Label("Sign In");
        title.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        // Username
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle(
                "-fx-background-radius: 20px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-border-color: #C8F0FF; " +
                        "-fx-padding: 8px;" +
                        "-fx-border-width: 3px;"
        );
        usernameField.setPrefWidth(250);

        // Email
        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setStyle(
                "-fx-background-radius: 20px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-border-color: #C8F0FF; " +
                        "-fx-padding: 8px;" +
                        "-fx-border-width: 3px;"
        );
        emailField.setPrefWidth(250);

        // Password
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle(
                "-fx-background-radius: 20px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-border-color: #C8F0FF; " +
                        "-fx-padding: 8px;" +
                        "-fx-border-width: 3px;"
        );
        passwordField.setPrefWidth(250);

        // Phone Number Section
        Label phoneLabel = new Label("Phone Number:");
        phoneLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        HBox phoneBox = new HBox(10);
        phoneBox.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> countryCodeDropdown = new ComboBox<>();
        countryCodeDropdown.getItems().addAll(
                "+1 (USA)", "+33 (France)", "+44 (UK)", "+49 (Germany)", "+91 (India)", "+961 (Lebanon)",
                "+61 (Australia)", "+81 (Japan)", "+82 (South Korea)", "+34 (Spain)", "+39 (Italy)"
        );
        countryCodeDropdown.setValue("+961");
        countryCodeDropdown.setPrefWidth(120);
        countryCodeDropdown.setStyle("-fx-background-color: white; -fx-border-radius: 50; -fx-border-color: #C8F0FF;" +
                "-fx-border-width: 3px");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > 10) {   // Check for numeric input and max length
                phoneField.setText(oldValue); // Revert to the old value
            }
        });
        phoneField.setStyle(
                "-fx-background-radius: 20px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-border-color: #C8F0FF; " +
                        "-fx-padding: 8px;" +
                        "-fx-border-width: 3px;"
        );
        phoneField.setPrefWidth(250);

        phoneBox.getChildren().addAll(countryCodeDropdown, phoneField);

        Button backButton = new Button("\u2190"); // Unicode for a left-pointing arrow
        backButton.setStyle(
                "-fx-background-color: #C8F0FF; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-radius: 30px; " +
                        "-fx-background-radius: 30px; " +
                        "-fx-padding: 0px 10px;" +
                        "-fx-font-size: 20px"
        );
        backButton.setOnAction(event -> {
            signInStage.close();
        });

        // FileChooser for Photo ID
        FileChooser fileChooser = new FileChooser();
        Button uploadPhotoButton = new Button("Upload Photo ID");
        uploadPhotoButton.setStyle(
                "-fx-background-color: #C8F0FF; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-radius: 30px; " +
                        "-fx-background-radius: 30px; " +
                        "-fx-padding: 5px 10px;"
        );
        final File[] selectedPhoto = {null}; // To store the chosen photo
        uploadPhotoButton.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(signInStage);
            if (file != null) {
                selectedPhoto[0] = file; // Save the selected file
                uploadPhotoButton.setText("Photo Uploaded");
            } else {
                uploadPhotoButton.setText("Upload Photo ID");
            }
        });


        // Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setStyle(
                "-fx-background-color: #C8F0FF; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-radius: 30px; " +
                        "-fx-background-radius: 30px; " +
                        "-fx-padding: 5px 10px;"
        );
        submitButton.setOnAction(event -> {
            // Validate inputs
            if (usernameField.getText().isEmpty() || emailField.getText().isEmpty() ||
                    passwordField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
                return;
            }

            byte[] photoID;
            try {
                if (selectedPhoto[0] != null) {
                    photoID = Files.readAllBytes(selectedPhoto[0].toPath());
                } else {
                    photoID = new byte[0]; // Default empty photo ID
                }

                UserModel newUser = new UserModel(
                        usernameField.getText(),
                        emailField.getText(),
                        passwordField.getText(),
                        photoID,
                        countryCodeDropdown.getValue() + " " + phoneField.getText()
                );
                UserService.addUser(newUser);

                userSession.put("user", newUser);
                new LandingPageController(primaryStage, userSession).showMain();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing your request.");
                e.printStackTrace();
            }
        });

        // Centering the buttons
        HBox buttonContainer = new HBox(10, backButton, uploadPhotoButton, submitButton);
        buttonContainer.setAlignment(Pos.CENTER);

        Label loginLabel = new Label("Already have an account? Log In");
        loginLabel.setStyle("-fx-font-size: 10px;");
        loginLabel.setOnMouseEntered(event -> {
            loginLabel.setStyle("-fx-font-size: 10px; -fx-underline: true;");
        });
        loginLabel.setOnMouseExited(event -> {
            loginLabel.setStyle("-fx-font-size: 10px; -fx-underline: false;");
        });
        loginLabel.setOnMouseClicked(event -> {
            try {
                signInStage.close();
                // LogInController.showLogInWindow(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Add all components to the layout
        layout.getChildren().addAll(
                title,
                usernameLabel, usernameField,
                emailLabel, emailField,
                passwordLabel, passwordField,
                phoneLabel, phoneBox,
                loginLabel,
                buttonContainer
        );

        Scene scene = new Scene(layout, 350, 550);

        signInStage.setX((Screen.getPrimary().getBounds().getWidth() - 350) / 2);
        signInStage.setY((Screen.getPrimary().getBounds().getHeight() - 500) / 2 - 50);

        signInStage.setScene(scene);
        signInStage.showAndWait();
    }

    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
