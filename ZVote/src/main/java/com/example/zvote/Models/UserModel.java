package com.example.zvote.Models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class UserModel {
    private int user_ID;
    private String username;
    private String user_email;
    private String user_pass;
    private byte[] user_photoID;
    private String phoneNb;
    private String role;

    public UserModel(String username, String user_email, String user_pass, byte[] user_photoID, String phoneNb) {
        setUsername(username);
        setUser_email(user_email);
        setUser_pass(user_pass);
        setUser_photoID(user_photoID);
        setPhoneNb(phoneNb);
        role = "USER";
    }

    public int getUser_ID() {
        return user_ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = hashPassword(user_pass);
    }

    public byte[] getUser_photoID() {
        return user_photoID;
    }

    public void setUser_photoID(byte[] user_photoID) {
        if(user_photoID != null){
            this.user_photoID = user_photoID;
        }
        else {
            try {
                // Use a default photo path and convert it to a byte array
                Path defaultPhotoPath = Path.of("\uD83D\uDC64");
                this.user_photoID = Files.readAllBytes(defaultPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
                this.user_photoID = new byte[0]; // Fallback to empty byte array on error
            }
        }

    }

    public String getPhoneNb() {
        return phoneNb;
    }

    public void setPhoneNb(String phoneNb) {
        this.phoneNb = phoneNb;
    }

    public String getRole() {
        return role;
    }

    private String hashPassword(String password) {
        return stringToHex(password);
    }

    public String stringToHex(String input) {
        byte[] bytes = input.getBytes(); // Convert String to byte array
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b)); // Format each byte as a hex value
        }
        return hexString.toString();
    }

    @Override
    public String toString() {
        return "user_ID = " + user_ID +
                ", username = " + username +
                ", user_email = " + user_email +
                ", user_pass = " + user_pass +
                ", user_photoID = " + Arrays.toString(user_photoID) +
                ", phoneNb = " + phoneNb +
                ", role = " + role;
    }
}
