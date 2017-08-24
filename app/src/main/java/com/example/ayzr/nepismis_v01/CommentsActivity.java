package com.example.ayzr.nepismis_v01;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.adapters.CommentsAdapter;
import com.example.ayzr.nepismis_v01.adapters.CookListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    final List<struct_comments> comm = new ArrayList<struct_comments>();
    private ProgressDialog progress;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        setTitle(R.string.comment_activity_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


         listView = (ListView) findViewById(R.id.list_comments);
        listView.setAdapter(null);


        parser();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void parser(){
        if(isNetworkAvailable()) {
            progress = new ProgressDialog(this);
            progress.setMessage("Checking comments...");
            progress.setIndeterminate(true);
            progress.show();

            HashMap<String, String> params_db;

            AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
            params_db = accountDatabase.kullaniciDetay();


            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.GET);
            httpCall.setUrl("http://nepismis.afakan.net/android/yorumGuncel");
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", params_db.get("tarih"));
            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public List<CookActivity.struct_menu> onResponse(String response) {
                    super.onResponse(response);
                    try {
                        progress.dismiss();

                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            struct_comments c = new struct_comments();

                            JSONObject initial = array.getJSONObject(i);

                            c.comment_id = initial.getInt("id");
                            c.comment = initial.getString("yorum");
                            c.commenr_rating = initial.getDouble("puan");

                            JSONObject ownerJson = initial.getJSONObject("yorumlayan");

                            c.comment_owner_name = ownerJson.getString("name");
                            c.comment_owner_id = ownerJson.getInt("id");
                            comm.add(i,c);
                        }
                        update_comments(comm);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(httpCall);

        } else {
            Toast.makeText(getApplicationContext(), "No internet access!", Toast.LENGTH_SHORT).show();
        }
    }

    private void update_comments( List<struct_comments> c){
       CommentsAdapter adapter = new CommentsAdapter(this, c);
        listView.setAdapter(adapter);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static class struct_comments{
        public String comment_owner_name;
        public int comment_owner_id;
        public double commenr_rating;
        public String comment;
        public  int comment_id;
    }
}
