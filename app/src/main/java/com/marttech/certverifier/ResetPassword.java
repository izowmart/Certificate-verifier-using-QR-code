package com.marttech.certverifier;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    EditText resetEmail;
    Button resetPasswordBtn;
    TextView back;
    FirebaseAuth mAuth;
    ProgressDialog mProgressDialog;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);

        resetEmail = findViewById(R.id.resetEmail);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(ResetPassword.this, LoginActivity.class));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog.setTitle("Resetting Password");
                mProgressDialog.setMessage("Please wait....");
                mProgressDialog.show();
                String enteredResetEmail = resetEmail.getText().toString();
                if (TextUtils.isEmpty(enteredResetEmail)){
                    mProgressDialog.dismiss();
                    resetEmail.setError("email is required!");
                    return;
                }

                if (!(enteredResetEmail.contains("@"))){
                    mProgressDialog.dismiss();
                    resetEmail.setError("Enter a valid email address!");
                    return;
                }

                mAuth.sendPasswordResetEmail(enteredResetEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()){
                                 mProgressDialog.dismiss();
                                 Toast.makeText(ResetPassword.this, "We have sent you instruction to rest your password in your email", Toast.LENGTH_SHORT).show();
                             }else{
                                 mProgressDialog.dismiss();
                                 Toast.makeText(ResetPassword.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                             }
                            }
                        });
            }
        });
    }
}
