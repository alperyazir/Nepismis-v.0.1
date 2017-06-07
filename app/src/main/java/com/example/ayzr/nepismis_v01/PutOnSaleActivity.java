package com.example.ayzr.nepismis_v01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PutOnSaleActivity extends AppCompatActivity {

    EditText editText_morning_order_count;
    EditText editText_noon_order_count;
    EditText editText_evening_order_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_on_sale);
        setTitle(R.string.put_on_sale_activity_name);

        editText_morning_order_count = (EditText) findViewById(R.id.editText_put_on_sale_morning_order_count);
        editText_noon_order_count = (EditText) findViewById(R.id.editText_put_on_sale_noon_order_count);
        editText_evening_order_count = (EditText) findViewById(R.id.editText_put_on_sale_evening_order_count);

    }

    public void button_put_on_sale_morning_increase_clicked(View view){
        int temp_count =  Integer.parseInt(editText_morning_order_count.getText().toString());
        ++temp_count;
        editText_morning_order_count.setText(""+temp_count);
    }

    public void button_put_on_sale_morning_decrease_clicked(View view){
        int temp_count =  Integer.parseInt(editText_morning_order_count.getText().toString());
        if(--temp_count > 0) {
            editText_morning_order_count.setText("" + temp_count);
            temp_count = 1;
        }
    }

    public void button_put_on_sale_noon_increase_clicked(View view){
        int temp_count =  Integer.parseInt(editText_noon_order_count.getText().toString());
        ++temp_count;
        editText_noon_order_count.setText(""+temp_count);
    }

    public void button_put_on_sale_noon_decrease_clicked(View view){
        int temp_count =  Integer.parseInt(editText_noon_order_count.getText().toString());
        if(--temp_count > 0) {
            editText_noon_order_count.setText("" + temp_count);
            temp_count = 1;
        }
    }

    public void button_put_on_sale_evening_increase_clicked(View view){
        int temp_count =  Integer.parseInt(editText_evening_order_count.getText().toString());
        ++temp_count;
        editText_evening_order_count.setText(""+temp_count);
    }

    public void button_put_on_sale_evening_decrease_clicked(View view){
        int temp_count =  Integer.parseInt(editText_evening_order_count.getText().toString());
        if(--temp_count > 0) {
            editText_evening_order_count.setText("" + temp_count);
            temp_count = 1;
        }
    }

}
