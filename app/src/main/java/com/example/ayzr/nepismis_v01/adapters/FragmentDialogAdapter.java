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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.CookActivity;
import com.example.ayzr.nepismis_v01.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;


public class FragmentDialogAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater mInflater;
    private List<CookActivity.struct_menu> mMenus;

    private TextView order_menu_1;
    private TextView order_menu_2;
    private TextView order_menu_3;
    private ImageView order_image;

    private List<Integer> list_selected_menus;

    public FragmentDialogAdapter(Activity activity, List<CookActivity.struct_menu> _menus, List<Integer> list_selected_menus) {
        this.activity = activity;
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mMenus = _menus;
        this.list_selected_menus = list_selected_menus;
    }

    @Override
    public int getCount() {
        return mMenus.size();
    }

    public int getMenuId(int id) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout rowLayout = null;

//        if (convertView == null) {
        convertView = mInflater.inflate(R.layout.dialog_fragment_design, null);
        /*} else {
            rowLayout = (LinearLayout) convertView;
        }*/
        final CookActivity.struct_menu menu = mMenus.get(position);

        order_menu_1 = (TextView) convertView.findViewById(R.id.text_put_on_sale_menu_1);
        order_menu_2 = (TextView) convertView.findViewById(R.id.text_put_on_sale_menu_2);
        order_menu_3 = (TextView) convertView.findViewById(R.id.text_put_on_sale_menu_3);
        order_image = (ImageView) convertView.findViewById(R.id.image_put_on_sale_cook);


        order_menu_1.setText(menu.meal.get(0));
        order_menu_2.setText(menu.meal.get(1));
        order_menu_3.setText(menu.meal.get(2));

        String url = "http://nepismis.afakan.net/images/yemek/" + menu.order_picture_id + ".jpg";

        Picasso.with(mInflater.getContext()).load(url).fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher_ne_pismis)
                .error(R.mipmap.ic_launcher_ne_pismis)
                .into(order_image);

        if(isSelected(menu.menu_id)){
            convertView.setBackgroundResource(R.color.colorToggleOff);
        } else{
            convertView.setBackgroundResource(R.color.colorWhite);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSelected(menu.menu_id)) {
                    if (list_selected_menus.size() < 3) {
                        view.setBackgroundResource(R.color.colorToggleOff);
                        list_selected_menus.add(menu.menu_id);
                    } else {
                        Toast.makeText(activity, "3'den fazla seçilemez", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    view.setBackgroundResource(R.color.colorWhite);
                    deleteListSelected(menu.menu_id);
                }
            }
        });

        return convertView;
    }

    private boolean isSelected(long id){
        boolean isSelected = false;

        control:
        for (int i = 0; i < list_selected_menus.size(); i++) {
            if(list_selected_menus.get(i) == id){
                isSelected = true;
                break control;
            }
        }

        return isSelected;
    }

    private void deleteListSelected(int id){
        control:
        for (int i = 0; i < list_selected_menus.size(); i++) {
            if(list_selected_menus.get(i) == id){
                list_selected_menus.remove(i);
                break control;
            }
        }
    }

}