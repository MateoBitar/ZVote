package com.example.zvote.Controllers;

import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Models.UserModel;
import com.example.zvote.Services.CandidateService;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

public class CreateCandidateController {
    public void showCreateCandidateForm(Stage primaryStage, UserModel user) throws Exception {
        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");

        // Top Bar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Create a shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);

        // Apply shadow to topBar
        topBar.setEffect(shadow);

        // Logo on the left
        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        // Back Button on the right
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;" +
                "-fx-cursor: hand");
        backButton.setOnAction(event -> {
            try {
                UserController.userSession.put("user", user);
                new CandidatesController().showCandidatesPage(primaryStage, UserController.userSession);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Align logo to the left and back button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); // Make the spacer take up all available space between the logo and back button

        topBar.getChildren().addAll(logo, spacer, backButton);
        layout.setTop(topBar);

        // Create Form
        GridPane createCandidateForm = new GridPane();
        createCandidateForm.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C8F0FF; -fx-border-width: 3px;");
        createCandidateForm.setPadding(new Insets(20));
        createCandidateForm.setAlignment(Pos.TOP_CENTER);
        createCandidateForm.setHgap(20);
        createCandidateForm.setVgap(20);

        Label title = new Label("Create Candidate");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
        title.setPadding(new Insets(0, 0, 0, 10));
        createCandidateForm.add(title, 0, 0);
        GridPane.setColumnSpan(title, 2);
        GridPane.setHalignment(title, HPos.CENTER);

        // Candidate Fields
        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        TextField nameField = new TextField();
        nameField.setPromptText("Candidate Name");
        nameField.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px; -fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px;");
        createCandidateForm.add(nameLabel, 0, 1);
        createCandidateForm.add(nameField, 1, 1);

        Label bioLabel = new Label("Bio:");
        bioLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        TextField bioField = new TextField();
        bioField.setPromptText("Enter Candidate Bio");
        bioField.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px; -fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px;");
        createCandidateForm.add(bioLabel, 0, 2);
        createCandidateForm.add(bioField, 1, 2);

        // FileChooser for Photo
        FileChooser fileChooser = new FileChooser();
        Button uploadPhotoButton = new Button("Upload Candidate Photo");
        uploadPhotoButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold; -fx-border-radius: 30px;" +
                " -fx-background-radius: 30px; -fx-padding: 5px 10px; -fx-cursor: hand");
        final File[] selectedPhoto = {null}; // To store the chosen photo
        uploadPhotoButton.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                selectedPhoto[0] = file; // Save the selected file
                uploadPhotoButton.setText("Photo Uploaded");
            } else {
                uploadPhotoButton.setText("Upload Candidate Photo");
            }
        });
        createCandidateForm.add(uploadPhotoButton, 0, 3);

        Button submitButton = new Button("Create Candidate");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold; -fx-border-radius: 10px;" +
                " -fx-font-size: 15px; -fx-cursor: hand");
        submitButton.setPrefWidth(160);
        submitButton.setPrefHeight(38);
        createCandidateForm.add(submitButton, 0, 4);
        GridPane.setColumnSpan(submitButton, 2);
        GridPane.setHalignment(submitButton, HPos.CENTER);

        submitButton.setOnAction(event -> {
            try {
                // Check if a photo was selected, otherwise use an empty byte array
                byte[] photoID = (selectedPhoto[0] != null) ? Files.readAllBytes(selectedPhoto[0].toPath()) : new byte[0];

                // Create the candidate object
                CandidateModel candidate = new CandidateModel(nameField.getText(), photoID, bioField.getText());

                // Check if the name is already taken
                CandidateService candidateService = new CandidateService();
                if (candidateService.isNameTaken(candidate.getName())) {
                    // Show an error alert if the name is already taken
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "A candidate with this name already exists.\n" +
                            "Modify the name a bit.");
                    errorAlert.showAndWait();
                    return; // Stop further execution
                }

                // Add the candidate if the name is unique
                candidateService.addCandidate(candidate);

                // Show a success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Candidate created successfully!");
                successAlert.showAndWait();
                new CandidatesController().showCandidatesPage(primaryStage, UserController.userSession);
            } catch (IOException e) {
                // Handle photo file reading issues
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while reading the photo file: " + e.getMessage());
                errorAlert.showAndWait();
            } catch (SQLException e) {
                // Handle database issues
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred while adding the candidate to the database: " + e.getMessage());
                errorAlert.showAndWait();
            } catch (Exception e) {
                // Handle unexpected errors
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An unexpected error occurred: " + e.getMessage());
                errorAlert.showAndWait();
            }
        });

        layout.setCenter(createCandidateForm);
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}