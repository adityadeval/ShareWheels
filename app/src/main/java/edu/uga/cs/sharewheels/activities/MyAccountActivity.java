package edu.uga.cs.sharewheels.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import edu.uga.cs.sharewheels.firebaseutils.FirebaseOps;
import edu.uga.cs.sharewheels.R;
import edu.uga.cs.sharewheels.datamodels.User;

public class MyAccountActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_user_name;
    private TextView tv_email;
    private TextView tv_ride_points;
    private Button btn_logout;
    private FirebaseAuth m_FBAuth_instance;
    private User m_user_obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        tv_user_name = findViewById(R.id.tv_user_name);
        tv_email = findViewById(R.id.tv_email);
        tv_ride_points = findViewById(R.id.tv_ride_points);
        btn_logout = findViewById(R.id.btn_logout);

        m_FBAuth_instance = FirebaseAuth.getInstance();

        btn_logout.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Below function would fetch the logged in user's details into an object of the User class.
        // Then, userDataFetchSuccess() would be called, passing this object of User class as an argument.
        // The userDataFetchSuccess() would then set the TextViews present for this activity's layout to values from the
        // object of the User class.
        new FirebaseOps().get_user_details(MyAccountActivity.this);
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        if (viewId == R.id.btn_logout) {
            // Signout the user from Firebase
            m_FBAuth_instance.signOut();

            Intent intent = new Intent(MyAccountActivity.this, LoginActivity.class);
            // NEW_TASK flag : This flag is used when you're starting a new activity outside the context of an existing activity.
            // CLEAR_TASK flag : This flag will remove any existing activities in the current task stack before starting the new activity.
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

    // This method would be called by FirebaseOps.get_user_details() method, if data is fetched
    // successfully from the Firebase DB. It would display all the user details in the layout of current activity.
    public void userDataFetchSuccess(User user){
        m_user_obj = user;
        tv_user_name.setText(user.getFirstName() + " " + user.getLastName());
        tv_email.setText(user.getEmailID());
        tv_ride_points.setText(String.valueOf(user.getRidePointsBalance()));
    }

}