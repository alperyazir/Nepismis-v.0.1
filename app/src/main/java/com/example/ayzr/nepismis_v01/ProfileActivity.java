package com.example.ayzr.nepismis_v01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void btn_my_account_clicked(View view){
        startActivity(new Intent(this,AccountActivity.class));
    }

    public void btn_comment_clicked(View view){
        startActivity(new Intent(this,CommentsActivity.class));
    }
}
