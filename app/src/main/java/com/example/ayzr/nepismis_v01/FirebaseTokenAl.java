package com.example.ayzr.nepismis_v01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by afakan on 19.08.2017.
 */

public class FirebaseTokenAl extends FirebaseInstanceIdService {

    public static String refreshedToken;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("tokenal", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);



        //tokenGonder();
        storeToken(refreshedToken);
    }

    private void storeToken(String token)
    {
        SharedPrefManager.getInstance(getApplicationContext()).storeToken(token);
    }


}
