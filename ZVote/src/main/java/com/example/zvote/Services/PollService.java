package com.example.zvote.Services;

import com.example.zvote.Models.PollModel;
import com.example.zvote.Connection.DBHandler;
import com.example.zvote.Utils.PollMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PollService {
    private Connection connection;

    public PollService() throws Exception {
        DBHandler dbHandler = new DBHandler();
        connection = dbHandler.getConnection();
    }

    // Add a poll
    public void addPoll(PollModel poll) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM polls WHERE title = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
             ResultSet resultSet = checkStatement.executeQuery()) {
            checkStatement.setString(1, poll.getTitle());
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                throw new IllegalArgumentException("Poll title already exists. Please choose another title.");
            }
        }

        String insertQuery = "INSERT INTO polls (title, description, start_date, end_date, nbOfVotes, nbOfAbstentions" +
                ", admin_ID) VALUES (?, ?, ?, ?, 0, 0, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, poll.getTitle());
            statement.setString(2, poll.getDescription());
            statement.setTimestamp(3, new Timestamp(poll.getStart_date().getTime()));
            statement.setTimestamp(4, new Timestamp(poll.getEnd_date().getTime()));
            statement.setInt(5, poll.getAdmin_ID());
            statement.executeUpdate();
        }
    }

    // Fetch all polls
    public List<PollModel> getAllPolls() throws SQLException {
        String query = "SELECT * FROM polls";
        List<PollModel> polls = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                polls.add(PollMapper.mapResultSetToPoll(resultSet));
            }
        }
        return polls;
    }

    // Fetch poll by title
    public PollModel getPollByTitle(String title) throws SQLException {
        String query = "SELECT * FROM polls WHERE title = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return PollMapper.mapResultSetToPoll(resultSet);
            }
        }
        return null;
    }

    // Update a poll
    public void updatePoll(PollModel poll) throws SQLException {
        String query = "UPDATE polls SET title = ?, description = ?, start_date = ?, end_date = ? WHERE poll_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, poll.getTitle());
            statement.setString(2, poll.getDescription());
            statement.setTimestamp(3, new Timestamp(poll.getStart_date().getTime()));
            statement.setTimestamp(4, new Timestamp(poll.getEnd_date().getTime()));
            statement.setInt(5, poll.getPoll_ID());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new IllegalArgumentException("Poll not found.");
            }
        }
    }

    //Delete a poll
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
