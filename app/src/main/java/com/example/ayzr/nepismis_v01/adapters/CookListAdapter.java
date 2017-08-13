package com.example.ayzr.nepismis_v01.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.example.ayzr.nepismis_v01.CookActivity;
import com.example.ayzr.nepismis_v01.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


public class CookListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<CookActivity.struct_order> mOrderList;

    public CookListAdapter(Activity activity, List<CookActivity.struct_order> orders) {
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
    public CookActivity.struct_order getItem(int position) {
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

        TextView order_name = (TextView) satirView.findViewById(R.id.text_cook_order_owner);
        TextView order_menu_1 = (TextView) satirView.findViewById(R.id.text_cook_order_menu_1);
        TextView order_menu_2 = (TextView) satirView.findViewById(R.id.text_cook_order_menu_2);
        TextView order_menu_3 = (TextView) satirView.findViewById(R.id.text_cook_order_menu_3);
        TextView order_count = (TextView)  satirView.findViewById(R.id.text_cook_order_count);
        ImageView order_image = (ImageView) satirView.findViewById(R.id.image_cook_order_menu);

        CookActivity.struct_order cook = mOrderList.get(position);

        order_name.setText(cook.order_name);
        order_menu_1.setText(cook.order_menu.meal.get(0));
        order_menu_2.setText(cook.order_menu.meal.get(1));
        order_menu_3.setText(cook.order_menu.meal.get(2));
        order_count.setText(""+cook.order_count + " Porsiyon");


        String url = "http://nepismis.afakan.net/images/yemek/" + cook.order_menu.order_picture_id +  ".jpg";
        Picasso.with(mInflater.getContext()).load(url).fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher_ne_pismis)
                .error(R.mipmap.ic_launcher_ne_pismis)
                .into(order_image);
        return satirView;
    }

    }