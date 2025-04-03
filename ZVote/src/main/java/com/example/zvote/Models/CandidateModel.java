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
    private int poll_ID;

    public CandidateModel(String name, byte[] photo, String bio, int poll_ID) {
        setName(name);
        setPhoto(photo);
        setBio(bio);
        setPoll_ID(poll_ID);
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
                Path defaultPhotoPath = Paths.get("C:\\Computer Science projects\\Repositories\\ZVote\\ZVote\\src\\main\\resources\\images\\Profile Pic.png"); // Replace photoPath with your actual path variable
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

    public int getPoll_ID() {
        return poll_ID;
    }

    public void setPoll_ID(int poll_ID) {
        this.poll_ID = poll_ID;
    }

    @Override
    public String toString() {
        return "candidate_Id = " + candidate_ID +
                ", name = " + name +
                ", photo = " + Arrays.toString(photo) +
                ", bio = " + bio +
                ", poll_ID = " + poll_ID;
    }
}
