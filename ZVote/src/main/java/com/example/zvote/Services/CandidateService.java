package com.example.zvote.Services;

import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Connection.DBHandler;
import com.example.zvote.Utils.CandidateMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidateService {
    private Connection connection;

    public CandidateService() throws Exception {
        DBHandler dbHandler = new DBHandler();
        connection = dbHandler.getConnection();
    }

    // Add a candidate
    public void addCandidate(CandidateModel candidate) throws SQLException {
        String insertQuery = "INSERT INTO candidates (name, photo, bio) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, candidate.getName());
            statement.setBytes(2, candidate.getPhoto());
            statement.setString(3, candidate.getBio());
            statement.executeUpdate();
        }
    }

    // Fetch all candidates
    public List<CandidateModel> getAllCandidates() throws SQLException {
        String query = "SELECT * FROM candidates";
        List<CandidateModel> candidates = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                candidates.add(CandidateMapper.mapResultSetToCandidate(resultSet));
            }
        }
        return candidates;
    }

    // Fetch candidate by ID
    public CandidateModel getCandidateByID(int candidate_ID) throws SQLException {
        String query = "SELECT * FROM candidates WHERE candidate_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, candidate_ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return CandidateMapper.mapResultSetToCandidate(resultSet);
            }
        }
        return null;
    }

    // Fetch all candidates linked to a poll via the result table
    public List<CandidateModel> getCandidatesByPollID(int poll_ID) throws SQLException {
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

    // Update a candidate
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

    // Delete a candidate
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
}
