package com.example.ayzr.nepismis_v01.Market.m_adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.AccountDatabase;
import com.example.ayzr.nepismis_v01.CookActivity;
import com.example.ayzr.nepismis_v01.HttpCall;
import com.example.ayzr.nepismis_v01.HttpRequest;
import com.example.ayzr.nepismis_v01.Market.MarkerOrdes;
import com.example.ayzr.nepismis_v01.Market.MarketProducts;
import com.example.ayzr.nepismis_v01.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.example.ayzr.nepismis_v01.R.id.market_order_layout;

public class Market_OrdersList_Adapter extends BaseExpandableListAdapter {


    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<MarkerOrdes.struct_market_order_details>> _listDataChild;

    private double total_price = 0.0;

    public Market_OrdersList_Adapter(Context context, List<String> listDataHeader, HashMap<String, List<MarkerOrdes.struct_market_order_details>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final MarkerOrdes.struct_market_order_details childText = (MarkerOrdes.struct_market_order_details) getChild(groupPosition, childPosition);


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



            if (isLastChild) {
                total_price += (childText.order_price);

                convertView = infalInflater.inflate(R.layout.list_market_order_row_design, null);
                Button button = (Button) convertView.findViewById(R.id.btn_market_order_ready);
               Button button_total_price = (Button) convertView.findViewById(R.id.btn_total_price);

               button_total_price.setText("Toplam "+total_price + "tl");

                total_price = 0;

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        market_send_order_ready(childText.order_id);
                    }
                });
            } else {
                convertView = infalInflater.inflate(R.layout.list_market_order_row_design_without_button, null);

            }
        }

        TextView txtListChild_name = (TextView) convertView.findViewById(R.id.market_order_name);
        TextView txtListChild_price = (TextView) convertView.findViewById(R.id.txt_market_product_price);
        TextView txtListChild_count = (TextView) convertView.findViewById(R.id.txt_market_product_count);

        txtListChild_name.setText(childText.order_name);
        txtListChild_price.setText(""+childText.order_price);
        txtListChild_count.setText(childText.order_count);

        total_price += (childText.order_price);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.market_orders_header_layour, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.txt_market_order_header);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void market_send_order_ready(String id) {
        Toast.makeText(this._context, "Hazır Clicked" + id, Toast.LENGTH_SHORT).show();
    }
}