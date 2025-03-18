package com.example.zvote;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloApplication  {
    public static void main(String[] args)
            throws SQLException, ClassNotFoundException {
        // Load the JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Driver loaded");

        // Connect to a database
        Connection connection = DriverManager.getConnection
                ("jdbc:mysql://192.168.1.4:3307/zvote" , "marco", "Marco.Bitar21");
        System.out.println("Database connected");

        // Create a statement
        Statement statement = connection.createStatement();

        // Close the connection
        connection.close();
    }

}
