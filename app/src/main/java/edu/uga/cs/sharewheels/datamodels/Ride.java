package edu.uga.cs.sharewheels.datamodels;

import java.time.LocalDate;

public class Ride {
    private String rideId;

    // Storing date as type String and not LocalDate or other formats as they are not supported by FirebaseDB.
    private String date;
    private String origin;
    private String destination;
    private int rideCost;
    private String riderID = "";
    private String driverID = "";
    private Boolean riderCompletionFlag = false;
    private Boolean driverCompletionFlag = false;

    public Ride() {
    }

    // Below constructor would be used while creating Ride offers by Drivers.
    public Ride(String rideId, String date, String origin, String destination, int rideCost, String driverID){
        this.rideId = rideId;
        this.date = date;
        this.origin = origin;
        this.destination = destination;
        this.rideCost = rideCost;
        this.driverID = driverID != null ? driverID : "";
    }

    // Below constructor would be used while creating Ride requests by Riders.
    public Ride(String rideId, String date, String origin, String destination, String riderID){
        this.rideId = rideId;
        this.date = date;
        this.origin = origin;
        this.destination = destination;
        this.riderID = riderID != null ? riderID : "";
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getRideCost() {
        return rideCost;
    }

    public void setRideCost(int rideCost) {
        this.rideCost = rideCost;
    }

    public String getRiderID() {
        return riderID;
    }

    public void setRiderID(String riderID) {
        this.riderID = riderID != null ? riderID : "";
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID != null ? driverID : "";
    }

    public Boolean getRiderCompletionFlag() {
        return riderCompletionFlag;
    }

    public void setRiderCompletionFlag(Boolean riderCompletionFlag) {
        this.riderCompletionFlag = riderCompletionFlag;
    }

    public Boolean getDriverCompletionFlag() {
        return driverCompletionFlag;
    }

    public void setDriverCompletionFlag(Boolean driverCompletionFlag) {
        this.driverCompletionFlag = driverCompletionFlag;
    }

}
