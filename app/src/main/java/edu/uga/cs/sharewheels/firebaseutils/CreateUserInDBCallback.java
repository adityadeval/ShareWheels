package edu.uga.cs.sharewheels.firebaseutils;

public interface CreateUserInDBCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}
