package com.example.ayzr.nepismis_v01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.ayzr.nepismis_v01.adapters.CookListAdapter;
import com.example.ayzr.nepismis_v01.adapters.QuestionnarieAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireActivity extends AppCompatActivity {

    final List<String> questionnaries=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        setTitle(R.string.questionaire_activty_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionnaries.add(0,"alper");
        questionnaries.add(1,"tiner");
        questionnaries.add(2,"şamp");

        ListView morning_list = (ListView) findViewById(R.id.questionnaire_morning_list);
        ListView noon_list= (ListView) findViewById(R.id.questionnaire_noon_list);
        ListView evening_list = (ListView) findViewById(R.id.questionnaire_evening_list);


        QuestionnarieAdapter morning_adapter = new QuestionnarieAdapter(this,questionnaries);
        morning_list.setAdapter(morning_adapter);

        QuestionnarieAdapter noon_adapter = new QuestionnarieAdapter(this,questionnaries);
        noon_list.setAdapter(noon_adapter);

        QuestionnarieAdapter evening_adapter = new QuestionnarieAdapter(this,questionnaries);
        evening_list.setAdapter(evening_adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
