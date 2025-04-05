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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class PollController {

    private static PollModel currentPoll;
    private static UserModel currentUser;

    public void showPollDetails(Stage primaryStage, PollModel poll, UserModel user) throws Exception {
        currentPoll = poll;
        currentUser = user;

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
                LandingPageController landingPageController = new LandingPageController();
                UserController.userSession.put("user", user);
                landingPageController.showLandingPage(primaryStage, UserController.userSession);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Align logo to the left and back button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); // Make the spacer take up all available space between the logo and back button

        topBar.getChildren().addAll(logo, spacer, backButton);
        layout.setTop(topBar);

        // Poll Info Section
        VBox pollInfoSection = new VBox(15);
        pollInfoSection.setPadding(new Insets(40));
        pollInfoSection.setAlignment(Pos.TOP_CENTER);
        pollInfoSection.setPrefWidth(300);

        Label pollTitleLabel = new Label("Poll Title: " + poll.getTitle());
        pollTitleLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label pollDescriptionLabel = new Label("Description: " + poll.getDescription());
        pollDescriptionLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: #555555;");

        // Poll status
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        Timestamp endDate = (Timestamp) poll.getEnd_date();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        long daysLeft = ChronoUnit.DAYS.between(today, endLocalDate);

        // Set status text and style
        if (daysLeft > 0) {
            statusLabel.setText("Status: Active • " + daysLeft + " day(s) left");
            statusLabel.setStyle("-fx-font-size:25px; -fx-font-weight: bold; -fx-text-fill: Green;");
        } else if (daysLeft == 0) {
            statusLabel.setText("Status: Last day to vote!");
            statusLabel.setStyle("-fx-font-size:25px; -fx-font-weight: bold; -fx-text-fill: Orange;");
        } else {
            statusLabel.setText("Status: Completed");
            statusLabel.setStyle("-fx-font-size:25px; -fx-font-weight: bold; -fx-text-fill: Red;");
        }

        // Pie Chart for Candidate Votes
        Label chartTitle = new Label("Candidate Votes Breakdown:");
        chartTitle.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Smaller space between chart title and the Pie Chart
        Region space = new Region();
        space.setPrefHeight(2); // Reduced height for smaller gap

        ResultService resultService = new ResultService();
        List<CandidateModel> candidates = resultService.getCandidatesWithVotesByPollID(poll.getPoll_ID());

        PieChart pieChart = new PieChart();
        for (CandidateModel candidate : candidates) {
            PieChart.Data slice = new PieChart.Data(candidate.getName(), candidate.getVotePercentage());
            pieChart.getData().add(slice);
        }
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setPrefWidth(400);
        pieChart.setPrefHeight(400);

        // Winner Display if Poll is Completed
        Label winnerLabel = null;
        if (daysLeft < 0) {
            winnerLabel = new Label("Winner: " + resultService.getWinnerByPollID(poll.getPoll_ID()).getName());
            winnerLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: Black;");
        }

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

        // Add components to the info section
        pollInfoSection.getChildren().addAll(pollTitleLabel, pollDescriptionLabel, statusLabel, chartTitle, space, pieChart);
        if (daysLeft >= 0) { // Only show Vote button if poll is active
            pollInfoSection.getChildren().add(voteButton);
        } else { // Show winner if poll is completed
            pollInfoSection.getChildren().add(winnerLabel);
        }

        layout.setCenter(pollInfoSection);

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