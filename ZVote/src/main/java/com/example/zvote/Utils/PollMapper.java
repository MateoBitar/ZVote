package com.example.zvote.Utils;

import com.example.zvote.Models.PollModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PollMapper {
    public static PollModel mapResultSetToPoll(ResultSet rs) throws SQLException {
        PollModel poll = new PollModel(
                rs.getString("title"),
                rs.getString("description"),
                rs.getTimestamp("start_date"),
                rs.getTimestamp("end_date"),
                rs.getInt("admin_ID")
        );
        poll.setPoll_ID(rs.getInt("poll_ID")); // Ensure poll_ID is retrieved
        return poll;
    }
}
