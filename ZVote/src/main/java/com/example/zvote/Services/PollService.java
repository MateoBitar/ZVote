package com.example.zvote.Services;  // Package declaration, specifies the namespace


// Importing necessary classes and utilities
import com.example.zvote.Models.PollModel;
import com.example.zvote.Connection.DBHandler;
import com.example.zvote.Utils.PollMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PollService {
    private Connection connection;  // Database connection instance


    // Constructor to initialize database connection
    public PollService() throws Exception {
        DBHandler dbHandler = new DBHandler();
        connection = dbHandler.getConnection();
    }


    // Method to add a new poll to the database
    public int addPoll(PollModel poll) throws SQLException {

        // Step 1: Check if the poll title already exists in the database
        String checkQuery = "SELECT COUNT(*) FROM polls WHERE title = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setString(1, poll.getTitle());  // Set the parameter for title
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    throw new IllegalArgumentException("Poll title already exists. Please choose another title.");
                }
            }
        }

        // Step 2: Insert the new poll into the database
        String insertQuery = "INSERT INTO polls (title, description, start_date, end_date, nbOfVotes, nbOfAbstentions, admin_ID) " +
                "VALUES (?, ?, ?, ?, 0, 0, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, poll.getTitle());  // Set the title
            insertStatement.setString(2, poll.getDescription());  // Set the description
            insertStatement.setTimestamp(3, new Timestamp(poll.getStart_date().getTime()));  // Set the start date
            insertStatement.setTimestamp(4, new Timestamp(poll.getEnd_date().getTime()));  // Set the end date
            insertStatement.setInt(5, poll.getAdmin_ID());  // Set the admin ID

            // Execute the insert query
            insertStatement.executeUpdate();

            // Retrieve the generated poll_ID
            try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);  // Return the generated poll_ID
                } else {
                    throw new SQLException("Creating poll failed, no poll_ID obtained.");
                }
            }
        }
    }


    // Method to fetch all polls from the database
    public List<PollModel> getAllPolls() throws SQLException {
        String query = "SELECT * FROM polls";
        List<PollModel> polls = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                polls.add(PollMapper.mapResultSetToPoll(resultSet));  // Map resultSet to PollModel objects
            }
        }
        return polls;
    }


    // Method to fetch a poll by its title
    public PollModel getPollByTitle(String title) throws SQLException {
        String query = "SELECT * FROM polls WHERE title = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);  // Set the title parameter
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return PollMapper.mapResultSetToPoll(resultSet);  // Return the mapped PollModel object
            }
        }
        return null;
    }


    // Method to update an existing poll
    public void updatePoll(PollModel poll) throws SQLException {
        String query = "UPDATE polls SET title = ?, description = ?, start_date = ?, end_date = ?, nbOfVotes = ?, " +
                "nbOfAbstentions = ? WHERE poll_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, poll.getTitle());
            statement.setString(2, poll.getDescription());
            statement.setTimestamp(3, new Timestamp(poll.getStart_date().getTime()));
            statement.setTimestamp(4, new Timestamp(poll.getEnd_date().getTime()));
            statement.setInt(5, poll.getNbOfVotes());
            statement.setInt(6, poll.getNbOfAbstentions());
            statement.setInt(7, poll.getPoll_ID());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new IllegalArgumentException("Poll not found.");
            }
        }
    }


    // Method to delete a poll by its ID
    public void deletePoll(int poll_ID) throws SQLException {
        String deleteQuery = "DELETE FROM polls WHERE poll_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, poll_ID);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new IllegalArgumentException("No poll found with this ID.");
            }
        }
    }
}