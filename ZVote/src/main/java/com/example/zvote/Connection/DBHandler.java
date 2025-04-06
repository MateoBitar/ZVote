package com.example.zvote.Connection;  // Package declaration, specifies the namespace


import java.sql.*;  // Import necessary SQL classes


public class DBHandler {
    private static final String URL = "jdbc:mysql://192.168.1.4:3307/zvote";  // Database URL
    private static final String USER = "marco";  // Database username
    private static final String PASSWORD = "Marco.Bitar21";  // Database password

    private Connection connection;  // Persistent database connection instance


    // Constructor to establish a persistent connection
    public DBHandler() {
        connect();
    }


    // Method to establish the database connection
    private void connect() {
        try {
            // Load the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");

            // Establish the database connection
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


    // Getter method to retrieve the database connection
    public Connection getConnection() {
        if (connection == null) {
            System.out.println("Connection is null. Attempting to reconnect.");
            connect();
        }
        return connection;
    }


    // Method to execute SELECT queries safely, returning a ResultSet
    public ResultSet executeQuery(String query) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);  // Create a prepared statement
            if (stmt != null) {
                return stmt.executeQuery(query);  // Execute the query
            } else {
                System.out.println("Statement is null. Cannot execute query.");
            }
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    // Method to execute INSERT, UPDATE, or DELETE queries
    public int executeUpdate(String query) {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            if (stmt != null) {
                return stmt.executeUpdate(query);  // Execute the update
            } else {
                System.out.println("Statement is null. Cannot execute update.");
            }
        } catch (SQLException e) {
            System.out.println("Error executing update: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }


    // Method to close the database connection when the application exits
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();  // Close the connection
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}