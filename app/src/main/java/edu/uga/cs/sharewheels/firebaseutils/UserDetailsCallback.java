package edu.uga.cs.sharewheels.firebaseutils;

import edu.uga.cs.sharewheels.datamodels.User;

public interface UserDetailsCallback {
    void onUserDetailsFetched(User user);
    void onUserDetailsFailed(String errorMessage);
}
