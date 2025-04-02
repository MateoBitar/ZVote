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

import java.util.Map;

public class LandingPageController {
    private Stage primaryStage;
    private Map<String, Object> userSession;

    public static void showLandingPage(Stage primaryStage) {
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
                " -fx-font-weight: bold; -fx-background-radius: 20");
        pollIcon.setPrefHeight(30);
        pollIcon.setPrefWidth(70);
        pollIcon.setOnMouseEntered(e -> pollIcon.setStyle(pollIcon.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        pollIcon.setOnMouseExited(e -> pollIcon.setStyle(pollIcon.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));

        // Profile Button
        Button profileIcon = new Button("\uD83D\uDC64"); // Unicode for user icon
        profileIcon.setStyle("-fx-font-family: Onyx; -fx-font-size: 30; -fx-background-color: #C8F0FF; -fx-text-fill: black;" +
                " -fx-font-weight: bold; -fx-background-radius: 20");
        profileIcon.setPrefHeight(30);
        profileIcon.setPrefWidth(70);
        profileIcon.setOnMouseEntered(e -> profileIcon.setStyle(profileIcon.getStyle().replace("-fx-text-fill: black;", "-fx-text-fill: white;")));
        profileIcon.setOnMouseExited(e -> profileIcon.setStyle(profileIcon.getStyle().replace("-fx-text-fill: white;", "-fx-text-fill: black;")));

        menu.getChildren().addAll(pollIcon, profileIcon);
        menu.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(menu, Priority.ALWAYS);
        topBar.getChildren().addAll(logo, menu);

        layout.setTop(topBar);

        // Poll Cards
        GridPane pollGrid = new GridPane();
        pollGrid.setHgap(30);
        pollGrid.setVgap(30);
        pollGrid.setAlignment(Pos.CENTER);

        for (int i = 1; i <= 8; i++) {
            VBox pollCard = new VBox();
            pollCard.setAlignment(Pos.TOP_CENTER);
            pollCard.setPadding(new Insets(10));
            pollCard.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                            "-fx-border-radius: 10px; " +
                            "-fx-background-radius: 10px;" +
                            "-fx-border-width: 3px;" +
                            "-fx-border-color: #C8F0FF;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0.0, 3, 3);"
            );
            pollCard.setPrefSize(300, 400);

            Label pollLabel = new Label("Poll " + i);
            pollLabel.setStyle(
                    "-fx-background-color: #FFFFFF;" +
                            "-fx-border-radius: 10px; " +
                            "-fx-background-radius: 10px;" +
                            "-fx-border-width: 3px;" +
                            "-fx-border-color: #C8F0FF;" +
                            "-fx-font-size: 30px;" +
                            "-fx-font-weight: bold;"
            );

            pollCard.getChildren().add(pollLabel);

            pollGrid.add(pollCard, (i - 1) % 4, (i - 1) / 4); // Dynamic positioning
        }

        layout.setCenter(pollGrid);

        // Scene and Stage
        Scene scene = new Scene(layout, Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight()-80);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
