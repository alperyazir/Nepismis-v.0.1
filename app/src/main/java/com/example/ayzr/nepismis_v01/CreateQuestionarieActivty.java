package com.example.ayzr.nepismis_v01;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CreateQuestionarieActivty extends AppCompatActivity {

    FragmentManager fm = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_questionarie_activty);
    }

    public void button_morning_create_questionarie_clicked(View view){
        MenusDialogFragment dFragment = new MenusDialogFragment();
        // Show DialogFragment
        dFragment.show(fm, "Dialog Fragment");
    }
    public void button_noon_create_questionarie_clicked(View view){
        MenusDialogFragment dFragment = new MenusDialogFragment();
        // Show DialogFragment
        dFragment.show(fm, "Dialog Fragment");
    }
    public void button_evening_create_questionarie_clicked(View view){
        MenusDialogFragment dFragment = new MenusDialogFragment();
        // Show DialogFragment
        dFragment.show(fm, "Dialog Fragment");
    }
}
