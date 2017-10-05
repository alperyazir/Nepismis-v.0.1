package com.example.ayzr.nepismis_v01.Market;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.AccountDatabase;
import com.example.ayzr.nepismis_v01.CookActivity;
import com.example.ayzr.nepismis_v01.HttpCall;
import com.example.ayzr.nepismis_v01.HttpRequest;
import com.example.ayzr.nepismis_v01.LoginActivity;
import com.example.ayzr.nepismis_v01.Market.m_adapter.Market_OrdersList_Adapter;
import com.example.ayzr.nepismis_v01.R;
import com.example.ayzr.nepismis_v01.adapters.CookListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarkerOrdes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private ProgressDialog progress;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;


    private ExpandableListView listView;

    List<String> listDataHeader;
    HashMap<String, List<MarkerOrdes.struct_market_order_details>> listDataChild;


    List<struct_market_order> o_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_ordes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Siparişler");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pref = getApplicationContext().getSharedPreferences("Login_pref", 0); // 0 - for private mode
        editor = pref.edit();

        listView = (ExpandableListView) findViewById(R.id.list_market_orders);

        o_list = new ArrayList<struct_market_order>();

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<MarkerOrdes.struct_market_order_details>>();

        parser();
    }

    public void parser() {
        if (isNetworkAvailable()) {
            progress = new ProgressDialog(this);
            progress.setMessage("Checking orders...");
            progress.setIndeterminate(true);
            progress.show();

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.GET);
            httpCall.setUrl("http://nepismis.afakan.net/android/marketSiparisGuncel");
            HashMap<String, String> params = new HashMap<>();
            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public List<CookActivity.struct_menu> onResponse(String response) {
                    super.onResponse(response);
                    try {
                        progress.dismiss();
                        //o_list.clear();
                        listDataHeader.clear();
                        listDataChild.clear();

                        JSONObject initial_object = new JSONObject(response);
                        JSONObject object_siparis = initial_object.getJSONObject("siparisler");
                        JSONArray array_users = initial_object.getJSONArray("user");

                        for (int i = 0; i < array_users.length(); i++) {

                            JSONObject user_obj = array_users.getJSONObject(i);
                            JSONArray array_user = object_siparis.getJSONArray("" + user_obj.getInt("user_id"));


                            //s_d_list.clear();
                            List<struct_market_order_details> s_d_list = new ArrayList<struct_market_order_details>();

                            for (int j = 0; j < array_user.length(); j++) {
                                JSONObject object_order = array_user.getJSONObject(j);
                                JSONObject object_so = object_order.getJSONObject("urun");
                                JSONObject object_usr = object_order.getJSONObject("user");

                                if (!listDataHeader.contains(object_usr.getString("name")))
                                    listDataHeader.add(object_usr.getString("name"));

                                struct_market_order_details s_d = new struct_market_order_details();
                                s_d.order_id = ""+object_usr.getInt("id");
                                s_d.order_price = object_so.getDouble("urun_fiyat");
                                s_d.order_name = object_so.getString("urun_adi");
                                s_d.order_count = object_order.getString("adet");
                                s_d_list.add(s_d);
                            }
                            listDataChild.put(listDataHeader.get(i), s_d_list);
                        }
                        update_orders();
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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(getApplicationContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();
        }
    }

    public void update_orders() {
        struct_market_order o = new struct_market_order();
        struct_market_order_details d = new struct_market_order_details();

        //listView.setAdapter(null);
        Market_OrdersList_Adapter adapter = new Market_OrdersList_Adapter(this, listDataHeader, listDataChild);
        listView.setAdapter(adapter);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.market_orders) {
            // Handle the camera action
        } else if (id == R.id.market_products) {
            startActivity(new Intent(this, MarketProducts.class));
        } else if (id == R.id.market_past_orders) {
            startActivity(new Intent(this, MarketPastOrders.class));

        } else if (id == R.id.market_profile) {

        } else if (id == R.id.market_about) {

        } else if (id == R.id.market_nav_sign_out) {
            editor.clear();
            editor.commit();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static class struct_market_order {
        public String _market_order_owner;
    }

    public static class struct_market_order_details {
        public String order_id;
        public String order_name;
        public double order_price;
        public String order_count;
    }
}
