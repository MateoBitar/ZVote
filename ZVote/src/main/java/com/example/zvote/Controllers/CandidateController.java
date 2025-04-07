package com.example.zvote.Controllers;  // Package declaration, specifies the namespace


// Importing necessary classes for functionality
import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Models.ResultModel;
import com.example.zvote.Services.CandidateService;
import com.example.zvote.Services.ResultService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;


public class CandidateController {

    // Method to display a list of candidates for a specific poll
    public void displayCandidates(Stage stage, int pollId) throws Exception {

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
        topBar.getChildren().add(logo);


        // Instructional Label
        Label label = new Label("Choose the candidates for the poll created:");
        label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #333333;");


        // Fetch all candidates from CandidateService
        ObservableList<CandidateModel> candidates = FXCollections.observableArrayList(
                new CandidateService().getAllCandidates()
        );


        // TableView to display candidates
        TableView<CandidateModel> tableView = new TableView<>();
        tableView.setStyle("-fx-background-color: #C8F0FF; -fx-font-size: 16px;");
        tableView.setItems(candidates);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(600);


        // Map to store CheckBoxes associated with each candidate
        Map<CandidateModel, CheckBox> checkBoxMap = new HashMap<>();


        // Candidate Name Column
        TableColumn<CandidateModel, String> nameColumn = new TableColumn<>("Candidate Name");
        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        nameColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black; -fx-font-weight: bold;");


        // CheckBox Column for selection
        TableColumn<CandidateModel, CheckBox> checkBoxColumn = new TableColumn<>("Select");
        checkBoxColumn.setCellValueFactory(data -> {
            CheckBox checkBox = new CheckBox();
            checkBoxMap.put(data.getValue(), checkBox);  // Associate CheckBox with CandidateModel
            return new javafx.beans.property.SimpleObjectProperty<>(checkBox);
        });
        checkBoxColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: black; -fx-font-weight: bold;");


        // Add columns to the TableView
        tableView.getColumns().addAll(nameColumn, checkBoxColumn);


        // ResultService for handling poll-candidate relationships
        ResultService resultService = new ResultService();


        // Submit button to save selected candidates
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;" +
                "-fx-cursor: hand");
        submitButton.setOnAction(event -> {
            // Iterate through selected candidates and create ResultModel objects
            for (CandidateModel candidate : candidates) {
                CheckBox checkBox = checkBoxMap.get(candidate);
                if (checkBox != null && checkBox.isSelected()) {
                    try {
                        resultService.addResult(new ResultModel(new Date(), candidate.getCandidate_ID(), pollId));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            // Confirmation alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Candidates linked to the poll successfully!");
            alert.showAndWait();

            try {
                new AdminLandingPageController().showAdminLandingPage(stage, UserController.userSession);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        // Layout configuration
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.getChildren().addAll(topBar, label, tableView, submitButton);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-background-color: white;");


        // Scene setup
        Scene scene = new Scene(vBox, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        stage.setScene(scene);
        stage.show();
    }
}