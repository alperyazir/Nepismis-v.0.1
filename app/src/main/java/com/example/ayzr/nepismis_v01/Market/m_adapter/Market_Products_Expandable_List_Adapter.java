package com.example.ayzr.nepismis_v01.Market.m_adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import com.example.ayzr.nepismis_v01.Market.MarketProducts;
import com.example.ayzr.nepismis_v01.R;
import com.squareup.picasso.Picasso;

public class Market_Products_Expandable_List_Adapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<MarketProducts.market_products_struct>> _listDataChild;

    public Market_Products_Expandable_List_Adapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<MarketProducts.market_products_struct>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final MarketProducts.market_products_struct childText = (MarketProducts.market_products_struct) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.market_products_list_item_design, null);
        }

        ImageView imgListChild = (ImageView) convertView.findViewById(R.id.image_market_products);
        TextView txtListChild_name = (TextView) convertView.findViewById(R.id.txt_market_product_name);
        TextView txtListChild_price = (TextView) convertView.findViewById(R.id.txt_market_product_price);

        txtListChild_name.setText(childText.product_name);
        txtListChild_price.setText(childText.product_price);

        String url = "http://nepismis.afakan.net/images/yemek/" +childText.product_picture_id + ".jpg";
        Picasso.with(convertView.getContext()).load(url).fit().centerCrop()
                .placeholder(R.mipmap.ic_launcher_ne_pismis)
                .error(R.mipmap.ic_launcher_ne_pismis)
                .into(imgListChild);
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
            convertView = infalInflater.inflate(R.layout.market_products_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.txt_market_products_list_group_design);
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
}