package com.example.zvote.Controllers;

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
import javafx.stage.StageStyle;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class AdminPollController {

    public void showCreatePoll(Stage primaryStage, UserModel user) throws Exception {
        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");

        // topBar
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
        backButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;");
        backButton.setOnAction(event -> {
            try {
                AdminLandingPageController adminLandingPageController = new AdminLandingPageController();
                UserController.userSession.put("user", user);
                adminLandingPageController.showAdminLandingPage(primaryStage, UserController.userSession);
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
        GridPane createPollForm = new GridPane();
        createPollForm.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #C8F0FF; -fx-border-width: 3px;");
        createPollForm.setPadding(new Insets(20));
        createPollForm.setAlignment(Pos.TOP_CENTER);
        createPollForm.setHgap(20);
        createPollForm.setVgap(20);

        Label title = new Label("Create Poll");
        title.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
        title.setPadding(new Insets(0, 0, 0, 10));
        createPollForm.add(title, 0, 0);
        // Make the title span 2 columns
        GridPane.setColumnSpan(title, 2);
        GridPane.setHalignment(title, HPos.CENTER); // Center horizontally
        GridPane.setValignment(title, VPos.CENTER); // Center vertically



        // Poll fields
        Label pollTitleLabel = new Label("Poll Title:");
        pollTitleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        TextField pollTitleField = new TextField();
        pollTitleField.setPromptText("Poll Title");
        pollTitleField.setStyle("-fx-background-radius: 20px; " +
                "-fx-border-radius: 20px; " +
                "-fx-border-color: #C8F0FF; " +
                "-fx-padding: 8px;" +
                "-fx-border-width: 3px;");
        createPollForm.add(pollTitleLabel, 0, 1);
        createPollForm.add(pollTitleField, 1, 1);


        Label pollDescriptionLabel = new Label("Poll Description:");
        pollDescriptionLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        TextField pollDescriptionField = new TextField();
        pollDescriptionField.setPromptText("Enter Poll Description");
        pollDescriptionField.setStyle("-fx-background-radius: 20px; " +
                "-fx-border-radius: 20px; " +
                "-fx-border-color: #C8F0FF; " +
                "-fx-padding: 8px;" +
                "-fx-border-width: 3px;");
        createPollForm.add(pollDescriptionLabel, 0, 2);
        createPollForm.add(pollDescriptionField, 1, 2);


        Label pollStartDateLabel = new Label("Poll Start Date:");
        pollStartDateLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");
        DatePicker startdatePicker = new DatePicker();
        startdatePicker.setStyle("-fx-background-radius: 20px; " +
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-radius: 20px; " +
                "-fx-border-color: #C8F0FF; " +
                "-fx-padding: 8px;" +
                "-fx-border-width: 3px;");
        createPollForm.add(pollStartDateLabel, 0, 3);
        createPollForm.add(startdatePicker, 1, 3);


        Label pollEndDateLabel = new Label("Poll End Date:");
        pollEndDateLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setStyle("-fx-background-radius: 20px; " +
                "-fx-background-color: #FFFFFF;" +
                "-fx-border-radius: 20px; " +
                "-fx-border-color: #C8F0FF; " +
                "-fx-padding: 8px;" +
                "-fx-border-width: 3px;");
        createPollForm.add(pollEndDateLabel, 0, 4);
        createPollForm.add(endDatePicker, 1, 4);


        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-text-fill: black; -fx-font-weight: bold;" +
                " -fx-border-radius: 10px; -fx-font-size: 15px;");
        submitButton.setPrefWidth(120);
        submitButton.setPrefHeight(38);
        submitButton.setOnAction(event -> {
            LocalDate startDate = startdatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            LocalDate today = LocalDate.now(); // Get the current date

            // Validate if all fields are filled
            if (pollTitleField.getText().isBlank() || pollDescriptionField.getText().isBlank() || startDate == null || endDate == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing Fields");
                alert.setHeaderText("Incomplete Form");
                alert.setContentText("Please ensure all fields are filled.");
                alert.showAndWait();
                return;
            }

            if (endDate.isBefore(startDate)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Date Error");
                alert.setHeaderText("Invalid Dates");
                alert.setContentText("The end date cannot be before the start date. Please correct it.");
                alert.showAndWait();
                return;
            }

            if (endDate.isBefore(today)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Date Error");
                alert.setHeaderText("End Date in the Past");
                alert.setContentText("The end date cannot be in the past. Please select a valid date.");
                alert.showAndWait();
                return;
            }

            PollModel newPoll = new PollModel(pollTitleField.getText(), pollDescriptionField.getText(),
                    Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    user.getUser_ID());

            try {
                newPoll.setPoll_ID(new PollService().addPoll(newPoll));
                new CandidateController().displayCandidates(primaryStage, newPoll.getPoll_ID());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Title Error");
                alert.setHeaderText("Title Unavailable");
                alert.setContentText("Title already exists. Choose a different title.");
                alert.showAndWait();
            }
        });
        createPollForm.add(submitButton, 0, 5);

        // Make the submitButton span 2 columns
        GridPane.setColumnSpan(submitButton, 2);
        GridPane.setHalignment(submitButton, HPos.CENTER); // Center horizontally
        GridPane.setValignment(submitButton, VPos.CENTER); // Center vertically


        layout.setCenter(createPollForm);

        // Scene setup
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

