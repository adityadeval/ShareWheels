package edu.uga.cs.sharewheels.firebaseutils;

public interface CreateRideInDBCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}
