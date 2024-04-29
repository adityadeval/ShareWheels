package edu.uga.cs.sharewheels.activities;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.uga.cs.sharewheels.R;
import edu.uga.cs.sharewheels.databinding.ActivityMyRidesBinding;

public class MyRidesActivity extends AppCompatActivity {

    private ActivityMyRidesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMyRidesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_pending_rides, R.id.navigation_active_rides, R.id.navigation_completed_rides)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_my_rides);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public void onBackPressed() {
        // Check if it's the last fragment or if there are no fragments managed by NavController
        if (isTaskRoot()) {
            super.onBackPressed(); // Let the system handle the back if this is the root of the task
        } else {
            finish(); // Otherwise, finish the activity to return to the previous one
        }
    }

}