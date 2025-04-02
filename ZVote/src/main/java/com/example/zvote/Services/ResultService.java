package com.example.zvote.Services;

import com.example.zvote.Models.CandidateModel;
import com.example.zvote.Models.ResultModel;
import com.example.zvote.Connection.DBHandler;
import com.example.zvote.Utils.CandidateMapper;
import com.example.zvote.Utils.ResultMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultService {
    private Connection connection;

    public ResultService() throws Exception {
        DBHandler dbHandler = new DBHandler();
        connection = dbHandler.getConnection();
    }

    // Add a result for candidate
    public void addResult(ResultModel result) throws SQLException {
        String insertQuery = "INSERT INTO result (registration_date, fees_paid, votes_casted, withdrawal_date," +
                " candidate_ID, poll_ID) VALUES (?, ?, ?, null, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setTimestamp(1, new Timestamp(result.getRegistration_date().getTime()));
            statement.setDouble(2, result.getFees_paid());
            statement.setInt(3, result.getVotes_casted());
            statement.setInt(4, result.getCandidate_ID());
            statement.setInt(5, result.getPoll_ID());
            statement.executeUpdate();
        }
    }

    // Fetch all results
    public List<ResultModel> getAllResults() throws SQLException {
        String query = "SELECT * FROM result";
        List<ResultModel> results = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                results.add(ResultMapper.mapResultSetToResult(resultSet));
            }
        }
        return results;
    }

    // Fetch result by ID
    public ResultModel getResultByID(int result_ID) throws SQLException {
        String query = "SELECT * FROM result WHERE result_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, result_ID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return ResultMapper.mapResultSetToResult(resultSet);
            }
        }
        return null;
    }

    // Fetch all results of a poll
    public List<ResultModel> getResultsByPollID(int poll_ID) throws SQLException {
        String query = "SELECT * FROM result WHERE poll_ID = ?";
        List<ResultModel> results = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, poll_ID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(ResultMapper.mapResultSetToResult(resultSet));
                }
            }
        }
        return results;
    }

    // Update a result
    public void updateResult(ResultModel result) throws SQLException {
        String query = "UPDATE result SET registration_date = ?, fees_paid = ?, votes_casted = ?, withdrawal_date = ?" +
                " WHERE result_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, new Timestamp(result.getRegistration_date().getTime()));
            statement.setDouble(2, result.getFees_paid());
            statement.setInt(3, result.getVotes_casted());
            if (result.getWithdrawal_date() != null) {
                statement.setTimestamp(4, new Timestamp(result.getWithdrawal_date().getTime()));
            } else {
                statement.setNull(4, Types.TIMESTAMP);
            }
            statement.setInt(5, result.getResult_ID());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Result not found.");
            }
        }
    }

    // Delete a result
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

    // Fetch winner of a poll
    public CandidateModel getWinnerByPollID(int poll_ID) throws SQLException {
        String query = "SELECT c.* FROM candidates c " +
                "JOIN result r ON c.candidate_ID = r.candidate_ID " +
                "WHERE r.poll_ID = ? ORDER BY r.votes_casted DESC LIMIT 1";
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
