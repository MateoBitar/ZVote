package com.example.zvote.Controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SignInController {
    public static void showSignInWindow() {
        Stage signInStage = new Stage();
        signInStage.initModality(Modality.APPLICATION_MODAL);
        signInStage.setTitle("Sign In - ZVote");
        signInStage.setResizable(false);

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setStyle("-fx-background-color: #C8F0FF;");

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
                        "-fx-border-color: lightgray; " +
                        "-fx-padding: 8px;"
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
                        "-fx-border-color: lightgray; " +
                        "-fx-padding: 8px;"
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
                        "-fx-border-color: lightgray; " +
                        "-fx-padding: 8px;"
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
        countryCodeDropdown.setStyle("-fx-background-color: white; -fx-border-radius: 50;");

        // To ensure the selected item in the combo box is just the code
        countryCodeDropdown.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    // Show only the country code in the combo box button
                    setText(item.split(" ")[0]);
                }
            }
        });


        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        phoneField.setStyle(
                "-fx-background-radius: 20px; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-border-color: lightgray; " +
                        "-fx-padding: 8px;"
        );
        phoneField.setPrefWidth(250);

        phoneBox.getChildren().addAll(countryCodeDropdown, phoneField);

        // Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setStyle(
                "-fx-background-color: #87CEEB; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-radius: 30px; " +
                        "-fx-background-radius: 30px; " +
                        "-fx-padding: 10px 30px;"
        );

        // Centering the button
        HBox buttonContainer = new HBox(submitButton);
        buttonContainer.setAlignment(Pos.CENTER);

        // Add all components to the layout
        layout.getChildren().addAll(
                title,
                usernameLabel, usernameField,
                emailLabel, emailField,
                passwordLabel, passwordField,
                phoneLabel, phoneBox,
                buttonContainer
        );

        Scene scene = new Scene(layout, 350, 500);
        signInStage.setScene(scene);
        signInStage.showAndWait();
    }
}
