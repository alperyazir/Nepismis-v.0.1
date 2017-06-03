package com.example.ayzr.nepismis_v01.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.example.ayzr.nepismis_v01.R;


public class CookListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String>   mOrderList;

    public CookListAdapter(Activity activity, List<String> orders) {
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
    public String getItem(int position) {
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
        satirView = mInflater.inflate(R.layout.cook_row_design, null);
        TextView textView = (TextView) satirView.findViewById(R.id.text_cook_order_owner);

        String cook = mOrderList.get(position);
        textView.setText(cook);
        return satirView;
    }
}