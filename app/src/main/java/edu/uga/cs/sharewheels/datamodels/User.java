package edu.uga.cs.sharewheels.datamodels;

import java.util.List;

public class User {
    private String userID;
    private String firstName;
    private String lastName;
    private String emailID;
    private String password;
    private int ridePointsBalance;

    // Below List stores all rideIDs the user was involved in.
    private List<String> ridesList;

    public User() {
    }

    public User(String userID, String firstName, String lastName, String emailID,
                String password) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailID = emailID;
        this.password = password;
    }

    // Getters and setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRidePointsBalance() {
        return ridePointsBalance;
    }

    public void setRidePointsBalance(int ridePointsBalance) {
        this.ridePointsBalance = ridePointsBalance;
    }

    public List<String> getRidesList() {
        return ridesList;
    }

    public void setRidesList(List<String> ridesList) {
        this.ridesList = ridesList;
    }
}
