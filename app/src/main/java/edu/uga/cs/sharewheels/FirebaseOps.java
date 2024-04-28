package edu.uga.cs.sharewheels;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class FirebaseOps {

    // Below line gets the singleton instance of FirebaseDatabase that points to my Firebase Realtime Database in
    // ShareWheels project present in Firebase.
    FirebaseDatabase DBref = FirebaseDatabase.getInstance();

    // Get a path to "users" within my database.
    // IMP : At this point, the "users" node won't be created yet. It will actually be created only when some data is set
    // at this reference or at a child of this reference. For eg. After using setValue(), updateChildren(), or similar operations
    // that modify the database.
    DatabaseReference users_node_ref = DBref.getReference( DBConstants.USERS_NODE_NAME );

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
}
