package com.example.ayzr.nepismis_v01;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(check_login(getApplicationContext())) {//önceden giriş yapmış ise
            // setContentView(R.layout.activity_main);
            startActivity(new Intent(this,CookActivity.class));
        }else{//giriş yapmamış ise login sayfasına yönlenecek
            startActivity(new Intent(this,LoginActivity.class));
        }
        finish();
    }


    public static boolean check_login(Context context){
        AccountDatabase db = new AccountDatabase(context);
        int count = db.getRowCount();// databasedeki table logindeki row sayisi
        if(count > 0){//0 dan fazla ise giris yapmss onceden demek
            //kullanici giris yapmis
            return true;
        }
        return false;
    }
}
