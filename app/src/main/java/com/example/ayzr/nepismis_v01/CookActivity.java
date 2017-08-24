package com.example.ayzr.nepismis_v01;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Handler;

import com.example.ayzr.nepismis_v01.adapters.CookListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CookActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final List<struct_order> orders = new ArrayList<>();
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ProgressDialog progress;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.cook_activity_name);
         listView = (ListView) findViewById(R.id.list_order);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        parser();
                    }
                }, 2000);
            }

        });
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        parser();

    }

    private void parser(){
        if(isNetworkAvailable()) {
            progress = new ProgressDialog(this);
            progress.setMessage("Checking orders...");
            progress.setIndeterminate(true);
            progress.show();

            HashMap<String, String> params_db;

            AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
            params_db = accountDatabase.kullaniciDetay();


            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.GET);
            httpCall.setUrl("http://nepismis.afakan.net/android/siparisGuncel");
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", params_db.get("tarih"));
            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public List<struct_menu> onResponse(String response) {
                    super.onResponse(response);
                    try {
                        progress.dismiss();

                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject initial = array.getJSONObject(i);
                            JSONArray menuArray = initial.getJSONArray("menusiparis");

                            struct_order o = new struct_order();
                            struct_menu m = new struct_menu();
                            for (int j = 0; j < menuArray.length(); j++) {
                                JSONObject json_menu = menuArray.getJSONObject(j);
                                String str = json_menu.getString("yemek_adi");
                                m.meal.add(j, str);
                                if (j == 0)
                                    m.order_picture_id = json_menu.getInt("yemek_id");
                            }
                            JSONObject order_json = initial.getJSONObject("uye");
                            o.order_name = order_json.getString("name");
                            o.order_count = initial.getInt("porsiyon");
                            o.order_menu = m;
                            o.order_price = initial.getDouble("yemek_tutar");

                            orders.add(i, o);
                        }
                        update_orders(orders);

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

    private void update_orders(List<struct_order> orders){

        listView.setAdapter(null);
        CookListAdapter adapter = new CookListAdapter(this,orders);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
            {
                super.onBackPressed();
                return;
            }
            else { Toast.makeText(getApplicationContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

            mBackPressed = System.currentTimeMillis();
        }
   }

    public void button_cook_order_ready_clicked(View view){
        /// TODO
        /// Add codes what happened when order ready
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_order) {
            // Handle the camera action
        } else if (id == R.id.nav_menu_on_order) {
            startActivity(new Intent(this, OrdersOnSale.class));
        } else if (id == R.id.nav_put_on_sale) {
            startActivity(new Intent(this,PutOnSaleActivity.class));
        } else if (id == R.id.nav_questionnaire) {
            startActivity(new Intent(this,Survey_activity.class));
        } else if (id == R.id.nav_my_account) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_help) {
            addNotification();
        } else if (id == R.id.nav_sign_out) {

            AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
            accountDatabase.resetTables();
            startActivity(new Intent(this,LoginActivity.class));
            finish();

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_menus){
            startActivity(new Intent(this,ManuActivity.class));
        } else if (id == R.id.nav_request_service){
            startActivity(new Intent(this,RequestService.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ne_pismis);
        mBuilder.setContentTitle("Basit Bildirim");
        mBuilder.setContentText("Bu basit bir bildirimdir.");
        mBuilder.setAutoCancel(true);

        //Uygulama da açılacak Activity, intent olarak tanımlanıyor.
        Intent resultIntent = new Intent(this, MenusActivity.class);

        //stackBuilder nesnesi activityler arasında geri geçişi oluşturuyor.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //Açılacak activity'i parent stack'e ekliyoruz böylece, ResultActivity'den geri geldiğimiz de MainActivity açılacak.
        stackBuilder.addParentStack(CookActivity.class);
        //Bildirim ile açılacak activity'i ekliyoruz.
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static class struct_menu{
        public int menu_id;
        public List<String> meal = new ArrayList<String>();
        public int order_picture_id;

    }
    public static class struct_order{
        public String order_name;
        public struct_menu order_menu;
        public int order_count;
        public double order_price;
    }
}



