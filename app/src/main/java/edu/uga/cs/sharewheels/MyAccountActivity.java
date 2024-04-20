package edu.uga.cs.sharewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MyAccountActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_user_name;
    private TextView tv_email;
    private TextView tv_ride_points;
    private Button btn_logout;
    private FirebaseAuth m_FBAuth_instance;
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



}