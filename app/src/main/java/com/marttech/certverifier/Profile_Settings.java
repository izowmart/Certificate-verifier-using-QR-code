package com.marttech.certverifier;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile_Settings extends AppCompatActivity {

    private EditText nameInput,phoneInput,placeInput,emailInput;
    private CircleImageView settingsProfileImg;
    //    TextInputEditText passwordInput;
    private Button settingsAccountBtn,editProfileImg;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference userDbRef;
    private StorageReference profileStorageRef;

    private static final int SELECT_FILE = 4;
    private Uri imageHoldUri = null;

    private String currentUID,downloadUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        userDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUID);
        profileStorageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        mProgressDialog = new ProgressDialog(this);

        nameInput = findViewById(R.id.settings_name);
        settingsAccountBtn = findViewById(R.id.settings_btn);
        phoneInput = findViewById(R.id.settings_phone);
        placeInput = findViewById(R.id.settings_place);
        emailInput = findViewById(R.id.settings_email);
        settingsProfileImg = findViewById(R.id.profile_img_settings);
        editProfileImg =findViewById(R.id.edit_profile_img);
        settingsProfileImg = findViewById(R.id.profile_img_settings);

        editProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryIntent();

            }
        });

        settingsAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccountProfileInfo();
            }
        });

        userDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild("profileimage")){
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(Profile_Settings.this)
                                .load(image)
                                .placeholder(R.drawable.profile_img)
                                .into(settingsProfileImg);
                    }else {
                        Toast.makeText(Profile_Settings.this, "Please select profile_img image", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void galleryIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_FILE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            if (imageUri != null) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
        }

        //    img crop library code
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                imageHoldUri = result.getUri();
                settingsProfileImg.setImageURI(imageHoldUri);

                final StorageReference filePath = profileStorageRef.child(currentUID + ".jpg");
                filePath.putFile(imageHoldUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            mProgressDialog.dismiss();
                            Toast.makeText(Profile_Settings.this, "Profile image stored successfully to FirebaseStorage", Toast.LENGTH_SHORT).show();
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = uri.toString();
                                }
                            });
                        }
                    }
                });

            }
        }
    }

    private void saveAccountProfileInfo() {
        final String enteredName, enteredPhone, enteredPlace, enteredEmail;

        enteredName = nameInput.getText().toString();
        enteredPhone = phoneInput.getText().toString();
        enteredPlace = placeInput.getText().toString();
        enteredEmail = emailInput.getText().toString();

        if (TextUtils.isEmpty(enteredName)) {
            nameInput.setError("Name is required!");
        } else if (TextUtils.isEmpty(enteredPhone)) {
            phoneInput.setError("Phone number is required!");
        } else if (TextUtils.isEmpty(enteredPlace)) {
            placeInput.setError("Place of work is required!");
        } else if (TextUtils.isEmpty(enteredEmail)) {
            emailInput.setError("email is required!");
        } else if (!(enteredEmail.contains("@"))) {
            emailInput.setError("Enter a valid email address!");
        } else {
            mProgressDialog.setTitle("Saving information");
            mProgressDialog.setMessage("Please wait ....");
            mProgressDialog.show();

            HashMap dataEntry = new HashMap();
            dataEntry.put("username", enteredName);
            dataEntry.put("email", enteredEmail);
            dataEntry.put("phone", enteredPhone);
            dataEntry.put("premises", enteredPlace);
            dataEntry.put("profileimage", downloadUrl);

            userDbRef.updateChildren(dataEntry).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(Profile_Settings.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                        Toast.makeText(Profile_Settings.this, "Your account has been updated successfully", Toast.LENGTH_LONG).show();
                    } else {
                        mProgressDialog.dismiss();
                        String message = task.getException().getMessage();
                        Toast.makeText(Profile_Settings.this, "An error occurred" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }



    }

}
