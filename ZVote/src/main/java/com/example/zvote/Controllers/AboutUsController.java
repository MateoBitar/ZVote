package com.example.zvote.Controllers;

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

public class AboutUsController {

    private Stage primaryStage;
    private Scene mainScene;

    public AboutUsController(Stage primaryStage, Scene mainScene) {
        this.primaryStage = primaryStage;
        this.mainScene = mainScene;
    }

    public void showAboutUsScene() {
        // Outer Background (C8F0FF)
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #C8F0FF;");

        // Centered VBox with smaller white background
        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
        contentBox.setSpacing(20);

        // Ensure contentBox does not resize
        contentBox.setMaxWidth(600);
        contentBox.setMaxHeight(530);

        // Create Labels for Titles with Outline Effect
        StackPane title = createOutlinedTitle("About Zvote");
        StackPane missionTitle = createOutlinedTitle("Our Mission");
        StackPane teamTitle = createOutlinedTitle("Our Team");

        // Content Labels
        Label aboutText = createContentLabel(
                "Welcome to Zvote! Our platform is dedicated to simplifying and securing the voting process. " +
                        "Zvote ensures fairness, transparency, and ease of access for all users."
        );

        Label missionText = createContentLabel(
                "At Zvote, our mission is to revolutionize the way elections are conducted. " +
                        "We prioritize security, accessibility, and efficiency to empower every voter."
        );

        Label teamText = createContentLabel(
                "Our team consists of passionate developers, security experts, and election specialists. " +
                        "We are committed to delivering a seamless and secure voting experience."
        );

        // Back Button
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-family: Onyx; -fx-font-size: 20; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand");
        backButton.setPrefHeight(30);
        backButton.setPrefWidth(100);
        backButton.setOnMouseEntered(e -> backButton.setStyle(backButton.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        backButton.setOnMouseExited(e -> backButton.setStyle(backButton.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));
        backButton.setOnAction(e -> goBack());

        // Add components to content box
        contentBox.getChildren().addAll(title, aboutText, missionTitle, missionText, teamTitle, teamText, backButton);

        // Center the content box in the BorderPane
        StackPane centerWrapper = new StackPane(contentBox);
        root.setCenter(centerWrapper);

        // Set Scene with Resizable Background
        Scene aboutScene = new Scene(root, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight()-80);
        primaryStage.setScene(aboutScene);
        primaryStage.setResizable(false);
    }

    private void goBack() {
        primaryStage.setScene(mainScene);
    }

    // Helper method to create bold, outlined title labels
    private StackPane createOutlinedTitle(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Onyx", FontWeight.BOLD, 40));

        // Create an outline effect by stacking two labels
        Label outline = new Label(text);
        outline.setFont(Font.font("Onyx", FontWeight.BOLD, 40));
        outline.setTextFill(Color.BLACK);
        outline.setTranslateX(1.5);
        outline.setTranslateY(1.5);

        label.setTextFill(Color.web("#C8F0FF"));

        StackPane stackedLabel = new StackPane(outline, label);
        return stackedLabel;
    }

    // Helper method for content labels
    private Label createContentLabel(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setFont(Font.font("Arial", 16));
        label.setTextFill(Color.BLACK);
        label.setMaxWidth(450); // Adjust width to ensure content fits
        return label;
    }
}
