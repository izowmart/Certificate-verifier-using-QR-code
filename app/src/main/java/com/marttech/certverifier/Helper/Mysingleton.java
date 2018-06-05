package com.marttech.certverifier.Helper;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Mysingleton{
    private static Mysingleton mysingleton;
    private RequestQueue requestQueue;
    private static Context mCtx;

    public Mysingleton(Context context) {
        this.mCtx = context;
        requestQueue = getRequestQueue();

    }
    public static synchronized Mysingleton getInstance(Context context){
        if (mysingleton == null){
            mysingleton = new Mysingleton(context);
        }
        return mysingleton;
    }
    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());

        }
        return requestQueue;
    }
    public<T> void addTorequestque(Request<T> request){
        requestQueue.add(request);
    }
}