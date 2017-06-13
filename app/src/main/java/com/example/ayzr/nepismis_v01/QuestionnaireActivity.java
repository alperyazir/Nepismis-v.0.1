package com.example.ayzr.nepismis_v01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.ayzr.nepismis_v01.adapters.CookListAdapter;
import com.example.ayzr.nepismis_v01.adapters.QuestionnarieAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireActivity extends AppCompatActivity {

    final List<String> questionnaries = new ArrayList<String>();

    private Button button_morning;
    private Button button_noon;
    private Button button_evening;

    boolean morning_visibility = true;
    boolean noon_visibility = true;
    boolean evening_visibility = true;

    private ListView morning_list;
    private ListView noon_list;
    private ListView evening_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        setTitle(R.string.questionaire_activty_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_morning = (Button) findViewById(R.id.morning_questionaire_buttom_down);
        button_noon = (Button) findViewById(R.id.noon_questionaire_buttom_down);
        button_evening = (Button) findViewById(R.id.evening_questionaire_buttom_down);

        questionnaries.add(0, "alper");
        questionnaries.add(1, "tiner");
        questionnaries.add(2, "şamp");

        morning_list = (ListView) findViewById(R.id.questionnaire_morning_list);
        noon_list = (ListView) findViewById(R.id.questionnaire_noon_list);
        evening_list = (ListView) findViewById(R.id.questionnaire_evening_list);


        QuestionnarieAdapter morning_adapter = new QuestionnarieAdapter(this, questionnaries);
        morning_list.setAdapter(morning_adapter);

        QuestionnarieAdapter noon_adapter = new QuestionnarieAdapter(this, questionnaries);
        noon_list.setAdapter(noon_adapter);

        QuestionnarieAdapter evening_adapter = new QuestionnarieAdapter(this, questionnaries);
        evening_list.setAdapter(evening_adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


    public void morning_questionaire_buttom_down_clicked(View view) {
        if (morning_visibility){
            morning_list.setVisibility(View.GONE);
            button_morning.setBackgroundResource(R.mipmap.right);
            morning_visibility = false;
        } else {
            morning_list.setVisibility(View.VISIBLE);
            button_morning.setBackgroundResource(R.mipmap.down);
            morning_visibility = true;
        }
    }

    public void noon_questionaire_buttom_down_clicked(View view) {
        if (noon_visibility){
            noon_list.setVisibility(View.GONE);
            button_noon.setBackgroundResource(R.mipmap.right);
            noon_visibility = false;
        } else {
            noon_list.setVisibility(View.VISIBLE);
            button_noon.setBackgroundResource(R.mipmap.down);
            noon_visibility = true;
        }
    }

    public void evening_questionaire_buttom_down_clicked(View view) {
        if (evening_visibility){
            evening_list.setVisibility(View.GONE);
            button_evening.setBackgroundResource(R.mipmap.right);
            evening_visibility = false;
        } else {
            evening_list.setVisibility(View.VISIBLE);
            button_evening.setBackgroundResource(R.mipmap.down);
            evening_visibility = true;
        }
    }
}
