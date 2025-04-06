package com.example.zvote.Models;  // Package declaration, specifies the namespace


// Importing necessary class for handling dates
import java.util.Date;


public class PollModel {
    // Instance variables to represent the properties of a poll
    private int poll_ID;
    private String title;
    private String description;
    private Date start_date;
    private Date end_date;

    // Enum for poll statuses (INACTIVE, ACTIVE, COMPLETED)
    public enum Status {
        INACTIVE,
        ACTIVE,
        COMPLETED
    }
    private Status status;
    private int nbOfVotes;
    private int nbOfAbstentions;
    private int admin_ID;


    // Constructor to initialize a PollModel instance
    public PollModel(String title, String description, Date start_date, Date end_date, int admin_ID) {
        setTitle(title);
        setDescription(description);
        setStart_date(start_date);
        setEnd_date(end_date);
        setAdmin_ID(admin_ID);
        setNbOfVotes(0);  // Initialize number of votes to 0
        setNbOfAbstentions(0);  // Initialize number of abstentions to 0
    }


    // Getter for poll_ID
    public int getPoll_ID() {
        return poll_ID;
    }


    // Setter for poll_ID
    public void setPoll_ID(int poll_ID) {
        this.poll_ID = poll_ID;
    }


    // Getter for title
    public String getTitle() {
        return title;
    }


    // Setter for title
    public void setTitle(String title) {
        this.title = title;
    }


    // Getter for description
    public String getDescription() {
        return description;
    }


    // Setter for description
    public void setDescription(String description) {
        this.description = description;
    }


    // Getter for start_date
    public Date getStart_date() {
        return start_date;
    }


    // Setter for start_date
    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }


    // Getter for end_date
    public Date getEnd_date() {
        return end_date;
    }


    // Setter for end_date
    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }


    // Getter for status
    public Status getStatus() {
        return status;
    }


    // Setter for status
    public void setStatus(Status status) {
        this.status = status;
    }


    // Getter for nbOfVotes (number of votes)
    public int getNbOfVotes() {
        return nbOfVotes;
    }


    // Setter for nbOfVotes
    public void setNbOfVotes(int nbOfVotes) {
        this.nbOfVotes = nbOfVotes;
    }


    // Getter for nbOfAbstentions (number of abstentions)
    public int getNbOfAbstentions() {
        return nbOfAbstentions;
    }


    // Setter for nbOfAbstentions
    public void setNbOfAbstentions(int nbOfAbstentions) {
        this.nbOfAbstentions = nbOfAbstentions;
    }


    // Getter for admin_ID
    public int getAdmin_ID() {
        return admin_ID;
    }


    // Setter for admin_ID
    public void setAdmin_ID(int admin_ID) {
        this.admin_ID = admin_ID;
    }


    // toString method to provide a string representation of the PollModel object
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