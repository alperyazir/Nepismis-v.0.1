package com.example.ayzr.nepismis_v01;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.adapters.QuestionnarieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Survey_activity extends AppCompatActivity implements DialogInterface.OnDismissListener {

    private ListView survey_list;
    private int ogun;

    private ProgressDialog progress;
    final List<CookActivity.struct_menu> menus_morning = new ArrayList<>();
    final List<CookActivity.struct_menu> menus_noon    = new ArrayList<>();
    final List<CookActivity.struct_menu> menus_evening = new ArrayList<>();
    private int current_tab_index;
    FragmentManager fm = getSupportFragmentManager();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    survey_list.setAdapter(null);
                    current_tab_index = 1;
                    parser(1);
                    return true;
                case R.id.navigation_dashboard:
                    survey_list.setAdapter(null);
                    current_tab_index =2;
                    parser(2);
                    return true;
                case R.id.navigation_notifications:
                    survey_list.setAdapter(null);
                    current_tab_index = 3;
                    parser(3);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_activity);
        setTitle(R.string.questionaire_activty_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        current_tab_index = 1;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_survey);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Anket eklenecek sıkıntı yok :)", Snackbar.LENGTH_LONG)
              //          .setAction("Action", null).show();
                send_to_dialog();
        }
        });

        survey_list = (ListView) findViewById(R.id.survey_list);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        parser(current_tab_index);
    }

    public void parser(final int it){
        if(isNetworkAvailable()) {
            progress = new ProgressDialog(this);
            progress.setMessage("Checking surveys...");
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
            httpCall.setUrl("http://nepismis.afakan.net/android/anketGuncel");
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
                            JSONObject initial = array.getJSONObject(i);
                            ogun = initial.getInt("ogun");
                            JSONArray anket_array = initial.getJSONArray("anketm");
                            if (ogun == 1 && it == 1) { // Sabah anketleri

                                for (int j = 0; j < anket_array.length(); j++) {
                                    JSONObject json_anket = anket_array.getJSONObject(j);
                                    JSONArray menu_anket = json_anket.getJSONArray("menuanket");
                                    CookActivity.struct_menu m = new CookActivity.struct_menu();
                                    for (int z = 0; z < menu_anket.length(); z++) {
                                        JSONObject result = menu_anket.getJSONObject(z);
                                        m.meal.add(z, result.getString("yemek_adi"));
                                        if (z == 0)
                                            m.order_picture_id = result.getInt("yemek_id");
                                    }
                                    menus_morning.add(j, m);
                                }
                                update_morning_menus(menus_morning);
                            } else if(ogun == 2 && it == 2){ // Öğle anketleri
                                for (int j = 0; j < anket_array.length(); j++) {
                                    JSONObject json_anket = anket_array.getJSONObject(j);
                                    JSONArray menu_anket = json_anket.getJSONArray("menuanket");

                                    CookActivity.struct_menu m = new CookActivity.struct_menu();
                                    for (int z = 0; z < menu_anket.length(); z++) {
                                        JSONObject result = menu_anket.getJSONObject(z);
                                        m.meal.add(z, result.getString("yemek_adi"));
                                        if (z == 0)
                                            m.order_picture_id = result.getInt("yemek_id");
                                    }
                                    menus_noon.add(j, m);
                                }
                                update_noon_menus(menus_noon);
                            }else if(ogun == 3 && it == 3){ // Akşam anketleri
                                for (int j = 0; j < anket_array.length(); j++) {
                                    JSONObject json_anket = anket_array.getJSONObject(j);
                                    JSONArray menu_anket = json_anket.getJSONArray("menuanket");

                                    CookActivity.struct_menu m = new CookActivity.struct_menu();
                                    for (int z = 0; z < menu_anket.length(); z++) {
                                        JSONObject result = menu_anket.getJSONObject(z);
                                        m.meal.add(z, result.getString("yemek_adi"));
                                        if (z == 0)
                                            m.order_picture_id = result.getInt("yemek_id");
                                    }
                                    menus_evening.add(j, m);
                                }
                                update_evening_menus(menus_evening);
                            }
                        }
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

    private void update_morning_menus(List<CookActivity.struct_menu> menus_m){
        QuestionnarieAdapter morning_adapter = new QuestionnarieAdapter(this, menus_m);
        survey_list.setAdapter(morning_adapter);
    }

    private void update_noon_menus(List<CookActivity.struct_menu> menu_n){
        QuestionnarieAdapter noon_adapter = new QuestionnarieAdapter(this, menu_n);
        survey_list.setAdapter(noon_adapter);
    }

    private void update_evening_menus(List<CookActivity.struct_menu> menu_e){
        QuestionnarieAdapter evening_adapter = new QuestionnarieAdapter(this, menu_e);
        survey_list.setAdapter(evening_adapter);
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

    private void send_to_dialog(){
        MenusDialogFragment dFragment = new MenusDialogFragment();
        // Show DialogFragment

        Bundle bundle = new Bundle();
        bundle.putInt("current_tab_index",current_tab_index);
        dFragment.setArguments(bundle);
        dFragment.show(fm, "Dialog Fragment");

    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
       parser(current_tab_index);
    }

}
