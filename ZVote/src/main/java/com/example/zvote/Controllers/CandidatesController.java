package com.example.zvote.Controllers;

import com.example.zvote.Main;
import com.example.zvote.Models.*;
import com.example.zvote.Services.CandidateService;
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

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

public class CandidatesController {
    TabPane tabPane;
    Stage primaryStage;
    Map<String, Object> userSession;

    public void showCandidatesPage(Stage primaryStage, Map<String, Object> userSession) throws Exception {
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
        pollIcon.setOnMouseEntered(e -> pollIcon.setStyle(pollIcon.getStyle().replace(
                "-fx-text-fill: black;", "-fx-text-fill: white;")));
        pollIcon.setOnMouseExited(e -> pollIcon.setStyle(pollIcon.getStyle().replace(
                "-fx-text-fill: white;", "-fx-text-fill: black;")));

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


        // Candidates Item
        MenuItem candidatesItem = new MenuItem("Candidates");
        candidatesItem.setOnAction(e -> {
            try {
                showCandidatesPage(primaryStage, userSession);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

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

        profileMenu.getItems().addAll(candidatesItem, userInfoItem, logoutItem, logoffItem);


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



        // Add Candidate Button with Image
        ImageView addImageViewButton = new ImageView(new Image(getClass().getResource("/images/Plus Sign.png").toExternalForm()));
        addImageViewButton.setFitHeight(30);
        addImageViewButton.setFitWidth(30);

        Button addCandidateButton = new Button("Add Candidate");
        addCandidateButton.setGraphic(addImageViewButton);
        addCandidateButton.setStyle("-fx-background-color: transparent; -fx-border-color: #C8F0FF; -fx-background-radius: 20px; -fx-border-radius: 20px;" +
                "-fx-border-width: 3px; -fx-border-style: dashed; -fx-cursor: hand;");
        addCandidateButton.setOnAction(e -> {
            try {
                new CreateCandidateController().showCreateCandidateForm(primaryStage, (UserModel) userSession.get("user"));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });



        // Wrap the candidateGrid in a ScrollPane for Add tab
        ScrollPane addScrollPane = new ScrollPane();
        addScrollPane.setContent(addcontent);
        addScrollPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - (topBar.getHeight() + addCandidateButton.getHeight()));
        addScrollPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 50);
        addScrollPane.setStyle("-fx-background-color: transparent;");
        addScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        addScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        // Wrap the candidateGrid in a ScrollPane for Delete tab
        ScrollPane deleteScrollPane = new ScrollPane();
        deleteScrollPane.setContent(deletecontent);
        deleteScrollPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - (topBar.getHeight() + addCandidateButton.getHeight()));
        deleteScrollPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 50);
        deleteScrollPane.setStyle("-fx-background-color: transparent;");
        deleteScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        deleteScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);



