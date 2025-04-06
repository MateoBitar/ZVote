package com.example.zvote.Services;  // Package declaration, specifies the namespace


// Importing necessary classes and utilities
import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Connection.DBHandler;
import com.example.zvote.Utils.CandidateMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CandidateService {
    private Connection connection;  // Database connection instance


    // Constructor to initialize the database connection
    public CandidateService() throws Exception {
        DBHandler dbHandler = new DBHandler();
        connection = dbHandler.getConnection();
    }


    // Method to add a new candidate
    public void addCandidate(CandidateModel candidate) throws SQLException {

        // Check if the candidate name already exists in the database
        String selectQuery = "SELECT COUNT(*) FROM candidates WHERE name = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setString(1, candidate.getName());
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    throw new SQLException("A candidate with the same name already exists.");
                }
            }
        }

        // Insert the new candidate if the name is unique
        String insertQuery = "INSERT INTO candidates (name, photo, bio) VALUES (?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, candidate.getName());
            insertStatement.setBytes(2, candidate.getPhoto());
            insertStatement.setString(3, candidate.getBio());
            insertStatement.executeUpdate();
        }
    }


    // Method to fetch all candidates from the database
    public List<CandidateModel> getAllCandidates() throws SQLException {
        String query = "SELECT * FROM candidates";
        List<CandidateModel> candidates = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                candidates.add(CandidateMapper.mapResultSetToCandidate(resultSet));  // Map resultSet to CandidateModel objects
            }
        }
        return candidates;
    }


    // Method to fetch a candidate by ID
    public CandidateModel getCandidateByID(int candidate_ID) throws SQLException {
        String query = "SELECT * FROM candidates WHERE candidate_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, candidate_ID);  // Set candidate_ID parameter
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return CandidateMapper.mapResultSetToCandidate(resultSet);  // Return the mapped CandidateModel object
            }
        }
        return null;
    }


    // Method to fetch all candidates linked to a poll via the result table
    public List<CandidateModel> getCandidatesByPollID(int poll_ID) throws SQLException {

        // Query to fetch candidate IDs linked to the poll
        String queryFetchCandidateIds = "SELECT candidate_ID FROM result WHERE poll_ID = ?";
        String queryFetchCandidates = "SELECT * FROM candidates WHERE candidates.candidate_ID = ?";
        List<CandidateModel> candidates = new ArrayList<>();

        try (PreparedStatement statementCandidateIds = connection.prepareStatement(queryFetchCandidateIds)) {
            statementCandidateIds.setInt(1, poll_ID);
            try (ResultSet resultSetCandidateIds = statementCandidateIds.executeQuery()) {
                while (resultSetCandidateIds.next()) {
                    int candidateId = resultSetCandidateIds.getInt("candidate_ID");

                    // Fetch candidate details for each candidate ID
                    try (PreparedStatement statementCandidates = connection.prepareStatement(queryFetchCandidates)) {
                        statementCandidates.setInt(1, candidateId);
                        try (ResultSet resultSetCandidates = statementCandidates.executeQuery()) {
                            while (resultSetCandidates.next()) {
                                candidates.add(CandidateMapper.mapResultSetToCandidate(resultSetCandidates));
                            }
                        }
                    }
                }
            }
        }
        return candidates;
    }


    // Method to update a candidate's details
    public void updateCandidate(CandidateModel candidate) throws SQLException {
        String query = "UPDATE candidates SET name = ?, photo = ?, bio = ? WHERE candidate_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, candidate.getName());
            statement.setBytes(2, candidate.getPhoto());
            statement.setString(3, candidate.getBio());
            statement.setInt(4, candidate.getCandidate_ID());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Candidate not found.");
            }
        }
    }


    // Method to delete a candidate by ID
    public void deleteCandidate(int candidate_ID) throws SQLException {
        String deleteQuery = "DELETE FROM candidates WHERE candidate_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, candidate_ID);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("No candidate found with this ID.");
            }
        }
    }


    // Method to check if a candidate name is already taken
    public boolean isNameTaken(String name) throws SQLException {
        String query = "SELECT COUNT(*) FROM candidates WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;  // Returns true if name exists
                }
            }
        }
        return false;
    }
}