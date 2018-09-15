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

public class SignUpActivity extends AppCompatActivity {

    private TextView login;
    private EditText emailInput,passwordInput,confirmPassword;
//    TextInputEditText passwordInput;
    private Button signUp_button;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        login = findViewById(R.id.login);
        signUp_button = findViewById(R.id.signUp_button);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.reg_confirm_password);

        mProgressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(SignUpActivity.this,LoginActivity.class));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        signUp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String enteredEmail, enteredPassword,enteredConfirmPass;
                enteredEmail = emailInput.getText().toString();
                enteredPassword= passwordInput.getText().toString();
                enteredConfirmPass = confirmPassword.getText().toString();

                if(TextUtils.isEmpty(enteredEmail)){
                    emailInput.setError("Field Required!");
                }else if (!enteredEmail.contains("@")){
                    emailInput.setError("Enter a valid email!");
                }else  if (TextUtils.isEmpty(enteredPassword)){
                    passwordInput.setError("Field Required!");
                }else if (enteredPassword.length() < 6){
                    passwordInput.setError("Password must be more than six");
                }else  if (TextUtils.isEmpty(enteredConfirmPass)) {
                    confirmPassword.setError("Field Required!");
                }else if (!enteredPassword.equals(enteredConfirmPass)){
                    confirmPassword.setError("Passwords not matching");
                }else{
                    mProgressDialog.setTitle("Registering the User");
                    mProgressDialog.setMessage("Please wait....");
                    mProgressDialog.show();

//                create user
                    mAuth.createUserWithEmailAndPassword(enteredEmail,enteredPassword)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()){
                                        mProgressDialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, "SignUp failed"+task.getException(), Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        finish();

                                    }

                                }
                            });

                }


            }
        });

    }

}
