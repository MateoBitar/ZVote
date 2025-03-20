package com.example.zvote;

import java.sql.*;

public class DBHandler {
    private static final String URL = "jdbc:mysql://192.168.1.4:3307/zvote";
    private static final String USER = "marco";
    private static final String PASSWORD = "Marco.Bitar21";

    private Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver once
            System.out.println("Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    // Constructor to establish a persistent connection
    public DBHandler() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection failed.");
        }
    }

    // Getter for connection
    public Connection getConnection() {
        return connection;
    }

    // Method to execute SELECT queries (Safe ResultSet handling)
    public ResultSet executeQuery(String query) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            return stmt.executeQuery(); // ResultSet stays open because connection is open
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to execute INSERT, UPDATE, DELETE queries
    public int executeUpdate(String query) {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Close the connection when the app exits
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
