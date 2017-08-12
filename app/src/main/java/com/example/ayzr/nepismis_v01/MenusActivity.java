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
import android.widget.GridView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.adapters.MenusAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenusActivity extends AppCompatActivity {

    GridView morning_grid;
    GridView noon_grid;
    GridView evening_grid;
    Button button_morning;
    Button button_noon;
    Button button_evening;

    MenusAdapter morning_adapter;
    MenusAdapter noon_adapter;
    MenusAdapter evening_adapter;

    boolean morning_visibility = true;
    boolean noon_visibility = true;
    boolean evening_visibility = true;


    private ProgressDialog progress;
    final List<CookActivity.struct_menu> menus_morning = new ArrayList<>();
    final List<CookActivity.struct_menu> menus_noon = new ArrayList<>();
    final List<CookActivity.struct_menu> menus_evening = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        setTitle(R.string.menus_activity_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_morning = (Button) findViewById(R.id.menus_morning_buttom_down);
        button_noon = (Button) findViewById(R.id.menus_noon_buttom_down);
        button_evening = (Button) findViewById(R.id.menus_evening_buttom_down);
        if(isNetworkAvailable()) {
            progress = new ProgressDialog(this);
            progress.setMessage("Checking menus...");
            progress.setIndeterminate(true);
            progress.show();

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
               public List<CookActivity.struct_menu> onResponse(String response) {
                   super.onResponse(response);
                    try {
                        progress.dismiss();

                        JSONObject initial = new JSONObject(response);
                        JSONArray morning_array = initial.getJSONArray("sabah");
                        JSONArray noon_array = initial.getJSONArray("ogle");
                        JSONArray evening_array = initial.getJSONArray("aksam");

                        for(int i = 0; i < morning_array.length(); i++){
                            JSONObject morning_menus = morning_array.getJSONObject(i);
                            JSONArray menuy_array = morning_menus.getJSONArray("menuy");

                            CookActivity.struct_menu m = new CookActivity.struct_menu();
                            for(int j = 0; j < menuy_array.length(); j++ ){
                                JSONObject morning_menuy = menuy_array.getJSONObject(j);
                                m.meal.add(j,morning_menuy.getString("yemek_adi"));
                                if (j == 0)
                                    m.order_picture_id = morning_menuy.getInt("yemek_id");
                            }
                            menus_morning.add(i,m);
                        }

                        for(int i = 0; i < noon_array.length(); i++){
                            JSONObject morning_menus = noon_array.getJSONObject(i);
                            JSONArray menuy_array = morning_menus.getJSONArray("menuy");

                            CookActivity.struct_menu m = new CookActivity.struct_menu();
                            for(int j = 0; j < menuy_array.length(); j++ ){
                                JSONObject morning_menuy = menuy_array.getJSONObject(j);
                                m.meal.add(j,morning_menuy.getString("yemek_adi"));
                                if (j == 0)
                                    m.order_picture_id = morning_menuy.getInt("yemek_id");
                            }
                            menus_noon.add(i,m);
                        }

                        for(int i = 0; i < evening_array.length(); i++){
                            JSONObject morning_menus = evening_array.getJSONObject(i);
                            JSONArray menuy_array = morning_menus.getJSONArray("menuy");

                            CookActivity.struct_menu m = new CookActivity.struct_menu();
                            for(int j = 0; j < menuy_array.length(); j++ ){
                                JSONObject morning_menuy = menuy_array.getJSONObject(j);
                                m.meal.add(j,morning_menuy.getString("yemek_adi"));
                                if (j == 0)
                                    m.order_picture_id = morning_menuy.getInt("yemek_id");
                            }
                            menus_evening.add(i,m);
                        }
                        update_morning_orders(menus_morning);
                        update_noon_orders(menus_noon);
                        update_evening_orders(menus_evening);

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
                   return null;
               }
            }.execute(httpCall);

        } else {
            Toast.makeText(getApplicationContext(), "No internet access!", Toast.LENGTH_SHORT).show();
        }


   //    morning_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
   //        @Override
   //        public void onItemClick(AdapterView<?> parent, View v,
   //                                int position, long id) {

   //            if (position == menus_morning.size() -1) {
   //                menus_morning.remove(position);
   //                menus_morning.add(position, "Capare");
   //                menus_morning.add(position + 1, "menu");

   //                morning_grid.setAdapter(morning_adapter);
   //            }
   //        }
   //    });

   //    noon_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
   //        @Override
   //        public void onItemClick(AdapterView<?> parent, View v,
   //                                int position, long id) {

   //            if (position == menus_noon.size() -1) {
   //                menus_noon.remove(position);
   //                menus_noon.add(position, "Capare");
   //                menus_noon.add(position + 1, "menu");

   //                noon_grid.setAdapter(noon_adapter);
   //            }
   //        }
   //    });

   //   evening_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
   //       @Override
   //       public void onItemClick(AdapterView<?> parent, View v,
   //                               int position, long id) {

   //           if (position == menus_evening.size() -1) {
   //               menus_evening.remove(position);
   //               menus_evening.add(position, "Capare");
   //               menus_evening.add(position + 1, "menu");

   //               evening_grid.setAdapter(evening_adapter);
   //           }
   //       }
   //   });
    }
    public void update_morning_orders(List<CookActivity.struct_menu> menu){
        morning_grid = (GridView) findViewById(R.id.menus_morning_grid);
        morning_adapter = new MenusAdapter(this, menu);
        morning_grid.setAdapter(morning_adapter);
    }

    public void update_noon_orders(List<CookActivity.struct_menu> menu){
        noon_grid = (GridView) findViewById(R.id.menus_noon_grid);
        noon_adapter = new MenusAdapter(this, menu);
        noon_grid.setAdapter(noon_adapter);
    }

    public void update_evening_orders(List<CookActivity.struct_menu> menu){
        evening_grid = (GridView) findViewById(R.id.menus_evening_grid);
        evening_adapter = new MenusAdapter(this, menu);
        evening_grid.setAdapter(evening_adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void buttonClicked(View view) {

        // FragmentManager fragmentManager = getFragmentManager();
        // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // MenusFragment hello = new MenusFragment();
        //  fragmentTransaction.add(R.id.  layout_container, hello, "HELLO");
        // fragmentTransaction.commit();

    }

    public void menus_morning_buttom_down_clicked(View view) {

        if (morning_visibility) {
            morning_grid.setVisibility(View.GONE);
            button_morning.setBackgroundResource(R.mipmap.right);
            morning_visibility = false;
        } else {
            morning_grid.setVisibility(View.VISIBLE);
            button_morning.setBackgroundResource(R.mipmap.down);
            morning_visibility = true;
        }
    }

    public void menus_noon_buttom_down_clicked(View view) {

        if (noon_visibility) {
            noon_grid.setVisibility(View.GONE);
            button_noon.setBackgroundResource(R.mipmap.right);
            noon_visibility = false;
        } else {
            noon_grid.setVisibility(View.VISIBLE);
            button_noon.setBackgroundResource(R.mipmap.down);
            noon_visibility = true;
        }
    }

    public void menus_evening_buttom_down_clicked(View view) {

        if (evening_visibility) {
            evening_grid.setVisibility(View.GONE);
            button_evening.setBackgroundResource(R.mipmap.right);
            evening_visibility = false;
        } else {
            evening_grid.setVisibility(View.VISIBLE);
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
