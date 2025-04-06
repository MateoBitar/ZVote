package com.example.zvote.Models;  // Package declaration, specifies the namespace


// Importing necessary class for timestamp handling
import java.time.LocalDateTime;


public class VoteModel {
    // Instance variables to represent the properties of a vote
    private int user_ID;
    private int poll_ID;
    private LocalDateTime timestamp;
    private int blank;
    private int candidate_ID;


    // Constructor to initialize a VoteModel instance
    public VoteModel(int user_ID, int poll_ID, int blank, int candidate_ID) {
        setUser_ID(user_ID);
        setPoll_ID(poll_ID);
        setBlank(blank);
        setCandidate_ID(candidate_ID);
    }


    // Getter for user_ID
    public int getUser_ID() {
        return user_ID;
    }


    // Setter for user_ID
    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }


    // Getter for poll_ID
    public int getPoll_ID() {
        return poll_ID;
    }


    // Setter for poll_ID
    public void setPoll_ID(int poll_ID) {
        this.poll_ID = poll_ID;
    }


    // Getter for timestamp
    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    // Setter for timestamp
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }


    // Getter for blank (represents abstention or blank vote flag)
    public int getBlank() {
        return blank;
    }


    // Setter for blank
    public void setBlank(int blank) {
        this.blank = blank;
    }


    // Getter for candidate_ID
    public int getCandidate_ID() {
        return candidate_ID;
    }


    // Setter for candidate_ID
    public void setCandidate_ID(int candidate_ID) {
        this.candidate_ID = candidate_ID;
    }


    // toString method to provide a string representation of the VoteModel object
    @Override
    public String toString() {
        return "user_ID = " + user_ID +
                ", poll_ID = " + poll_ID +
                ", timestamp = " + timestamp +
                ", blank = " + blank +
                ", candidate_ID = " + candidate_ID;
    }
}