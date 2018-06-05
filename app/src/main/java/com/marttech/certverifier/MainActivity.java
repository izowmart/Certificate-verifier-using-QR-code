package com.marttech.certverifier;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.marttech.certverifier.Adapters.SectionsPagerAdapter;
import com.marttech.certverifier.BottomNavActivies.Profile_NavBar;
import com.marttech.certverifier.BottomNavActivies.Report_NavBar;
import com.marttech.certverifier.BottomNavActivies.Submitted_NavBar;
import com.marttech.certverifier.Fragments.Info_fragment;
import com.marttech.certverifier.Fragments.QRfragment;
import com.marttech.certverifier.Fragments.Report_fragment;
import com.marttech.certverifier.Helper.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {


    private ViewPager mViewPager;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        startActivity(new Intent(MainActivity.this,Report_NavBar.class));
                        break;
                    case R.id.submitted:
                        startActivity(new Intent(MainActivity.this,Submitted_NavBar.class));
                        break;
                    case R.id.profile:
                        startActivity(new Intent(MainActivity.this,Profile_NavBar.class));
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

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Info_fragment());
        adapter.addFragment(new QRfragment());
        adapter.addFragment(new Report_fragment());

        viewPager.setAdapter(adapter);
    }

}
