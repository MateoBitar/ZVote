package com.example.zvote;

import com.example.zvote.Controllers.AboutUsController;
import com.example.zvote.Controllers.ContactUsController;
import com.example.zvote.Controllers.SignInController;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Top Bar
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10,10,10,40));
        topBar.setStyle("-fx-background-color: #C8F0FF;");

        Label logo = new Label("ZVote");
        logo.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        HBox menu = new HBox(-10);

        // About Button
        Button about = new Button("About");
        about.setStyle("-fx-font-family: Onyx; -fx-font-size: 30; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20");
        about.setPrefHeight(30);
        about.setPrefWidth(90);
        // Add hover effects for buttons
        about.setOnMouseEntered(e -> about.setStyle(about.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        about.setOnMouseExited(e -> about.setStyle(about.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));


        // Contact a Button
        Button contact = new Button("Contact");
        contact.setStyle("-fx-font-family: Onyx; -fx-font-size: 30; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20");
        contact.setPrefHeight(30);
        contact.setPrefWidth(100);
        contact.setOnMouseEntered(e -> contact.setStyle(contact.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        contact.setOnMouseExited(e -> contact.setStyle(contact.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));


        // Profile Button
        Button profileIcon = new Button("\uD83D\uDC64"); // Unicode for user icon
        profileIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 30; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20");
        profileIcon.setPrefHeight(30);
        profileIcon.setPrefWidth(70);
        profileIcon.setOnMouseEntered(e -> profileIcon.setStyle(profileIcon.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        profileIcon.setOnMouseExited(e -> profileIcon.setStyle(profileIcon.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));

        menu.getChildren().addAll(about, contact, profileIcon);
        menu.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(menu, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, menu);

        // Background Image
        StackPane content = new StackPane();
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        ImageView votingImage = new ImageView(new Image(getClass().getResource("/images/Main Page.png").toExternalForm()));
        votingImage.setPreserveRatio(false);

        // Make the image resize with the window
        votingImage.fitWidthProperty().bind(content.widthProperty());
        votingImage.fitHeightProperty().bind(content.heightProperty());

        // Text Section
        VBox textSection = new VBox(20);
        textSection.setPadding(new Insets(50, 50, 250, 1100));
        Label title = new Label("ZVote");
        title.setFont(Font.font("Onyx", FontWeight.EXTRA_BOLD, 150));

        Label subtitle = new Label("Online Voting System");
        subtitle.setFont(Font.font("Onyx", FontWeight.BOLD, 60));

        Label tagline = new Label("Democracy At Your Fingertips");
        tagline.setFont(Font.font("Onyx", FontWeight.BOLD, 40));
        tagline.setStyle("-fx-opacity: 0.6;");

        Button voteNow = new Button("Vote Now");
        voteNow.setStyle("-fx-font-family: Onyx; -fx-font-size: 40; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 50");
        voteNow.setPrefWidth(200);
        voteNow.setPadding(new Insets(3,50,3,50));

        voteNow.setOnMouseEntered(e -> voteNow.setStyle(voteNow.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        voteNow.setOnMouseExited(e -> voteNow.setStyle(voteNow.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));

        textSection.getChildren().addAll(title, subtitle, tagline, voteNow);
        textSection.setAlignment(Pos.CENTER);

        content.getChildren().addAll(votingImage, textSection);
        StackPane.setAlignment(textSection, Pos.CENTER_RIGHT);

        // Ensure content grows in VBox
        VBox.setVgrow(content, Priority.ALWAYS);

        // Main Layout
        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(topBar, content);

        Scene scene = new Scene(mainLayout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight()-80);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ZVote - Online Voting System");
        primaryStage.show();

        about.setOnAction(e -> {
            AboutUsController aboutUsController = new AboutUsController(primaryStage, scene);
            aboutUsController.showAboutUsScene();
        });
        contact.setOnAction(e -> {
            ContactUsController contactUsController = new ContactUsController(primaryStage, scene);
            contactUsController.showContactUsScene();
        });

        profileIcon.setOnAction(e-> {
            SignInController.showSignInWindow(primaryStage);
        });

        voteNow.setOnAction(e -> {
            SignInController.showSignInWindow(primaryStage);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
