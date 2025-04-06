package com.example.zvote.Services;  // Package declaration, specifies the namespace


// Importing necessary classes and utilities
import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Models.ResultModel;
import com.example.zvote.Connection.DBHandler;
import com.example.zvote.Utils.CandidateMapper;
import com.example.zvote.Utils.ResultMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ResultService {
    private Connection connection;  // Database connection instance


    // Constructor to initialize database connection
    public ResultService() throws Exception {
        DBHandler dbHandler = new DBHandler();
        connection = dbHandler.getConnection();
    }


    // Method to add a result for a candidate
    public void addResult(ResultModel result) throws SQLException {
        String insertQuery = "INSERT INTO result (registration_date, votes_casted, withdrawal_date, candidate_ID, poll_ID) " +
                "VALUES (?, ?, null, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setTimestamp(1, new Timestamp(result.getRegistration_date().getTime()));
            statement.setInt(2, result.getVotes_casted());
            statement.setInt(3, result.getCandidate_ID());
            statement.setInt(4, result.getPoll_ID());
            statement.executeUpdate();
        }
    }


    // Method to fetch all results
    public List<ResultModel> getAllResults() throws SQLException {
        String query = "SELECT * FROM result";
        List<ResultModel> results = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                results.add(ResultMapper.mapResultSetToResult(resultSet));  // Map resultSet to ResultModel objects
            }
        }
        return results;
    }


    // Method to fetch a result by ID
    public ResultModel getResultByID(int result_ID) throws SQLException {
        String query = "SELECT * FROM result WHERE result_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, result_ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return ResultMapper.mapResultSetToResult(resultSet);  // Return the mapped ResultModel object
            }
        }
        return null;
    }


    // Method to fetch a result by poll ID and candidate ID
    public ResultModel getResultByPollAndCandidateID(int poll_ID, int candidate_ID) throws SQLException {
        String query = "SELECT * FROM result WHERE poll_ID = ? AND candidate_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, poll_ID);
            statement.setInt(2, candidate_ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return ResultMapper.mapResultSetToResult(resultSet);  // Return the mapped ResultModel object
            }
        }
        return null;
    }


    // Method to fetch all results of a poll
    public List<ResultModel> getResultsByPollID(int poll_ID) throws SQLException {
        String query = "SELECT * FROM result WHERE poll_ID = ?";
        List<ResultModel> results = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, poll_ID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(ResultMapper.mapResultSetToResult(resultSet));  // Map resultSet to ResultModel objects
                }
            }
        }
        return results;
    }


    // Method to fetch all results by candidate ID
    public List<ResultModel> getResultsByCandidateID(int candidate_ID) throws SQLException {
        String query = "SELECT * FROM result WHERE candidate_ID = ?";
        List<ResultModel> results = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, candidate_ID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(ResultMapper.mapResultSetToResult(resultSet));  // Map resultSet to ResultModel objects
                }
            }
        }
        return results;
    }


    // Method to update a result
    public void updateResult(ResultModel result) throws SQLException {
        String query = "UPDATE result SET registration_date = ?, votes_casted = ?, withdrawal_date = ? WHERE result_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, new Timestamp(result.getRegistration_date().getTime()));
            statement.setInt(2, result.getVotes_casted());
            if (result.getWithdrawal_date() != null) {
                statement.setTimestamp(3, new Timestamp(result.getWithdrawal_date().getTime()));
            } else {
                statement.setNull(3, Types.TIMESTAMP);
            }
            statement.setInt(4, result.getResult_ID());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Result not found.");
            }
        }
    }


    // Method to delete a result
    public void deleteResult(int result_ID) throws SQLException {
        String deleteQuery = "DELETE FROM result WHERE result_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, result_ID);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("No result found with this ID.");
            }
        }
    }


    // Method to calculate total votes for a specific poll
    public int getTotalVotesForPoll(int poll_ID) throws SQLException {
        int totalVotes = 0;
        String query = "SELECT SUM(votes_casted) AS totalVotes FROM result WHERE poll_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, poll_ID);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    totalVotes = rs.getInt("totalVotes");
                }
            }
        }
        return totalVotes;
    }


    // Method to calculate percentage of votes for a candidate
    public static double getVotePercentage(int candidateVotes, int totalVotes) {
        if (totalVotes == 0) return 0.0;
        return ((double) candidateVotes / totalVotes);
    }


    // Method to fetch candidates with votes by poll ID
    public List<CandidateModel> getCandidatesWithVotesByPollID(int poll_ID) throws SQLException {
        List<CandidateModel> candidates = new ArrayList<>();
        String query = "SELECT c.*, r.votes_casted FROM candidates c " +
                "JOIN result r ON c.candidate_ID = r.candidate_ID WHERE r.poll_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, poll_ID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    CandidateModel candidate = CandidateMapper.mapResultSetToCandidate(resultSet);
                    candidate.setVoteCount(resultSet.getInt("votes_casted"));

                    int totalVotes = getTotalVotesForPoll(poll_ID);
                    double percentage = getVotePercentage(candidate.getVoteCount(), totalVotes);
                    candidate.setVotePercentage(percentage);

                    candidates.add(candidate);
                }
            }
        }
        return candidates;
    }


    // Method to fetch the winner of a poll
    public CandidateModel getWinnerByPollID(int poll_ID) throws SQLException {
        String query = "SELECT c.* FROM candidates c " +
                "JOIN result r ON c.candidate_ID = r.candidate_ID WHERE r.poll_ID = ? " +
                "ORDER BY r.votes_casted DESC LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, poll_ID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return CandidateMapper.mapResultSetToCandidate(resultSet);
                }
            }
        }
        return null;
    }
}