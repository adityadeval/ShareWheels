package edu.uga.cs.sharewheels.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import edu.uga.cs.sharewheels.R;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void showCustomSnackBar(String message, boolean errorMessage) {
        // Create the Snackbar
        Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        View snackBarView = snackBar.getView();

        // Set background color based on the type of error message
        int backgroundColor;
        if (errorMessage) {
            backgroundColor = ContextCompat.getColor(this, R.color.colorSnackBarError);
            //snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSnackBarError));
        } else {
            backgroundColor = ContextCompat.getColor(this, R.color.colorSnackBarSuccess);
            //snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSnackBarSuccess));
        }
        snackBarView.setBackgroundColor(backgroundColor);

        // Show the Snackbar
        snackBar.show();
    }
}