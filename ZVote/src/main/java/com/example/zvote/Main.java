package com.example.zvote;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        DBHandler dbHandler = new DBHandler();
        Connection connection = dbHandler.getConnection(); // Get connection


        dbHandler.closeConnection(); // Always close the connection when done
    }
}


