package com.example.zvote;

import java.sql.Connection;

public class HelloApplication {
    public static void main(String[] args) {
        DBHandler dbHandler = new DBHandler();
        Connection connection = dbHandler.getConnection(); // Get connection

        if (connection != null) {
            System.out.println("Connected successfully!");
        } else {
            System.out.println("Connection failed.");
        }

        dbHandler.closeConnection(); // Always close the connection when done
    }
}


