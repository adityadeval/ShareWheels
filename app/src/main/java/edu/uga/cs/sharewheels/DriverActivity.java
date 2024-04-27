package edu.uga.cs.sharewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DriverActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
    }

    public void ride_request_accepted_succes(){
        showCustomSnackBar("Ride request accepted successfully!", false);
    }
}