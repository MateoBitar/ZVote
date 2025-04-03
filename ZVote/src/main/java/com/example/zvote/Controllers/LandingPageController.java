package com.example.zvote.Controllers;

import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Models.PollModel;
import com.example.zvote.Services.CandidateService;
import com.example.zvote.Services.PollService;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class LandingPageController {
    private Stage primaryStage;
    private Map<String, Object> userSession;

    public static void showLandingPage(Stage primaryStage) throws Exception {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #FFFFFF");
        // topBar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10,10,10,40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER);

        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        HBox menu = new HBox(-10);

        // Polls Button
        Button pollIcon = new Button("\uD83D\uDCCB");
        pollIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 25; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        pollIcon.setPrefHeight(30);
        pollIcon.setPrefWidth(70);
        pollIcon.setTranslateX(150);
        pollIcon.setOnMouseEntered(e -> pollIcon.setStyle(pollIcon.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        pollIcon.setOnMouseExited(e -> pollIcon.setStyle(pollIcon.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));

        // Profile Button
        Button profileIcon = new Button("\uD83D\uDC64"); // Unicode for user icon
        profileIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 30; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        profileIcon.setPrefHeight(30);
        profileIcon.setPrefWidth(70);
        profileIcon.setTranslateX(150);
        profileIcon.setOnMouseEntered(e -> profileIcon.setStyle(profileIcon.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        profileIcon.setOnMouseExited(e -> profileIcon.setStyle(profileIcon.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));

        Label menuIcon = new Label("\u283F"); // Unicode for ☰ (menu icon)
        menuIcon.setStyle("-fx-font-size: 24px; -fx-padding: 10px; -fx-cursor: hand;");
        menuIcon.setOnMouseClicked(e -> animateMenu(pollIcon, profileIcon));

        menu.getChildren().addAll(pollIcon, profileIcon, menuIcon);
        menu.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(menu, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, menu);

        layout.setTop(topBar);

        // Poll Cards
        GridPane pollGrid = new GridPane();
        pollGrid.setHgap(30);
        pollGrid.setVgap(30);
        pollGrid.setAlignment(Pos.CENTER);

        PollService pollService = new PollService();
        try {
            List<PollModel> polls = pollService.getAllPolls();
            for (PollModel poll : polls) {
                VBox pollCard = new VBox();
                pollCard.setAlignment(Pos.TOP_CENTER);
                pollCard.setPadding(new Insets(10));
                pollCard.setStyle(
                        "-fx-background-color: #C8F0FF;" +
                                "-fx-border-radius: 10px; " +
                                "-fx-background-radius: 10px;" +
                                "-fx-border-width: 3px;" +
                                "-fx-border-color: #000000;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0.0, 3, 3);"
                );
                pollCard.setPrefSize(300, 400);

                Label pollLabel = new Label(poll.getTitle());
                pollLabel.setStyle(
                        "-fx-background-color: #FFFFFF;" +
                                "-fx-border-radius: 10px; " +
                                "-fx-background-radius: 10px;" +
                                "-fx-border-width: 3px;" +
                                "-fx-border-color: #000000;" +
                                "-fx-font-size: 30px;" +
                                "-fx-font-weight: bold;"
                );
                pollLabel.setAlignment(Pos.CENTER);
                pollLabel.setWrapText(true);
                pollLabel.setMaxWidth(250);

                // Fetch candidates for the poll
                CandidateService candidateService = new CandidateService();
                List<CandidateModel> candidates = candidateService.getCandidatesByPollID(poll.getPoll_ID());

                // Create a VBox to hold candidate names
                VBox candidatesBox = new VBox(5); // 5px spacing between candidate names
                candidatesBox.setAlignment(Pos.CENTER);

                // Add each candidate as a Label
                Label candidateLabel = new Label();
                for (CandidateModel candidate : candidates) {
                    candidateLabel = new Label(candidate.getName());
                    candidateLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal;");
                    candidatesBox.getChildren().add(candidateLabel);
                }

                // Add space between title and button
                Region spacer = new Region();
                VBox.setVgrow(spacer, Priority.ALWAYS);

                Button viewPollButton = new Button("View Poll");
                viewPollButton.setStyle(
                        "-fx-background-color: #C8F0FF; " +
                                "-fx-text-fill: black; " +
                                "-fx-border-radius: 5px;" +
                                "-fx-border-color: #000000; " +
                                "-fx-font-size: 14px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-cursor: hand; " +
                                "-fx-background-radius: 20;"
                );
                viewPollButton.setPrefHeight(30);

                pollCard.getChildren().addAll(pollLabel, candidateLabel, spacer, viewPollButton);

                // Hover effect for moving the card up
                pollCard.setOnMouseEntered(e -> {
                    TranslateTransition hoverIn = new TranslateTransition(Duration.millis(150), pollCard);
                    hoverIn.setToY(-10);  // Move up by 10 pixels
                    hoverIn.play();
                });

                pollCard.setOnMouseExited(e -> {
                    TranslateTransition hoverOut = new TranslateTransition(Duration.millis(150), pollCard);
                    hoverOut.setToY(10);  // Move back down by 10 pixels
                    hoverOut.play();
                });


                pollGrid.add(pollCard, (polls.indexOf(poll) % 4), (polls.indexOf(poll) / 4)); // Dynamic positioning
            }

            layout.setCenter(pollGrid);

            // Scene and Stage
            Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight()-80);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static boolean isMenuOpen = false;

    private static void animateMenu(Button poll, Button profile) {
        isMenuOpen = !isMenuOpen; // Toggle state
        double targetX = isMenuOpen ? 0 : 150; // Move in or out
        animateItem(poll, targetX);
        animateItem(profile, targetX);
    }

    private static void animateItem(Button item, double targetX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), item);
        transition.setToX(targetX);
        transition.play();
    }
}