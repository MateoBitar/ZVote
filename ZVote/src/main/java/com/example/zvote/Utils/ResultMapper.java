package com.example.zvote.Utils;  // Package declaration, specifies the namespace


// Importing necessary classes
import com.example.zvote.Models.ResultModel;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ResultMapper {

    // Static method to map a ResultSet to a ResultModel instance
    public static ResultModel mapResultSetToResult(ResultSet rs) throws SQLException {

        // Create a ResultModel object using data from the ResultSet
        ResultModel result = new ResultModel(
                rs.getTimestamp("registration_date"), // Retrieve registration date (as timestamp) from ResultSet
                rs.getInt("candidate_ID"),            // Retrieve candidate_ID from ResultSet
                rs.getInt("poll_ID")                  // Retrieve poll_ID from ResultSet
        );

        // Set result_ID retrieved from the ResultSet to the ResultModel object
        result.setResult_ID(rs.getInt("result_ID"));

        // Set votes_casted retrieved from the ResultSet to the ResultModel object
        result.setVotes_casted(rs.getInt("votes_casted"));

        // Set withdrawal_date retrieved from the ResultSet to the ResultModel object
        result.setWithdrawal_date(rs.getTimestamp("withdrawal_date"));

        // Return the mapped ResultModel object
        return result;
    }
}