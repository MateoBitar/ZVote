package com.example.zvote.Controllers;  // Package declaration, specifies the namespace


// Importing necessary classes for UI and functionality
import com.example.zvote.Models.*;
import com.example.zvote.Services.PollService;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;


public class AdminPollController {

    // Method to display the "Create Poll" form
    public void showCreatePoll(Stage primaryStage, UserModel user) throws Exception {

        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");


        // Top bar configuration
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Add shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);
        topBar.setEffect(shadow);

        // Logo
        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        // Back button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;" +
                "-fx-cursor: hand");
        backButton.setOnAction(event -> {
            try {
                AdminLandingPageController adminLandingPageController = new AdminLandingPageController();
                UserController.userSession.put("user", user);
                adminLandingPageController.showAdminLandingPage(primaryStage, UserController.userSession);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Align elements within the top bar
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, spacer, backButton);
        layout.setTop(topBar);


        // Create the poll form
        GridPane createPollForm = new GridPane();
        createPollForm.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C8F0FF; -fx-border-width: 3px;");
        createPollForm.setPadding(new Insets(20));
        createPollForm.setAlignment(Pos.TOP_CENTER);
        createPollForm.setHgap(20);
        createPollForm.setVgap(20);

        // Form Title
        Label title = new Label("Create Poll");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
        title.setPadding(new Insets(0, 0, 0, 10));
        createPollForm.add(title, 0, 0);
        GridPane.setColumnSpan(title, 2);
        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setValignment(title, VPos.CENTER);


        // Poll fields
        Label pollTitleLabel = new Label("Poll Title:");
        pollTitleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        TextField pollTitleField = new TextField();
        pollTitleField.setPromptText("Poll Title");
        pollTitleField.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px;");
        createPollForm.add(pollTitleLabel, 0, 1);
        createPollForm.add(pollTitleField, 1, 1);


        Label pollDescriptionLabel = new Label("Poll Description:");
        pollDescriptionLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        TextField pollDescriptionField = new TextField();
        pollDescriptionField.setPromptText("Enter Poll Description");
        pollDescriptionField.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px;");
        createPollForm.add(pollDescriptionLabel, 0, 2);
        createPollForm.add(pollDescriptionField, 1, 2);


        Label pollStartDateLabel = new Label("Poll Start Date:");
        pollStartDateLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px; -fx-background-color: white");
        createPollForm.add(pollStartDateLabel, 0, 3);
        createPollForm.add(startDatePicker, 1, 3);


        Label pollEndDateLabel = new Label("Poll End Date:");
        pollEndDateLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setStyle("-fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-color: #C8F0FF; -fx-padding: 8px; -fx-border-width: 3px; -fx-background-color: white");
        createPollForm.add(pollEndDateLabel, 0, 4);
        createPollForm.add(endDatePicker, 1, 4);


        // Submit button
        Button submitButton = new Button("Choose Candidates");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold;" +
                "-fx-border-radius: 10px; -fx-font-size: 15px; -fx-cursor: hand");
        submitButton.setPrefWidth(160);
        submitButton.setPrefHeight(38);
        submitButton.setOnAction(event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            LocalDate today = LocalDate.now();

            if (pollTitleField.getText().isBlank() || pollDescriptionField.getText().isBlank() || startDate == null || endDate == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Incomplete Form");
                alert.setContentText("Please ensure all fields are filled.");
                alert.showAndWait();
                return;
            }

            if (endDate.isBefore(startDate) || endDate.isBefore(today)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Invalid Dates");
                alert.setContentText("Ensure dates are valid and not in the past.");
                alert.showAndWait();
                return;
            }


            PollModel newPoll = new PollModel(pollTitleField.getText(), pollDescriptionField.getText(),
                        Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), user.getUser_ID());

            try {
                newPoll.setPoll_ID(new PollService().addPoll(newPoll));
                new CandidateController().displayCandidates(primaryStage, newPoll.getPoll_ID());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Title Unavailable");
                alert.setContentText("Choose a different title.");
                alert.showAndWait();
            }
        });
        createPollForm.add(submitButton, 0, 5);
        GridPane.setColumnSpan(submitButton, 2);
        GridPane.setHalignment(submitButton, HPos.CENTER);


        // Add form to the layout and set up the scene
        layout.setCenter(createPollForm);
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}