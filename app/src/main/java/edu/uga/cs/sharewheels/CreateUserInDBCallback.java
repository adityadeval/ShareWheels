package edu.uga.cs.sharewheels;

public interface CreateUserInDBCallback {
    void onSuccess();
    void onFailure(String errorMessage);
}
