package com.example.zvote.Controllers;

import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Models.PollModel;
import com.example.zvote.Models.UserModel;
import com.example.zvote.Models.VoteModel;
import com.example.zvote.Services.CandidateService;
import com.example.zvote.Services.ResultService;
import com.example.zvote.Services.VoteService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

public class PollController {

    private static PollModel currentPoll;
    private static UserModel currentUser;

    public void showPollDetails(Stage primaryStage, PollModel poll, UserModel user) throws Exception {
        currentPoll = poll;
        currentUser = user;

        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");

        // Poll Info Section
        VBox pollInfoSection = new VBox(20);
        pollInfoSection.setPadding(new Insets(40));
        pollInfoSection.setAlignment(Pos.TOP_CENTER); // Center alignment
        pollInfoSection.setPrefWidth(300); // Approx. 25% of the total width

        Label pollTitleLabel = new Label("Poll Title: " + poll.getTitle());
        pollTitleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label pollDescriptionLabel = new Label("Description: " + poll.getDescription());
        pollDescriptionLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #555555;");

        Label pollStatusLabel = new Label("Status: Active");
        pollStatusLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");

        // Pie Chart for Candidate Votes
        Label chartTitle = new Label("Candidate Votes Breakdown:");
        chartTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        ResultService resultService = new ResultService();
        List<CandidateModel> candidates = resultService.getCandidatesWithVotesByPollID(poll.getPoll_ID());

        PieChart pieChart = new PieChart();
        for (CandidateModel candidate : candidates) {
            PieChart.Data slice = new PieChart.Data(candidate.getName(), candidate.getVotePercentage());
            pieChart.getData().add(slice);
        }
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setPrefWidth(400); // Set the size of the Pie Chart

        // Vote Button
        Button voteButton = new Button("Vote");
        voteButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 18px;");
        voteButton.setOnAction(e -> {
            try {
                showVotingSection(primaryStage, poll, user);
            } catch (Exception exe) {
                throw new RuntimeException(exe);
            }
        });

        pollInfoSection.getChildren().addAll(pollTitleLabel, pollDescriptionLabel, pollStatusLabel, chartTitle, pieChart, voteButton);
        layout.setCenter(pollInfoSection); // Adjust placement to the center of the page

        // Scene setup
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showVotingSection(Stage primaryStage, PollModel poll, UserModel user) throws Exception {
        VBox votingLayout = new VBox(20);
        votingLayout.setPadding(new Insets(20));
        votingLayout.setAlignment(Pos.CENTER);

        Label candidateLabel = new Label("Vote for your Candidate");
        candidateLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        // Candidates Section
        CandidateService candidateService = new CandidateService();
        List<CandidateModel> candidates = candidateService.getCandidatesByPollID(poll.getPoll_ID());
        ToggleGroup candidatesGroup = new ToggleGroup();
        VBox candidatesSection = new VBox(10);
        for (CandidateModel candidate : candidates) {
            RadioButton candidateButton = new RadioButton(candidate.getName());
            candidateButton.setToggleGroup(candidatesGroup);
            candidateButton.setUserData(candidate.getCandidate_ID());
            candidateButton.setStyle("-fx-font-size: 18px;");
            candidatesSection.getChildren().add(candidateButton);
        }

        Button submitButton = new Button("Submit Vote");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px;");
        submitButton.setOnAction(event -> {
            RadioButton selectedCandidate = (RadioButton) candidatesGroup.getSelectedToggle();
            if (selectedCandidate == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please select a candidate before submitting your vote.");
                alert.showAndWait();
            } else {
                try {
                    int pollId = poll.getPoll_ID();
                    int candidateId = (int) selectedCandidate.getUserData();
                    int voterId = user.getUser_ID();
                    int timestamp = (int) (System.currentTimeMillis() / 1000);

                    VoteModel vote = new VoteModel(pollId, candidateId, voterId, timestamp);
                    VoteService voteService = new VoteService();
                    voteService.addVote(vote);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Your vote has been submitted successfully!");
                    alert.showAndWait();
                    primaryStage.close();

                    // Optionally return to the main page or poll list
                    LandingPageController landingPageController = new LandingPageController();
                    landingPageController.showLandingPage(primaryStage, UserController.userSession);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("An error occurred while submitting your vote. Please try again.");
                    alert.showAndWait();
                }
            }
        });

        votingLayout.getChildren().addAll(candidateLabel, candidatesSection, submitButton);

        // Scene setup
        Scene scene = new Scene(votingLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}