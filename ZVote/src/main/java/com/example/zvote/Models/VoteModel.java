package com.example.zvote.Models;

import java.time.LocalDateTime;

public class VoteModel {
    private int user_ID;
    private int poll_ID;
    private LocalDateTime timestamp;
    private int blank;
    private int candidate_ID;

    public VoteModel(int user_ID, int poll_ID, int blank, int candidate_ID) {
        setUser_ID(user_ID);
        setPoll_ID(poll_ID);
        setBlank(blank);
        setCandidate_ID(candidate_ID);
    }

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public int getPoll_ID() {
        return poll_ID;
    }

    public void setPoll_ID(int poll_ID) {
        this.poll_ID = poll_ID;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getBlank() {
        return blank;
    }

    public void setBlank(int blank) {
        this.blank = blank;
    }

    public int getCandidate_ID() {
        return candidate_ID;
    }

    public void setCandidate_ID(int candidate_ID) {
        this.candidate_ID = candidate_ID;
    }

    @Override
    public String toString() {
        return "user_ID = " + user_ID +
                ", poll_ID = " + poll_ID +
                ", timestamp = " + timestamp +
                ", blank = " + blank +
                ", candidate_ID = " + candidate_ID;
    }
}
