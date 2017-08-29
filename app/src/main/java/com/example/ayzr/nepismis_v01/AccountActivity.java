package com.example.ayzr.nepismis_v01;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    ToggleButton toggle_today;
    ToggleButton toggle_month;
    ToggleButton toggle_total;
    private ProgressDialog progress;


    private TextView text_user_name;
    private TextView text_user_email;
    private TextView text_phone_number;
    private TextView text_user_adress;

    private TextView order_count;
    private TextView order_price;
    private TextView progress_payment;

    String user_name = "-";
    String user_email = "-";
    String user_phone_number = "-";
    String user_adress = "-";

    int today_order_count = 0;
    double today_order_price = 0;
    double today_progress_payment = 0;

    int month_order_count = 0;
    double month_order_price = 0;
    double month_progress_payment = 0;

    int total_order_count = 0;
    double total_order_price = 0;
    double total_progress_payment = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle(R.string.account_activity_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        text_user_name = (TextView) findViewById(R.id.text_account_name);
        text_user_email = (TextView) findViewById(R.id.text_account_email);
        text_phone_number = (TextView) findViewById(R.id.text_account_phone_number);
        text_user_adress = (TextView) findViewById(R.id.text_account_adress);
        order_count = (TextView) findViewById(R.id.text_order_count);
        order_price = (TextView) findViewById(R.id.text_account_order_price);
        progress_payment = (TextView) findViewById(R.id.text_account_earned_money);


        toggle_today = (ToggleButton) findViewById(R.id.toggle_button_account_today);
        toggle_month = (ToggleButton) findViewById(R.id.toggle_button_account_this_month);
        toggle_total = (ToggleButton) findViewById(R.id.toggle_button_account_this_total);

        toggle_today.setChecked(true);
        toggle_month.setChecked(false);
        toggle_total.setChecked(false);

        toggle_today.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_on));
        toggle_month.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));
        toggle_total.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));

        toggle_today.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    toggle_today.setChecked(true);
                    toggle_month.setChecked(false);
                    toggle_total.setChecked(false);

                    toggle_today.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_on));
                    toggle_month.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));
                    toggle_total.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));

                    order_count.setText(today_order_count + " Porsiyon");
                    order_price.setText("" + today_order_price + " TL");
                    progress_payment.setText("" + today_progress_payment + " TL");

                } else {
                }
            }
        });

        toggle_month.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    toggle_today.setChecked(false);
                    toggle_month.setChecked(true);
                    toggle_total.setChecked(false);

                    toggle_today.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));
                    toggle_month.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_on));
                    toggle_total.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));

                    order_count.setText(month_order_count + " Porsiyon");
                    order_price.setText("" + month_order_price + " TL");
                    progress_payment.setText("" + month_progress_payment + " TL");

                } else {

                }
            }
        });

        toggle_total.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    toggle_today.setChecked(false);
                    toggle_month.setChecked(false);
                    toggle_total.setChecked(true);

                    toggle_today.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));
                    toggle_month.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));
                    toggle_total.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_on));

                    order_count.setText(total_order_count + " Porsiyon");
                    order_price.setText("" + total_order_price + " TL");
                    progress_payment.setText("" + total_progress_payment + " TL");

                } else {
                }
            }
        });

        parser();
    }

    private void parser() {
        if (isNetworkAvailable()) {
            progress = new ProgressDialog(this);
            progress.setMessage("Loading ...");
            progress.setIndeterminate(true);
            progress.show();

            HashMap<String, String> params_db;

            AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
            params_db = accountDatabase.kullaniciDetay();


            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.GET);
            httpCall.setUrl("http://nepismis.afakan.net/android/hesap");
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
                        JSONObject user_json = initial.getJSONObject("user");
                        user_name = user_json.getString("name");
                        user_email = user_json.getString("email");

                        if (!initial.isNull("bugun_porsiyon"))
                            today_order_count = initial.getInt("bugun_porsiyon");
                        if (!initial.isNull("bugun_tutar"))
                            today_order_price = initial.getDouble("bugun_tutar");
                        today_progress_payment = today_order_price * 65 / 100;

                        if (!initial.isNull("buay_porsiyon"))
                            month_order_count = initial.getInt("buay_porsiyon");
                        if (!initial.isNull("buay_tutar"))
                            month_order_price = initial.getDouble("buay_tutar");
                        month_progress_payment = month_order_price * 65 / 100;

                        if (!initial.isNull("toplam_porsiyon"))
                            total_order_count = initial.getInt("toplam_porsiyon");
                        if (!initial.isNull("toplam_porsiyon"))
                            total_order_price = initial.getDouble("toplam_porsiyon");

                        total_progress_payment = total_order_price * 65 / 100;

                        update_infos();
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

    private void update_infos() {
        text_user_name.setText(user_name);
        text_user_email.setText(user_email);
        text_phone_number.setText(user_phone_number);
        text_user_adress.setText(user_adress);
        order_count.setText(today_order_count + " Porsiyon");
        order_price.setText("" + today_order_price + " TL");
        progress_payment.setText("" + today_progress_payment + " TL");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
