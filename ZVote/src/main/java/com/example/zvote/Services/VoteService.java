package com.example.zvote.Services;  // Package declaration, specifies the namespace


// Importing necessary classes and utilities
import com.example.zvote.Models.VoteModel;
import com.example.zvote.Connection.DBHandler;
import com.example.zvote.Utils.VoteMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class VoteService {
    private Connection connection;  // Database connection instance


    // Constructor to initialize database connection
    public VoteService() throws Exception {
        DBHandler dbHandler = new DBHandler();
        connection = dbHandler.getConnection();
    }


    // Method to add a vote to the database
    public void addVote(VoteModel vote) throws SQLException {

        // Query to check if the user has already voted in the poll
        String checkQuery = "SELECT COUNT(*) FROM votes WHERE user_ID = ? AND poll_ID = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setInt(1, vote.getUser_ID());  // Set user_ID parameter
            checkStatement.setInt(2, vote.getPoll_ID());  // Set poll_ID parameter
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                throw new SQLException("User has already voted in this poll.");  // Throw exception if already voted
            }
        }

        // Query to insert a new vote
        String query = "INSERT INTO votes (user_ID, poll_ID, blank, candidate_ID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, vote.getUser_ID());
            statement.setInt(2, vote.getPoll_ID());
            statement.setInt(3, vote.getBlank());

            // Handle candidate_ID for abstention votes or normal votes
            if (vote.getCandidate_ID() == 0) {
                statement.setNull(4, java.sql.Types.INTEGER);  // Set candidate_ID as NULL for abstention votes
            } else {
                statement.setInt(4, vote.getCandidate_ID());  // Set candidate_ID for normal votes
            }
            statement.executeUpdate();  // Execute the insertion
        }
    }


    // Method to fetch all votes from the database
    public List<VoteModel> getAllVotes() throws SQLException {
        String query = "SELECT * FROM votes";
        List<VoteModel> votes = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                votes.add(VoteMapper.mapResultSetToVote(resultSet));  // Map resultSet to VoteModel objects
            }
        }
        return votes;
    }


    // Method to fetch votes by poll ID
    public List<VoteModel> getVotesByPollID(int poll_ID) throws SQLException {
        String query = "SELECT * FROM votes WHERE poll_ID = ?";
        List<VoteModel> votes = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, poll_ID);  // Set poll_ID parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    votes.add(VoteMapper.mapResultSetToVote(resultSet));  // Map resultSet to VoteModel objects
                }
            }
        }
        return votes;
    }


    // Method to fetch votes by candidate ID
    public List<VoteModel> getVotesByCandidateID(int candidate_ID) throws SQLException {
        String query = "SELECT * FROM votes WHERE candidate_ID = ?";
        List<VoteModel> votes = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, candidate_ID);  // Set candidate_ID parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    votes.add(VoteMapper.mapResultSetToVote(resultSet));  // Map resultSet to VoteModel objects
                }
            }
        }
        return votes;
    }


    // Method to delete a vote by user ID and poll ID
    public void deleteVote(int user_ID, int poll_ID) throws SQLException {
        String query = "DELETE FROM votes WHERE user_ID = ? AND poll_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user_ID);  // Set user_ID parameter
            statement.setInt(2, poll_ID);  // Set poll_ID parameter
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new IllegalArgumentException("No vote found with these IDs.");  // Throw exception if no match found
            }
        }
    }


    // Method to check if a user has voted in a specific poll
    public boolean hasUserVoted(int userID, int pollID) throws SQLException {
        String query = "SELECT * FROM votes WHERE user_ID = ? AND poll_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);  // Set user_ID parameter
            statement.setInt(2, pollID);  // Set poll_ID parameter
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();  // Returns true if a row exists
        }
    }
}