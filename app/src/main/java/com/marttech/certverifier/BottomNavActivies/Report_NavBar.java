package com.marttech.certverifier.BottomNavActivies;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.marttech.certverifier.Adapters.Mysingleton;
import com.marttech.certverifier.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Report_NavBar extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    EditText ETplace, ETdescr, ETname, ETemail;
    ImageButton btnPhoto;
    RadioButton radio1, radio2, radio3;
    RadioGroup radiogroup;

    private FirebaseAuth mAuth;
    private DatabaseReference dbSaveRef,dbReference;
    private StorageReference captureImgRef;

    private String image,selectedRadio,currentUserId,downloadedUrl;
    private ProgressDialog mProgressDialog;
    static final int CAM_REQUEST = 1;
    static final int SELECT_FILE = 4;
    private Bitmap bitmap;
    private UUID uuid;
    private byte [] dataBAOS;

    String server_url ="http://192.168.42.93/Certificate_verification/report.php";
    AlertDialog.Builder builder;

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_nav_bar);

        uuid = UUID.randomUUID();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        dbSaveRef = FirebaseDatabase.getInstance().getReference().child("Reports").child(currentUserId).child(uuid.toString());
        captureImgRef = FirebaseStorage.getInstance().getReference().child("capturedImages");
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

//        setting up the title for the ActionBar
        Toolbar reportToobar = findViewById(R.id.reportToobar);
        setSupportActionBar(reportToobar);
        reportToobar.setTitle("New report");
//        setting up the back navigation arrow
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);
        radiogroup = findViewById(R.id.radiogroup);

        ETplace = findViewById(R.id.ETplace);
        ETdescr = findViewById(R.id.ETdesc);
        ETname = findViewById(R.id.ETname);
        ETemail = findViewById(R.id.ETemail);
        btnPhoto = findViewById(R.id.btnPhoto);
        builder = new AlertDialog.Builder(Report_NavBar.this);

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraIntent();
            }
        });
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("username")){
                    String myProfileName = dataSnapshot.child("username").getValue().toString();
                    ETname.setText(myProfileName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void cameraIntent(){
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camera_intent.resolveActivity(getPackageManager()) !=null){
            startActivityForResult(camera_intent,CAM_REQUEST);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAM_REQUEST && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            dataBAOS = baos.toByteArray();

            final StorageReference filePath = captureImgRef.child(currentUserId +".jpg");
            filePath.putBytes(dataBAOS).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
//          when the image has successfully uploaded, get its download Url
                        mProgressDialog.dismiss();
                        Toast.makeText(Report_NavBar.this, "Image stored successfully to Firebase storage....", Toast.LENGTH_SHORT).show();
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadedUrl = uri.toString();
                                mProgressDialog.dismiss();
                                dbSaveRef.child("capturedimage").setValue(downloadedUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                mProgressDialog.dismiss();
                                                Toast.makeText(Report_NavBar.this, "Image has been stored to database successfully ", Toast.LENGTH_SHORT).show();
                                                dbSaveRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.hasChild("capturedimage")){
                                                            mProgressDialog.dismiss();
                                                            image = dataSnapshot.child("capturedimage").getValue().toString();
                                                            Picasso.with(getApplicationContext())
                                                                    .load(image)
                                                                    .fit()
                                                                    .placeholder(R.drawable.post)
                                                                    .into(btnPhoto);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }else {
                                                mProgressDialog.dismiss();
                                                String message = task.getException().getMessage();
                                                Toast.makeText(Report_NavBar.this, "An error occurred: "+message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            }
                        });
                    }
                }
            });
        }else{
            Toast.makeText(this, "No image Bitmap", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDataEntry() {
//        getting data input
        final String name, email, descr, place;
        name = ETname.getText().toString();
        email = ETemail.getText().toString();
        descr = ETdescr.getText().toString();
        place = ETplace.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ETname.setError("Field Required!");
        } else if (TextUtils.isEmpty(email)) {
            ETemail.setError("Field Required!");
        }else  if(!email.contains("@")){
            ETemail.setError("Enter a valid email!");
        }else if(TextUtils.isEmpty(descr)){
            ETdescr.setError("Field Required!");
        }else if(TextUtils.isEmpty(place)) {
            ETplace.setError("Field Required!");
        }else{
            mProgressDialog.setTitle("Sending Report");
            mProgressDialog.setMessage("Please wait ....");
            mProgressDialog.show();

            HashMap dataParams = new HashMap<>();
            dataParams.put("radiogroup",selectedRadio);
            dataParams.put("name",name);
            dataParams.put("email",email);
            dataParams.put("descr",descr);
            dataParams.put("place",place);

            dbSaveRef.updateChildren(dataParams).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Report_NavBar.this, "Data inserted successfully to firebase database", Toast.LENGTH_SHORT).show();
                    }else{
                        String message = task.getException().getMessage();
                        Toast.makeText(Report_NavBar.this, "Failed to enter data to firebase database" + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
//        sending data to MYSQL via PHP using volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                builder.setTitle("Server Response");
                builder.setMessage(response);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ETname.setText("");
                        ETemail.setText("");
                        ETplace.setText("");
                        ETdescr.setText("");
                        btnPhoto.setImageResource(R.drawable.camera);
                        radio1.setChecked(false);
                        radio2.setChecked(false);
                        radio3.setChecked(false);
                    }
                });
                builder.create().show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Report_NavBar.this, "Some error found ....\n"+error, Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map <String, String> params = new HashMap<>();

                String image = Base64.encodeToString(dataBAOS,Base64.DEFAULT);
                params.put("name",name);
                params.put("email",email);
                params.put("descr",descr);
                params.put("place",place);
                params.put("image",image);
                params.put("radiogroup",selectedRadio);
                return params;
            }
        };
        Mysingleton.getInstance(Report_NavBar.this).addTorequestque(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.send:
                onDataEntry();
                if (radio1.isChecked()){
                    selectedRadio = radio1.getText().toString();
                }else if(radio2.isChecked()){
                    selectedRadio = radio2.getText().toString();
                }else if (radio3.isChecked()){
                    selectedRadio = radio3.getText().toString();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

