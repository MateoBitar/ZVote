package com.example.zvote.Utils;

import com.example.zvote.Models.ResultModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultMapper {
    public static ResultModel mapResultSetToResult(ResultSet rs) throws SQLException {
        ResultModel result = new ResultModel(
                rs.getTimestamp("registration_date"),
                rs.getInt("candidate_ID"),
                rs.getInt("poll_ID")
        );
        result.setResult_ID(rs.getInt("result_ID")); // Ensure result_ID is retrieved

        result.setVotes_casted(rs.getInt("votes_casted"));
        result.setWithdrawal_date(rs.getTimestamp("withdrawal_date"));

        return result;
    }
}
