package com.example.ayzr.nepismis_v01;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ayzr.nepismis_v01.Market.MarkerOrdes;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getApplicationContext().getSharedPreferences("Login_pref", 0); // 0 - for private mode
        editor = pref.edit();

        if(check_login()) {//önceden giriş yapmış ise
            // setContentView(R.layout.activity_main);
           // HashMap<String, String> params_db;
           // AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
           // params_db = accountDatabase.kullaniciDetay();

            if(pref.getInt("role",0) == 3) {      // Market
                startActivity(new Intent(this, CookActivity.class));
            }else if(pref.getInt("role",0) == 4) { // Kurye

            }else if(pref.getInt("role",0) == 5) { // Aşçı
                startActivity(new Intent(this, MarkerOrdes.class));
            }

        }else{//giriş yapmamış ise login sayfasına yönlenecek
            startActivity(new Intent(this,LoginActivity.class));
        }
        finish();
    }


    public boolean check_login(){
        if(pref.getAll().size() > 0){//0 dan fazla ise giris yapmss onceden demek
            //kullanici giris yapmis
            return true;
        }
        return false;
    }
}
