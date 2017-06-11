package com.example.ayzr.nepismis_v01.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ayzr.nepismis_v01.R;

import java.util.List;


public class MenusAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> mMenus;

    public MenusAdapter(Activity activity, List<String> menus) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mMenus = menus;
    }

    @Override
    public int getCount() {
        return mMenus.size();
    }

    @Override
    public String getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            View satirView;
            String cook = mMenus.get(position);

            if(cook == "menu") {
                satirView = mInflater.inflate(R.layout.add_button_design, null);
            }
            else {
                satirView = mInflater.inflate(R.layout.menus_design, null);
                TextView textView = (TextView) satirView.findViewById(R.id.text_put_on_sale_menu_1);
                textView.setText(cook);
            }

                return satirView;




    }

}