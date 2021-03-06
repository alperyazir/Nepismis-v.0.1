package com.example.ayzr.nepismis_v01.Market;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.AccountDatabase;
import com.example.ayzr.nepismis_v01.CookActivity;
import com.example.ayzr.nepismis_v01.HttpCall;
import com.example.ayzr.nepismis_v01.HttpRequest;
import com.example.ayzr.nepismis_v01.Market.m_adapter.Marker_Past_Orders_Expandable_List_Adapter;
import com.example.ayzr.nepismis_v01.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketPastOrders extends AppCompatActivity {

    Marker_Past_Orders_Expandable_List_Adapter listAdapter;
    ExpandableListView expListView;
    List<market_past_orders> listDataHeader;
    HashMap<String, List<market_past_orders_details>> listDataChild;
    private ProgressDialog progress;
    List<market_past_orders_details> a = new ArrayList<market_past_orders_details>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_past_orders);

        setTitle("Geçmiş Siparişler");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandable);

        // preparing list data
        prepareListData();


    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {


        listDataHeader = new ArrayList<market_past_orders>();
        listDataChild = new HashMap<String, List<market_past_orders_details>>();

        if (isNetworkAvailable()) {
            progress = new ProgressDialog(this);
            progress.setMessage("Checking orders...");
            progress.setIndeterminate(true);
            progress.show();


            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.GET);
            httpCall.setUrl("http://nepismis.afakan.net/android/marketOncekiSiparisler");
            HashMap<String, String> params = new HashMap<>();
            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public List<CookActivity.struct_menu> onResponse(String response) {
                    super.onResponse(response);
                    try {
                        progress.dismiss();

                        JSONObject initial = new JSONObject(response);

                        JSONArray array = initial.getJSONArray("siparisler");
                        a.clear();
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject siparis_object = array.getJSONObject(i);

                            market_past_orders m = new market_past_orders();
                            m.date = siparis_object.getString("created_at");
                            m.total_price = "0 TL";
                            listDataHeader.add(m);

                            market_past_orders_details m_d = new market_past_orders_details();
                            m_d.order_count = siparis_object.getString("adet");
                            m_d.order_price = siparis_object.getString("fiyat");
                            JSONObject user_json = siparis_object.getJSONObject("user");
                            m_d.owner = user_json.getString("name");

                            a.add(m_d);
                            listDataChild.put(listDataHeader.get(i).date, a);
                        }
                        update_past_orders();

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

    private void update_past_orders() {
        listAdapter = new Marker_Past_Orders_Expandable_List_Adapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


    public static class market_past_orders {
        public String date;
        public String total_price;
    }

    public static class market_past_orders_details {
        public String owner;
        public String order_count;
        public String order_price;
    }
}

