package edu.uga.cs.sharewheels.firebaseutils;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import edu.uga.cs.sharewheels.activities.DriverActivity;
import edu.uga.cs.sharewheels.activities.MyAccountActivity;
import edu.uga.cs.sharewheels.activities.RiderActivity;
import edu.uga.cs.sharewheels.datamodels.Ride;
import edu.uga.cs.sharewheels.datamodels.User;

public class FirebaseOps {
    public static final String DEBUG_TAG = "FirebaseOps";

    // Below line gets the singleton instance of FirebaseDatabase that points to my Firebase Realtime Database in
    // ShareWheels project present in Firebase.
    FirebaseDatabase DBref = FirebaseDatabase.getInstance();

    // Get a path to "users" within my database.
    // IMP : At this point, the "users" node won't be created yet. It will actually be created only when some data is set
    // at this reference or at a child of this reference. For eg. After using setValue(), updateChildren(), or similar operations
    // that modify the database.
    DatabaseReference users_node_ref = DBref.getReference( DBConstants.USERS_NODE_NAME );
    DatabaseReference rides_node_ref = DBref.getReference( DBConstants.RIDES_NODE_NAME );


    public void create_user_in_DB(String userID, String firstName, String lastName, String emailID, String password, CreateUserInDBCallback callback){

        User user_obj = new User(userID, firstName, lastName, emailID, password);

        // Check if RidesList is empty (which it would be initially).
        // Firebase DB would just not add RidesList to the child node being created if RidesList is empty.
        // Hence it is necessary to initialize it with some default value.
        if (user_obj.getRidesList() == null || user_obj.getRidesList().isEmpty()) {
            user_obj.setRidesList(Arrays.asList("defaultRideId"));
        }

        // Create a new child node called <whatever value userID contains>.
        // Contents of this child node would be all data of the user_obj.
        users_node_ref.child(userID).setValue(user_obj)
                .addOnSuccessListener(aVoid -> {
                    // Call onSuccess when the Firebase operation succeeds
                    // This method would need to be overriden in the calling activity to actually perform some action.
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    // Call onFailure when the Firebase operation fails
                    // This method would need to be overriden in the calling activity to actually perform some action.
                    callback.onFailure(e.getMessage());
                });
    }

    // Below function is used to fetch all details of a user into an object of the User class.
    // If this function is called by the MyAccountActivity, only then userDataFetchSuccess() would be called, which would
    // utilize this User object for displaying user's data such as name, email and ride points on the UI screen.
    public void get_user_details(Activity activity){
        // Below line is used for navigating to the exact node.
        users_node_ref.child(getLoggedInUserID())
                // Setup a listener that reacts to a single read of the data located at the specified child node.
                // Once it's triggered the listener is immediately unregistered.
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    // onDataChange() is invoked when data at the specified reference is successfully read.
                    // The data is returned in a DataSnapshot object.
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        // getValue() converts the snapshot of the data into an instance of the User class.
                        User user_obj = snapshot.getValue(User.class);

                        /*
                        SharedPreferences sharedPreferences =
                                activity.getSharedPreferences(DBConstants.SHARED_PREF_FILE_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString(DBConstants.USER_ID, user_obj.getUserID());
                        editor.putString(DBConstants.FIRST_NAME, user_obj.getFirstName());
                        editor.putString(DBConstants.LAST_NAME, user_obj.getLastName());
                        editor.putString(DBConstants.EMAIL_ID, user_obj.getEmailID());
                        editor.putInt(DBConstants.RIDE_POINTS_BALANCE, user_obj.getRidePointsBalance());
                        editor.apply();

                         */

                        if (activity instanceof MyAccountActivity) {
                            ((MyAccountActivity) activity).userDataFetchSuccess(user_obj);
                        }
                     //   return null;
                    }

