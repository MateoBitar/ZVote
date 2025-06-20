module com.example.zvote {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires dotenv.java;

    opens com.example.zvote to javafx.fxml;
    exports com.example.zvote;
    exports com.example.zvote.Connection;
    opens com.example.zvote.Connection to javafx.fxml;
}