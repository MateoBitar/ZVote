package com.example.zvote.Utils;

import com.example.zvote.Models.CandidateModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CandidateMapper {
    public static CandidateModel mapResultSetToCandidate(ResultSet rs) throws SQLException {
        CandidateModel candidate = new CandidateModel(
                rs.getString("name"),
                rs.getBytes("photo"),
                rs.getString("bio")
        );
        candidate.setCandidate_ID(rs.getInt("candidate_ID")); // Ensure candidate_ID is retrieved

        return candidate;
    }
}
