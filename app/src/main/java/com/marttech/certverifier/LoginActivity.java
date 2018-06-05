package com.marttech.certverifier;

import android.content.Intent;
import android.support.annotation.NonNull;
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

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput,passwordInput;
    private TextView signUp,resetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        signUp = findViewById(R.id.register);
        resetPassword = findViewById(R.id.resetPassword);

        // Get Firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPassword.class));
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        final Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String enteredEmail = emailInput.getText().toString().trim();
                final String enteredPassword = passwordInput.getText().toString();

                if (TextUtils.isEmpty(enteredEmail)){
                    Toast.makeText(LoginActivity.this, "Email field can't be empty!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!(enteredEmail.contains("@"))){
                    Toast.makeText(LoginActivity.this, "Invalidate email entered", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(enteredPassword)){
                    Toast.makeText(LoginActivity.this, "Password field can't be empty!", Toast.LENGTH_LONG).show();
                    return;
                }

//                authenticate user
                mAuth.signInWithEmailAndPassword(enteredEmail,enteredPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
//                                    there is an error
                                    if (enteredPassword.length()< 6){
                                        passwordInput.setError(getString(R.string.minimum_password));
                                    }else{
                                        Toast.makeText(LoginActivity.this, "SignIn failed", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });

            }
        });
    }

}
