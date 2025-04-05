package com.example.zvote.Controllers;

import com.example.zvote.Main;
import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Models.PollModel;
import com.example.zvote.Models.UserModel;
import com.example.zvote.Services.PollService;
import com.example.zvote.Services.ResultService;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class LandingPageController {
    public void showLandingPage(Stage primaryStage,Map<String, Object> userSession) throws Exception {
        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #FFFFFF");

        // topBar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10,10,10,40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER);

        // Create a shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);

        // Apply shadow to topBar
        topBar.setEffect(shadow);

        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        HBox menu = new HBox(-10);

        // Polls Button
        Button pollIcon = new Button("\uD83D\uDCCB");
        pollIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 25; -fx-background-color: #C8F0FF; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        pollIcon.setPrefHeight(30);
        pollIcon.setPrefWidth(60);
        pollIcon.setTranslateX(150);

        // Profile Menu Button
        MenuButton profileMenu = new MenuButton("\uD83D\uDC64"); // Unicode for user icon
        profileMenu.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        profileMenu.setPrefHeight(30);
        profileMenu.setPrefWidth(75);
        profileMenu.setTranslateX(200);
        profileMenu.setOnMouseEntered(e -> profileMenu.setStyle(profileMenu.getStyle().replace(
                "-fx-text-fill: black;", "-fx-text-fill: white;")));
        profileMenu.setOnMouseExited(e -> profileMenu.setStyle(profileMenu.getStyle().replace(
                "-fx-text-fill: white;", "-fx-text-fill: black;")));

        // Menu Items
        MenuItem userInfoItem = new MenuItem("User Info");
        userInfoItem.setOnAction(e -> {
            UserController userController = new UserController();
            userController.showUserProfile(primaryStage, (UserModel) userSession.get("user"));
        });

        MenuItem logoutItem = new MenuItem("Log Out");
        logoutItem.setOnAction(e -> {
            primaryStage.close();
            new Main().start(new Stage());
        });

        MenuItem logoffItem = new MenuItem("Close Application");
        logoffItem.setOnAction(e -> {
            primaryStage.close();
        });

        profileMenu.getItems().addAll(userInfoItem, logoutItem, logoffItem);

        Label menuIcon = new Label("\u283F");
        menuIcon.setStyle("-fx-font-size: 24px; -fx-padding: 10px; -fx-cursor: hand;");
        menuIcon.setOnMouseClicked(e -> animateMenu(pollIcon, profileMenu));

        menu.getChildren().addAll(pollIcon, profileMenu, menuIcon);
        menu.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(menu, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, menu);

        layout.setTop(topBar);

        // Poll Cards
        GridPane pollGrid = new GridPane();
        pollGrid.setHgap(30);
        pollGrid.setVgap(30);
        pollGrid.setAlignment(Pos.CENTER);
        pollGrid.setPadding(new Insets(40, 0, 0,200));


        // Wrap the pollGrid in a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(pollGrid);
        scrollPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - topBar.getHeight());
        scrollPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


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
                pollCard.setPrefSize(300,400);

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
                pollLabel.setMinHeight(100);
                pollLabel.setPadding(new Insets(0,0,0,10));

                // Poll status
                Label statusLabel = new Label();
                statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #000000;");

                Timestamp startDate = (Timestamp) poll.getStart_date(); // java.sql.Timestamp
                Timestamp endDate = (Timestamp) poll.getEnd_date();     // java.sql.Timestamp

                LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                LocalDate today = LocalDate.now();
                long daysLeft = ChronoUnit.DAYS.between(today, endLocalDate);

                // Set status text based on time
                if (today.isBefore(startLocalDate)) {
                    statusLabel.setText("Status: Inactive");
                    statusLabel.setStyle("-fx-font-size:20px; -fx-font-weight: bold; -fx-text-fill: Gray;");
                } else if (daysLeft > 0) {
                    statusLabel.setText("Status: Active • " + daysLeft + " day(s) left");
                    statusLabel.setStyle("-fx-font-size:20px; -fx-font-weight: bold; -fx-text-fill: Green;");
                } else if (daysLeft == 0) {
                    statusLabel.setText("Status: Last day to vote!");
                    statusLabel.setStyle("-fx-font-size:20px; -fx-font-weight: bold; -fx-text-fill: Orange;");
                } else {
                    statusLabel.setText("Status: Completed");
                    statusLabel.setStyle("-fx-font-size:20px; -fx-font-weight: bold; -fx-text-fill: Red;");
                }


                // Fetch candidates for the poll
                ResultService resultService = new ResultService();
                List<CandidateModel> candidates = resultService.getCandidatesWithVotesByPollID(poll.getPoll_ID());

                // Create a VBox to hold candidate names
                VBox candidatesBox = new VBox(5); // 5px spacing between candidate names
                candidatesBox.setAlignment(Pos.TOP_LEFT);
                candidatesBox.setPadding(new Insets(20, 0, 0, 0));

                Label candidatesLabel = new Label("Candidates:");
                candidatesLabel.setStyle(
                        "-fx-font-weight: bold;" +
                                "-fx-font-size: 20px;"
                );

                candidatesBox.getChildren().add(candidatesLabel);

                for (CandidateModel candidate : candidates) {
                    HBox candidateBox = new HBox(10);  // Spacing between elements
                    candidateBox.setAlignment(Pos.CENTER_LEFT);  // Align everything to the left

                    // Candidate name label
                    Label nameLabel = new Label(candidate.getName());
                    nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal;");

                    // Percentage label
                    Label percentageLabel = new Label(String.format("%.1f%%", candidate.getVotePercentage() * 100));
                    percentageLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal;");


                    // Progress bar for vote percentage
                    ProgressBar voteBar = new ProgressBar(candidate.getVotePercentage()); // Between 0.0 and 1.0
                    voteBar.setPrefWidth(80); // Width of the progress bar

                    // Dynamically update the percentage label whenever the progress bar changes
                    voteBar.progressProperty().addListener((observable, oldValue, newValue) -> {
                        percentageLabel.setText(String.format("%.1f%%", newValue.doubleValue() * 100));  // Update percentage label dynamically
                    });

                    // Add the name, progress bar, and percentage label to the HBox
                    candidateBox.getChildren().addAll(nameLabel, voteBar, percentageLabel);

                    // Add the candidate box to the candidates container
                    candidatesBox.getChildren().add(candidateBox);
                }

                // Add space between title and button
                Region spacer = new Region();
                VBox.setVgrow(spacer, Priority.ALWAYS);

                Button viewPollButton = new Button("View Poll");
                viewPollButton.setStyle(
                        "-fx-background-color: #C8F0FF; " +
                                "-fx-text-fill: black; " +
                                "-fx-border-radius: 5px;" +
                                "-fx-border-color: #000000;" +
                                "-fx-border-width: 2px; " +
                                "-fx-font-size: 14px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-cursor: hand; " +
                                "-fx-background-radius: 20;"
                );
                viewPollButton.setPrefHeight(30);
                viewPollButton.setOnAction(e -> {
                    PollController pollController = new PollController();
                    try {
                        // Fetch the user data from userSession and ensure type safety
                        UserModel user = (UserModel) userSession.get("user");
                        if (user == null) {
                            throw new Exception("User data is missing in the session!");
                        }

                        // Navigate to the poll details
                        pollController.showPollDetails(primaryStage, poll, user);

                    } catch (Exception ex) {
                        // Provide detailed exception handling for debugging
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Navigation Error");
                        errorAlert.setContentText("An error occurred while navigating to the poll details:\n" + ex.getMessage());
                        errorAlert.showAndWait();
                    }
                });

                pollCard.getChildren().addAll(pollLabel, statusLabel, candidatesBox, spacer, viewPollButton);

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

            layout.setCenter(scrollPane);

            // Scene and Stage
            Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight()-80);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private static boolean isMenuOpen = false;

    static void animateMenu(Button poll, MenuButton profile) {
        isMenuOpen = !isMenuOpen; // Toggle state
        double targetX = isMenuOpen ? 0 : 200; // Move in or out
        animateItem(poll, targetX);
        animateItem(profile, targetX);
    }

    private static void animateItem(Button item, double targetX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), item);
        transition.setToX(targetX);
        transition.play();
    }

    private static void animateItem(MenuButton item, double targetX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), item);
        transition.setToX(targetX);
        transition.play();
    }
}