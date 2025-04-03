package com.example.zvote.Connection;

import java.sql.*;

public class DBHandler {
    private static final String URL = "jdbc:mysql://10.40.3.228:3307/zvote";
    private static final String USER = "marcoUNI";
    private static final String PASSWORD = "Marco.Bitar21";

    private Connection connection;

    // Constructor to establish a persistent connection
    public DBHandler() {
        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver once
            System.out.println("Driver loaded successfully.");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");

        } catch (ClassNotFoundException e) {
            System.out.println("Error: MySQL JDBC Driver not found.");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Error: Failed to establish connection to the database.");
            e.printStackTrace();
        }
    }

    // Getter for connection
    public Connection getConnection() {
        if (connection == null) {
            System.out.println("Connection is null. Attempting to reconnect.");
            connect();
        }
        return connection;
    }

    // Method to execute SELECT queries (Safe ResultSet handling)
    public ResultSet executeQuery(String query) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            if (stmt != null) {
                return stmt.executeQuery(query);
            } else {
                System.out.println("Statement is null. Cannot execute query.");
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Method to execute INSERT, UPDATE, DELETE queries
    public int executeUpdate(String query) {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            if (stmt != null) {
                return stmt.executeUpdate(query);
            } else {
                System.out.println("Statement is null. Cannot execute update.");
            }
        } catch (SQLException e) {
            System.out.println("Error executing update: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Close the connection when the app exits
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
