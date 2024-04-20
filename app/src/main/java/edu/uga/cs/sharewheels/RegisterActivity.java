package edu.uga.cs.sharewheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_first_name;
    private EditText et_last_name;
    private EditText et_email;
    private EditText et_password;
    private EditText et_confirm_password;
    private Button btn_register;
    private TextView tv_login;
    private FirebaseAuth m_FBAuth_instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        btn_register = findViewById(R.id.btn_register);
        tv_login = findViewById(R.id.tv_login);

        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);

        m_FBAuth_instance = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        // Fetch ID of view which was just clicked on.
        int viewId = v.getId();

        // If register button was clicked by the user.
        if (viewId == R.id.btn_register) {
            registerUser();
        }
        // If Login text view was clicked on by user.
        else if (viewId == R.id.tv_login) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void registerUser() {
        if (validate_Registration_Details()) {

            // Fetch email and password entered by user.
            String email = et_email.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            // Below function is designed to register a new user inside our Firebase Authentication module.
            m_FBAuth_instance.createUserWithEmailAndPassword(email, password)
                    // Listener for whether user was registered or not. It returns a task object.
                    .addOnCompleteListener(this, task -> {
                        // User was registered successfully.
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            //FirebaseUser firebaseUser = m_FBAuth_instance.getCurrentUser();
                            showCustomSnackBar("User " + firebaseUser.getUid() + " registered successfully!", false);

                            user_registration_successful();
                        }
                        // User couldn't be registered in Firebase Authentication module.
                        else {

                            // This exception indicates that the email is already in use.
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                showCustomSnackBar(getResources().getString(R.string.err_msg_email_already_in_use), true);
                            } else {
                                showCustomSnackBar(task.getException().getMessage(), true);
                            }
                        }
                    });
        }
    }

    private void loginUser() {
        // Registration logic here
    }

    // Below function returns true only if user has entered valid details on the Registration screen.
    // If not, it displays a red colored Snack bar with message about the field which contains incorrect values.
    private Boolean validate_Registration_Details() {
        String firstName = et_first_name.getText().toString().trim();
        if (TextUtils.isEmpty(firstName)) {
            showCustomSnackBar(getResources().getString(R.string.err_msg_enter_first_name), true);
            return false;
        }

        String lastName = et_last_name.getText().toString().trim();
        if (TextUtils.isEmpty(lastName)) {
            showCustomSnackBar(getResources().getString(R.string.err_msg_enter_last_name), true);
            return false;
        }

        String email = et_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            showCustomSnackBar(getResources().getString(R.string.err_msg_enter_email), true);
            return false;
        }
        // Below condition will reject email address which have :
        // 1) Missing an '@' Symbol, 2) Invalid Characters (!, *, $),
        // 3) spaces around @, 4) Emails Without a Domain, 5) Multiple '@' Symbols 6) Emails with Missing Top-Level Domain
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Check if the email format is valid
            showCustomSnackBar(getResources().getString(R.string.err_msg_invalid_email), true);
            return false;
        }

        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            showCustomSnackBar(getResources().getString(R.string.err_msg_enter_password), true);
            return false;
        }

        String confirmPassword = et_confirm_password.getText().toString().trim();
        if (TextUtils.isEmpty(confirmPassword)) {
            showCustomSnackBar(getResources().getString(R.string.err_msg_enter_confirm_password), true);
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showCustomSnackBar(getResources().getString(R.string.err_msg_password_and_confirm_password_mismatch), true);
            return false;
        }

        return true; // If all checks passed
    }

    // Function that decides what happens next once user registration is successful.
    // This will be called only when user registration is successful.
    private void user_registration_successful() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        // Remove the Register Activity from the activity stack.
        finish();
    }

}