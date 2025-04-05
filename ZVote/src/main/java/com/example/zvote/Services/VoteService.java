package com.example.zvote.Services;

import com.example.zvote.Models.VoteModel;
import com.example.zvote.Connection.DBHandler;
import com.example.zvote.Utils.VoteMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoteService {
    private Connection connection;

    public VoteService() throws Exception {
        DBHandler dbHandler = new DBHandler();
        connection = dbHandler.getConnection();
    }

    // Add a vote
    public void addVote(VoteModel vote) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM votes WHERE user_ID = ? AND poll_ID = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
            checkStatement.setInt(1, vote.getUser_ID());
            checkStatement.setInt(2, vote.getPoll_ID());
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                throw new SQLException("User has already voted in this poll.");
            }
        }

        String query = "INSERT INTO votes (user_ID, poll_ID, blank, candidate_ID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, vote.getUser_ID());
            statement.setInt(2, vote.getPoll_ID());
            statement.setInt(3, vote.getBlank());
            if (vote.getCandidate_ID() == 0) {
                statement.setNull(4, java.sql.Types.INTEGER); // Set candidate_ID as NULL for abstention votes
            } else {
                statement.setInt(4, vote.getCandidate_ID()); // Set candidate_ID for normal votes
            }
            statement.executeUpdate();
        }
    }

    // Fetch all votes
    public List<VoteModel> getAllVotes() throws SQLException {
        String query = "SELECT * FROM votes";
        List<VoteModel> votes = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                votes.add(VoteMapper.mapResultSetToVote(resultSet));
            }
        }
        return votes;
    }

    public boolean hasUserVoted(int userID, int pollID) throws SQLException {
        String query = "SELECT * FROM votes WHERE user_ID = ? AND poll_ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userID);
            statement.setInt(2, pollID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Returns true if a row exists
        }
    }
}
