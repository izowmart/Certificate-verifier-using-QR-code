package com.marttech.certverifier;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
//    TextInputEditText passwordInput;
    private TextView signUp,resetPassword;
    private FirebaseAuth mAuth;
    ProgressDialog mProgressDialog;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        signUp = findViewById(R.id.register);
        resetPassword = findViewById(R.id.resetPassword);

        // Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(LoginActivity.this, ResetPassword.class));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(LoginActivity.this, SignUpActivity.class));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        final Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String enteredEmail = emailInput.getText().toString().trim();
                final String enteredPassword = passwordInput.getText().toString();

                if (TextUtils.isEmpty(enteredEmail)){
                    emailInput.setError("Email field can't be empty!");
                }else if (!(enteredEmail.contains("@"))){
                    emailInput.setError("Invalid email entered");
                }else if (TextUtils.isEmpty(enteredPassword)){
                    passwordInput.setError("Password field can't be empty!");
                }else if (enteredPassword.length()< 6){
                    passwordInput.setError(getString(R.string.minimum_password));
                }else{
                    mProgressDialog.setTitle("Loging in the User");
                    mProgressDialog.setMessage("Please wait....");
                    mProgressDialog.show();

                    mAuth.signInWithEmailAndPassword(enteredEmail,enteredPassword)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        mProgressDialog.dismiss();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();

                                    }else{
                                        mProgressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "SignIn failed", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Intent loginIntent = new Intent(LoginActivity.this,MainActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }
    }

}
