package edu.uga.cs.sharewheels;

// This class is going to be used for defining key names used in the Firebase RealtimeDB.
// It is used to ensure that whenever data is written into or read from the database, the key names remain consistent.

public final class DBConstants {

    public static final String SHARED_PREF_FILE_NAME = "shared_preferences_file";

    // Defining names of nodes to be created inside the DB.
    public static final String USERS_NODE_NAME = "users";
    public static final String RIDES_NODE_NAME = "rides";


    // Keys of USERS node.
    public static final String USER_ID = "userID";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL_ID = "emailID";
    public static final String PASSWORD = "password";
    public static final String RIDE_POINTS_BALANCE = "ridePointsBalance";
    public static final String RIDES_LIST = "rides_list";


    // Keys of RIDES node.
    public static final String RIDE_ID = "rideId";
    public static final String DATE = "date";
    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destination";
    public static final String RIDE_COST = "rideCost";
    public static final String RIDER_ID = "riderID";  // Refers to Users.USER_ID
    public static final String DRIVER_ID = "driverID";  // Refers to Users.USER_ID
    public static final String RIDER_COMPLETION_FLAG = "completion_flag_rider"; // Rider confirms ride happened.
    public static final String DRIVER_COMPLETION_FLAG = "completion_flag_driver"; // Driver confirms ride happened.


    private DBConstants() {
        // This utility class is not publicly instantiable
    }


}
