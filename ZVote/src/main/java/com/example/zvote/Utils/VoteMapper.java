package com.example.zvote.Utils;  // Package declaration, specifies the namespace

// Importing necessary classes
import com.example.zvote.Models.VoteModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteMapper {
    // Static method to map a ResultSet to a VoteModel instance
    public static VoteModel mapResultSetToVote(ResultSet rs) throws SQLException {

        // Create a VoteModel object using data from the ResultSet
        VoteModel vote = new VoteModel(
                rs.getInt("user_ID"),           // Retrieve user_ID from ResultSet
                rs.getInt("poll_ID"),           // Retrieve poll_ID from ResultSet
                rs.getInt("blank"),             // Retrieve blank value (e.g., possibly a flag or counter) from ResultSet
                rs.getInt("candidate_ID")       // Retrieve candidate_ID from ResultSet
        );

        // Set timestamp from ResultSet to the VoteModel object as LocalDateTime
        vote.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());

        // Return the mapped VoteModel object
        return vote;
    }
}