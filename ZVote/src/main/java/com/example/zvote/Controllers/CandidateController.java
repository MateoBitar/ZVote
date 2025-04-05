package com.example.zvote.Controllers;

import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Models.ResultModel;
import com.example.zvote.Services.CandidateService;
import com.example.zvote.Services.ResultService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.*;

public class CandidateController {

    public void displayCandidates(Stage stage, int pollId) throws Exception {
        Label label = new Label("Choose the candidates for the poll created:");

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

        // Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(event -> {
            // Create ResultModel objects for selected candidates
            List<ResultModel> results = new ArrayList<>();
            for (CandidateModel candidate : candidates) {
                CheckBox checkBox = checkBoxMap.get(candidate);
                if (checkBox != null && checkBox.isSelected()) {
                    results.add(new ResultModel(new Date(), 0.00, candidate.getCandidate_ID(), pollId));
                }
            }
            saveResultsToDatabase(results);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Candidates linked to the poll successfully!");
            alert.showAndWait();

            try {
                new AdminLandingPageController().showAdminLandingPage(stage, UserController.userSession);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        VBox vBox = new VBox(label, tableView, submitButton);
        Scene scene = new Scene(vBox, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        stage.setScene(scene);
        stage.show();
    }

    // Replace this with actual database saving logic
    private void saveResultsToDatabase(List<ResultModel> results) {
        try {
            for (ResultModel result : results) {
                new ResultService().addResult(result); // Use your existing function to add each ResultModel to the database
            }
            System.out.println("All results successfully saved to the database.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to save results to the database: " + e.getMessage());
        }

    }
}