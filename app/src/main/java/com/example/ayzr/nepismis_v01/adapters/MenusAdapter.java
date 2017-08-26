package com.example.ayzr.nepismis_v01.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.AccountDatabase;
import com.example.ayzr.nepismis_v01.CookActivity;
import com.example.ayzr.nepismis_v01.HttpCall;
import com.example.ayzr.nepismis_v01.HttpRequest;
import com.example.ayzr.nepismis_v01.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static android.R.id.progress;
import static java.security.AccessController.getContext;


public class MenusAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<CookActivity.struct_menu> mMenus;

    private ImageView imageClick;

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
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View satirView;
        final CookActivity.struct_menu menu = mMenus.get(position);


        satirView = mInflater.inflate(R.layout.menus_design, null);

        TextView order_name_1 = (TextView) satirView.findViewById(R.id.text_put_on_sale_menu_1);
        TextView order_menu_2 = (TextView) satirView.findViewById(R.id.text_put_on_sale_menu_2);
        TextView order_menu_3 = (TextView) satirView.findViewById(R.id.text_put_on_sale_menu_3);
        ImageView order_image = (ImageView) satirView.findViewById(R.id.image_put_on_sale_cook);
        imageClick = (ImageView) satirView.findViewById(R.id.row_click_imageView1);

        try {
            imageClick.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.row_click_imageView1:

                            PopupMenu popup = new PopupMenu(imageClick.getContext(), v);
                            popup.getMenuInflater().inflate(R.menu.popup_menu,
                                    popup.getMenu());
                            popup.show();
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {

                                    switch (item.getItemId()) {
                                        case R.id.menu_delete:
                                            delete_menu(menu.menu_id);
                                            break;
                                    }

                                    return true;
                                }
                            });

                            break;

                        default:
                            break;
                    }
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }

        order_name_1.setText(menu.meal.get(0));
        order_menu_2.setText(menu.meal.get(1));
        order_menu_3.setText(menu.meal.get(2));

        String url = "http://nepismis.afakan.net/images/yemek/" + menu.order_picture_id + ".jpg";

        Picasso.with(mInflater.getContext()).load(url).fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher_ne_pismis)
                .error(R.mipmap.ic_launcher_ne_pismis)
                .into(order_image);

        return satirView;

    }

    public void callBack() {
    }

    private void delete_menu(int i) {
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("http://nepismis.afakan.net/android/menuSil");
        HashMap<String, String> params = new HashMap<>();
        params.put("menu_id", "" + i);
        httpCall.setParams(params);

        new HttpRequest() {
            @Override
            public List<CookActivity.struct_menu> onResponse(String response) {
                super.onResponse(response);
                try {
                    JSONObject initial = new JSONObject(response);
                    int saved = initial.getInt("save");
                    if (saved == 1) {
                        Toast.makeText(imageClick.getContext(), "Menu başarıyla silindi :)", Toast.LENGTH_SHORT).show();
                        callBack();
                    } else {
                        Toast.makeText(imageClick.getContext(), "Menü silinemedi :(", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(httpCall);

    }
}