                    // onCancelled is called when the read operation couldn't be completed successfully.
                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e(activity.getClass().getSimpleName(), "Error while fetching logged in user's details.", error.toException());
                    }
        });
    }

    public String getLoggedInUserID() {
        // Create an instance of FirebaseAuth's current user.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // currentUserID will store the user id of an actual user who's currently logged in.
        // Note: The id is coming from the 'Authentication' module of Firebase.
        String currentUserID = "";
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
        }

        return currentUserID;
    }

    // Below function is going to creates a ride inside the "Rides" node of the DB.
    // IMP : This function would create both types of rides : Ride Requests and Ride Offers.
    // It will recognize what to create based on the Activity who's calling this function.
    // If called by DriverActivity, it'll create a "Ride Offer".
    // If called by RiderActivity, it'll create a "Ride Request".
    // The constructor of Ride class used in each case also plays a major role here.
    public void createRideInDB(Activity activity, String date, String origin, String destination, CreateRideInDBCallback callback){
        int rideCost = 50;
        // push().getKey() generates a unique ID for each new ride, before the ride is actually created.
        String rideId = rides_node_ref.push().getKey();

        // If current function is being called by the DriverActivity, then set the driverID to current UserID
        // AND Call the constructor of Ride class which is suitable for creating Ride "Offers"
        // (It contains rideCost and driverID).
        if (activity instanceof DriverActivity) {
            String driverID = getLoggedInUserID();
            Ride ride_obj = new Ride(rideId, date, origin, destination, rideCost, driverID);
            Log.d("FirebaseOps.createRideInDB", "Date in Object of Ride Class :" + ride_obj.getDate());

            rides_node_ref.child(rideId).setValue(ride_obj)
                    .addOnSuccessListener(aVoid -> {
                        // Notify success callback
                        callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        // Notify failure callback
                        callback.onFailure(e.getMessage());
                    });
        }

        // If current function is being called by the RiderActivity, then set the riderID to current UserID
        // AND Call the constructor of Ride which is suitable for creating Ride "Requests"
        // (rideCost will be absent here, and it'll contain riderID instead of driverID).
        else if (activity instanceof RiderActivity) {
            String riderID = getLoggedInUserID();
            Ride ride_obj = new Ride(rideId, date, origin, destination, riderID);

            rides_node_ref.child(rideId).setValue(ride_obj)
                    .addOnSuccessListener(aVoid -> {
                        // Notify success callback
                        callback.onSuccess();
                    })
                    .addOnFailureListener(e -> {
                        // Notify failure callback
                        callback.onFailure(e.getMessage());
                    });
        }
    }

    public void get_all_rides(Activity activity, GetAllRidesFromDBCallback callback) {
        Log.d( DEBUG_TAG, "Inside get_all_rides" );
        rides_node_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d( DEBUG_TAG, "Inside onDataChange" );

                ArrayList<Ride> rideList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ride ride_obj = snapshot.getValue(Ride.class);
                    if (activity instanceof RiderActivity) {
                        if(ride_obj.getRiderID() == null || ride_obj.getRiderID().isEmpty()) {
                            if (!getLoggedInUserID().equals(ride_obj.getDriverID())){
                                Log.d( DEBUG_TAG, "Driver/Ride Offeror: "+ride_obj.getDriverID());
                                rideList.add(ride_obj);
                            }
                        }
                    }
                    if (activity instanceof DriverActivity) {
                        if(ride_obj.getDriverID() == null || ride_obj.getDriverID().isEmpty()) {
                            if (!getLoggedInUserID().equals(ride_obj.getRiderID())){
                                Log.d( DEBUG_TAG, "Driver/Ride Requester: "+ride_obj.getRiderID());
                                rideList.add(ride_obj);
                            }
                        }
                    }
                }
                Log.d( DEBUG_TAG, "rideList: "+rideList );

                callback.onRideDataReceived(rideList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(activity.getClass().getSimpleName(), "Error while fetching rides.", error.toException());
            }
        });
    }

    public void acceptRide(Ride ride, CreateRideInDBCallback callback) {
        rides_node_ref.child(ride.getRideId()).setValue(ride)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}
