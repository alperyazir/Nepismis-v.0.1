package com.example.ayzr.nepismis_v01;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.adapters.QuestionnarieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionnaireActivity extends AppCompatActivity {

    private Button button_morning;
    private Button button_noon;
    private Button button_evening;

    boolean morning_visibility = true;
    boolean noon_visibility = true;
    boolean evening_visibility = true;

    private ListView morning_list;
    private ListView noon_list;
    private ListView evening_list;

    int ogun;

    private ProgressDialog progress;
    final List<CookActivity.struct_menu> menus_morning = new ArrayList<>();
    final List<CookActivity.struct_menu> menus_noon    = new ArrayList<>();
    final List<CookActivity.struct_menu> menus_evening = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        setTitle(R.string.questionaire_activty_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_morning = (Button) findViewById(R.id.morning_questionaire_buttom_down);
        button_noon = (Button) findViewById(R.id.noon_questionaire_buttom_down);
        button_evening = (Button) findViewById(R.id.evening_questionaire_buttom_down);

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
                            if (ogun == 1) { // Sabah anketleri
                                JSONArray anket_array = initial.getJSONArray("anketm");

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
                            } else if(ogun == 2){ // Öğle anketleri
                                JSONArray anket_array = initial.getJSONArray("anletm");

                                for (int j = 0; j < anket_array.length(); j++) {
                                    JSONObject json_anket = anket_array.getJSONObject(j);
                                    JSONArray menu_anket = json_anket.getJSONArray("menuanket");

                                    CookActivity.struct_menu m = new CookActivity.struct_menu();
                                    for (int z = 0; z < menu_anket.length(); z++) {
                                        JSONObject result = menu_anket.getJSONObject(z);
                                        m.meal.add(z, result.getString("yemek_adi"));
                                        if (j == 0)
                                            m.order_picture_id = result.getInt("yemek_id");
                                    }
                                    menus_noon.add(j, m);
                                }
                                update_noon_menus(menus_noon);
                            }else if(ogun == 3){ // Akşam anketleri
                                JSONArray anket_array = initial.getJSONArray("anletm");

                                for (int j = 0; j < anket_array.length(); j++) {
                                    JSONObject json_anket = anket_array.getJSONObject(j);
                                    JSONArray menu_anket = json_anket.getJSONArray("menuanket");

                                    CookActivity.struct_menu m = new CookActivity.struct_menu();
                                    for (int z = 0; z < menu_anket.length(); z++) {
                                        JSONObject result = menu_anket.getJSONObject(z);
                                        m.meal.add(z, result.getString("yemek_adi"));
                                        if (j == 0)
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

        morning_list = (ListView) findViewById(R.id.questionnaire_morning_list);
        QuestionnarieAdapter morning_adapter = new QuestionnarieAdapter(this, menus_m);
        morning_list.setAdapter(morning_adapter);
    }

    private void update_noon_menus(List<CookActivity.struct_menu> menu_n){

        noon_list = (ListView) findViewById(R.id.questionnaire_noon_list);
        QuestionnarieAdapter noon_adapter = new QuestionnarieAdapter(this, menu_n);
        noon_list.setAdapter(noon_adapter);
    }

    private void update_evening_menus(List<CookActivity.struct_menu> menu_e){

        evening_list = (ListView) findViewById(R.id.questionnaire_evening_list);
        QuestionnarieAdapter evening_adapter = new QuestionnarieAdapter(this, menu_e);
        evening_list.setAdapter(evening_adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


    public void morning_questionaire_buttom_down_clicked(View view) {
        if (morning_visibility){
            morning_list.setVisibility(View.GONE);
            button_morning.setBackgroundResource(R.mipmap.right);
            morning_visibility = false;
        } else {
            morning_list.setVisibility(View.VISIBLE);
            button_morning.setBackgroundResource(R.mipmap.down);
            morning_visibility = true;
        }
    }

    public void noon_questionaire_buttom_down_clicked(View view) {
        if (noon_visibility){
            noon_list.setVisibility(View.GONE);
            button_noon.setBackgroundResource(R.mipmap.right);
            noon_visibility = false;
        } else {
            noon_list.setVisibility(View.VISIBLE);
            button_noon.setBackgroundResource(R.mipmap.down);
            noon_visibility = true;
        }
    }

    public void evening_questionaire_buttom_down_clicked(View view) {
        if (evening_visibility){
            evening_list.setVisibility(View.GONE);
            button_evening.setBackgroundResource(R.mipmap.right);
            evening_visibility = false;
        } else {
            evening_list.setVisibility(View.VISIBLE);
            button_evening.setBackgroundResource(R.mipmap.down);
            evening_visibility = true;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
