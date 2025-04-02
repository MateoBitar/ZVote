package com.example.zvote.Models;

import java.util.Date;

public class ResultModel {
    private int result_ID;
    private Date registration_date;
    private double fees_paid;
    private int votes_casted;
    private Date withdrawal_date;
    private int candidate_ID;
    private int poll_ID;

    public ResultModel(Date registration_date, double fees_paid, int candidate_ID, int poll_ID) {
        setRegistration_date(registration_date);
        setFees_paid(fees_paid);
        setWithdrawal_date(null);
        setCandidate_ID(candidate_ID);
        setPoll_ID(poll_ID);
    }

    public int getResult_ID() {
        return result_ID;
    }

    public void setResult_ID(int result_ID) {
        this.result_ID = result_ID;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }

    public double getFees_paid() {
        return fees_paid;
    }

    public void setFees_paid(double fees_paid) {
        this.fees_paid = fees_paid;
    }

    public int getVotes_casted() {
        return votes_casted;
    }

    public void setVotes_casted(int votes_casted) {
        this.votes_casted = votes_casted;
    }

    public Date getWithdrawal_date() {
        return withdrawal_date;
    }

    public void setWithdrawal_date(Date withdrawal_date) {
        this.withdrawal_date = withdrawal_date;
    }

    public int getCandidate_ID() {
        return candidate_ID;
    }

    public void setCandidate_ID(int candidate_ID) {
        this.candidate_ID = candidate_ID;
    }

    public int getPoll_ID() {
        return poll_ID;
    }

    public void setPoll_ID(int poll_ID) {
        this.poll_ID = poll_ID;
    }

    @Override
    public String toString() {
        return "result_ID = " + result_ID +
                ", registration_date = " + registration_date +
                ", fees_paid = " + fees_paid +
                ", votes_casted = " + votes_casted +
                ", withdrawal_date = " + withdrawal_date +
                ", candidate_ID = " + candidate_ID +
                ", poll_ID = " + poll_ID;
    }
}
