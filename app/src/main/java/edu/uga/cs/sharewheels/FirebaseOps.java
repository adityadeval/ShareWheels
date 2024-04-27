package edu.uga.cs.sharewheels;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import edu.uga.cs.sharewheels.FirebaseOperationCallback;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class FirebaseOps {

    // Below line gets the singleton instance of FirebaseDatabase that points to my Firebase Realtime Database in
    // ShareWheels project present in Firebase.
    FirebaseDatabase DBref = FirebaseDatabase.getInstance();

    // Get a path to "users" within my database.
    // IMP : At this point, the "users" node won't be created yet. It will actually be created only when some data is set
    // at this reference or at a child of this reference. For eg. After using setValue(), updateChildren(), or similar operations
    // that modify the database.
    DatabaseReference users_node_ref = DBref.getReference( DBConstants.USERS_NODE_NAME );

    public void create_user_in_DB(String userID, String firstName, String lastName, String emailID, String password, FirebaseOperationCallback callback){

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

}
