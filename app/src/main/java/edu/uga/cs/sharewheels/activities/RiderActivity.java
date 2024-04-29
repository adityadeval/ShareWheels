package edu.uga.cs.sharewheels.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import edu.uga.cs.sharewheels.R;
import edu.uga.cs.sharewheels.adapters.AdapterDisplayRides;
import edu.uga.cs.sharewheels.datamodels.Ride;
import edu.uga.cs.sharewheels.datamodels.User;
import edu.uga.cs.sharewheels.firebaseutils.CreateRideInDBCallback;
import edu.uga.cs.sharewheels.firebaseutils.GetAllRidesFromDBCallback;
import edu.uga.cs.sharewheels.firebaseutils.FirebaseOps;

public class RiderActivity extends BaseActivity implements View.OnClickListener{
    public static final String DEBUG_TAG = "RiderActivity";

    private FloatingActionButton fabNewRideRequest;
    private FirebaseOps m_firebaseops_instance;
    private AdapterDisplayRides adapter;
    private RecyclerView recyclerView;
    private ArrayList<Ride> rides;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d( DEBUG_TAG, "Inside onCreate" );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);

        m_firebaseops_instance = new FirebaseOps();
        fabNewRideRequest = findViewById(R.id.fabNewRideRequest);
        fabNewRideRequest.setOnClickListener(this);

        showRideOffers();
    }

    public void ride_offer_accepted_success(){
        showCustomSnackBar("Ride offer accepted successfully!", false);
    }

    @Override
    public void onClick(View v) {
        // Fetch ID of view which was just clicked on.
        int viewId = v.getId();

        if (viewId == R.id.fabNewRideRequest){
            showRideRequestDialog();
        }
    }

    // Below function is used to show a dialog that would enable a user to create a ride request.
    // It uses the function createRideInDB() of the FirebaseOps for creating a ride request.
    private void showRideRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_ride_request, null);
        builder.setView(dialogView);

        final EditText etDate = dialogView.findViewById(R.id.etDate);
        EditText etOrigin = dialogView.findViewById(R.id.etOrigin);
        EditText etDestination = dialogView.findViewById(R.id.etDestination);
        Button btnCreateRideRequest = dialogView.findViewById(R.id.btnCreateRideRequest);

        // Creating Date picker dialog when user clicks on etDate.
        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(RiderActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    // Month is zero based in DatePicker
                                    LocalDate date = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                                    etDate.setText(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // Sets the text to the selected date in ISO format
                                }
                            }, LocalDate.now().getYear(), LocalDate.now().getMonthValue() - 1, LocalDate.now().getDayOfMonth());
                    datePickerDialog.show();

                    // Set the DatePicker minimum date to today to allow future dates only
                    DatePicker datePicker = datePickerDialog.getDatePicker();
                    datePicker.setMinDate(System.currentTimeMillis() - 1000); // Set to just below current time to include today

                    // Optionally reset any maximum date if previously set
                    datePicker.setMaxDate(Long.MAX_VALUE); // Remove maximum date restriction

                    // Prevent further execution of onFocusChange to avoid recursion
                    etDate.setOnFocusChangeListener(null);
                }
            }
        });

        final AlertDialog dialog = builder.create();

        btnCreateRideRequest.setOnClickListener(v -> {
            String date = etDate.getText().toString().trim();
            String origin = etOrigin.getText().toString().trim();
            String destination = etDestination.getText().toString().trim();

            if(validateDialogEntries(date, origin, destination)){

                Log.d("Values being passed to createRideInDB. Date = ", date + " Origin = " + origin + " Dest = " + destination);
                m_firebaseops_instance.createRideInDB(RiderActivity.this, date, origin, destination, new CreateRideInDBCallback() {
                    @Override
                    public void onSuccess() {
                        showCustomSnackBar("Ride request created successfully", false);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        showCustomSnackBar("Ride request could not be created", true);
                    }
                });
            }

            dialog.dismiss();
        });

        dialog.show();

    }

    private Boolean validateDialogEntries(String date, String origin, String destination){
        if (TextUtils.isEmpty(date)) {
            showCustomSnackBar("Date cannot be left empty", true);
            return false;
        }

        if (TextUtils.isEmpty(origin)) {
            showCustomSnackBar("Origin cannot be left empty", true);
            return false;
        }

        if (TextUtils.isEmpty(destination)) {
            showCustomSnackBar("Destination cannot be left empty", true);
            return false;
        }
        return true; // If all checks passed
    }

    public void showRideOffers(){
        Log.d( DEBUG_TAG, "Inside showRideOffers" );

        recyclerView = findViewById(R.id.rv_ride_offers);
        recyclerView.setLayoutManager(new LinearLayoutManager(RiderActivity.this));
        m_firebaseops_instance.get_all_rides(RiderActivity.this, new GetAllRidesFromDBCallback() {
            @Override
            public void onRideDataReceived(ArrayList<Ride> rideList) {
                Log.d( DEBUG_TAG, "Inside onRideDataReceived, rideList: "+rideList );
                rides = rideList;
                adapter = new AdapterDisplayRides(RiderActivity.this, rides);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRideDataFailed(String error) {
                Log.e(DEBUG_TAG, "Failed to fetch rides: " + error);
            }
        });
    }

}