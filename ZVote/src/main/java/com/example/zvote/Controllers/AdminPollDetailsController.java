package com.example.zvote.Controllers;  // Package declaration, specifies the namespace


// Importing necessary classes for UI and functionality
import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Models.PollModel;
import com.example.zvote.Services.ResultService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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


public class AdminPollDetailsController {

    // Method to display poll details for admins
    public void showAdminPollDetails(Stage primaryStage, PollModel poll) throws Exception {

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
                adminLandingPageController.showAdminLandingPage(primaryStage, UserController.userSession);
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


        // Poll status
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #000000;");

        // Convert poll start and end dates to LocalDate
        Timestamp startDate = (Timestamp) poll.getStart_date();
        Timestamp endDate = (Timestamp) poll.getEnd_date();
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        long daysLeft = ChronoUnit.DAYS.between(today, endLocalDate);

        // Set status text based on time
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


        // Winner Display if Poll is Completed
        Label winnerLabel = null;
        if (daysLeft < 0) {
            winnerLabel = new Label("Winner: " + resultService.getWinnerByPollID(poll.getPoll_ID()).getName());
            winnerLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: Black;");
            pollInfoSection.getChildren().add(winnerLabel);
        }

        pollInfoSection.getChildren().addAll(pollTitleLabel, pollDescriptionLabel, statusLabel, chartTitle, space, pieChart);
        layout.setCenter(pollInfoSection);


        // Scene setup
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}