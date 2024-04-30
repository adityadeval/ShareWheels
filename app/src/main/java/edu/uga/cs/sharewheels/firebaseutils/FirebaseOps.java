package edu.uga.cs.sharewheels.firebaseutils;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        /*
        if (user_obj.getRidesList() == null || user_obj.getRidesList().isEmpty()) {
            user_obj.setRidesList(Arrays.asList("defaultRideId"));
        }
         */

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

    public void get_user_as_obj(Activity activity, final UserDetailsCallback callback){
        String userId = getLoggedInUserID();
        users_node_ref.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    callback.onUserDetailsFetched(user);
                } else {
                    callback.onUserDetailsFailed("User not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onUserDetailsFailed(error.getMessage());
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
                        //callback.onSuccess();

                        // Add the rideID to the logged in user's ridesList.
                        // Here the loggedIn user's ID is same as driverID as the currently loggedIn user is acting
                        // as driver (Since this function is being called from the DriverActivity).
                        updateUserRidesList(driverID, rideId, callback);
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

                        // Add the rideID to the logged in user's ridesList.
                        // Here the loggedIn user's ID is same as riderID, as the currently loggedIn user is acting
                        // as rider (Since this function is being called from the RiderActivity).
                        updateUserRidesList(riderID, rideId, callback);
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

    public void fetchUserRides(List<String> rideIds, GetAllRidesFromDBCallback callback) {
        ArrayList<Ride> fetched_rides = new ArrayList<>();
        int count = rideIds.size();
        for (String rideId : rideIds) {
            count--;
            if (rideId != null && !rideId.isEmpty()) {
                Log.d("FireBaseOps.fetchUserRides()", "Fetching data for rideID: " + rideId);
                int finalCount = count;
                rides_node_ref.child(rideId).addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Ride ride = snapshot.getValue(Ride.class);
                        if (ride != null) {
                            fetched_rides.add(ride);
                        }
                        int ride_count = (int) (rideIds.stream().filter(Objects::nonNull).filter(id -> !id.isEmpty()).count());
                        Log.d("FireBaseOps.fetchUserRides()", "fetched_rides.size() :" + fetched_rides.size());
                        Log.d("FireBaseOps.fetchUserRides()", "ride_count:" + ride_count);
                        if (finalCount == 0) { // Ensure all rides are fetched before calling onSuccess
                            Log.d("FireBaseOps.fetchUserRides()", "Inside second if, fetched_rides: "+fetched_rides);
                            callback.onRideDataReceived(fetched_rides);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onRideDataFailed(error.getMessage());
                    }
                });
            }
        }
    }

    public void acceptRide(Ride ride, CreateRideInDBCallback callback) {
        rides_node_ref.child(ride.getRideId()).setValue(ride)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void deleteRide(String rideID, DeleteRideCallBack callback){
        if (rideID == null || rideID.isEmpty()) {
            Log.e(DEBUG_TAG, "Invalid rideID: Cannot be null or empty.");
            callback.onFailure("Invalid rideID: Cannot be null or empty.");
            return;
        }

        // Obtain a reference to the specific ride in the 'Rides' node
        DatabaseReference rideRef = rides_node_ref.child(rideID);

        // Perform the delete operation
        rideRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d(DEBUG_TAG, "Ride successfully deleted: " + rideID);
                    callback.onSuccess();  // Notify success through the callback
                })
                .addOnFailureListener(e -> {
                    Log.e(DEBUG_TAG, "Failed to delete ride: " + rideID, e);
                    callback.onFailure(e.getMessage());  // Notify failure through the callback
                });
    }


    // Below function is used to add the RideID that the loggedIn user created (it could be a ride offer or request)
    // into the same user's 'ridesList'. So this would basically enable us to understand all rides the logged in user was
    // involved in.
    // We give this function the userID whose ridesList we want to update, and the rideId is the one we want to add into
    // the user's ridesList.
    private void updateUserRidesList(String userId, String rideId, CreateRideInDBCallback callback) {
        // Here currentUserRef would be the child node for currently logged in user, present inside root node "Users".
        DatabaseReference currentUserRef = users_node_ref.child(userId);

        // Get ref to 'ridesList' list of the user who's logged in.
        // And then add a listener at this position.
        currentUserRef.child("ridesList").addListenerForSingleValueEvent(new ValueEventListener() {

            // onDataChange would be called in two cases :
            // 1) When a listener is attached to a certain location in the DB, it'll be called immediately to retrieve the
            //    initial data at that location.
            // 2)  Any subsequent changes to the data at the location to which the listener is attached will also trigger a
            //     call to onDataChange.
            // The DataSnapshot contains the data at the specific path you're listening to at the time the event is triggered.
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> ridesList;

                // Below if else will either initialize the local variable ridesList to whatever is present in the logged in user's ridesList
                // OR If nothing is present, it'll create an empty ArrayList and assign it to local variable ridesList.
                if (snapshot.exists()) {
                    ridesList = snapshot.getValue(new GenericTypeIndicator<List<String>>() {});
                } else {
                    ridesList = new ArrayList<>();
                }
                ridesList.add(rideId);

                // The below Map is used to actually update the values inside our DB.
                // The Key of the map = Key_name in our DB's child node. Let's call it child_node_key.
                // Value of the Map = Value that we want to set for the child_node_key.
                Map<String, Object> updates_hashmap = new HashMap<>();
                updates_hashmap.put(DBConstants.RIDES_LIST, ridesList);

                currentUserRef.updateChildren(updates_hashmap)
                        .addOnSuccessListener(aVoid -> callback.onSuccess())
                        .addOnFailureListener(e -> callback.onFailure(e.getMessage()) );
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onFailure(error.getMessage());
            }
        });
    }

}
