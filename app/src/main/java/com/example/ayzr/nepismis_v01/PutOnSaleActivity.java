package com.example.ayzr.nepismis_v01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PutOnSaleActivity extends AppCompatActivity {

    EditText editText_morning_order_count;
    EditText editText_noon_order_count;
    EditText editText_evening_order_count;

    TextView order_name_1;
    TextView order_menu_2;
    TextView order_menu_3;
    ImageView order_image;
    TextView ogun_tag;

    Button button_put_on_sale;


    private int ogun;
    private int anket_id;
    private CookActivity.struct_menu menu;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_put_on_sale);
        setTitle(R.string.put_on_sale_activity_name);

        order_name_1 = (TextView) findViewById(R.id.txt_put_on_sale_menu_1);
        order_menu_2 = (TextView) findViewById(R.id.txt_put_on_sale_menu_2);
        order_menu_3 = (TextView) findViewById(R.id.txt_put_on_sale_menu_3);
        order_image = (ImageView) findViewById(R.id.imge_put_on_sale_cook);
        ogun_tag = (TextView) findViewById(R.id.txt_put_on_sale_tag);
        button_put_on_sale = (Button) findViewById(R.id.button_put_on_sale_order_ready) ;

        editText_morning_order_count = (EditText) findViewById(R.id.editText_put_on_sale_order_count);
        parser();


        button_put_on_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_put_on_sale(Integer.parseInt(editText_morning_order_count.getText().toString()));
            }
        });


    }

    public void parser() {
        progress = new ProgressDialog(this);
        progress.setMessage("Checking surveys...");
        progress.setIndeterminate(true);
        progress.show();

        HashMap<String, String> params_db;

        AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
        params_db = accountDatabase.kullaniciDetay();

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("http://nepismis.afakan.net/android/satisaKoy");
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

                    menu = new CookActivity.struct_menu();
                    menu.menu_id = initial.getInt("menu_id");

                    JSONObject obje_anket = initial.getJSONObject("anket");

                    anket_id = obje_anket.getInt("anket_id");
                    ogun = initial.getInt("menu_ogun");
                    JSONArray array_result = initial.getJSONArray("menuy");


                    for (int i = 0; i < array_result.length(); i++) {

                        JSONObject json_ogun = array_result.getJSONObject(i);
                        menu.meal.add(i, json_ogun.getString("yemek_adi"));
                        if (i == 1) {
                            menu.order_picture_id = json_ogun.getInt("yemek_id");
                        }
                    }

                    update_menu();
                } catch (
                        JSONException e)

                {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(httpCall);

    }

    private void update_menu() {
        order_name_1.setText(menu.meal.get(0));
        order_menu_2.setText(menu.meal.get(1));
        order_menu_3.setText(menu.meal.get(2));
        if(ogun == 1){
            ogun_tag.setText("Sabah");
        }else if(ogun == 2){
            ogun_tag.setText("Öğle");
        }else if(ogun == 3){
            ogun_tag.setText("Akşam");
        }

        String url = "http://nepismis.afakan.net/images/yemek/" + menu.order_picture_id + ".jpg";

        Picasso.with(getApplicationContext()).load(url).fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher_ne_pismis)
                .error(R.mipmap.ic_launcher_ne_pismis)
                .into(order_image);

    }

    public void button_put_on_sale_increase_clicked(View view) {
        int temp_count = Integer.parseInt(editText_morning_order_count.getText().toString());
        ++temp_count;
        editText_morning_order_count.setText("" + temp_count);
    }

    public void button_put_on_sale_decrease_clicked(View view) {
        int temp_count = Integer.parseInt(editText_morning_order_count.getText().toString());
        if (--temp_count > 0) {
            editText_morning_order_count.setText("" + temp_count);
            temp_count = 1;
        }
    }

    private void send_put_on_sale(int count_sale){
        progress = new ProgressDialog(this);
        progress.setMessage("Porsiyon Gönderiliyor...");
        progress.setIndeterminate(true);
        progress.show();

        HashMap<String, String> params_db;

        AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
        params_db = accountDatabase.kullaniciDetay();

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("http://nepismis.afakan.net/android/satisaKoyPost");
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", params_db.get("tarih"));
        params.put("porsiyon","" + count_sale);
        params.put("anket_id","" + anket_id);
        params.put("menu_id","" + menu.menu_id);
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
                        Toast.makeText(getApplicationContext(), "Gönderildi :)", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Hata :(", Toast.LENGTH_SHORT).show();
                    }

                } catch (
                        JSONException e)

                {
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
