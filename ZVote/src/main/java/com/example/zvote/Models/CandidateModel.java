package com.example.zvote.Models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class CandidateModel {
    private int candidate_ID;
    private String name;
    private byte[] photo;
    private String bio;
    private int voteCount;
    private double votePercentage;


    public CandidateModel(String name, byte[] photo, String bio) {
        setName(name);
        setPhoto(photo);
        setBio(bio);
    }

    public int getCandidate_ID() {
        return candidate_ID;
    }

    public void setCandidate_ID(int candidate_ID) {
        this.candidate_ID = candidate_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        if(photo != null){
            this.photo = photo;
        }
        else {
            try {
                // Use a default photo path and convert it to a byte array
                Path defaultPhotoPath = Paths.get("src/main/resources/images/Profile Pic.png");
                if (Files.exists(defaultPhotoPath)) {
                    // Proceed with reading the file
                    this.photo = Files.readAllBytes(defaultPhotoPath);
                } else {
                    System.out.println("File not found: /images/Profile Pic.png");
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.photo = new byte[0]; // Fallback to empty byte array on error
            }

        }

    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public double getVotePercentage() {
        return votePercentage;
    }

    public void setVotePercentage(double votePercentage) {
        this.votePercentage = votePercentage;
    }

    @Override
    public String toString() {
        return "candidate_Id = " + candidate_ID +
                ", name = " + name +
                ", photo = " + Arrays.toString(photo) +
                ", bio = " + bio;
    }

}
