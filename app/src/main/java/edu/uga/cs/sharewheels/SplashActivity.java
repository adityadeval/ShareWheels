package edu.uga.cs.sharewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentUserID = new FirebaseOps().getLoggedInUserID();
                if (!currentUserID.isEmpty()) {
                    // Launch MainActivity.
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    // Launch the Login Activity
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 3000);
    }
}