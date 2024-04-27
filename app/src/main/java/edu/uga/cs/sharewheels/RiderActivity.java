package edu.uga.cs.sharewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RiderActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
    }

    public void ride_offer_accepted_succes(){
        showCustomSnackBar("Ride offer accepted successfully!", false);
    }

}