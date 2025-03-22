package com.example.zvote;

import com.example.zvote.Connection.DBHandler;

public class Main {
    public static void main(String[] args) {
        DBHandler dbHandler = new DBHandler();


        dbHandler.closeConnection(); // Always close the connection when done
    }
}