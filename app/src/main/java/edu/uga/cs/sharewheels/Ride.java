package edu.uga.cs.sharewheels;

import java.time.LocalDate;

public class Ride {
    private String rideId;
    private LocalDate date;
    private String origin;
    private String destination;
    private int rideCost;
    private String riderID;
    private String driverID;
    private Boolean riderCompletionFlag;
    private Boolean driverCompletionFlag;

    public void Ride() {
    }

    public void Ride(String rideId, LocalDate date, String origin, String destination, int rideCost,
                     String riderName, String driverName, Boolean riderCompletionFlag, Boolean driverCompletionFlag) {
        this.rideId = rideId;
        this.date = date;
        this.origin = origin;
        this.destination = destination;
        this.rideCost = rideCost;
        this.riderID = riderName;
        this.driverID = driverName;
        this.riderCompletionFlag = riderCompletionFlag;
        this.driverCompletionFlag = driverCompletionFlag;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
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

    public String getRiderName() {
        return riderID;
    }

    public void setRiderName(String riderID) {
        this.riderID = riderID;
    }

    public String getDriverName() {
        return driverID;
    }

    public void setDriverName(String driverName) {
        this.driverID = driverID;
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
