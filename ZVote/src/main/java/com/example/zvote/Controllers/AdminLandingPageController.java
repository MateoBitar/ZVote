package com.example.zvote.Controllers;

import com.example.zvote.Main;
import com.example.zvote.Models.*;
import com.example.zvote.Services.PollService;
import com.example.zvote.Services.ResultService;
import com.example.zvote.Services.VoteService;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class AdminLandingPageController {
    TabPane tabPane;
    Stage primaryStage;
    Map<String, Object> userSession;

    public void showAdminLandingPage(Stage primaryStage, Map<String, Object> userSession) throws Exception {
        this.primaryStage = primaryStage;
        this.userSession = userSession;

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #FFFFFF");


        TextField searchBar = new TextField();
        searchBar.setPromptText("Search...");
        searchBar.setPrefWidth(300);
        searchBar.setTranslateX(Screen.getPrimary().getVisualBounds().getWidth() / 2 - 300);
        searchBar.setStyle("-fx-background-color: #FFFFFF; -fx-font-size: 14px; -fx-border-color: #C8F0FF; -fx-border-radius: 30px;" +
                "-fx-background-radius: 30px;");

        // Ensure the search bar can always regain focus
        searchBar.setFocusTraversable(true);


        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));


        // Polls Button
        Button pollIcon = new Button("\uD83D\uDCCB");
        pollIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 25; -fx-background-color: #C8F0FF; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        pollIcon.setPrefSize(70,30);
        pollIcon.setTranslateX(150);


        // Profile Menu Button
        MenuButton profileMenu = new MenuButton("\uD83D\uDC64"); // Unicode for user icon
        profileMenu.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        profileMenu.setPrefSize(75,30);
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


        pollIcon.setFocusTraversable(false);
        profileMenu.setFocusTraversable(false);


        // MenuIcon
        Label menuIcon = new Label("\u283F"); // Unicode for menu icon
        menuIcon.setStyle("-fx-font-size: 24px; -fx-padding: 10px; -fx-cursor: hand;");
        menuIcon.setOnMouseClicked(e -> animateMenu(pollIcon, profileMenu));


        // Menu
        HBox menu = new HBox(-10);
        menu.getChildren().addAll(pollIcon, profileMenu, menuIcon);
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
        topBar.getChildren().addAll(logo, searchBar, menu);

        layout.setTop(topBar);  // Add the topBar to the top of the layout


        // Tabs
        Tab add = new Tab("Add");
        Tab delete = new Tab("Delete");


        // Tab Pane
        tabPane = new TabPane();
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
        addPollGrid.setFocusTraversable(false);
        addPollGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> searchBar.requestFocus());


        // Grid for Delete Tab
        GridPane deletePollGrid = new GridPane();
        deletePollGrid.setHgap(30);
        deletePollGrid.setVgap(30);
        deletePollGrid.setAlignment(Pos.CENTER);
        deletePollGrid.setPadding(new Insets(10, 0, 0, 0));
        deletePollGrid.setFocusTraversable(false);
        deletePollGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> searchBar.requestFocus());


        // Fetch all polls from the PollService
        PollService pollService = new PollService();;
        List<PollModel> allPolls = pollService.getAllPolls();

        populatePollGrid(addPollGrid, allPolls);  // Populate grid for Add tab
        populatePollGrid(deletePollGrid, allPolls);  // Populate grid for Delete tab


        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                // Fetch all polls from PollService
                PollService pollServiceForFilter = new PollService();
                List<PollModel> allPollsForFilter = pollServiceForFilter.getAllPolls();

                // Filter polls based on the search query
                List<PollModel> filteredPolls = filterPolls(allPollsForFilter, newValue);

                // Determine the active tab and update the corresponding grid
                Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                if (selectedTab.getText().equals("Add")) {
                    populatePollGrid(addPollGrid, filteredPolls);
                } else if (selectedTab.getText().equals("Delete")) {
                    populatePollGrid(deletePollGrid, filteredPolls);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        addcontent.getChildren().addAll(addPollButton, addPollGrid);    // Add Button And Grid to addContent
        deletecontent.getChildren().addAll(deletePollGrid);   // Add Button And Grid to deleteContent


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

    static void animateMenu(Button poll, MenuButton profile) {
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

    private static void animateItem(MenuButton item, double targetX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), item);
        transition.setToX(targetX);
        transition.play();
    }


    private List<PollModel> filterPolls(List<PollModel> allPolls, String query) {
        return allPolls.stream()
                .filter(poll -> poll.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        poll.getDescription().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }


    private void populatePollGrid(GridPane pollGrid, List<PollModel> polls) throws Exception {
        // Clear existing content in the grid
        pollGrid.getChildren().clear();

        // Loop through the polls and add poll cards to the grid
        for (PollModel poll : polls) {
            VBox pollCard = createPollCard(poll); // Use the reusable method to create poll cards
            pollGrid.add(
                    pollCard,
                    (polls.indexOf(poll) % 4), // Column index
                    (polls.indexOf(poll) / 4)  // Row index
            );

            pollCard.setFocusTraversable(false);
        }
    }

    private VBox createPollCard(PollModel poll) throws Exception {
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

        // Poll Label
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
        pollLabel.setPadding(new Insets(0, 0, 0, 10));

        // Poll Status
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

        // Candidates Section
        Label candidatesLabel = new Label("Candidates:");
        candidatesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

        VBox candidatesBox = new VBox(5);
        candidatesBox.setAlignment(Pos.TOP_LEFT);
        candidatesBox.setPadding(new Insets(20, 0, 0, 0));
        candidatesBox.getChildren().add(candidatesLabel);

        ResultService resultService = new ResultService();
        List<CandidateModel> candidates = resultService.getCandidatesWithVotesByPollID(poll.getPoll_ID());

        for (CandidateModel candidate : candidates) {
            HBox candidateBox = new HBox(10);
            candidateBox.setAlignment(Pos.CENTER_LEFT);

            Label nameLabel = new Label(candidate.getName());
            nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal;");

            Label percentageLabel = new Label(String.format("%.1f%%", candidate.getVotePercentage() * 100));
            percentageLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal;");

            ProgressBar voteBar = new ProgressBar(candidate.getVotePercentage());
            voteBar.setPrefWidth(80);
            voteBar.progressProperty().addListener((observable, oldValue, newValue) -> {
                percentageLabel.setText(String.format("%.1f%%", newValue.doubleValue() * 100));
            });

            candidateBox.getChildren().addAll(nameLabel, voteBar, percentageLabel);
            candidatesBox.getChildren().add(candidateBox);
        }

        // Poll Button
        Button pollButton = new Button();

        // Initial Page
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab.getText().equals("Add")) {
            pollButton.setText("View Poll");
            pollButton.setStyle(
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
            pollButton.setPrefHeight(30);

            pollButton.setOnAction(event -> {
                try {
                    new AdminPollDetailsController().showAdminPollDetails(primaryStage, poll);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        } else if (selectedTab.getText().equals("Delete")) {
            // Delete Poll Button with Image
            ImageView deleteImageViewButton = new ImageView(new Image(getClass().getResource("/images/Minus Sign.png").toExternalForm()));
            deleteImageViewButton.setFitHeight(20);
            deleteImageViewButton.setFitWidth(20);


            pollButton.setText("Delete Poll");
            pollButton.setGraphic(deleteImageViewButton);
            pollButton.setStyle(
                    "-fx-background-color: red; " +
                            "-fx-text-fill: black; " +
                            "-fx-border-radius: 5px;" +
                            "-fx-border-color: #000000;" +
                            "-fx-border-width: 2px; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-cursor: hand; " +
                            "-fx-background-radius: 5;"
            );
            pollButton.setPrefHeight(30);
            pollButton.setOnAction(event -> {
                try {
                    PollService pollService = new PollService();
                    VoteService voteService = new VoteService();

                    // Step 1: Fetch and delete all results associated with the poll
                    List<ResultModel> results = resultService.getResultsByPollID(poll.getPoll_ID());
                    for (ResultModel result : results) {
                        resultService.deleteResult(result.getResult_ID());
                    }

                    // Step 2: Fetch and delete all votes associated with the poll
                    List<VoteModel> votes = voteService.getVotesByPollID(poll.getPoll_ID());
                    for (VoteModel vote : votes) {
                        voteService.deleteVote(vote.getUser_ID(), vote.getPoll_ID());
                    }

                    // Step 3: Delete the poll itself
                    pollService.deletePoll(poll.getPoll_ID());

                    // Reload the admin landing page
                    showAdminLandingPage(primaryStage, userSession);

                    // Explicitly reselect the "Delete" tab after the page reload
                    Platform.runLater(() -> {
                        tabPane.getSelectionModel().select(tabPane.getTabs().stream()
                                .filter(tab -> tab.getText().equals("Delete"))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Delete Tab not found")));
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }


        // Dynamically change the buttons when tabs are changed
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab.getText().equals("Add")) {
                pollButton.setText("View Poll");
                pollButton.setStyle(
                        "-fx-background-color: #C8F0FF; " +
                                "-fx-text-fill: black; " +
                                "-fx-border-radius: 5px;" +
                                "-fx-border-color: #000000;" +
                                "-fx-border-width: 2px; " +
                                "-fx-font-size: 14px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-cursor: hand; " +
                                "-fx-background-radius: 5;"
                );
                pollButton.setPrefHeight(30);

                pollButton.setOnAction(event -> {
                    try {
                        new AdminPollDetailsController().showAdminPollDetails(primaryStage, poll);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

            } else if (newTab.getText().equals("Delete")) {
                // Delete Poll Button with Image
                ImageView deleteImageViewButton = new ImageView(new Image(getClass().getResource("/images/Minus Sign.png").toExternalForm()));
                deleteImageViewButton.setFitHeight(20);
                deleteImageViewButton.setFitWidth(20);


                pollButton.setText("Delete Poll");
                pollButton.setGraphic(deleteImageViewButton);
                pollButton.setStyle(
                        "-fx-background-color: red; " +
                                "-fx-text-fill: black; " +
                                "-fx-border-radius: 5px;" +
                                "-fx-border-color: #000000;" +
                                "-fx-border-width: 2px; " +
                                "-fx-font-size: 14px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-cursor: hand; " +
                                "-fx-background-radius: 5;"
                );
                pollButton.setPrefHeight(30);

                pollButton.setOnAction(event -> {
                    try {
                        PollService pollService = new PollService();
                        VoteService voteService = new VoteService();

                        // Step 1: Fetch and delete all results associated with the poll
                        List<ResultModel> results = resultService.getResultsByPollID(poll.getPoll_ID());
                        for (ResultModel result : results) {
                            resultService.deleteResult(result.getResult_ID());
                        }

                        // Step 2: Fetch and delete all votes associated with the poll
                        List<VoteModel> votes = voteService.getVotesByPollID(poll.getPoll_ID());
                        for (VoteModel vote : votes) {
                            voteService.deleteVote(vote.getUser_ID(), vote.getPoll_ID());
                        }

                        // Step 3: Delete the poll itself
                        pollService.deletePoll(poll.getPoll_ID());

                        // Reload the admin landing page
                        showAdminLandingPage(primaryStage, userSession);

                        // Explicitly reselect the "Delete" tab after the page reload
                        Platform.runLater(() -> {
                            tabPane.getSelectionModel().select(tabPane.getTabs().stream()
                                    .filter(tab -> tab.getText().equals("Delete"))
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("Delete Tab not found")));
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });


        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        pollCard.getChildren().addAll(pollLabel, statusLabel, candidatesBox, spacer, pollButton);

        // Hover Effects
        pollCard.setOnMouseEntered(e -> {
            TranslateTransition hoverIn = new TranslateTransition(Duration.millis(150), pollCard);
            hoverIn.setToY(-10);
            hoverIn.play();
        });
        pollCard.setOnMouseExited(e -> {
            TranslateTransition hoverOut = new TranslateTransition(Duration.millis(150), pollCard);
            hoverOut.setToY(0);
            hoverOut.play();
        });

        return pollCard;
    }
}