        // Grid for Add Tab
        GridPane addCandidateGrid = new GridPane();
        addCandidateGrid.setHgap(30);
        addCandidateGrid.setVgap(30);
        addCandidateGrid.setAlignment(Pos.CENTER);
        addCandidateGrid.setPadding(new Insets(10, 0, 0, 0));
        addCandidateGrid.setFocusTraversable(false);
        addCandidateGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> searchBar.requestFocus());


        // Grid for Delete Tab
        GridPane deleteCandidateGrid = new GridPane();
        deleteCandidateGrid.setHgap(30);
        deleteCandidateGrid.setVgap(30);
        deleteCandidateGrid.setAlignment(Pos.CENTER);
        deleteCandidateGrid.setPadding(new Insets(10, 0, 0, 0));
        deleteCandidateGrid.setFocusTraversable(false);
        deleteCandidateGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> searchBar.requestFocus());


        // Fetch all candidates from CandidateService
        CandidateService candidateService = new CandidateService();;
        List<CandidateModel> allCandidates = candidateService.getAllCandidates();

        populateCandidateGrid(addCandidateGrid, allCandidates);  // Populate grid for Add tab
        populateCandidateGrid(deleteCandidateGrid, allCandidates);  // Populate grid for Delete tab


        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                // Fetch all candidates from CandidateService
                CandidateService candidateServiceForFilter = new CandidateService();
                List<CandidateModel> allCandidatesForFilter = candidateServiceForFilter.getAllCandidates();

                // Filter candidates based on the search query
                List<CandidateModel> filteredCandidates = filterCandidates(allCandidatesForFilter, newValue);

                // Determine the active tab and update the corresponding grid
                Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                if (selectedTab.getText().equals("Add")) {
                    populateCandidateGrid(addCandidateGrid, filteredCandidates);
                } else if (selectedTab.getText().equals("Delete")) {
                    populateCandidateGrid(deleteCandidateGrid, filteredCandidates);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


        addcontent.getChildren().addAll(addCandidateButton, addCandidateGrid);    // Add Button And Grid to addContent
        deletecontent.getChildren().addAll(deleteCandidateGrid);   // Add Button And Grid to deleteContent


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

    private List<CandidateModel> filterCandidates(List<CandidateModel> allCandidates, String query) {
        return allCandidates.stream()
                .filter(candidate -> candidate.getName().toLowerCase().contains(query.toLowerCase()) ||
                        candidate.getBio().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }


    private void populateCandidateGrid(GridPane candidateGrid, List<CandidateModel> candidates) throws Exception {
        // Clear existing content in the grid
        candidateGrid.getChildren().clear();

        // Loop through the candidates and add candidate cards to the grid
        for (CandidateModel candidate : candidates) {
            VBox candidateCard = createCandidateCard(candidate); // Use the reusable method to create candidate cards
            candidateGrid.add(
                    candidateCard,
                    (candidates.indexOf(candidate) % 4), // Column index
                    (candidates.indexOf(candidate) / 4)  // Row index
            );

            candidateCard.setFocusTraversable(false);
        }
    }

    private VBox createCandidateCard(CandidateModel candidate) throws Exception {
        VBox candidateCard = new VBox();
        candidateCard.setAlignment(Pos.TOP_CENTER);
        candidateCard.setPadding(new Insets(10));
        candidateCard.setStyle(
                "-fx-background-color: #C8F0FF;" +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-color: #000000;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0.0, 3, 3);"
        );
        candidateCard.setPrefSize(300, 400);

        VBox candidateMainInfo = new VBox();
        candidateMainInfo.setAlignment(Pos.TOP_CENTER);
        candidateMainInfo.setPadding(new Insets(0,0,0,10));

        // Candidate Photo
        ImageView photoView = new ImageView();
        photoView.setFitWidth(150);
        photoView.setFitHeight(150);
        photoView.setStyle("-fx-border-color: #C8F0FF; -fx-border-width: 3px; -fx-border-radius: 10px;");
        if (candidate.getPhoto() != null && candidate.getPhoto().length > 0) {
            photoView.setImage(new Image(new ByteArrayInputStream(candidate.getPhoto())));
        } else {
            photoView.setImage(new Image(getClass().getResource("/images/Profile Pic.png").toExternalForm()));
        }



        // Candidate Name
        Label candidateLabel = new Label(candidate.getName());
        candidateLabel.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px;" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-color: #000000;" +
                        "-fx-font-size: 30px;" +
                        "-fx-font-weight: bold;"
        );
        candidateLabel.setAlignment(Pos.CENTER);
        candidateLabel.setWrapText(true);
        candidateLabel.setMaxWidth(250);
        candidateLabel.setMinHeight(100);


        candidateMainInfo.getChildren().addAll(photoView, candidateLabel);


        // Bio Label
        Label bioLabel = new Label(candidate.getBio());
        bioLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");


        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);


        // Delete Candidate Button with Image
        ImageView deleteImageViewButton = new ImageView(new Image(getClass().getResource("/images/Minus Sign.png").toExternalForm()));
        deleteImageViewButton.setFitHeight(20);
        deleteImageViewButton.setFitWidth(20);


        Button deleteCandidateButton = new Button("Delete Candidate");
        deleteCandidateButton.setGraphic(deleteImageViewButton);
        deleteCandidateButton.setStyle(
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
        deleteCandidateButton.setPrefHeight(30);
        deleteCandidateButton.setOnAction(event -> {
            try {
                CandidateService candidateService = new CandidateService();
                VoteService voteService = new VoteService();
                ResultService resultService = new ResultService();

                // Step 1: Fetch and delete all results associated with the candidate
                List<ResultModel> results = resultService.getResultsByCandidateID(candidate.getCandidate_ID());
                // Ensure that there are results before proceeding
                if (results != null && !results.isEmpty()) {
                    for (ResultModel result : results) {
                        resultService.deleteResult(result.getResult_ID());
                    }
                }


                // Step 2: Fetch and delete all votes associated with the candidate
                List<VoteModel> votes = voteService.getVotesByCandidateID(candidate.getCandidate_ID());
                // Ensure that there are votes before proceeding
                if (votes != null && !votes.isEmpty()) {
                    for (VoteModel vote : votes) {
                        voteService.deleteVote(vote.getUser_ID(), vote.getPoll_ID());
                    }
                }


                // Step 3: Delete the candidate itself
                candidateService.deleteCandidate(candidate.getCandidate_ID());

                // Reload the admin landing page
                showCandidatesPage(primaryStage, userSession);

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


        candidateCard.getChildren().addAll(candidateMainInfo, spacer, bioLabel);


        // Initial Page
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab.getText().equals("Add")) {
            candidateCard.getChildren().remove(deleteCandidateButton);
        } else if (selectedTab.getText().equals("Delete")) {
            candidateCard.getChildren().add(deleteCandidateButton);
        }


        // Dynamically change the buttons when tabs are changed
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab.getText().equals("Add")) {
                candidateCard.getChildren().remove(deleteCandidateButton);
            } else if (newTab.getText().equals("Delete")) {
                candidateCard.getChildren().add(deleteCandidateButton);
            }
        });


        // Hover Effects
        candidateCard.setOnMouseEntered(e -> {
            TranslateTransition hoverIn = new TranslateTransition(Duration.millis(150), candidateCard);
            hoverIn.setToY(-10);
            hoverIn.play();
        });
        candidateCard.setOnMouseExited(e -> {
            TranslateTransition hoverOut = new TranslateTransition(Duration.millis(150), candidateCard);
            hoverOut.setToY(0);
            hoverOut.play();
        });

        return candidateCard;
    }
}
