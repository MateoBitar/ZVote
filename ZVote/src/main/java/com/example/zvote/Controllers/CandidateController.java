package com.example.zvote.Controllers;

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
import javafx.stage.StageStyle;

import java.util.*;

public class CandidateController {

    public void displayCandidates(Stage stage, int pollId) throws Exception {
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
        topBar.getChildren().add(logo);

        Label label = new Label("Choose the candidates for the poll created:");
        label.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Fetch all candidates from the CandidateService
        ObservableList<CandidateModel> candidates = FXCollections.observableArrayList(
                new CandidateService().getAllCandidates());

        TableView<CandidateModel> tableView = new TableView<>();
        tableView.setItems(candidates);

        // Map to store CheckBoxes associated with each CandidateModel
        Map<CandidateModel, CheckBox> checkBoxMap = new HashMap<>();

        // Candidate Name Column
        TableColumn<CandidateModel, String> nameColumn = new TableColumn<>("Candidate Name");
        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        // CheckBox Column for selection
        TableColumn<CandidateModel, CheckBox> checkBoxColumn = new TableColumn<>("Select");
        checkBoxColumn.setCellValueFactory(data -> {
            CheckBox checkBox = new CheckBox();
            checkBoxMap.put(data.getValue(), checkBox); // Associate the CheckBox with the CandidateModel
            return new javafx.beans.property.SimpleObjectProperty<>(checkBox);
        });

        tableView.getColumns().addAll(nameColumn, checkBoxColumn);

        ResultService resultService = new ResultService();

        // Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;");
        submitButton.setOnAction(event -> {
            // Create ResultModel objects for selected candidates
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Candidates linked to the poll successfully!");
            alert.showAndWait();

            try {
                new AdminLandingPageController().showAdminLandingPage(stage, UserController.userSession);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        VBox vBox = new VBox();
        vBox.setSpacing(20);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(tableView);
        scrollPane.setFitToWidth(true); // Ensures the table width matches the ScrollPane's width
        scrollPane.setStyle("-fx-background: transparent;");

        vBox.getChildren().addAll(topBar, label, scrollPane, submitButton);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setStyle("-fx-background-color: white;");

        Scene scene = new Scene(vBox, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        stage.setScene(scene);
        stage.show();
    }
}