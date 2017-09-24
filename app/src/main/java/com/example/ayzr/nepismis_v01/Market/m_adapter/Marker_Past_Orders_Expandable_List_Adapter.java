package com.example.ayzr.nepismis_v01.Market.m_adapter;


import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.ayzr.nepismis_v01.Market.MarketPastOrders;
import com.example.ayzr.nepismis_v01.R;


public class Marker_Past_Orders_Expandable_List_Adapter extends BaseExpandableListAdapter {

    private Context _context;
    private  List<MarketPastOrders.market_past_orders> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<MarketPastOrders.market_past_orders_details>> _listDataChild;

    public Marker_Past_Orders_Expandable_List_Adapter(Context context,List<MarketPastOrders.market_past_orders> listDataHeader,
                                                      HashMap<String, List<MarketPastOrders.market_past_orders_details>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).date).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final MarketPastOrders.market_past_orders_details child = (MarketPastOrders.market_past_orders_details) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.market_past_order_expandable_list_content_design, null);
        }

        TextView txtListChild_owner = (TextView) convertView.findViewById(R.id.market_past_order_expanable_list_content_design_product_owner);
        TextView txtListChild_count = (TextView) convertView.findViewById(R.id.market_past_order_expanable_list_content_design_product_count);
        TextView txtListChild_price = (TextView) convertView.findViewById(R.id.market_past_order_expanable_list_content_design_product_price);

        txtListChild_owner.setText(child.owner);
        txtListChild_count.setText(child.order_count + " Adet");
        txtListChild_price.setText(child.order_price + " tl");
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).date)
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        MarketPastOrders.market_past_orders headerTitle = (MarketPastOrders.market_past_orders) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.market_past_order_expandable_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.text_market_past_order_expandable_date);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.date.substring(0,10));

        TextView lblListHeaderPrice = (TextView) convertView.findViewById(R.id.text_market_past_order_expandable_header_price);
        lblListHeaderPrice.setTypeface(null, Typeface.BOLD);
        lblListHeaderPrice.setText(headerTitle.total_price);

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
