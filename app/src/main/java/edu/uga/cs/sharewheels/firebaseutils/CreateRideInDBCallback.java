package edu.uga.cs.sharewheels.firebaseutils;

import java.util.ArrayList;

import edu.uga.cs.sharewheels.datamodels.Ride;

public interface CreateRideInDBCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}
