package com.example.zvote;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Top Bar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #C8F0FF;");

        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 30));

        HBox menu = new HBox(20);

        Label about = new Label("About");
        about.setFont(Font.font("Onyx", FontWeight.BOLD, 20));

        Label contact = new Label("Contact");
        contact.setFont(Font.font("Onyx", FontWeight.BOLD, 20));

        Label profileIcon = new Label("\uD83D\uDC64"); // Unicode for user icon
        profileIcon.setFont(Font.font("Onyx", FontWeight.BOLD, 20));

        menu.getChildren().addAll(about, contact, profileIcon);
        menu.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(menu, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, menu);

        // Background Image
        StackPane content = new StackPane();
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ImageView votingImage = new ImageView(new Image("C:\\Users\\Marco\\OneDrive\\Pictures\\Screenshots\\Landing Page.jpg"));
        votingImage.setPreserveRatio(false);

        // Make the image resize with the window
        votingImage.fitWidthProperty().bind(content.widthProperty());
        votingImage.fitHeightProperty().bind(content.heightProperty());

        // Text Section
        VBox textSection = new VBox(10);
        textSection.setPadding(new Insets(50, 50, 100, 530)); // Moves text to the right
        Label title = new Label("ZVote");
        title.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        Label subtitle = new Label("Online Voting System");
        subtitle.setFont(Font.font("Onyx", FontWeight.BOLD, 40));

        Label tagline = new Label("Democracy At Your Fingertips");
        tagline.setFont(Font.font("Onyx", FontWeight.BOLD, 20));
        tagline.setStyle("-fx-opacity: 0.6;");

        Button voteNow = new Button("Vote Now");
        voteNow.setStyle("-fx-font-family: Onyx; -fx-font-size: 20; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20");
        voteNow.setPrefHeight(30);
        voteNow.setPrefWidth(100);

        textSection.getChildren().addAll(title, subtitle, tagline, voteNow);
        textSection.setAlignment(Pos.CENTER);

        content.getChildren().addAll(votingImage, textSection);
        StackPane.setAlignment(textSection, Pos.CENTER_RIGHT);

        // Ensure content grows in VBox
        VBox.setVgrow(content, Priority.ALWAYS);

        // Main Layout
        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(topBar, content);

        Scene scene = new Scene(mainLayout, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ZVote - Online Voting System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
