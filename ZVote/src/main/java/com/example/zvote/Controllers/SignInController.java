package com.example.zvote.Controllers;  // Package declaration, specifies the namespace


// Importing necessary classes for UI, file handling, and managing user details
import com.example.zvote.Models.UserModel;
import com.example.zvote.Services.UserService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


public class SignInController {

    public static Map<String, Object> userSession = new HashMap<>();  // Holds session details


    // Method to display the Sign-Up/Log-In window
    public static void showSignInWindow(Stage primaryStage) {
        Stage signInStage = new Stage();
        signInStage.initModality(Modality.APPLICATION_MODAL);
        signInStage.setTitle("Sign Up/Log In - ZVote");
        signInStage.setResizable(false);
        signInStage.initStyle(StageStyle.UNDECORATED);

        BorderPane layout = new BorderPane();
        HBox form = new HBox();
        form.setAlignment(Pos.CENTER);


        // Sign-Up Section Layout
        VBox signInLayout = new VBox(15);
        signInLayout.setPadding(new Insets(20));
        signInLayout.setAlignment(Pos.TOP_LEFT);
        signInLayout.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C8F0FF; -fx-border-width: 3px;");


        // Log-In Section Layout
        VBox loginLayout = new VBox(15);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.TOP_LEFT);
        loginLayout.setStyle("-fx-background-color: #C8F0FF;");


        // Titles for Sign-Up and Log-In Sections
        Label STitle = new Label("Sign Up");
        STitle.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");

        Label LTitle = new Label("Log In");
        LTitle.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");


        // Username Fields
        Label SUsernameLabel = new Label("Username:");
        SUsernameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        TextField SUsernameField = new TextField();
        SUsernameField.setPromptText("Enter your username");
        SUsernameField.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px;");
        SUsernameField.setPrefWidth(250);

        Label LUsernameLabel = new Label("Username:");
        LUsernameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        TextField LUsernameField = new TextField();
        LUsernameField.setPromptText("Enter your username");
        LUsernameField.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px;");
        LUsernameField.setPrefWidth(250);


        // Email Field for Sign-Up Section
        Label SEmailLabel = new Label("Email:");
        SEmailLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        TextField SEmailField = new TextField();
        SEmailField.setPromptText("Enter your email");
        SEmailField.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px;");
        SEmailField.setPrefWidth(250);


        // Password Fields
        Label SPasswordLabel = new Label("Password:");
        SPasswordLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        PasswordField SPasswordField = new PasswordField();
        SPasswordField.setPromptText("Enter your password");
        SPasswordField.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px;");
        SPasswordField.setPrefWidth(250);

        Label LPasswordLabel = new Label("Password:");
        LPasswordLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        PasswordField LPasswordField = new PasswordField();
        LPasswordField.setPromptText("Enter your password");
        LPasswordField.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px;");
        LPasswordField.setPrefWidth(250);


        // Phone Number Section for Sign-Up Section
        Label SPhoneLabel = new Label("Phone Number:");
        SPhoneLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        HBox SPhoneBox = new HBox(10);
        SPhoneBox.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> countryCodeDropdown = new ComboBox<>();
        countryCodeDropdown.getItems().addAll(
                "+1 (USA)", "+33 (France)", "+44 (UK)", "+49 (Germany)", "+91 (India)", "+961 (Lebanon)",
                "+61 (Australia)", "+81 (Japan)", "+82 (South Korea)", "+34 (Spain)", "+39 (Italy)"
        );
        countryCodeDropdown.setValue("+961");
        countryCodeDropdown.setPrefWidth(120);
        countryCodeDropdown.setStyle("-fx-background-color: white; -fx-border-radius: 50;" +
                "-fx-border-color: #C8F0FF; -fx-border-width: 3px;");

