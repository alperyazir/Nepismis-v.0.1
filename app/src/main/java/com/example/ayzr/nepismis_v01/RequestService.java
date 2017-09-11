package com.example.ayzr.nepismis_v01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class RequestService extends AppCompatActivity {

    EditText editText_service_request;
    private Button button_send_request;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);
        setTitle(R.string.request_service_activity_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText_service_request = (EditText) findViewById(R.id.editText_request_service);
        button_send_request = (Button) findViewById(R.id.button_send_request);


        button_send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_service_request();
            }
        });
    }

    public void send_service_request(){
        progress = new ProgressDialog(this);
        progress.setMessage("Servis isteniyor ...");
        progress.setIndeterminate(true);
        progress.show();

        HashMap<String, String> params_db;
        AccountDatabase accountDatabase = new AccountDatabase(getApplicationContext());
        params_db = accountDatabase.kullaniciDetay();

        HttpCall httpCall2 = new HttpCall();
        httpCall2.setMethodtype(HttpCall.GET);
        httpCall2.setUrl("http://nepismis.afakan.net/android/servisIste");
        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",params_db.get("tarih"));
        params.put("servis", editText_service_request.getText().toString());
        httpCall2.setParams(params);
        new HttpRequest(){
            @Override
            public List<CookActivity.struct_menu> onResponse(String response) {
                super.onResponse(response);
                try {
                    progress.dismiss();
                    JSONObject initial = new JSONObject(response);
                    int saved = initial.getInt("save");
                    if (saved == 1) {
                        Toast.makeText(getApplicationContext(),  editText_service_request.getText().toString() +" Adet Servis İstendi :)", Toast.LENGTH_SHORT).show();
                        editText_service_request.setText("");

                    } else {
                        Toast.makeText(getApplicationContext(), "Servis istenirken hata oluştu :(", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(),"Başarısız servis isteme!",Toast.LENGTH_SHORT).show();

                }
                return null;
            }

        }.execute(httpCall2);
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
