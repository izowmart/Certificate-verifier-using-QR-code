package com.marttech.certverifier.BottomNavActivies;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marttech.certverifier.Filed_Reports;
import com.marttech.certverifier.MainActivity;
import com.marttech.certverifier.R;
import com.squareup.picasso.Picasso;

public class Submitted_NavBar extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference subPostRef;
    private String currentUID;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted_nav_bar);

        mAuth = FirebaseAuth.getInstance();
        currentUID = mAuth.getCurrentUser().getUid();
        subPostRef = FirebaseDatabase.getInstance().getReference().child("Reports");

        Toolbar toolbar = findViewById(R.id.submittedToobar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Filed Reports");

        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        bottomNavigationView = findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent6 = new Intent(Submitted_NavBar.this,MainActivity.class);
                        intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent6);
                        break;
                    case R.id.report:
                        Intent intent7 = new Intent(Submitted_NavBar.this,Report_NavBar.class);
                        intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent7);
                        break;
                    case R.id.submitted:
                        break;
                    case R.id.profile:
                        Intent intent8 = new Intent(Submitted_NavBar.this,Profile_NavBar.class);
                        intent8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent8);
                        break;
                }
                return false;
            }
        });

       displayAllFilledReports();
    }

    private void displayAllFilledReports() {
        FirebaseRecyclerAdapter<Filed_Reports,ReportsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Filed_Reports, ReportsViewHolder>
                (
                    Filed_Reports.class,
                    R.layout.model,
                    ReportsViewHolder.class,
                    subPostRef

                ) {
            @Override
            protected void populateViewHolder(ReportsViewHolder viewHolder, Filed_Reports model, int position) {

                viewHolder.setDescr(model.getDescr());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setName(model.getName());
                viewHolder.setPlace(model.getPlace());
                viewHolder.setRadiogroup(model.getRadiogroup());
                viewHolder.setCapturedimage(getApplicationContext(),model.getCapturedimage());
            }

        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class ReportsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ReportsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView mName = mView.findViewById(R.id.name);
            mName.setText("Name :"+name);
        }

        public void setEmail(String email) {
            TextView mEmail = mView.findViewById(R.id.email);
            mEmail.setText("Email :"+email);
        }

        public void setPlace(String place) {
            TextView mPremises = mView.findViewById(R.id.place);
            mPremises.setText("Premises :"+place);

        }

        public void setRadiogroup(String radiogroup) {
            TextView mRadiogroup = mView.findViewById(R.id.radiogroup);
            mRadiogroup.setText("Radiobox :"+radiogroup);

        }

        public void setDescr(String descr) {
            TextView mDescr = mView.findViewById(R.id.descr);
            mDescr.setText("Description :"+descr);

        }

        public void setCapturedimage(Context ctx, String capturedimage) {
            ImageView postImg = mView.findViewById(R.id.imgCapture);
            Picasso.with(ctx)
                    .load(capturedimage)
                    .fit()
                    .into(postImg);

        }
    }
}
