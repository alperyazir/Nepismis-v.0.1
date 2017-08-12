package com.example.ayzr.nepismis_v01.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ayzr.nepismis_v01.CookActivity;
import com.example.ayzr.nepismis_v01.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;



public class FragmentDialogAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<CookActivity.struct_menu> mMenus;



    public FragmentDialogAdapter(Activity activity, List<CookActivity.struct_menu> _menus) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mMenus = _menus;
    }

    @Override
    public int getCount() {
        return mMenus.size();
    }

    public int getMenuId(int id){
        return mMenus.get(id).menu_id;
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
        ViewHolder holder = new ViewHolder();
        CookActivity.struct_menu menu = mMenus.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dialog_fragment_design, null);

             holder.holder_order_name_1 = (TextView) convertView.findViewById(R.id.text_put_on_sale_menu_1);
             holder.holder_order_menu_2 = (TextView) convertView.findViewById(R.id.text_put_on_sale_menu_2);
             holder.holder_order_menu_3 = (TextView) convertView.findViewById(R.id.text_put_on_sale_menu_3);
             holder.holder_order_image = (ImageView) convertView.findViewById(R.id.image_put_on_sale_cook);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.holder_order_name_1.setText(menu.meal.get(0));
        holder.holder_order_menu_2.setText(menu.meal.get(1));
        holder.holder_order_menu_3.setText(menu.meal.get(2));

        String url = "http://nepismis.afakan.net/images/yemek/" + menu.order_picture_id +  ".jpg";

        Picasso.with(mInflater.getContext()).load(url).fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher_ne_pismis)
                .error(R.mipmap.ic_launcher_ne_pismis)
                .into(holder.holder_order_image);


        return convertView;
    }

}
       class ViewHolder {
        TextView holder_order_name_1;
        TextView holder_order_menu_2;
        TextView holder_order_menu_3;
        ImageView holder_order_image;
    }