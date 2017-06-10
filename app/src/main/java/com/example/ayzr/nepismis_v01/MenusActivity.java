package com.example.ayzr.nepismis_v01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MenusActivity extends AppCompatActivity {

    LinearLayout lay;
    Button buton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        lay = (LinearLayout) findViewById(R.id.layList);
        buton = (Button) findViewById(R.id.button2);

    }

    public void buttonClicked(View view){


    }
}
