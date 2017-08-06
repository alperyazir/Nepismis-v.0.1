package com.example.ayzr.nepismis_v01;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.adapters.MenusAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManuActivity extends AppCompatActivity {

    private ListView menu_list;
    private ProgressDialog progress;
    final List<CookActivity.struct_menu> menus_morning = new ArrayList<>();
    final List<CookActivity.struct_menu> menus_noon    = new ArrayList<>();
    final List<CookActivity.struct_menu> menus_evening = new ArrayList<>();

    MenusAdapter morning_adapter;
    MenusAdapter noon_adapter;
    MenusAdapter evening_adapter;

    private int current_tab_index;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    current_tab_index =1;
                    menu_list.setAdapter(null);
                    parser(1);
                    return true;
                case R.id.navigation_dashboard:
                    current_tab_index =2;
                    menu_list.setAdapter(null);
                    parser(2);
                    return true;
                case R.id.navigation_notifications:
                    current_tab_index =3;
                    menu_list.setAdapter(null);
                    parser(3);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manu);
        setTitle(R.string.menus_activity_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_menu);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Menu eklenecek sıkıntı yok :)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        menu_list = (ListView) findViewById(R.id.menu_list);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        parser(1);
        current_tab_index =1;
    }
    public void parser(final int it){
        if(isNetworkAvailable()) {
            progress = new ProgressDialog(this);
            progress.setMessage("Checking menus...");
            progress.setIndeterminate(true);
            progress.show();

            menus_morning.clear();
            menus_noon.clear();
            menus_evening.clear();
            HashMap<String, String> params_db;

            AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
            params_db = accountDatabase.kullaniciDetay();


            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.GET);
            httpCall.setUrl("http://nepismis.afakan.net/android/menuGuncel");
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", params_db.get("tarih"));
            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    try {
                        progress.dismiss();

                        JSONObject initial = new JSONObject(response);
                        JSONArray morning_array = initial.getJSONArray("sabah");
                        JSONArray noon_array = initial.getJSONArray("ogle");
                        JSONArray evening_array = initial.getJSONArray("aksam");

                        if(it == 1) {
                            for (int i = 0; i < morning_array.length(); i++) {
                                JSONObject morning_menus = morning_array.getJSONObject(i);
                                JSONArray menuy_array = morning_menus.getJSONArray("menuy");

                                CookActivity.struct_menu m = new CookActivity.struct_menu();
                                for (int j = 0; j < menuy_array.length(); j++) {
                                    JSONObject morning_menuy = menuy_array.getJSONObject(j);
                                    m.meal.add(j, morning_menuy.getString("yemek_adi"));
                                    if (j == 0)
                                        m.order_picture_id = morning_menuy.getInt("yemek_id");
                                }
                                menus_morning.add(i, m);
                            }
                            update_morning_orders(menus_morning);
                        }
                        else if(it == 2) {
                            for (int i = 0; i < noon_array.length(); i++) {
                                JSONObject morning_menus = noon_array.getJSONObject(i);
                                JSONArray menuy_array = morning_menus.getJSONArray("menuy");

                                CookActivity.struct_menu m = new CookActivity.struct_menu();
                                for (int j = 0; j < menuy_array.length(); j++) {
                                    JSONObject morning_menuy = menuy_array.getJSONObject(j);
                                    m.meal.add(j, morning_menuy.getString("yemek_adi"));
                                    if (j == 0)
                                        m.order_picture_id = morning_menuy.getInt("yemek_id");
                                }
                                menus_noon.add(i, m);
                            }
                            update_noon_orders(menus_noon);
                        }
                        else if(it == 3) {
                            for (int i = 0; i < evening_array.length(); i++) {
                                JSONObject morning_menus = evening_array.getJSONObject(i);
                                JSONArray menuy_array = morning_menus.getJSONArray("menuy");

                                CookActivity.struct_menu m = new CookActivity.struct_menu();
                                for (int j = 0; j < menuy_array.length(); j++) {
                                    JSONObject morning_menuy = menuy_array.getJSONObject(j);
                                    m.meal.add(j, morning_menuy.getString("yemek_adi"));
                                    if (j == 0)
                                        m.order_picture_id = morning_menuy.getInt("yemek_id");
                                }
                                menus_evening.add(i, m);
                            }
                            update_evening_orders(menus_evening);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }.execute(httpCall);

        } else {
            Toast.makeText(getApplicationContext(), "No internet access!", Toast.LENGTH_SHORT).show();
        }
    }

    public void update_morning_orders(List<CookActivity.struct_menu> menu){
        morning_adapter = new MenusAdapter(this, menu);
        menu_list.setAdapter(morning_adapter);
    }

    public void update_noon_orders(List<CookActivity.struct_menu> menu){
        noon_adapter = new MenusAdapter(this, menu);
        menu_list.setAdapter(noon_adapter);
    }

    public void update_evening_orders(List<CookActivity.struct_menu> menu){
        evening_adapter = new MenusAdapter(this, menu);
        menu_list.setAdapter(evening_adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
