package com.example.zvote.Controllers;  // Package declaration, specifies the namespace


// Importing necessary classes for handling models, services, and UI functionality
import com.example.zvote.Models.*;
import com.example.zvote.Services.CandidateService;
import com.example.zvote.Services.PollService;
import com.example.zvote.Services.ResultService;
import com.example.zvote.Services.VoteService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import static com.example.zvote.Controllers.SignInController.showAlert;


public class PollController {

    // Method to display poll details
    public void showPollDetails(Stage primaryStage, PollModel poll, UserModel user) throws Exception {

        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: transparent;");


        // Top Bar Configuration
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

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;" +
                " -fx-cursor: hand");
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
        HBox.setHgrow(spacer, Priority.ALWAYS);
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


        // Poll Status Label
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        // Convert poll start and end dates to LocalDate
        Timestamp startDate = (Timestamp) poll.getStart_date();
        Timestamp endDate = (Timestamp) poll.getEnd_date();
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        long daysLeft = ChronoUnit.DAYS.between(today, endLocalDate);

        // Set status text based on the current date
        if (today.isBefore(startLocalDate)) {
            statusLabel.setText("Status: Inactive");
            statusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: Gray;");
        } else if (daysLeft > 0) {
            statusLabel.setText("Status: Active • " + daysLeft + " day(s) left");
            statusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: Green;");
        } else if (daysLeft == 0) {
            statusLabel.setText("Status: Last day to vote!");
            statusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: Orange;");
        } else {
            statusLabel.setText("Status: Completed");
            statusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: Red;");
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


        // Display Winner if Poll is Completed
        Label winnerLabel = null;
        if (daysLeft < 0) {
            winnerLabel = new Label("Winner: " + resultService.getWinnerByPollID(poll.getPoll_ID()).getName());
            winnerLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: Black;");
        }


        // Vote Button for Active Polls
        Button voteButton = new Button("Vote");
        voteButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 18px;" +
                "-fx-cursor: hand");
        voteButton.setOnAction(e -> {
            try {
                showVotingSection(primaryStage, poll, user);
            } catch (Exception exe) {
                throw new RuntimeException(exe);
            }
        });

        pollInfoSection.getChildren().addAll(pollTitleLabel, pollDescriptionLabel, statusLabel, chartTitle, space, pieChart);

        VoteService voteService = new VoteService();
        boolean hasVoted = voteService.hasUserVoted(user.getUser_ID(), poll.getPoll_ID());

        if (today.isBefore(startLocalDate)) {
            Label inactiveLabel = new Label("Voting Starts On • " + startLocalDate);
            inactiveLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: Black;");
            pollInfoSection.getChildren().add(inactiveLabel);
        } else if (daysLeft >= 0) {
            if (!hasVoted) {
                pollInfoSection.getChildren().add(voteButton);
            } else {
                Label votedLabel = new Label("You Voted Already!");
                votedLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: Black;");
                pollInfoSection.getChildren().add(votedLabel);
            }
        } else {
            pollInfoSection.getChildren().add(winnerLabel);
        }

        layout.setCenter(pollInfoSection);


        // Scene Setup
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    private void showVotingSection(Stage primaryStage, PollModel poll, UserModel user) throws Exception {
        // Main voting layout
        VBox votingLayout = new VBox(30);  // Vertical layout with spacing of 30px
        votingLayout.setAlignment(Pos.TOP_CENTER);  // Align items at the top and center


        // Top Bar Configuration
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setMaxWidth(Double.MAX_VALUE);  // Ensure the top bar spans the full width


        // Add shadow effect to the top bar
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);
        topBar.setEffect(shadow);


