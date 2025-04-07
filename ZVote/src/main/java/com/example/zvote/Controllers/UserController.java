// UserController class handles the User Profile functionality
package com.example.zvote.Controllers;

import com.example.zvote.Main;
import com.example.zvote.Models.UserModel;
import com.example.zvote.Services.UserService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import static com.example.zvote.Controllers.LandingPageController.animateMenu;

// Class definition
public class UserController {

    private static UserModel currentUser;  // Holds the logged-in user information
    public static Map<String, Object> userSession = new HashMap<>();  // Stores session-related data

    // Method to display user profile
    public void showUserProfile(Stage primaryStage, UserModel user) {
        currentUser = user;  // Store the current user
        userSession.put("user", user);  // Add user details to session

        // Main layout setup
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #FFFFFF");  // Set background color

        // Top bar configuration
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 10, 10, 40));  // Add padding
        topBar.setStyle("-fx-background-color: #C8F0FF;");  // Set background color
        topBar.setAlignment(Pos.CENTER);  // Center align content

        // Add shadow effect to top bar
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);  // Set shadow radius
        shadow.setOffsetY(2);  // Add vertical offset
        shadow.setColor(Color.LIGHTGRAY);  // Set shadow color
        topBar.setEffect(shadow);  // Apply shadow to top bar

        // Logo
        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));  // Set font style and size

        // Menu setup
        HBox menu = new HBox(-10);

        // Polls button
        Button pollIcon = new Button("\uD83D\uDCCB");  // Unicode for clipboard icon
        pollIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 25; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        pollIcon.setPrefHeight(30);  // Set button height
        pollIcon.setPrefWidth(60);  // Set button width
        pollIcon.setTranslateX(150);  // Adjust position
        pollIcon.setOnMouseEntered(e -> pollIcon.setStyle(pollIcon.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));  // Hover effect
        pollIcon.setOnMouseExited(e -> pollIcon.setStyle(pollIcon.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));  // Hover exit
        pollIcon.setOnAction(e -> {
            // Navigate based on user role
            try {
                if (currentUser.getRole().equals("admin")) {
                    new AdminLandingPageController().showAdminLandingPage(primaryStage, userSession);  // Admin landing page
                } else if (currentUser.getRole().equals("voter")) {
                    new LandingPageController().showLandingPage(primaryStage, userSession);  // Voter landing page
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Profile Menu Button
        MenuButton profileMenu = new MenuButton("\uD83D\uDC64");  // Unicode for user icon
        profileMenu.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-background-color: #C8F0FF; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        profileMenu.setPrefHeight(30);  // Set button height
        profileMenu.setPrefWidth(75);  // Set button width
        profileMenu.setTranslateX(200);  // Adjust position

        // Menu items
        MenuItem updateInfoItem = new MenuItem("Update Info");
        updateInfoItem.setOnAction(e -> showUpdateForm(primaryStage));  // Show update form

        MenuItem logoutItem = new MenuItem("Log Out");
        logoutItem.setOnAction(e -> {
            primaryStage.close();  // Close current stage
            new Main().start(new Stage());  // Restart application
        });

        MenuItem logoffItem = new MenuItem("Close Application");
        logoffItem.setOnAction(e -> primaryStage.close());  // Close application

        profileMenu.getItems().addAll(updateInfoItem, logoutItem, logoffItem);  // Add items to profile menu

        Label menuIcon = new Label("\u283F");  // Unicode for menu icon
        menuIcon.setStyle("-fx-font-size: 24px; -fx-padding: 10px; -fx-cursor: hand;");
        menuIcon.setOnMouseClicked(e -> animateMenu(pollIcon, profileMenu));  // Animate menu

        menu.getChildren().addAll(pollIcon, profileMenu, menuIcon);
        menu.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(menu, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, menu);

        layout.setTop(topBar);  // Add top bar to layout

        // Center content
        VBox userInfoSection = new VBox(20);
        userInfoSection.setPadding(new Insets(20));
        userInfoSection.setAlignment(Pos.TOP_CENTER);

        VBox userContentSection = new VBox();
        userContentSection.setPadding(new Insets(20));
        userContentSection.setAlignment(Pos.CENTER_LEFT);

        // User details
        Label title = new Label("User Profile");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: BOLD; -fx-text-fill: #333333;");
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(20, 0, 20, 0));

        Label usernameLabel = new Label("Username: " + user.getUsername());
        usernameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #555555;");

        Label emailLabel = new Label("Email: " + user.getUser_email());
        emailLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #555555;");

        Label phoneLabel = new Label("Phone Number: " + user.getPhoneNb());
        phoneLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #555555;");

        Label roleLabel = new Label("Role: " + user.getRole());
        roleLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #555555;");

        userContentSection.getChildren().addAll(usernameLabel, emailLabel, phoneLabel, roleLabel);
        userContentSection.setPadding(new Insets(20, 0, 0, 720));
        userContentSection.setSpacing(10);

        // User profile image
        ImageView photoView = new ImageView();
        photoView.setFitWidth(200);
        photoView.setFitHeight(200);
        photoView.setStyle("-fx-border-color: #C8F0FF; -fx-border-width: 3px; -fx-border-radius: 10px;");
        if (user.getUser_photoID() != null && user.getUser_photoID().length > 0) {
            photoView.setImage(new Image(new ByteArrayInputStream(user.getUser_photoID())));
        } else {
            photoView.setImage(new Image(getClass().getResource("/images/Profile Pic.png").toExternalForm()));
        }

        userInfoSection.getChildren().addAll(title, photoView, userContentSection);
        userInfoSection.setPadding(new Insets(100, 0, 0, 0));
        layout.setCenter(userInfoSection);  // Add user info section to layout


        // Bottom buttons section
        HBox buttonSection = new HBox(20);  // Horizontal box layout with spacing of 20px
        buttonSection.setPadding(new Insets(0, 0, 40, 0));  // Padding for spacing
        buttonSection.setAlignment(Pos.CENTER);  // Align buttons in the center

        // Update Info Button
        Button updateButton = new Button("Update Info");
        updateButton.setStyle("-fx-font-size: 20; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 50; -fx-cursor: hand");
        updateButton.setPrefWidth(200);  // Set button width
        updateButton.setPadding(new Insets(5));  // Add padding around the text

        // Hover effects for Update Info button
        updateButton.setOnMouseEntered(e -> updateButton.setStyle(updateButton.getStyle().replace(
                "-fx-text-fill: black;", "-fx-text-fill: white;")));  // Change text color on hover
        updateButton.setOnMouseExited(e -> updateButton.setStyle(updateButton.getStyle().replace(
                "-fx-text-fill: white;", "-fx-text-fill: black;")));  // Revert text color when hover ends

        // Action to show the update form
        updateButton.setOnAction(event -> showUpdateForm(primaryStage));
        buttonSection.getChildren().add(updateButton);  // Add button to the button section
        buttonSection.setPadding(new Insets(0, 0, 200, 0));  // Add bottom padding
        layout.setBottom(buttonSection);  // Add button section to the bottom of the layout

        // Background Image setup
        ImageView backgroundImageView = new ImageView(new Image(getClass().getResource("/images/UserProfile.png").toExternalForm()));
        backgroundImageView.setPreserveRatio(false);  // Do not preserve aspect ratio
        backgroundImageView.setFitWidth(Screen.getPrimary().getBounds().getWidth());  // Fit to screen width
        backgroundImageView.setFitHeight(Screen.getPrimary().getBounds().getHeight() - 80);  // Fit to adjusted height

        // Ensure the layout does not hide the background
        layout.setStyle("-fx-background-color: transparent;");

        // StackPane for layering background and layout
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, layout);  // Add background and layout to the StackPane

        // Ensure the background image stays at the back
        backgroundImageView.toBack();

        // Scene setup
        Scene scene = new Scene(root, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);  // Set the scene on the primary stage
        primaryStage.setResizable(false);  // Disable resizing of the window
        primaryStage.show();  // Display the scene
    }

    // Method to show the Update Info form
    public void showUpdateForm(Stage ownerStage) {
        VBox updateForm = new VBox(20);  // Vertical box layout with spacing of 20px
        updateForm.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C8F0FF; -fx-border-width: 3px;");
        updateForm.setPadding(new Insets(20));  // Add padding around the form
        updateForm.setAlignment(Pos.CENTER);  // Align content in the center

        // Title for the update form
        Label updateTitle = new Label("Update Info");
        updateTitle.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");

        // Editable fields
        TextField emailField = new TextField(currentUser.getUser_email());
        emailField.setPromptText("Enter your new email.");
        emailField.setStyle("-fx-border-color: #C8F0FF; -fx-border-width: 2px; -fx-padding: 10px; " +
                "-fx-border-radius: 20px; -fx-background-radius: 20px");
        emailField.setPrefWidth(5);

        TextField phoneField = new TextField(currentUser.getPhoneNb());
        phoneField.setPromptText("Enter your new phone number.");
        phoneField.setStyle("-fx-border-color: #C8F0FF; -fx-border-width: 2px; -fx-padding: 10px;" +
                "-fx-border-radius: 20px; -fx-background-radius: 20px");
        phoneField.setPrefWidth(5);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password or old password if it is the same.");
        passwordField.setStyle("-fx-border-color: #C8F0FF; -fx-border-width: 2px; -fx-padding: 10px;" +
                "-fx-border-radius: 20px; -fx-background-radius: 20px");
        passwordField.setPrefWidth(5);

        // Save Changes Button
        Button submitButton = new Button("Save Changes");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold;" +
                " -fx-border-radius: 10px; -fx-font-size: 15px; -fx-cursor: hand");
        submitButton.setPrefWidth(120);  // Button width
        submitButton.setPrefHeight(38);  // Button height

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold;" +
                " -fx-border-radius: 10px; -fx-font-size: 15px; -fx-cursor: hand");
        backButton.setPrefWidth(120);  // Button width
        backButton.setPrefHeight(35);  // Button height

        // Return to user profile when Back button is clicked
        backButton.setOnAction(event -> {
            ((Stage) backButton.getScene().getWindow()).close();  // Close the update form
            UserController userController = new UserController();
            userController.showUserProfile(ownerStage, currentUser);  // Show user profile
        });

        // Save changes to user information
        submitButton.setOnAction(event -> {
            currentUser.setUser_email(emailField.getText());
            currentUser.setPhoneNb(phoneField.getText());

            if (passwordField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("All fields must be filled.");
                alert.showAndWait();  // Display error alert
            } else {
                currentUser.setUser_pass(passwordField.getText());
            }

            try {
                if (!passwordField.getText().isEmpty()) {
                    UserService userService = new UserService();
                    userService.updateUser(currentUser);  // Update user information

                    userSession.put("user", currentUser);  // Update session information

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Your information has been updated.");
                    alert.showAndWait();  // Display success alert

                    ((Stage) submitButton.getScene().getWindow()).close();  // Close update form
                    UserController userController = new UserController();
                    userController.showUserProfile(ownerStage, currentUser);  // Return to user profile
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Unable to update information.");
                alert.showAndWait();  // Display error alert
            }
        });

        updateForm.getChildren().addAll(updateTitle, emailField, phoneField, passwordField, submitButton, backButton);  // Add components to the form

        Scene updateScene = new Scene(updateForm, 500, 500);  // Create the scene

        Stage updateStage = new Stage();
        updateStage.setTitle("Update Your Info");
        updateStage.setScene(updateScene);
        updateStage.initModality(Modality.APPLICATION_MODAL);  // Block other windows
        updateStage.initOwner(ownerStage);  // Set owner stage
        updateStage.setResizable(false);  // Disable resizing
        updateStage.show();  // Display the update form
    }
}