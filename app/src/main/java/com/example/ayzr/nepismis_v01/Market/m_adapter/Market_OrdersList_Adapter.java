package com.example.ayzr.nepismis_v01.Market.m_adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.AccountDatabase;
import com.example.ayzr.nepismis_v01.CookActivity;
import com.example.ayzr.nepismis_v01.HttpCall;
import com.example.ayzr.nepismis_v01.HttpRequest;
import com.example.ayzr.nepismis_v01.Market.MarkerOrdes;
import com.example.ayzr.nepismis_v01.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Market_OrdersList_Adapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MarkerOrdes.struct_market_order> mOrderList;
    private Button button_ready;
    private ProgressDialog progress;


    public Market_OrdersList_Adapter(Activity activity, List<MarkerOrdes.struct_market_order> orders) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mOrderList = orders;
    }

    @Override
    public int getCount() {
        return mOrderList.size();
    }

    @Override
    public MarkerOrdes.struct_market_order getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View satirView;
        satirView = mInflater.inflate(R.layout.list_market_order_row_design, null);

        TextView order_owner = (TextView) satirView.findViewById(R.id.txt_market_orders_owner);
        TextView order_count = (TextView) satirView.findViewById(R.id.txt_market_order_count);
        TextView order_name = (TextView) satirView.findViewById(R.id.txt_market_order_name);
        TextView order_price = (TextView) satirView.findViewById(R.id.txt_market_order_price);

        button_ready = (Button) satirView.findViewById(R.id.btn_market_order_ready);


        MarkerOrdes.struct_market_order market_order= mOrderList.get(position);

        order_owner.setText(market_order._market_order_owner);
        order_count.setText( market_order.market_order_list_details.get(position).order_count);
        order_name.setText(market_order.market_order_list_details.get(position).order_name);
        order_price.setText(market_order.market_order_list_details.get(position).order_price);




      //final AlertDialog.Builder builder = new AlertDialog.Builder(button_ready.getContext());

      //builder.setTitle("Onay");
      //builder.setMessage("Emin miyiz?");

      //builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {

      //    public void onClick(DialogInterface dialog, int which) {
      //        send_order_ready(cook.order_id);
      //        dialog.dismiss();
      //    }
      //});

      //builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {

      //    @Override
      //    public void onClick(DialogInterface dialog, int which) {
      //        dialog.dismiss();
      //    }
      //});

    // button_ready.setOnClickListener(new View.OnClickListener() {
    //     @Override
    //     public void onClick(View v) {
    //         builder.show();
    //     }
    // });


        return satirView;
    }

    private void send_order_ready(int id) {
        HashMap<String, String> params_db;
        AccountDatabase accountDatabase = new AccountDatabase(button_ready.getContext());
        params_db = accountDatabase.kullaniciDetay();


        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("http://nepismis.afakan.net/android/siparisOnay");
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", params_db.get("tarih"));
        params.put("siparis_id", "" + id);
        httpCall.setParams(params);

        new HttpRequest() {
            @Override
            public List<CookActivity.struct_menu> onResponse(String response) {
                super.onResponse(response);
                try {

                    JSONObject object = new JSONObject(response);
                    boolean b = object.getBoolean("save");
                    if (b) {
                        Toast.makeText(button_ready.getContext(), "Sipariş onaylandı :)", Toast.LENGTH_SHORT).show();
                        callBack();
                    } else {
                        Toast.makeText(button_ready.getContext(), "Sipariş Onaylanamadı ! :)", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(httpCall);

    }

    public void callBack(){}
}

