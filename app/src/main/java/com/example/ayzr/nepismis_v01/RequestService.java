package com.example.ayzr.nepismis_v01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RequestService extends AppCompatActivity {

    EditText editText_service_request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);
        setTitle(R.string.request_service_activity_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText_service_request = (EditText) findViewById(R.id.editText_request_service);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void send_request_clicked(View view){
        Toast.makeText(getApplicationContext(),editText_service_request.getText().toString() +  " adet servis istendi!",Toast.LENGTH_LONG).show();
    }
}
