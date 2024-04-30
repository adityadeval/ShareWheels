package edu.uga.cs.sharewheels.firebaseutils;

public interface DeleteRideCallBack {
    void onSuccess();
    void onFailure(String errorMessage);
}
