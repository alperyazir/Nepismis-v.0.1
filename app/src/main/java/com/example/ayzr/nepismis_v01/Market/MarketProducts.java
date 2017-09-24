package com.example.ayzr.nepismis_v01.Market;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.CookActivity;
import com.example.ayzr.nepismis_v01.HttpCall;
import com.example.ayzr.nepismis_v01.HttpRequest;
import com.example.ayzr.nepismis_v01.Market.m_adapter.Marker_Past_Orders_Expandable_List_Adapter;
import com.example.ayzr.nepismis_v01.Market.m_adapter.Market_Products_Expandable_List_Adapter;
import com.example.ayzr.nepismis_v01.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketProducts extends AppCompatActivity {
    private ProgressDialog progress;
    SharedPreferences pref;

    Market_Products_Expandable_List_Adapter listAdapter;
    ExpandableListView expListView_product;

    List<String> listDataHeader;
    HashMap<String, List<market_products_struct>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_products);

        setTitle("Ürünlerim");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getApplicationContext().getSharedPreferences("Login_pref", 0); // 0 - for private mode

        // get the listview
        expListView_product = (ExpandableListView) findViewById(R.id.market_products_expandable);

        // preparing list data
        prepareListData();
    }

    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<market_products_struct>>();



        if (isNetworkAvailable()) {
            progress = new ProgressDialog(this);
            progress.setMessage("Checking orders...");
            progress.setIndeterminate(true);
            progress.show();

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.GET);
            httpCall.setUrl("http://nepismis.afakan.net/android/marketUrunler");
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", "" + pref.getInt("user_id", 0));
            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public List<CookActivity.struct_menu> onResponse(String response) {
                    super.onResponse(response);
                    try {
                        progress.dismiss();
                        JSONObject initial = new JSONObject(response);

                        JSONArray array_groups = initial.getJSONArray("gruplar");
                        JSONArray array_urunler = initial.getJSONArray("urunler");
                        for (int i = 0; i < array_groups.length(); i++) {
                            JSONObject object_group = array_groups.getJSONObject(i);
                            listDataHeader.add(object_group.getString("grup_adi"));
                            int group_id = object_group.getInt("id");

                            List<market_products_struct> a =  new ArrayList<market_products_struct>();

                            for (int j = 0; j < array_urunler.length(); j++) {
                                JSONObject object_urunler = array_urunler.getJSONObject(j);
                                int urun_group_id = object_urunler.getInt("grup_id");
                                if (group_id == urun_group_id) {
                                    market_products_struct m_p = new market_products_struct();
                                    m_p.product_picture_id = "" + object_urunler.getInt("urun_id");
                                    m_p.product_name = object_urunler.getString("urun_adi");
                                    m_p.product_price = object_urunler.getString("urun_fiyat");
                                    a.add(m_p);
                                }
                            }
                            listDataChild.put(object_group.getString("grup_adi"), a);
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
        listAdapter = new Market_Products_Expandable_List_Adapter(this, listDataHeader, listDataChild);
        expListView_product.setAdapter(listAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


    public static class market_products_struct {
        public String product_picture_id;
        public String product_name;
        public String product_price;
    }


}
