package com.example.zvote.Controllers;

import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Models.PollModel;
import com.example.zvote.Models.UserModel;
import com.example.zvote.Services.PollService;
import com.example.zvote.Services.ResultService;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
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
import javafx.util.Duration;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class AdminLandingPageController {
    public void showAdminLandingPage(Stage primaryStage, Map<String, Object> userSession) throws Exception {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #FFFFFF");


        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));


        // Polls Button
        Button pollIcon = new Button("\uD83D\uDCCB");
        pollIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 25; -fx-background-color: #C8F0FF; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        pollIcon.setPrefSize(70,30);
        pollIcon.setTranslateX(150);


        // Profile Button
        Button profileIcon = new Button("\uD83D\uDC64"); // Unicode for user icon
        profileIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 30; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        profileIcon.setPrefSize(70, 30);
        profileIcon.setTranslateX(150);
        profileIcon.setOnMouseEntered(e -> profileIcon.setStyle(profileIcon.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        profileIcon.setOnMouseExited(e -> profileIcon.setStyle(profileIcon.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));
        profileIcon.setOnAction(e -> {
            UserController userController = new UserController();
            userController.showUserProfile(primaryStage, (UserModel) userSession.get("user"));
        });


        // MenuIcon
        Label menuIcon = new Label("\u283F"); // Unicode for menu icon
        menuIcon.setStyle("-fx-font-size: 24px; -fx-padding: 10px; -fx-cursor: hand;");
        menuIcon.setOnMouseClicked(e -> animateMenu(pollIcon, profileIcon));


        // Menu
        HBox menu = new HBox(-10);
        menu.getChildren().addAll(pollIcon, profileIcon, menuIcon);
        menu.setAlignment(Pos.CENTER_RIGHT);



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

        topBar.setEffect(shadow);   // Apply shadow to topBar


        // Add the topBar info to topBar
        HBox.setHgrow(menu, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, menu);

        layout.setTop(topBar);  // Add the topBar to the top of the layout



        // Tabs
        Tab add = new Tab("Add");
        Tab delete = new Tab("Delete");


        // Tab Pane
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-font-weight: bold");

        tabPane.getTabs().addAll(add, delete); // Apply tabs to the tabPane



        // Add Tab content
        VBox addcontent = new VBox(10);
        addcontent.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        addcontent.setAlignment(Pos.CENTER);
        addcontent.setPadding(new Insets(5, 0, 0, 0));
        addcontent.setStyle("-fx-background-color: #FFFFFF;");


        // Delete Tab Content
        VBox deletecontent = new VBox(10);
        deletecontent.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        deletecontent.setAlignment(Pos.CENTER);
        deletecontent.setPadding(new Insets(5, 0, 0, 0));
        deletecontent.setStyle("-fx-background-color: #FFFFFF;");



        // Add Poll Button with Image
        ImageView addImageViewButton = new ImageView(new Image(getClass().getResource("/images/Plus Sign.png").toExternalForm()));
        addImageViewButton.setFitHeight(30);
        addImageViewButton.setFitWidth(30);

        Button addPollButton = new Button("Add Poll");
        addPollButton.setGraphic(addImageViewButton);
        addPollButton.setStyle("-fx-background-color: transparent; -fx-border-color: #C8F0FF; -fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-width: 3px; -fx-border-style: dashed; -fx-cursor: hand;");
        addPollButton.setOnAction(e -> {
            try {
                new AdminPollController().showCreatePoll(primaryStage, (UserModel) userSession.get("user"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


        // Delete Poll Button with Image
        ImageView deleteImageViewButton = new ImageView(new Image(getClass().getResource("/images/Minus Sign.png").toExternalForm()));
        deleteImageViewButton.setFitHeight(30);
        deleteImageViewButton.setFitWidth(30);

        Button deletePollButton = new Button("Delete Poll");
        deletePollButton.setGraphic(deleteImageViewButton);
        deletePollButton.setStyle("-fx-background-color: transparent; -fx-border-color: #C8F0FF; -fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-width: 3px; -fx-border-style: dashed; -fx-cursor: hand;");



        // Wrap the pollGrid in a ScrollPane for Add tab
        ScrollPane addScrollPane = new ScrollPane();
        addScrollPane.setContent(addcontent);
        addScrollPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - (topBar.getHeight() + addPollButton.getHeight()));
        addScrollPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 50);
        addScrollPane.setStyle("-fx-background-color: transparent;");
        addScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        addScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        // Wrap the pollGrid in a ScrollPane for Delete tab
        ScrollPane deleteScrollPane = new ScrollPane();
        deleteScrollPane.setContent(deletecontent);
        deleteScrollPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - (topBar.getHeight() + addPollButton.getHeight()));
        deleteScrollPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 50);
        deleteScrollPane.setStyle("-fx-background-color: transparent;");
        deleteScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        deleteScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);



        // Grid for Add Tab
        GridPane addPollGrid = new GridPane();
        addPollGrid.setHgap(30);
        addPollGrid.setVgap(30);
        addPollGrid.setAlignment(Pos.CENTER);
        addPollGrid.setPadding(new Insets(10, 0, 0, 0));


        // Grid for Delete Tab
        GridPane deletePollGrid = new GridPane();
        deletePollGrid.setHgap(30);
        deletePollGrid.setVgap(30);
        deletePollGrid.setAlignment(Pos.CENTER);
        deletePollGrid.setPadding(new Insets(10, 0, 0, 0));



        populatePollGrid(addPollGrid);  // Populate grid for Add tab
        populatePollGrid(deletePollGrid);  // Populate grid for Delete tab


        addcontent.getChildren().addAll(addPollButton, addPollGrid);    // Add Button And Grid to addContent
        deletecontent.getChildren().addAll(deletePollButton, deletePollGrid);   // Add Button And Grid to deleteContent


        add.setContent(addScrollPane);  // Add addScrollPane to Add Tab
        delete.setContent(deleteScrollPane);    // Add deleteScrollPane to Delete Tab

        layout.setCenter(tabPane);  // Add the TabPane to Layout

        // Scene and Stage
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight()-80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private static boolean isMenuOpen = false;

    static void animateMenu(Button poll, Button profile) {
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

    private void populatePollGrid(GridPane pollGrid) throws Exception {
        PollService pollService = new PollService();
        List<PollModel> polls = pollService.getAllPolls();
        for (PollModel poll : polls) {
            // Poll Cards for All Tabs
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


            // Poll Label for All Tabs
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


            // Label above Candidate Box
            Label candidatesLabel = new Label("Candidates:");
            candidatesLabel.setStyle(
                    "-fx-font-weight: bold;" +
                            "-fx-font-size: 20px;"
            );


            // Create a VBox to hold candidate names
            VBox candidatesBox = new VBox(5);
            candidatesBox.setAlignment(Pos.TOP_LEFT);
            candidatesBox.setPadding(new Insets(20, 0, 0, 0));

            candidatesBox.getChildren().add(candidatesLabel); // Apply candidatesLabel to Candidates Box


            // Loop over Info Of Each Candidate
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


                candidateBox.getChildren().addAll(nameLabel, voteBar, percentageLabel); // Apply candidate info to Candidate Box

                candidatesBox.getChildren().add(candidateBox); // Apply Each Candidate Box to Candidates Box
            }



            // Add Button To View Poll Details
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


            // Add Empty Space between candidates and button
            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            // Apply All Poll Card info to Poll Card
            pollCard.getChildren().addAll(pollLabel, statusLabel, candidatesBox, spacer, viewPollButton);


            // Hover effect for moving the card up
            pollCard.setOnMouseEntered(e -> {
                TranslateTransition hoverIn = new TranslateTransition(Duration.millis(150), pollCard);
                hoverIn.setToY(-10);  // Move up by 10 pixels
                hoverIn.play();
            });


            // Hover effect for moving card down
            pollCard.setOnMouseExited(e -> {
                TranslateTransition hoverOut = new TranslateTransition(Duration.millis(150), pollCard);
                hoverOut.setToY(10);  // Move back down by 10 pixels
                hoverOut.play();
            });


            // Dynamic positioning of poll Cards in the PollGrid
            pollGrid.add(pollCard, (polls.indexOf(poll) % 4), (polls.indexOf(poll) / 4));
        }
    }
}