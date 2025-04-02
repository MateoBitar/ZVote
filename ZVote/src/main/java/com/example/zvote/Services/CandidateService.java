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
        String insertQuery = "INSERT INTO candidates (name, photo, bio, poll_ID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, candidate.getName());
            statement.setBytes(2, candidate.getPhoto());
            statement.setString(3, candidate.getBio());
            statement.setInt(4, candidate.getPoll_ID());
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

    // Fetch all candidates of a poll
    public List<CandidateModel> getCandidatesByPollID(int poll_ID) throws SQLException {
        String query = "SELECT * FROM candidates WHERE poll_ID = ?";
        List<CandidateModel> candidates = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, poll_ID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    candidates.add(CandidateMapper.mapResultSetToCandidate(resultSet));
                }
            }
        }
        return candidates;
    }

    // Update a candidate
    public void updateCandidate(CandidateModel candidate) throws SQLException {
        String query = "UPDATE candidates SET name = ?, photo = ?, bio = ?, poll_ID = ? WHERE candidate_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, candidate.getName());
            statement.setBytes(2, candidate.getPhoto());
            statement.setString(3, candidate.getBio());
            statement.setInt(4, candidate.getPoll_ID());
            statement.setInt(5, candidate.getCandidate_ID());
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
