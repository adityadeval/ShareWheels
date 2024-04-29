package edu.uga.cs.sharewheels.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import edu.uga.cs.sharewheels.adapters.AdapterDisplayRides;
import edu.uga.cs.sharewheels.datamodels.Ride;
import edu.uga.cs.sharewheels.firebaseutils.CreateRideInDBCallback;
import edu.uga.cs.sharewheels.firebaseutils.FirebaseOps;
import edu.uga.cs.sharewheels.R;
import edu.uga.cs.sharewheels.firebaseutils.GetAllRidesFromDBCallback;

public class DriverActivity extends BaseActivity implements View.OnClickListener{
    public static final String DEBUG_TAG = "DriverActivity";
    private FloatingActionButton fabNewRideOffer;
    private FirebaseOps m_firebaseops_instance;
    private AdapterDisplayRides adapter;
    private RecyclerView recyclerView;
    private ArrayList<Ride> rides;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        m_firebaseops_instance = new FirebaseOps();

        fabNewRideOffer = findViewById(R.id.fabNewRideOffer);
        fabNewRideOffer.setOnClickListener(this);

        //Call the method to display the Ride Requests
        showRideRequests();
    }

    public void ride_request_accepted_success(){
        showCustomSnackBar("Ride request accepted successfully!", false);
    }

    @Override
    public void onClick(View v) {
        // Fetch ID of view which was just clicked on.
        int viewId = v.getId();

        if (viewId == R.id.fabNewRideOffer){
            showRideOfferDialog();
        }
    }

    // Below function is used to show a dialog that would enable a user to create a ride offer.
    // It uses the function createRideInDB() of the FirebaseOps for creating a ride offer.
    private void showRideOfferDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_ride_offer, null);
        builder.setView(dialogView);

        final EditText etDate = dialogView.findViewById(R.id.etDate);
        //EditText etDate = dialogView.findViewById(R.id.etDate);
        EditText etOrigin = dialogView.findViewById(R.id.etOrigin);
        EditText etDestination = dialogView.findViewById(R.id.etDestination);
        Button btnCreateRideOffer = dialogView.findViewById(R.id.btnCreateRideOffer);

        // Below function pops up a Date picker when user clicks on etDate.
        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(DriverActivity.this,
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

                    /*
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // Optional: Set max date to today
                    // Prevent further execution of onFocusChange to avoid recursion
                    etDate.setOnFocusChangeListener(null);
                     */
                }
            }
        });

        final AlertDialog dialog = builder.create();

        btnCreateRideOffer.setOnClickListener(v -> {
            // Remove empty spaces.
            String date = etDate.getText().toString().trim();
            String origin = etOrigin.getText().toString().trim();
            String destination = etDestination.getText().toString().trim();

            if(validateDialogEntries(date, origin, destination)){

                Log.d("Values being passed to createRideInDB. Date = ", date + " Origin = " + origin + " Dest = " + destination);
                m_firebaseops_instance.createRideInDB(DriverActivity.this, date, origin, destination, new CreateRideInDBCallback() {
                    @Override
                    public void onSuccess() {
                        showCustomSnackBar("Ride offer created successfully", false);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        showCustomSnackBar("Ride offer could not be created", true);
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

    public void showRideRequests(){
        Log.d( DEBUG_TAG, "Inside showRideOffers" );

        recyclerView = findViewById(R.id.rv_ride_requests);
        recyclerView.setLayoutManager(new LinearLayoutManager(DriverActivity.this));
        m_firebaseops_instance.get_all_rides(DriverActivity.this, new GetAllRidesFromDBCallback() {
            @Override
            public void onRideDataReceived(ArrayList<Ride> rideList) {
                Log.d( DEBUG_TAG, "Inside onRideDataReceived, rideList: "+rideList );
                rides = rideList;
                adapter = new AdapterDisplayRides(DriverActivity.this, rides);
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