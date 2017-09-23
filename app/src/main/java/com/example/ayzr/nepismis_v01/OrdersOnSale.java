package com.example.ayzr.nepismis_v01;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class OrdersOnSale extends AppCompatActivity {
    private ProgressDialog progress;
    private CookActivity.struct_menu menu;
    private int count_menu_on_sale;
    private int count_remain_menu;
    double rating_no;

    TextView meal_1;
    TextView meal_2;
    TextView meal_3;
    ImageView image;
    RatingBar rating;
    TextView menu_sale;
    TextView remain_menu;
    Button remove_order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_on_sale);

        setTitle(R.string.orders_on_sale);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        meal_1 = (TextView) findViewById(R.id.text_on_sale_menu_1);
        meal_2 = (TextView) findViewById(R.id.text_on_sale_menu_2);
        meal_3 = (TextView) findViewById(R.id.text_on_sale_menu_3);
        image = (ImageView) findViewById(R.id.image_on_sale);
        rating = (RatingBar) findViewById(R.id.rating_on_sale);
        menu_sale = (TextView) findViewById(R.id.text_count_menu_on_sale);
        remain_menu = (TextView) findViewById(R.id.text_count_remain_menu_on_sale);
        remove_order = (Button) findViewById(R.id.button_on_sale_remove);

        parser();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
        builder1.setTitle("Onay");
        builder1.setMessage("Emin miyiz?");

        builder1.setPositiveButton(
                "Evet",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        remove_order_ready();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Hayır",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alert11 = builder1.create();


        remove_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // alert11.show();
                remove_order_ready();
            }
        });
    }

    public void parser() {
        progress = new ProgressDialog(this);
        progress.setMessage("Checking menus...");
        progress.setIndeterminate(true);
        progress.show();


        HashMap<String, String> params_db;

        AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
        params_db = accountDatabase.kullaniciDetay();


        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("http://nepismis.afakan.net/android/satisMenu");
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
                    count_menu_on_sale = initial.getInt("ilk_porsiyon");
                    count_remain_menu = initial.getInt("porsiyon");
                    rating_no = initial.getDouble("puan");
                    menu = new CookActivity.struct_menu();
                    menu.menu_id = initial.getInt("menu_id");

                    JSONArray array_menu = initial.getJSONArray("menusatis");
                    for (int i = 0; i < array_menu.length(); i++) {
                        JSONObject ogun_object = array_menu.getJSONObject(i);
                        menu.meal.add(i, ogun_object.getString("yemek_adi"));
                        if (i == 1) {
                            menu.order_picture_id = ogun_object.getInt("yemek_id");
                        }
                    }
                    update_menus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(httpCall);

    }

    public void update_menus() {

        meal_1.setText(menu.meal.get(0));
        meal_2.setText(menu.meal.get(1));
        meal_3.setText(menu.meal.get(2));

        menu_sale.setText("Satışa Sunulan :" + count_menu_on_sale + " Adet");
        remain_menu.setText("Kalan : " + count_remain_menu + " Adet");

        rating.setRating((float) rating_no);

        String url = "http://nepismis.afakan.net/images/yemek/" + menu.order_picture_id + ".jpg";

        Picasso.with(getApplicationContext()).load(url).fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher_ne_pismis)
                .error(R.mipmap.ic_launcher_ne_pismis)
                .into(image);

    }

    public void remove_order_ready() {
        progress = new ProgressDialog(this);
        progress.setMessage("Kaldırılıyor...");
        progress.setIndeterminate(true);
        progress.show();


        HashMap<String, String> params_db;

        AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
        params_db = accountDatabase.kullaniciDetay();


        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("http://nepismis.afakan.net/android/satisMenuSil");
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", params_db.get("tarih"));
        params.put("menu_id", ""+menu.menu_id);
        httpCall.setParams(params);

        new HttpRequest() {
            @Override
            public List<CookActivity.struct_menu> onResponse(String response) {
                super.onResponse(response);
                try {
                    progress.dismiss();

                    JSONObject initial = new JSONObject(response);
                    int saved = initial.getInt("save");
                    if (saved == 1) {
                        Toast.makeText(getApplicationContext(), "Kaldırıldı :)", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Hata! Kaldıralamadı  :(", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(httpCall);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
