package com.marttech.certverifier.BottomNavActivies;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.marttech.certverifier.Helper.Mysingleton;
import com.marttech.certverifier.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Report_NavBar extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    EditText ETplace, ETdescr, ETname, ETemail;
    ImageButton btnPhoto;
    RadioButton radio1, radio2, radio3;
    RadioGroup radiogroup;

    String selectedRadio;
    static final int CAM_REQUEST = 1;
    Bitmap bitmap;

    String server_url ="http://192.168.0.27/Certificate_verification/report.php";
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

////        setting up the title for the ActionBar
//        getSupportActionBar().setTitle("New report");
////        setting up the back navigation arrow
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (camera_intent.resolveActivity(getPackageManager()) !=null){
                    startActivityForResult(camera_intent,CAM_REQUEST);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAM_REQUEST && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            btnPhoto.setImageBitmap(bitmap);
        }
    }
    public void onDataEntry(){
        final String name,email,descr,place;
        name = ETname.getText().toString();
        email = ETemail.getText().toString();
        descr = ETdescr.getText().toString();
        place = ETplace.getText().toString();

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

                String image = getStringImage(bitmap);
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
    public String getStringImage(Bitmap bmp){
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) btnPhoto.getDrawable());
        if (bitmapDrawable == null){
            btnPhoto.buildDrawingCache();
            bitmap = btnPhoto.getDrawingCache();
            btnPhoto.buildDrawingCache(false);
        }else{
            bitmap = bitmapDrawable.getBitmap();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte [] bytes = byteArrayOutputStream.toByteArray();
        String temp = Base64.encodeToString(bytes,Base64.DEFAULT);

        return temp;
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

