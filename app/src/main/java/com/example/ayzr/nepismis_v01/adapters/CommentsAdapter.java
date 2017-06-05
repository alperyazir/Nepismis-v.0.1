package com.example.ayzr.nepismis_v01.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ayzr.nepismis_v01.R;

import java.util.List;

/**
 * Created by ayzr on 6.06.2017.
 */

public class CommentsAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<String> mCommentsList;

    public CommentsAdapter(Activity activity, List<String> comments) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mCommentsList = comments;
    }

    @Override
    public int getCount() {
        return mCommentsList.size();
    }

    @Override
    public String getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mCommentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View satirView;
        satirView = mInflater.inflate(R.layout.comments_list_row_design, null);
        TextView textView = (TextView) satirView.findViewById(R.id.text_comment_name);

        String cook = mCommentsList.get(position);
        textView.setText(cook);
        return satirView;
    }
}
