package com.example.zvote.Models;

import java.util.Date;

public class PollModel {
    private int poll_ID;
    private String title;
    private String description;
    private Date start_date;
    private Date end_date;

    public enum Status {
        INACTIVE,
        ACTIVE,
        COMPLETED
    }
    private Status status;
    private int nbOfVotes;
    private int nbOfAbstentions;
    private int admin_ID;

    public PollModel(String title, String description, Date start_date, Date end_date, int admin_ID) {
        setTitle(title);
        setDescription(description);
        setStart_date(start_date);
        setEnd_date(end_date);
        setAdmin_ID(admin_ID);
    }

    public int getPoll_ID() {
        return poll_ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Status getStatus() {
        return status;
    }

    public int getNbOfVotes() {
        return nbOfVotes;
    }

    public int getNbOfAbstentions() {
        return nbOfAbstentions;
    }

    public int getAdmin_ID() {
        return admin_ID;
    }

    public void setAdmin_ID(int admin_ID) {
        this.admin_ID = admin_ID;
    }

    @Override
    public String toString() {
        return "poll_ID = " + poll_ID +
                ", title = " + title +
                ", description = " + description +
                ", start_date = " + start_date +
                ", end_date = " + end_date +
                ", status = " + status +
                ", nbOfVotes = " + nbOfVotes +
                ", nbOfAbstentions = " + nbOfAbstentions +
                ", admin_ID = " + admin_ID;
    }
}
