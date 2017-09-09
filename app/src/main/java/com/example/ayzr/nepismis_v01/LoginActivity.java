package com.example.ayzr.nepismis_v01;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passEditText;
    private boolean is_login;
    private String username;
    private String password;
    private int user_id;

    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Address the email and password field
        emailEditText = (EditText) findViewById(R.id.username);
        passEditText = (EditText) findViewById(R.id.password);

    }

    public void checkLogin(View arg0) {

        final String email = emailEditText.getText().toString();
        if (!isValidEmail(email)) {
            //Set error message for email field
            emailEditText.setError("Invalid Email");
        }

        final String pass = passEditText.getText().toString();
        if (!isValidPassword(pass)) {
            //Set error message for password field
            passEditText.setError("Password cannot be empty");
        }

        if(isValidEmail(email) && isValidPassword(pass)) {
            try_login();
        }
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 4) {
            return true;
        }
        return false;
    }

    private void try_login(){

         if(!isNetworkAvailable()) {
             Toast.makeText(getApplicationContext(),"No internet access!",Toast.LENGTH_SHORT).show();
             return;
         }
        progress = new ProgressDialog(this);
        progress.setMessage("Logging in ...");
        progress.setIndeterminate(true);
        progress.show();

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("http://nepismis.afakan.net/android/gonder");
        HashMap<String,String> params = new HashMap<>();
        params.put("name",emailEditText.getText().toString());
        params.put("password",passEditText.getText().toString());
        httpCall.setParams(params);
        new HttpRequest(){
            @Override
            public List<CookActivity.struct_menu> onResponse(String response) {
                super.onResponse(response);
                try {
                    progress.dismiss();
                    JSONObject obje = new JSONObject(response);
                    is_login = obje.getBoolean("giris");
                    JSONObject user = obje.getJSONObject("user");
                    user_id = user.getInt("id");

                    username = user.getString("email");
                    password = user.getString("password");

                    if(is_login){

                        AccountDatabase db = new AccountDatabase(getApplicationContext());
                        db.resetTables();
                        db.kullaniciEkle(username, password, ""+user_id);

                        tokenGonder(user_id,FirebaseTokenAl.refreshedToken);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(!is_login){
                        Toast.makeText(getApplicationContext(),"Başarısız!",Toast.LENGTH_SHORT).show();
                    }
                }
                return null;
            }

        }.execute(httpCall);
    }

        private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public  void tokenGonder(int user,String token)
    {
        HashMap<String, String> params_db;
        AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
        params_db = accountDatabase.kullaniciDetay();

        HttpCall httpCall2 = new HttpCall();
        httpCall2.setMethodtype(HttpCall.GET);
        httpCall2.setUrl("http://nepismis.afakan.net/android/tokenYaz");
        HashMap<String,String> params = new HashMap<>();
        params.put("token",token);
        params.put("user_id",params_db.get("tarih"));
        httpCall2.setParams(params);
        new HttpRequest(){
            @Override
            public List<CookActivity.struct_menu> onResponse(String response) {
                super.onResponse(response);
                try {

                    JSONObject initial = new JSONObject(response);
                    int saved = initial.getInt("save");
                    if (saved == 1) {
                        Toast.makeText(getApplicationContext(), "Token Successful :)", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),CookActivity.class));
                        finish();


                    } else {
                        Toast.makeText(getApplicationContext(), "Token crash :(", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(),"Başarısız Token!",Toast.LENGTH_SHORT).show();

                }
                return null;
            }

        }.execute(httpCall2);

    }


}
