package edu.uga.cs.sharewheels;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

public class Rides {
    private int rideId;
    private LocalDate date;
    private String origin;
    private String destination;
    private int rideCost;
    private String riderName;
    private String driverName;
    private Boolean riderCompletionFlag;
    private Boolean driverCompletionFlag;

    public void Ride() {
    }

    public void Ride(int rideId, LocalDate date, String origin, String destination, int rideCost,
                     String riderName, String driverName, Boolean riderCompletionFlag, Boolean driverCompletionFlag) {
        this.rideId = rideId;
        this.date = date;
        this.origin = origin;
        this.destination = destination;
        this.rideCost = rideCost;
        this.riderName = riderName;
        this.driverName = driverName;
        this.riderCompletionFlag = riderCompletionFlag;
        this.driverCompletionFlag = driverCompletionFlag;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
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
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
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
