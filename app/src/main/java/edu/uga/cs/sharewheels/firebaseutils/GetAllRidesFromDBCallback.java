package edu.uga.cs.sharewheels.firebaseutils;

import java.util.ArrayList;

import edu.uga.cs.sharewheels.datamodels.Ride;

public interface GetAllRidesFromDBCallback {

    void onRideDataReceived(ArrayList<Ride> rideList);
    void onRideDataFailed(String error);
}
