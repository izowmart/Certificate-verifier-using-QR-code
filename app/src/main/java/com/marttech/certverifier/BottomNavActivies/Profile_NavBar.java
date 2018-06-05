package com.marttech.certverifier.BottomNavActivies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.marttech.certverifier.Helper.BottomNavigationViewHelper;
import com.marttech.certverifier.MainActivity;
import com.marttech.certverifier.R;

public class Profile_NavBar extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_nav_bar);

        bottomNavigationView = findViewById(R.id.bottomNav);

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
                        startActivity(new Intent(Profile_NavBar.this,MainActivity.class));
                        break;
                    case R.id.report:
                        startActivity(new Intent(Profile_NavBar.this,Report_NavBar.class));
                        break;
                    case R.id.submitted:
                        startActivity(new Intent(Profile_NavBar.this,Submitted_NavBar.class));
                        break;
                    case R.id.profile:
                        break;
                }
                return false;
            }
        });
    }
}
