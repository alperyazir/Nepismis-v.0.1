package com.example.ayzr.nepismis_v01.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ayzr.nepismis_v01.R;

import java.util.ArrayList;
import java.util.List;



public class FragmentDialogAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> mOrderList;



    public FragmentDialogAdapter(Activity activity, List<String> orders) {
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
        satirView = mInflater.inflate(R.layout.dialog_fragment_design, null);
        TextView textView = (TextView) satirView.findViewById(R.id.text_put_on_sale_menu_1);

        String cook = mOrderList.get(position);
        textView.setText(cook);
        return satirView;
    }
}
