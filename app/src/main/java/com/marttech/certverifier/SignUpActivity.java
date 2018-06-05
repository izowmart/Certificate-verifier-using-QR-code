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

public class SignUpActivity extends AppCompatActivity {

    private TextView login;
    private EditText nameInput,phoneInput,placeInput,emailInput,passwordInput;
    Button signUp_button;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//        Get firebase mAuth instance
        mAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.login);
        nameInput = findViewById(R.id.name);
        phoneInput = findViewById(R.id.phone);
        placeInput = findViewById(R.id.place);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });

        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredName, enteredPhone, enteredPlace, enteredEmail, enteredPassword;

                enteredName = nameInput.getText().toString();
                enteredPhone = phoneInput.getText().toString();
                enteredPlace = placeInput.getText().toString();
                enteredEmail = emailInput.getText().toString();
                enteredPassword= passwordInput.getText().toString();

                if (TextUtils.isEmpty(enteredName)){
                    Toast.makeText(SignUpActivity.this, "Enter your name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(enteredPhone)){
                    Toast.makeText(SignUpActivity.this, "Enter your phone number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(enteredPlace)){
                    Toast.makeText(SignUpActivity.this, "Enter your place of work!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(enteredEmail)){
                    Toast.makeText(SignUpActivity.this, "Enter your email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(enteredEmail.contains("@"))){
                    Toast.makeText(SignUpActivity.this, "Invalid email entered", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(enteredPassword)){
                    Toast.makeText(SignUpActivity.this, "Enter your password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (enteredPassword.length() < 6){
                    passwordInput.setError(getString(R.string.minimum_password));
                    return;
                }

//                create user
                mAuth.createUserWithEmailAndPassword(enteredEmail,enteredPassword)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpActivity.this, "createdUserWithEmail : onComplete:"+task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this, "Authentication failed."+task.getException(), Toast.LENGTH_SHORT).show();
                                }else{
                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                    finish();

                                }

                            }
                        });

            }
        });

    }
}
