package com.marttech.certverifier;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marttech.certverifier.Adapters.SectionsPagerAdapter;
import com.marttech.certverifier.BottomNavActivies.BottomNavigationViewHelper;
import com.marttech.certverifier.BottomNavActivies.Profile_NavBar;
import com.marttech.certverifier.BottomNavActivies.Report_NavBar;
import com.marttech.certverifier.BottomNavActivies.Submitted_NavBar;
import com.marttech.certverifier.Fragments.Info_fragment;
import com.marttech.certverifier.Fragments.QRfragment;
import com.marttech.certverifier.Fragments.Report_fragment;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private CircleImageView profileImg;
    private BottomNavigationView bottomNavigationView;
    private DatabaseReference dbProfileRef;

    boolean doubleBackToExitPressedOnce = false;

    private String currentUID;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();

        setContentView(R.layout.activity_main);

        dbProfileRef = FirebaseDatabase.getInstance().getReference().child("Users");

        toolbar = findViewById(R.id.toobar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        profileImg = findViewById(R.id.profileImg);
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                send to settings activity
                sendToSettingsActivity();
            }
        });
        dbProfileRef.child(currentUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild("profileimage")){
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(MainActivity.this)
                                .load(image)
                                .placeholder(R.drawable.profile_img)
                                .into(profileImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bottomNavigationView = findViewById(R.id.bottomNav);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        break;
                    case R.id.report:
                        Intent intent3 = new Intent(MainActivity.this,Report_NavBar.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);
                        break;
                    case R.id.submitted:
                        Intent intent4 = new Intent(MainActivity.this,Submitted_NavBar.class);
                        intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent4);
                        break;
                    case R.id.profile:
                        Intent intent5 = new Intent(MainActivity.this,Profile_NavBar.class);
                        intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent5);

                        break;
                }
                return false;
            }
        });

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(R.string.tab_text_1);
        tabLayout.getTabAt(1).setText(R.string.tab_text_2);
        tabLayout.getTabAt(2).setText(R.string.tab_text_3);

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
        }else{
            checkUserExistence();
        }
    }

    private void checkUserExistence() {
        dbProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(currentUID)){
                    sendToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendToSettingsActivity() {
        Intent intentSettings = new Intent(new Intent(MainActivity.this, Profile_Settings.class));
        intentSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentSettings);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Info_fragment());
        adapter.addFragment(new QRfragment());
        adapter.addFragment(new Report_fragment());

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(),Profile_NavBar.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),Profile_Settings.class));
                return true;
            case R.id.sign_out:
                signOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void signOut(){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        finish();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        },2000);
    }

}
