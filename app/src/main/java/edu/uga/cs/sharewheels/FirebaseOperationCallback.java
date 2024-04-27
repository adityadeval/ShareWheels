package edu.uga.cs.sharewheels;

public interface FirebaseOperationCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}
