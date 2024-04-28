package edu.uga.cs.sharewheels.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.uga.cs.sharewheels.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_register;
    private Button btn_login;
    private EditText et_password;
    private TextView tv_forgot_password;
    private EditText et_email;
    private FirebaseAuth m_FBAuth_instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        // Click event assigned to Forgot Password text view.
        tv_forgot_password.setOnClickListener(this);
        // Click event assigned to Login button.
        btn_login.setOnClickListener(this);
        // Click event assigned to the Register text view.
        tv_register.setOnClickListener(this);

        m_FBAuth_instance = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        // Fetch ID of view which was just clicked on.
        int viewId = v.getId();

        if (viewId == R.id.btn_login) {
            loginUser();
        }

        if (viewId == R.id.tv_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        if (viewId == R.id.tv_forgot_password) {
            Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
            startActivity(intent);
            //finish();
        }

    }

    // Function for Logging in user into the Firebase Authentication module.
    // This function will also move the user to the main activity if Login was successful.
    private void loginUser() {
        if(validate_login_Details()){
            String email = et_email.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            m_FBAuth_instance.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            //FirebaseUser firebaseUser = m_FBAuth_instance.getCurrentUser();
                            showCustomSnackBar("User " + firebaseUser.getUid() + " registered successfully!", false);

                            user_login_successful();

                        } else {
                            if (task.getException() != null) {
                                showCustomSnackBar(task.getException().getMessage(), true);
                            }
                        }
                    });
        }

    }

    // Checks if email id and password values are non-empty.
    private Boolean validate_login_Details() {
        // Show error snack bar if entered email id is empty.
        if (TextUtils.isEmpty(et_email.getText().toString().trim())) {
            showCustomSnackBar(getResources().getString(R.string.err_msg_enter_email), true);
            return false;
        } else if (TextUtils.isEmpty(et_password.getText().toString().trim())) {
            showCustomSnackBar(getResources().getString(R.string.err_msg_enter_password), true);
            return false;
        } else {
            return true;
        }
    }


    // This function decides what happens next once user login is successful. It's called only after user login is successfull.
    private void user_login_successful() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        // Remove the Login Activity from the activity stack.
        finish();
    }

}