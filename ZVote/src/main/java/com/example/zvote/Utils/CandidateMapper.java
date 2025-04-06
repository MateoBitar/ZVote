package com.example.zvote.Utils;  // Package declaration, specifies the namespace


// Importing necessary classes
import com.example.zvote.Models.CandidateModel;

import java.sql.ResultSet;
import java.sql.SQLException;


public class CandidateMapper {

    // Static method to map a ResultSet to a CandidateModel instance
    public static CandidateModel mapResultSetToCandidate(ResultSet rs) throws SQLException {

        // Create a CandidateModel object using data from the ResultSet
        CandidateModel candidate = new CandidateModel(
                rs.getString("name"),             // Retrieve candidate name from ResultSet
                rs.getBytes("photo"),            // Retrieve photo (binary data) from ResultSet
                rs.getString("bio")              // Retrieve candidate bio from ResultSet
        );

        // Set candidate_ID retrieved from the ResultSet to the CandidateModel object
        candidate.setCandidate_ID(rs.getInt("candidate_ID"));

        // Return the mapped CandidateModel object
        return candidate;
    }
}