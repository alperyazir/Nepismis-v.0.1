package com.example.ayzr.nepismis_v01.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ayzr.nepismis_v01.CookActivity;
import com.example.ayzr.nepismis_v01.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class MenusAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<CookActivity.struct_menu> mMenus;

    public MenusAdapter(Activity activity, List<CookActivity.struct_menu> menus) {
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
    public CookActivity.struct_menu getItem(int position) {
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
            CookActivity.struct_menu menu = mMenus.get(position);


        satirView = mInflater.inflate(R.layout.menus_design, null);

        TextView order_name_1 = (TextView) satirView.findViewById(R.id.text_put_on_sale_menu_1);
        TextView order_menu_2 = (TextView) satirView.findViewById(R.id.text_put_on_sale_menu_2);
        TextView order_menu_3 = (TextView) satirView.findViewById(R.id.text_put_on_sale_menu_3);
        ImageView order_image = (ImageView) satirView.findViewById(R.id.image_put_on_sale_cook);


        order_name_1.setText(menu.meal.get(0));
        order_menu_2.setText(menu.meal.get(1));
        order_menu_3.setText(menu.meal.get(2));

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.WHITE)
                .borderWidthDp(0)
                .cornerRadiusDp(10)
                .oval(false)
                .build();

        String url = "http://nepismis.afakan.net/images/yemek/" + menu.order_picture_id +  ".jpg";

        Picasso.with(mInflater.getContext()).load(url).fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher_ne_pismis)
                .error(R.mipmap.ic_launcher_ne_pismis)
                .into(order_image);

        return satirView;

    }
}