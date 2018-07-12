package com.marttech.certificateverification;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    String singleParsed = "";
    String dataParsed = "";

    String server_url = "http://192.168.0.95/Certificate_verification/response.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());
        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mScannerView == null){
            mScannerView = new ZXingScannerView(getActivity());
        }
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();
        Log.d("QRCodeScanner",result.getText());
        Log.d("QRCodeScanner",result.getBarcodeFormat().toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray JA = new JSONArray(response);
                    JSONObject JO = JA.getJSONObject(0);
                    singleParsed = "Name : " + JO.getString("name")+ "\n"+
                            "Institution : " + JO.getString("institution")+ "\n"+
                            "Reg_Number : " + JO.getString("reg_number")+ "\n"+
                            "Grade : " + JO.getInt("grade")+ "\n"+
                            "Date of issue : " + JO.getInt("date")+ "\n";
                    dataParsed = dataParsed + singleParsed;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Server Response");
                builder.setMessage(dataParsed);
                builder.setPositiveButton("Rescan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mScannerView.resumeCameraPreview(QRFragment.this);
                    }
                });
                builder.setCancelable(false);
                builder.create().show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error Message");
                builder.setIcon(R.drawable.warning);
                builder.setMessage("Some error found when compiling results ....\n"+error);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mScannerView.resumeCameraPreview(QRFragment.this);
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> Params = new HashMap<>();
                Params.put("id", scanResult);
                return Params;
            }
        };
        Mysingleton.getInstance(getActivity()).addTorequestque(stringRequest);

    }
}

///////////////////////////////////////////////////////////////////////////////////
hanging code but oky

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission_group.CAMERA;

public class QRFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView mScannerView;
    String singleParsed = "";
    String dataParsed = "";

    String server_url = "http://192.168.0.95/Certificate_verification/response.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());

        if (checkPermission()){
            Toast.makeText(getActivity(), "Permission is granted", Toast.LENGTH_SHORT).show();
        }else{
            requestPermission();
        }

        return mScannerView;
    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(getActivity(),CAMERA)== PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{CAMERA},REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();
       if (checkPermission()){
           if (mScannerView == null){
               mScannerView = new ZXingScannerView(getActivity());
           }
           mScannerView.setResultHandler(this);
           mScannerView.startCamera();
       }else{
           requestPermission();
       }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permission[], @NonNull int grantResults[]){
        switch (requestCode){
            case REQUEST_CAMERA:
                if (grantResults.length >0){
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(getActivity(), "Permission Granted, Now you can access camera", Toast.LENGTH_SHORT).show();
                    }else{Toast.makeText(getActivity(), "Permission Denied, You cannot access camera", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                                displayAlertMessage("You need to allow access for both permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }
    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }

    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();
        Log.d("QRCodeScanner",result.getText());
        Log.d("QRCodeScanner",result.getBarcodeFormat().toString());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray JA = new JSONArray(response);
                    JSONObject JO = JA.getJSONObject(0);
                    singleParsed = "Name : " + JO.getString("name")+ "\n"+
                            "Institution : " + JO.getString("institution")+ "\n"+
                            "Reg_Number : " + JO.getString("reg_number")+ "\n"+
                            "Grade : " + JO.getInt("grade")+ "\n"+
                            "Date of issue : " + JO.getInt("date")+ "\n";
                    dataParsed = dataParsed + singleParsed;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Server Response");
                builder.setMessage(dataParsed);
                builder.setPositiveButton("Rescan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mScannerView.resumeCameraPreview(QRFragment.this);
                    }
                });
                builder.setCancelable(false);
                builder.create().show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Error Message");
                builder.setIcon(R.drawable.warning);
                builder.setMessage("Some error found when compiling results ....\n"+error);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mScannerView.resumeCameraPreview(QRFragment.this);
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> Params = new HashMap<>();
                Params.put("id", scanResult);
                return Params;
            }
        };
        Mysingleton.getInstance(getActivity()).addTorequestque(stringRequest);

    }

}

