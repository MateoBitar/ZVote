package com.example.zvote.Utils;

import com.example.zvote.Models.VoteModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VoteMapper {
    public static VoteModel mapResultSetToVote(ResultSet rs) throws SQLException {
        VoteModel vote = new VoteModel(
                rs.getInt("user_ID"),
                rs.getInt("poll_ID"),
                rs.getInt("blank"),
                rs.getInt("candidate_ID")
        );
        vote.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime()); // Ensure timestamp is retrieved

        return vote;
    }
}
