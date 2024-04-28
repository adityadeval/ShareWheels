package edu.uga.cs.sharewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends BaseActivity implements View.OnClickListener{

    private Button button_submit;
    private EditText et_email;

    private FirebaseAuth m_FBAuth_instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        button_submit = findViewById(R.id.button_submit);
        et_email = findViewById(R.id.et_email);

        m_FBAuth_instance = FirebaseAuth.getInstance();

        button_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.button_submit) {
            reset_pass();
        }

    }

    private void reset_pass(){

        String email = et_email.getText().toString().trim();

        if (email.isEmpty()) {
            showCustomSnackBar(getResources().getString(R.string.err_msg_enter_email), true);
        }
        else {
            m_FBAuth_instance.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            showCustomSnackBar(getResources().getString(R.string.email_sent_success), false);
                            //finish();
                        } else {
                            // Ensure there is an exception message to display
                            if (task.getException() != null) {
                                showCustomSnackBar(task.getException().getMessage(), true);
                            }
                        }
                    });
        }
    }



}