        // Logo
        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));


        // Back Button for returning to poll details
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;" +
                " -fx-cursor: hand");
        backButton.setOnAction(event -> {
            try {
                showPollDetails(primaryStage, poll, user);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        // Align logo to the left and back button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, spacer, backButton);


        // Candidate Label
        Label candidateLabel = new Label("Vote for your Candidate");
        candidateLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");


        // Candidates Section
        CandidateService candidateService = new CandidateService();
        List<CandidateModel> candidates = candidateService.getCandidatesByPollID(poll.getPoll_ID());
        ToggleGroup candidatesGroup = new ToggleGroup();  // Group for RadioButtons to ensure only one is selected


        VBox candidatesSection = new VBox(10);  // Vertical layout with spacing of 10px
        candidatesSection.setAlignment(Pos.CENTER);
        for (CandidateModel candidate : candidates) {
            RadioButton candidateButton = new RadioButton(candidate.getName());
            candidateButton.setToggleGroup(candidatesGroup);
            candidateButton.setUserData(candidate.getCandidate_ID());  // Associate candidate ID with button
            candidateButton.setStyle("-fx-font-size: 20px; -fx-cursor: hand");
            candidatesSection.getChildren().add(candidateButton);
        }


        // Submit Button
        Button submitButton = new Button("Submit Vote");
        submitButton.setStyle("-fx-background-color: #C8F0FF; -fx-font-weight: bold; -fx-border-radius: 10px; -fx-font-size: 22px;" +
                " -fx-cursor: hand");
        submitButton.setOnAction(event -> {
            RadioButton selectedCandidate = (RadioButton) candidatesGroup.getSelectedToggle();

            if (selectedCandidate == null) {  // No candidate selected, submit as abstention
                try {
                    VoteModel abstainVote = new VoteModel(user.getUser_ID(), poll.getPoll_ID(), 1, 0);
                    new VoteService().addVote(abstainVote);

                    poll.setNbOfVotes(poll.getNbOfVotes() + 1);
                    poll.setNbOfAbstentions(poll.getNbOfAbstentions() + 1);
                    new PollService().updatePoll(poll);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Vote Submitted");
                    alert.setContentText("Your abstention vote has been successfully recorded.");
                    alert.showAndWait();

                    UserController.userSession.put("user", user);
                    new LandingPageController().showLandingPage(primaryStage, UserController.userSession);

                } catch (Exception e) {
                    handleVoteError(e);
                }
            } else {  // Candidate selected, submit vote for candidate
                try {
                    int pollId = poll.getPoll_ID();
                    int candidateId = (int) selectedCandidate.getUserData();
                    int voterId = user.getUser_ID();

                    VoteModel vote = new VoteModel(voterId, pollId, 0, candidateId);
                    new VoteService().addVote(vote);

                    ResultModel result = new ResultService().getResultByPollAndCandidateID(pollId, candidateId);
                    if (result == null) {
                        showAlert(Alert.AlertType.ERROR, "Failed to submit vote", "No result object found for this poll and candidate!");
                    } else {
                        result.setVotes_casted(result.getVotes_casted() + 1);
                        new ResultService().updateResult(result);

                        poll.setNbOfVotes(poll.getNbOfVotes() + 1);
                        new PollService().updatePoll(poll);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setContentText("Your vote has been submitted successfully!");
                        alert.showAndWait();

                        UserController.userSession.put("user", user);
                        new LandingPageController().showLandingPage(primaryStage, UserController.userSession);
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while submitting your vote. Please try again.");
                }
            }
        });


        // Add components to the voting layout
        votingLayout.getChildren().addAll(topBar, candidateLabel, candidatesSection, submitButton);


        // Background Image
        ImageView backgroundImageView = new ImageView(new Image(getClass().getResource("/images/VotePic.png").toExternalForm()));
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setFitWidth(Screen.getPrimary().getBounds().getWidth());
        backgroundImageView.setFitHeight(Screen.getPrimary().getBounds().getHeight() - 80);


        // Root Pane Setup
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundImageView, votingLayout);


        // Scene Setup
        Scene scene = new Scene(root, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    // Helper Method to Handle Vote Errors
    private void handleVoteError(Exception e) {
        if (e.getMessage().contains("User has already voted")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Duplicate Vote");
            alert.setContentText("You have already voted in this poll. Multiple votes are not allowed.");
            alert.showAndWait();
        } else {
            throw new RuntimeException(e);  // Handle other unexpected exceptions
        }
    }
}