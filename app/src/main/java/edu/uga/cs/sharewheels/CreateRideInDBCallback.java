package edu.uga.cs.sharewheels;

public interface CreateRideInDBCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}
