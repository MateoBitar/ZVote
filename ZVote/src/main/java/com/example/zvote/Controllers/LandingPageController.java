package com.example.zvote.Controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class LandingPageController {
    private Stage primaryStage;
    private Map<String, Object> userSession;

    public LandingPageController(Stage primaryStage, Map<String, Object> userSession) {
        this.primaryStage = primaryStage;
        this.userSession = userSession; // Store the session details
    }

    public void showMain() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);

        HBox topBar = new HBox();

        primaryStage.setScene(new Scene(layout));
    }
}
