package com.example.zvote.Controllers;

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
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import static com.example.zvote.Controllers.LandingPageController.animateMenu;

public class UserController {
    private static UserModel currentUser; // Holds the logged-in user info
    public static Map<String, Object> userSession = new HashMap<>();

    public void showUserProfile(Stage primaryStage, UserModel user) {
        currentUser = user;
        userSession.put("user", user);

        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");

        // Top Bar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);
        topBar.setEffect(shadow);

        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        HBox menu = new HBox(-10);

        // Polls Button
        Button pollIcon = new Button("\uD83D\uDCCB");
        pollIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 25; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        pollIcon.setPrefHeight(30);
        pollIcon.setPrefWidth(70);
        pollIcon.setTranslateX(150);
        pollIcon.setOnMouseEntered(e -> pollIcon.setStyle(pollIcon.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        pollIcon.setOnMouseExited(e -> pollIcon.setStyle(pollIcon.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));
        pollIcon.setOnAction(e -> {
            LandingPageController main = new LandingPageController();
            try {
                main.showLandingPage(primaryStage, userSession);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Profile Button
        Button profileIcon = new Button("\uD83D\uDC64");
        profileIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 30; -fx-background-color: #C8F0FF; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        profileIcon.setPrefHeight(30);
        profileIcon.setPrefWidth(70);
        profileIcon.setTranslateX(150);

        Label menuIcon = new Label("\u283F");
        menuIcon.setStyle("-fx-font-size: 24px; -fx-padding: 10px; -fx-cursor: hand;");
        menuIcon.setOnMouseClicked(e -> animateMenu(pollIcon, profileIcon));

        menu.getChildren().addAll(pollIcon, profileIcon, menuIcon);
        menu.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(menu, Priority.ALWAYS);

        topBar.getChildren().addAll(logo, menu);
        layout.setTop(topBar);

        // Center content
        VBox userInfoSection = new VBox(20);
        userInfoSection.setPadding(new Insets(20));
        userInfoSection.setAlignment(Pos.TOP_CENTER);

        VBox userContentSection = new VBox();
        userContentSection.setPadding(new Insets(20));
        userContentSection.setAlignment(Pos.CENTER_LEFT);

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

        ImageView photoView = new ImageView();
        photoView.setFitWidth(200);
        photoView.setFitHeight(200);
        photoView.setStyle("-fx-border-color: #C8F0FF; -fx-border-width: 3px; -fx-border-radius: 10px;");
        if (user.getUser_photoID() != null && user.getUser_photoID().length > 0) {
            photoView.setImage(new Image(new ByteArrayInputStream(user.getUser_photoID())));
        } else {
            photoView.setImage(new Image("/images/default-profile.png"));
        }

        userInfoSection.getChildren().addAll(title, photoView, userContentSection);
        userInfoSection.setPadding(new Insets(100, 0, 0, 0));
        layout.setCenter(userInfoSection);

        // Bottom buttons
        HBox buttonSection = new HBox(20);
        buttonSection.setPadding(new Insets(0, 0, 40, 0));
        buttonSection.setAlignment(Pos.CENTER);

        Button updateButton = new Button("Update Info");
        updateButton.setStyle("-fx-font-size: 20; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 50");
        updateButton.setPrefWidth(200);
        updateButton.setPadding(new Insets(5));
        updateButton.setOnMouseEntered(e -> updateButton.setStyle(updateButton.getStyle().replace(
                "-fx-text-fill: black;", "-fx-text-fill: white;")));
        updateButton.setOnMouseExited(e -> updateButton.setStyle(updateButton.getStyle().replace(
                "-fx-text-fill: white;", "-fx-text-fill: black;")));

        updateButton.setOnAction(event -> showUpdateForm(primaryStage));
        buttonSection.getChildren().add(updateButton);
        buttonSection.setPadding(new Insets(0, 0, 200, 0));
        layout.setBottom(buttonSection);

        // Background Image
        ImageView backgroundImageView = new ImageView(new Image(getClass().getResource("/images/UserProfile.jpg").toExternalForm()));
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setFitWidth(Screen.getPrimary().getBounds().getWidth());
        backgroundImageView.setFitHeight(Screen.getPrimary().getBounds().getHeight() - 80);

        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, layout);

        Scene scene = new Scene(root, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    private static void showUpdateForm(Stage ownerStage) {
        VBox updateForm = new VBox(20);
        updateForm.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C8F0FF; -fx-border-width: 3px;");
        updateForm.setPadding(new Insets(20));
        updateForm.setAlignment(Pos.CENTER);

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

        Button submitButton = new Button("Save Changes");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold;" +
                " -fx-border-radius: 10px; -fx-font-size: 15px;");
        submitButton.setPrefWidth(120);
        submitButton.setPrefHeight(38);

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold;" +
                " -fx-border-radius: 10px; -fx-font-size: 15px");
        backButton.setPrefWidth(120);
        backButton.setPrefHeight(35);

        // Back to user profile
        backButton.setOnAction(event -> {
            ((Stage) backButton.getScene().getWindow()).close();
            UserController userController = new UserController();
            userController.showUserProfile(ownerStage, currentUser);
        });

        submitButton.setOnAction(event -> {
            currentUser.setUser_email(emailField.getText());
            currentUser.setPhoneNb(phoneField.getText());

            if (passwordField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("All fields must be filled.");
                alert.showAndWait();
            } else {
                currentUser.setUser_pass(passwordField.getText());
            }

            try {
                if (!passwordField.getText().isEmpty()) {
                    UserService userService = new UserService();
                    userService.updateUser(currentUser);

                    // Update session
                    userSession.put("user", currentUser);


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Your information has been updated.");
                    alert.showAndWait();

                    ((Stage) submitButton.getScene().getWindow()).close();
                    UserController userController = new UserController();
                    userController.showUserProfile(ownerStage, currentUser);
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Unable to update information.");
                alert.showAndWait();
            }
        });

        updateForm.getChildren().addAll(updateTitle, emailField, phoneField, passwordField, submitButton, backButton);

        Scene updateScene = new Scene(updateForm, 500, 500);

        Stage updateStage = new Stage();
        updateStage.setTitle("Update Your Info");
        updateStage.setScene(updateScene);
        updateStage.initModality(Modality.APPLICATION_MODAL); // This makes it block other windows
        updateStage.initOwner(ownerStage); // Owner for modality context
        updateStage.setResizable(false);
        updateStage.show();
    }

}