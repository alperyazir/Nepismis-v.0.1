package com.example.ayzr.nepismis_v01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.ayzr.nepismis_v01.adapters.CommentsAdapter;
import com.example.ayzr.nepismis_v01.adapters.CookListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    final List<String> yorumlar=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        setTitle(R.string.comment_activity_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       yorumlar.add(0,"alper");
       yorumlar.add(1,"tiner");
       yorumlar.add(2,"şamp");
       yorumlar.add(3,"kocabas");
       yorumlar.add(4,"izzet");
       yorumlar.add(5,"neşet");

        ListView listView = (ListView) findViewById(R.id.list_comments);
        CommentsAdapter adapter = new CommentsAdapter(this,yorumlar);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
