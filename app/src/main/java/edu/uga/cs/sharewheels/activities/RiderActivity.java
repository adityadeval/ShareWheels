package edu.uga.cs.sharewheels.activities;

import android.os.Bundle;

import edu.uga.cs.sharewheels.R;

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