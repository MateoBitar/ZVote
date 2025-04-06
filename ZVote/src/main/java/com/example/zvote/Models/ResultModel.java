package com.example.zvote.Models;  // Package declaration, specifies the namespace


// Importing necessary class for handling dates
import java.util.Date;


public class ResultModel {
    // Instance variables to represent the properties of a result
    private int result_ID;
    private Date registration_date;
    private int votes_casted;
    private Date withdrawal_date;
    private int candidate_ID;
    private int poll_ID;


    // Constructor to initialize a ResultModel instance
    public ResultModel(Date registration_date, int candidate_ID, int poll_ID) {
        setRegistration_date(registration_date);
        setWithdrawal_date(null);  // Default withdrawal date is set to null
        setCandidate_ID(candidate_ID);
        setPoll_ID(poll_ID);
    }


    // Getter for result_ID
    public int getResult_ID() {
        return result_ID;
    }


    // Setter for result_ID
    public void setResult_ID(int result_ID) {
        this.result_ID = result_ID;
    }


    // Getter for registration_date
    public Date getRegistration_date() {
        return registration_date;
    }


    // Setter for registration_date
    public void setRegistration_date(Date registration_date) {
        this.registration_date = registration_date;
    }


    // Getter for votes_casted
    public int getVotes_casted() {
        return votes_casted;
    }


    // Setter for votes_casted
    public void setVotes_casted(int votes_casted) {
        this.votes_casted = votes_casted;
    }


    // Getter for withdrawal_date
    public Date getWithdrawal_date() {
        return withdrawal_date;
    }


    // Setter for withdrawal_date
    public void setWithdrawal_date(Date withdrawal_date) {
        this.withdrawal_date = withdrawal_date;
    }


    // Getter for candidate_ID
    public int getCandidate_ID() {
        return candidate_ID;
    }


    // Setter for candidate_ID
    public void setCandidate_ID(int candidate_ID) {
        this.candidate_ID = candidate_ID;
    }


    // Getter for poll_ID
    public int getPoll_ID() {
        return poll_ID;
    }


    // Setter for poll_ID
    public void setPoll_ID(int poll_ID) {
        this.poll_ID = poll_ID;
    }


    // toString method to provide a string representation of the ResultModel object
    @Override
    public String toString() {
        return "result_ID = " + result_ID +
                ", registration_date = " + registration_date +
                ", votes_casted = " + votes_casted +
                ", withdrawal_date = " + withdrawal_date +
                ", candidate_ID = " + candidate_ID +
                ", poll_ID = " + poll_ID;
    }
}