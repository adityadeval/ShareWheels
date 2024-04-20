package edu.uga.cs.sharewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";
    private Button btn_driver;
    private Button btn_rider;
    private Button btn_settings;
    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_driver = findViewById(R.id.btn_driver);
        btn_rider = findViewById(R.id.btn_rider);
        btn_settings = findViewById(R.id.btn_settings);

        btn_settings.setOnClickListener(this);

        /*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference( "message" );
        // Read from the database value for ”message”
        myRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            // This method is called once, initially, and when data is updated
                String message = dataSnapshot.getValue(String.class);
                Log.d( TAG, "Read message: " + message );
                textView.setText( message );
            }
            @Override
            public void onCancelled( DatabaseError error ) {
            // Failed to read value
                Log.d( TAG, "Failed to read value.", error.toException() );
            }
        });
         */

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.btn_driver) {


        }

        else if (viewId == R.id.btn_rider) {


        }

        else if (viewId == R.id.btn_settings) {
            Intent intent = new Intent(MainActivity.this, MyAccountActivity.class);
            startActivity(intent);
        }

    }
}