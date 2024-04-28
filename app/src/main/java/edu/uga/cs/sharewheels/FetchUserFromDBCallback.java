package edu.uga.cs.sharewheels;

public interface FetchUserFromDBCallback {
    void onSuccess(User user);  // Now accepts a User object
    void onFailure(String errorMessage);
}
