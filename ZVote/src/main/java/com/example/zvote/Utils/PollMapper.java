package com.example.zvote.Utils;  // Package declaration, specifies the namespace


// Importing necessary classes
import com.example.zvote.Models.PollModel;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PollMapper {

    // Static method to map a ResultSet to a PollModel instance
    public static PollModel mapResultSetToPoll(ResultSet rs) throws SQLException {

        // Create a PollModel object using data from the ResultSet
        PollModel poll = new PollModel(
                rs.getString("title"),            // Retrieve the poll title from ResultSet
                rs.getString("description"),      // Retrieve the poll description from ResultSet
                rs.getTimestamp("start_date"),    // Retrieve the start date (as timestamp) from ResultSet
                rs.getTimestamp("end_date"),      // Retrieve the end date (as timestamp) from ResultSet
                rs.getInt("admin_ID")             // Retrieve the admin ID who created the poll from ResultSet
        );

        // Set poll_ID retrieved from the ResultSet to the PollModel object
        poll.setPoll_ID(rs.getInt("poll_ID"));

        // Retrieve and convert the status field (ensure proper case conversion)
        String statusString = rs.getString("status");
        PollModel.Status status = PollModel.Status.valueOf(statusString.toUpperCase());

        // Set number of votes retrieved from the ResultSet to the PollModel object
        poll.setNbOfVotes(rs.getInt("nbOfVotes"));

        // Set number of abstentions retrieved from the ResultSet to the PollModel object
        poll.setNbOfAbstentions(rs.getInt("nbOfAbstentions"));

        // Return the mapped PollModel object
        return poll;
    }
}