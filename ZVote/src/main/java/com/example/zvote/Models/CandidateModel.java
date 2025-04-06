package com.example.zvote.Models;  // Package declaration, specifies the namespace


// Importing necessary classes for handling file operations
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class CandidateModel {
    // Instance variables to represent the properties of a candidate
    private int candidate_ID;
    private String name;
    private byte[] photo;
    private String bio;
    private int voteCount;
    private double votePercentage;


    // Constructor to initialize a CandidateModel instance
    public CandidateModel(String name, byte[] photo, String bio) {
        setName(name);
        setPhoto(photo);
        setBio(bio);
    }


    // Getter for candidate_ID
    public int getCandidate_ID() {
        return candidate_ID;
    }


    // Setter for candidate_ID
    public void setCandidate_ID(int candidate_ID) {
        this.candidate_ID = candidate_ID;
    }


    // Getter for name
    public String getName() {
        return name;
    }


    // Setter for name
    public void setName(String name) {
        this.name = name;
    }


    // Getter for photo
    public byte[] getPhoto() {
        return photo;
    }


    // Setter for photo (assigns a default photo if none is provided)
    public void setPhoto(byte[] photo) {
        if (photo != null) {
            this.photo = photo;
        } else {
            try {
                // Use a default photo path and convert it to a byte array
                Path defaultPhotoPath = Paths.get("src/main/resources/images/Profile Pic.png");
                if (Files.exists(defaultPhotoPath)) {
                    this.photo = Files.readAllBytes(defaultPhotoPath);  // Read the file
                } else {
                    System.out.println("File not found: /images/Profile Pic.png");
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.photo = new byte[0];  // Fallback to empty byte array on error
            }
        }
    }


    // Getter for bio
    public String getBio() {
        return bio;
    }


    // Setter for bio
    public void setBio(String bio) {
        this.bio = bio;
    }


    // Getter for voteCount
    public int getVoteCount() {
        return voteCount;
    }


    // Setter for voteCount
    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }


    // Getter for votePercentage
    public double getVotePercentage() {
        return votePercentage;
    }


    // Setter for votePercentage
    public void setVotePercentage(double votePercentage) {
        this.votePercentage = votePercentage;
    }


    // toString method to provide a string representation of the CandidateModel object
    @Override
    public String toString() {
        return "candidate_Id = " + candidate_ID +
                ", name = " + name +
                ", photo = " + Arrays.toString(photo) +
                ", bio = " + bio;
    }
}