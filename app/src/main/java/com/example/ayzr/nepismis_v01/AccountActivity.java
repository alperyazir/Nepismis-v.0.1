package com.example.ayzr.nepismis_v01;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class AccountActivity extends AppCompatActivity {

    ToggleButton toggle_today;
    ToggleButton toggle_month;
    ToggleButton toggle_total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setTitle(R.string.account_activity_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                if(isChecked) {
                    toggle_today.setChecked(true);
                    toggle_month.setChecked(false);
                    toggle_total.setChecked(false);

                    toggle_today.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_on));
                    toggle_month.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));
                    toggle_total.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));
                }else {
                }
            }
        });

        toggle_month.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked) {
                    toggle_today.setChecked(false);
                    toggle_month.setChecked(true);
                    toggle_total.setChecked(false);

                    toggle_today.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));
                    toggle_month.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_on));
                    toggle_total.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));
                }else {

                }
            }
        });

        toggle_total.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked) {
                    toggle_today.setChecked(false);
                    toggle_month.setChecked(false);
                    toggle_total.setChecked(true);

                    toggle_today.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));
                    toggle_month.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_off));
                    toggle_total.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.account_toogle_on));
                }else {
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