        countryCodeDropdown.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Extracting just the numerical part of the country code
                String numericCode = newValue.split(" ")[0]; // Splits by space and takes the first part (e.g., "+961")
                // Use the numericCode variable as needed
                System.out.println("Selected country code: " + numericCode);
            }
        });

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*") || newValue.length() > 10) {
                phoneField.setText(oldValue);
            }
        });
        phoneField.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px;");
        phoneField.setPrefWidth(250);

        SPhoneBox.getChildren().addAll(countryCodeDropdown, phoneField);


        // FileChooser for Photo ID Upload
        FileChooser fileChooser = new FileChooser();
        Button uploadPhotoButton = new Button("Upload Photo ID");
        uploadPhotoButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold;" +
                " -fx-border-radius: 30px; -fx-background-radius: 30px; -fx-padding: 5px 10px;" +
                " -fx-cursor: hand");

        final File[] selectedPhoto = {null};  // To store the chosen photo
        uploadPhotoButton.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(signInStage);
            if (file != null) {
                selectedPhoto[0] = file;
                uploadPhotoButton.setText("Photo Uploaded");
            } else {
                uploadPhotoButton.setText("Upload Photo ID");
            }
        });


        // Submit Button for Sign-Up Section
        Button SSubmitButton = new Button("Submit");
        SSubmitButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold;" +
                " -fx-border-radius: 30px; -fx-background-radius: 30px; -fx-padding: 5px 10px;" +
                " -fx-cursor: hand");

        SSubmitButton.setOnAction(event -> {
            // Validate inputs
            if (SUsernameField.getText().isEmpty() || SEmailField.getText().isEmpty() ||
                    SPasswordField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
                return;
            }

            byte[] photoID;
            try {
                photoID = (selectedPhoto[0] != null) ? Files.readAllBytes(selectedPhoto[0].toPath()) : new byte[0];

                // Create a new user
                UserModel newUser = new UserModel(
                        SUsernameField.getText(),
                        SEmailField.getText(),
                        SPasswordField.getText(),
                        photoID,
                        countryCodeDropdown.getValue() + " " + phoneField.getText()
                );

                // Add user to the database
                UserService userService = new UserService();
                userService.addUser(newUser);

                userSession.put("user", newUser);

                // Clear fields after successful sign-up
                SUsernameField.clear();
                SEmailField.clear();
                SPasswordField.clear();
                phoneField.clear();
                countryCodeDropdown.setValue("+961");
                uploadPhotoButton.setText("Upload Photo ID");

                // Transition to the landing page
                signInStage.close();
                LandingPageController main = new LandingPageController();
                main.showLandingPage(primaryStage, userSession);

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing your request.");
                e.printStackTrace();
            }
        });


        // Submit Button for Log-In Section
        Button LSubmitButton = new Button("Submit");
        LSubmitButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black; -fx-font-weight: bold;" +
                " -fx-border-radius: 30px; -fx-background-radius: 30px; -fx-padding: 5px 10px;" +
                " -fx-cursor: hand");

        LSubmitButton.setOnAction(event -> {
            // Validate inputs
            if (LUsernameField.getText().isEmpty() || LPasswordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
                return;
            }

            boolean isValidUser = false;
            try {
                UserService userService = new UserService();
                isValidUser = userService.checkLogin(LUsernameField.getText(), LPasswordField.getText());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (isValidUser) {
                try {
                    UserService userService = new UserService();
                    UserModel user = userService.getUserByUsername(LUsernameField.getText());

                    userSession.put("user", user);

                    // Clear fields after successful login
                    LUsernameField.clear();
                    LPasswordField.clear();

                    // Close the sign-in stage and navigate to the landing page
                    signInStage.close();

                    if (user.getRole().equals("admin")) {
                        AdminLandingPageController main = new AdminLandingPageController();
                        main.showAdminLandingPage(primaryStage, userSession);
                    } else {
                        LandingPageController main = new LandingPageController();
                        main.showLandingPage(primaryStage, userSession);
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while transitioning to the landing page.");
                    e.printStackTrace();
                }
            } else {
                // Show error alert for invalid credentials
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password. Please try again.");
                LUsernameField.clear();
                LPasswordField.clear();
            }
        });


        // Back Button for returning to the previous stage
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C8F0FF; -fx-border-width: 3px;" +
                " -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 0px 10px;" +
                " -fx-font-size: 15px; -fx-cursor: hand");
        backButton.setOnAction(event -> signInStage.close());
        backButton.setPrefWidth(800);
        backButton.setPrefHeight(35);


        // ImageView for Ballot Image
        ImageView ballotImage = new ImageView(new Image(SignInController.class.getResource("/images/Ballot Image.png").toExternalForm()));
        ballotImage.setPreserveRatio(false);  // Disable aspect ratio preservation to customize dimensions
        ballotImage.setFitHeight(250);  // Set height of the ballot image
        ballotImage.setFitWidth(300);   // Set width of the ballot image

        // Wrapper for Ballot Image to align it centrally
        HBox ballotWrapper = new HBox();
        ballotWrapper.setAlignment(Pos.CENTER);
        ballotWrapper.getChildren().add(ballotImage);  // Add the ballot image to the wrapper


        // Add components to the Sign-Up layout
        signInLayout.getChildren().addAll(
                STitle,                       // Title for the Sign-Up section
                SUsernameLabel, SUsernameField,  // Username label and input field
                SEmailLabel, SEmailField,        // Email label and input field
                SPasswordLabel, SPasswordField,  // Password label and input field
                SPhoneLabel, SPhoneBox,          // Phone label and input box with dropdown
                uploadPhotoButton,               // Button for uploading photo ID
                SSubmitButton                    // Submit button for sign-up
        );


        // Add components to the Log-In layout
        loginLayout.getChildren().addAll(
                LTitle,                         // Title for the Log-In section
                LUsernameLabel, LUsernameField, // Username label and input field
                LPasswordLabel, LPasswordField, // Password label and input field
                LSubmitButton,                  // Submit button for log-in
                ballotWrapper                   // Wrapper containing the ballot image
        );


        // Set dimensions for layouts
        signInLayout.setPrefHeight(300);
        signInLayout.setPrefWidth(400);
        loginLayout.setPrefHeight(300);
        loginLayout.setPrefWidth(400);


        // Add Sign-Up and Log-In layouts to the form
        form.getChildren().addAll(signInLayout, loginLayout);
        layout.setCenter(form);  // Place the form in the center of the layout
        layout.setBottom(backButton);  // Place the back button at the bottom of the layout


        // Create the scene and set dimensions
        Scene scene = new Scene(layout, 800, 635);

        // Position the Sign-In stage in the center of the screen
        signInStage.setX((Screen.getPrimary().getBounds().getWidth() - 800) / 2);
        signInStage.setY((Screen.getPrimary().getBounds().getHeight() - 500) / 2 - 50);


        // Display the Sign-In window
        signInStage.setScene(scene);
        signInStage.showAndWait();
    }


    // Utility method for displaying alerts
    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);  // Create a new alert of the specified type
        alert.setTitle(title);               // Set the title of the alert
        alert.setContentText(message);       // Set the content of the alert
        alert.showAndWait();                 // Display the alert and wait for user input
    }
}
