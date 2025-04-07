package com.example.zvote.Controllers;  // Package declaration, specifies the namespace


// Importing necessary classes for UI and layout functionality
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class ContactUsController {

    private Stage primaryStage;  // Main application stage
    private Scene mainScene;  // Reference to the main scene for navigation


    // Constructor to initialize the controller with stage and main scene
    public ContactUsController(Stage primaryStage, Scene mainScene) {
        this.primaryStage = primaryStage;
        this.mainScene = mainScene;
    }


    // Method to display the "Contact Us" scene
    public void showContactUsScene() {

        // Outer Background (C8F0FF)
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #C8F0FF;");


        // Centered VBox with smaller white background
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
        contentBox.setSpacing(20);
        contentBox.setMaxWidth(500);
        contentBox.setMaxHeight(350);


        // Create Labels for Titles with Outline Effect
        StackPane title = createOutlinedTitle("Contact Us");


        // Content Labels for contact information
        Label contactInfo = createContentLabel(
                "We're here to help! You can reach us via the following methods:\n" +
                        "\nEmail: support@zvote.com" +
                        "\nPhone: +123 456 789" +
                        "\nInstagram: @zvote_official" +
                        "\nFacebook: facebook.com/zvote" +
                        "\nTikTok: @zvote_official"
        );
        contactInfo.setPadding(new Insets(0, 0, 20, 0));


        // Back Button for navigation
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-family: Onyx; -fx-font-size: 20; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand");
        backButton.setPrefHeight(30);
        backButton.setPrefWidth(100);

        // Add hover effects to Back Button
        backButton.setOnMouseEntered(e -> backButton.setStyle(backButton.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        backButton.setOnMouseExited(e -> backButton.setStyle(backButton.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));

        // Add Back Button Action to Navigate to Main Scene
        backButton.setOnAction(e -> goBack());


        // Add components to the content box
        contentBox.getChildren().addAll(title, contactInfo, backButton);


        // Center the content box in the BorderPane
        StackPane centerWrapper = new StackPane(contentBox);
        root.setCenter(centerWrapper);


        // Set Scene with Resizable Background
        Scene contactScene = new Scene(root, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight() - 80);
        primaryStage.setScene(contactScene);
        primaryStage.setResizable(false);
    }


    // Method to return to the main scene
    private void goBack() {
        primaryStage.setScene(mainScene);
    }


    // Helper Method to Create Bold, Outlined Title Labels
    private StackPane createOutlinedTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Onyx", FontWeight.BOLD, 40));

        // Create an Outline Effect by Stacking Two Labels
        Label outline = new Label(text);
        outline.setFont(Font.font("Onyx", FontWeight.BOLD, 40));
        outline.setTextFill(Color.BLACK);
        outline.setTranslateX(1.5);
        outline.setTranslateY(1.5);

        label.setTextFill(Color.web("#C8F0FF"));

        return new StackPane(outline, label);
    }


    // Helper Method for Content Labels
    private Label createContentLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setFont(Font.font("Arial", 16));
        label.setTextFill(Color.BLACK);
        label.setMaxWidth(500);  // Adjust Width to Ensure Content Fits
        label.setPrefHeight(Region.USE_COMPUTED_SIZE);  // Allow Height to Adjust Dynamically
        return label;
    }
}