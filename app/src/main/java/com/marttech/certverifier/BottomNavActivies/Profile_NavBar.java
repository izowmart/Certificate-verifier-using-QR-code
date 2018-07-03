package com.marttech.certverifier.BottomNavActivies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marttech.certverifier.MainActivity;
import com.marttech.certverifier.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_NavBar extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private CircleImageView profileImg;
    private TextView profileName, premises,email,phone;
    private FirebaseAuth mAuth;
    private DatabaseReference dbReference;
    private StorageReference mStorageRef;
    private String currentUID;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_nav_bar);

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        bottomNavigationView = findViewById(R.id.bottomNav);
        profileImg = findViewById(R.id.profileImg);
        profileName = findViewById(R.id.profile_name);
        premises = findViewById(R.id.place);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        mProgressDialog = new ProgressDialog(this);

//        onclick save profile_img

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent = new Intent(Profile_NavBar.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case R.id.report:
                        Intent intent1 = new Intent(Profile_NavBar.this,Report_NavBar.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        break;
                    case R.id.submitted:
                        Intent intent2 = new Intent(Profile_NavBar.this,Submitted_NavBar.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        break;
                    case R.id.profile:
                        break;
                }
                return false;
            }
        });
    }



}
