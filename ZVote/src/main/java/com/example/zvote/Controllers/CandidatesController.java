package com.example.zvote.Controllers;  // Package declaration, specifies the namespace

// Importing necessary classes for UI, animation, and functionality
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

    TabPane tabPane;  // Tab pane for managing Add/Delete tabs
    Stage primaryStage;  // Primary stage for the application
    Map<String, Object> userSession;  // Holds session details for the logged-in user


    // Method to show the Candidates page
    public void showCandidatesPage(Stage primaryStage, Map<String, Object> userSession) throws Exception {
        this.primaryStage = primaryStage;
        this.userSession = userSession;

        // Main layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #FFFFFF;");  // White background for the layout


        // Search Bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Search...");  // Placeholder text
        searchBar.setPrefWidth(300);  // Set preferred width
        searchBar.setTranslateX(Screen.getPrimary().getVisualBounds().getWidth() / 2 - 300);  // Position in the center
        searchBar.setStyle("-fx-background-color: #FFFFFF; -fx-font-size: 14px; -fx-border-color: #C8F0FF; -fx-border-radius: 30px;" +
                "-fx-background-radius: 30px;");
        searchBar.setFocusTraversable(true);  // Ensure the search bar can regain focus


        // Logo
        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));  // Set logo font and size


        // Polls Button
        Button pollIcon = new Button("\uD83D\uDCCB");  // Unicode for clipboard icon
        pollIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 25; -fx-background-color: #C8F0FF; -fx-text-fill: white;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        pollIcon.setPrefSize(70, 30);  // Set size
        pollIcon.setTranslateX(150);  // Adjust position
        pollIcon.setOnMouseEntered(e -> pollIcon.setStyle(pollIcon.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        pollIcon.setOnMouseExited(e -> pollIcon.setStyle(pollIcon.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));
        pollIcon.setOnAction(e -> {
            try {
                new AdminLandingPageController().showAdminLandingPage(primaryStage, userSession);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // Profile Menu Button
        MenuButton profileMenu = new MenuButton("\uD83D\uDC64");  // Unicode for user icon
        profileMenu.setStyle("-fx-font-family: Arial; -fx-font-size: 20; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        profileMenu.setPrefSize(75, 30);  // Set size
        profileMenu.setTranslateX(200);  // Adjust position
        profileMenu.setOnMouseEntered(e -> profileMenu.setStyle(profileMenu.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        profileMenu.setOnMouseExited(e -> profileMenu.setStyle(profileMenu.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));


        // Menu Items
        MenuItem candidatesItem = new MenuItem("Candidates");
        candidatesItem.setOnAction(e -> {
            try {
                showCandidatesPage(primaryStage, userSession);  // Reload Candidates page
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        MenuItem userInfoItem = new MenuItem("User Info");
        userInfoItem.setOnAction(e -> {
            UserController userController = new UserController();
            userController.showUserProfile(primaryStage, (UserModel) userSession.get("user"));
        });

        MenuItem logoutItem = new MenuItem("Log Out");
        logoutItem.setOnAction(e -> {
            primaryStage.close();
            new Main().start(new Stage());  // Restart the application
        });

        MenuItem logoffItem = new MenuItem("Close Application");
        logoffItem.setOnAction(e -> primaryStage.close());

        profileMenu.getItems().addAll(candidatesItem, userInfoItem, logoutItem, logoffItem);  // Add items to profile menu


        pollIcon.setFocusTraversable(false);  // Disable focus traversal for polls button
        profileMenu.setFocusTraversable(false);  // Disable focus traversal for profile menu


        // Menu Icon
        Label menuIcon = new Label("\u283F");  // Unicode for menu icon
        menuIcon.setStyle("-fx-font-size: 24px; -fx-padding: 10px; -fx-cursor: hand;");
        menuIcon.setOnMouseClicked(e -> animateMenu(pollIcon, profileMenu));


        // Menu Bar
        HBox menu = new HBox(-10);
        menu.getChildren().addAll(pollIcon, profileMenu, menuIcon);
        menu.setAlignment(Pos.CENTER_RIGHT);


        // Top Bar Configuration
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10, 10, 10, 40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");
        topBar.setAlignment(Pos.CENTER);

        // Add shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        shadow.setColor(Color.LIGHTGRAY);
        topBar.setEffect(shadow);

        // Add components to top bar
        HBox.setHgrow(menu, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, searchBar, menu);
        layout.setTop(topBar);  // Set top bar in the layout


        // Tabs for Add/Delete
        Tab add = new Tab("Add");
        Tab delete = new Tab("Delete");

        // Tab Pane
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);  // Prevent tab closing
        tabPane.setStyle("-fx-font-weight: bold;");
        tabPane.getTabs().addAll(add, delete);  // Add tabs to tab pane


        // Content for Add Tab
        VBox addcontent = new VBox(10);
        addcontent.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        addcontent.setAlignment(Pos.CENTER);
        addcontent.setPadding(new Insets(5, 0, 0, 0));
        addcontent.setStyle("-fx-background-color: #FFFFFF;");


        // Content for Delete Tab
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
        addScrollPane.setContent(addcontent);  // Set content to addContent
        addScrollPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - (topBar.getHeight() + addCandidateButton.getHeight()));  // Adjust height dynamically
        addScrollPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 50);  // Adjust width dynamically
        addScrollPane.setStyle("-fx-background-color: transparent;");  // Transparent background for ScrollPane
        addScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  // Vertical scrollbar as needed
        addScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // Horizontal scrollbar hidden


        // Wrap the candidateGrid in a ScrollPane for Delete tab
        ScrollPane deleteScrollPane = new ScrollPane();
        deleteScrollPane.setContent(deletecontent);  // Set content to deleteContent
        deleteScrollPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - (topBar.getHeight() + addCandidateButton.getHeight()));  // Adjust height dynamically
        deleteScrollPane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 50);  // Adjust width dynamically
        deleteScrollPane.setStyle("-fx-background-color: transparent;");  // Transparent background for ScrollPane
        deleteScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);  // Vertical scrollbar as needed
        deleteScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // Horizontal scrollbar hidden


        // Grid for Add Tab
        GridPane addCandidateGrid = new GridPane();
        addCandidateGrid.setHgap(30);  // Horizontal gap between grid items
        addCandidateGrid.setVgap(30);  // Vertical gap between grid items
        addCandidateGrid.setAlignment(Pos.CENTER);  // Center-align grid content
        addCandidateGrid.setPadding(new Insets(10, 0, 0, 0));  // Add padding to grid
        addCandidateGrid.setFocusTraversable(false);  // Disable focus traversal for grid
        addCandidateGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> searchBar.requestFocus());  // Re-focus search bar on grid click


        // Grid for Delete Tab
        GridPane deleteCandidateGrid = new GridPane();
        deleteCandidateGrid.setHgap(30);  // Horizontal gap between grid items
        deleteCandidateGrid.setVgap(30);  // Vertical gap between grid items
        deleteCandidateGrid.setAlignment(Pos.CENTER);  // Center-align grid content
        deleteCandidateGrid.setPadding(new Insets(10, 0, 0, 0));  // Add padding to grid
        deleteCandidateGrid.setFocusTraversable(false);  // Disable focus traversal for grid
        deleteCandidateGrid.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> searchBar.requestFocus());  // Re-focus search bar on grid click


        // Fetch all candidates from CandidateService
        CandidateService candidateService = new CandidateService();
        List<CandidateModel> allCandidates = candidateService.getAllCandidates();  // Retrieve all candidates

        populateCandidateGrid(addCandidateGrid, allCandidates);  // Populate Add tab grid with candidates
        populateCandidateGrid(deleteCandidateGrid, allCandidates);  // Populate Delete tab grid with candidates


        // Add listener to dynamically filter candidates based on input
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CandidateService candidateServiceForFilter = new CandidateService();
                List<CandidateModel> allCandidatesForFilter = candidateServiceForFilter.getAllCandidates();  // Retrieve all candidates

                // Filter candidates using the search query
                List<CandidateModel> filteredCandidates = filterCandidates(allCandidatesForFilter, newValue);

                // Update the corresponding grid based on the active tab
                Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                if (selectedTab.getText().equals("Add")) {
                    populateCandidateGrid(addCandidateGrid, filteredCandidates);  // Update Add tab grid
                } else if (selectedTab.getText().equals("Delete")) {
                    populateCandidateGrid(deleteCandidateGrid, filteredCandidates);  // Update Delete tab grid
                }
            } catch (Exception e) {
                throw new RuntimeException(e);  // Handle exceptions appropriately
            }
        });

        // Add components to Add and Delete tabs
        addcontent.getChildren().addAll(addCandidateButton, addCandidateGrid);  // Add Button and Grid to Add tab content
        deletecontent.getChildren().addAll(deleteCandidateGrid);  // Add Grid to Delete tab content


        // Set ScrollPane content to respective tabs
        add.setContent(addScrollPane);  // Assign ScrollPane to Add tab
        delete.setContent(deleteScrollPane);  // Assign ScrollPane to Delete tab


        // Add the TabPane to the center of the layout
        layout.setCenter(tabPane);


        // Scene and Stage setup
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);  // Disable resizing of the stage
        primaryStage.show();  // Display the candidates page
    }


    // Tracks menu state (open or closed)
    private static boolean isMenuOpen = false;


    // Animate menu items (Poll and Profile buttons)
    static void animateMenu(Button poll, MenuButton profile) {
        isMenuOpen = !isMenuOpen;  // Toggle state
        double targetX = isMenuOpen ? 0 : 150;  // Calculate target position based on state
        animateItem(poll, targetX);  // Animate poll button
        animateItem(profile, targetX);  // Animate profile menu button
    }


    // Animation for Button elements
    private static void animateItem(Button item, double targetX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), item);  // Configure transition
        transition.setToX(targetX);  // Set target X position
        transition.play();  // Execute animation
    }


    // Animation for MenuButton elements
    private static void animateItem(MenuButton item, double targetX) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), item);  // Configure transition
        transition.setToX(targetX);  // Set target X position
        transition.play();  // Execute animation
    }


    // Filter candidates based on search query
    private List<CandidateModel> filterCandidates(List<CandidateModel> allCandidates, String query) {
        return allCandidates.stream()  // Stream through all candidates
                .filter(candidate -> candidate.getName().toLowerCase().contains(query.toLowerCase()) ||
                        candidate.getBio().toLowerCase().contains(query.toLowerCase()))  // Match name or bio
                .toList();  // Collect filtered candidates into a list
    }


    // Populate grid with candidates
    private void populateCandidateGrid(GridPane candidateGrid, List<CandidateModel> candidates) throws Exception {
        candidateGrid.getChildren().clear();  // Clear existing grid content

        // Iterate over candidates to add them as cards to the grid
        for (CandidateModel candidate : candidates) {
            VBox candidateCard = createCandidateCard(candidate);  // Create reusable candidate card
            candidateGrid.add(candidateCard, (candidates.indexOf(candidate) % 4), (candidates.indexOf(candidate) / 4));  // Add candidate card to grid
            candidateCard.setFocusTraversable(false);  // Disable focus traversal for candidate cards
        }
    }


    // Create a reusable candidate card component
    private VBox createCandidateCard(CandidateModel candidate) throws Exception {
        VBox candidateCard = new VBox();
        candidateCard.setAlignment(Pos.TOP_CENTER);  // Align card content to top-center
        candidateCard.setPadding(new Insets(10));  // Add padding to card
        candidateCard.setStyle("-fx-background-color: #C8F0FF; -fx-border-radius: 10px; -fx-background-radius: 10px;" +
                "-fx-border-width: 3px; -fx-border-color: #000000; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0.0, 3, 3);");
        candidateCard.setPrefSize(300, 400);  // Set preferred size for candidate card

        VBox candidateMainInfo = new VBox();
        candidateMainInfo.setAlignment(Pos.TOP_CENTER);  // Align main info content to top-center
        candidateMainInfo.setPadding(new Insets(0, 0, 0, 10));  // Add padding to main info

        // Candidate Photo
        ImageView photoView = new ImageView();
        photoView.setFitWidth(150);
        photoView.setFitHeight(150);
        photoView.setStyle("-fx-border-color: #C8F0FF; -fx-border-width: 3px; -fx-border-radius: 10px;");
        if (candidate.getPhoto() != null && candidate.getPhoto().length > 0) {
            photoView.setImage(new Image(new ByteArrayInputStream(candidate.getPhoto())));  // Display photo from byte array
        } else {
            photoView.setImage(new Image(getClass().getResource("/images/Profile Pic.png").toExternalForm()));  // Display default profile image
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

        candidateMainInfo.getChildren().addAll(photoView, candidateLabel);  // Add photo and candidate name to the main info container


        // Bio Label
        Label bioLabel = new Label(candidate.getBio());
        bioLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");  // Style for the bio label


        // Spacer for dynamic layout adjustment
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);  // Allow the spacer to grow and push content dynamically


        // Delete Candidate Button with Image
        ImageView deleteImageViewButton = new ImageView(new Image(getClass().getResource("/images/Minus Sign.png").toExternalForm()));
        deleteImageViewButton.setFitHeight(20);  // Set height for the delete icon
        deleteImageViewButton.setFitWidth(20);   // Set width for the delete icon

        Button deleteCandidateButton = new Button("Delete Candidate");
        deleteCandidateButton.setGraphic(deleteImageViewButton);  // Attach the delete icon to the button
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
                if (results != null && !results.isEmpty()) {
                    for (ResultModel result : results) {
                        resultService.deleteResult(result.getResult_ID());
                    }
                }

                // Step 2: Fetch and delete all votes associated with the candidate
                List<VoteModel> votes = voteService.getVotesByCandidateID(candidate.getCandidate_ID());
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
                throw new RuntimeException(e);  // Handle exceptions
            }
        });

        candidateCard.getChildren().addAll(candidateMainInfo, spacer, bioLabel);  // Add components to the candidate card


        // Initial Page
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab.getText().equals("Add")) {
            candidateCard.getChildren().remove(deleteCandidateButton);  // Remove delete button in Add tab
        } else if (selectedTab.getText().equals("Delete")) {
            candidateCard.getChildren().add(deleteCandidateButton);  // Add delete button in Delete tab
        }


        // Dynamically change the buttons when tabs are changed
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab.getText().equals("Add")) {
                candidateCard.getChildren().remove(deleteCandidateButton);
            } else if (newTab.getText().equals("Delete")) {
                candidateCard.getChildren().add(deleteCandidateButton);
            }
        });


        // Hover Effects for Candidate Card
        candidateCard.setOnMouseEntered(e -> {
            TranslateTransition hoverIn = new TranslateTransition(Duration.millis(150), candidateCard);
            hoverIn.setToY(-10);  // Lift card slightly on hover
            hoverIn.play();
        });

        candidateCard.setOnMouseExited(e -> {
            TranslateTransition hoverOut = new TranslateTransition(Duration.millis(150), candidateCard);
            hoverOut.setToY(0);  // Return card to original position
            hoverOut.play();
        });

        return candidateCard;  // Return the constructed candidate card
    }
}
