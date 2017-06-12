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

public class QuestionnarieAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> mQuestionaireList;

    public QuestionnarieAdapter(Activity activity, List<String> orders) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mQuestionaireList = orders;
    }

    @Override
    public int getCount() {
        return mQuestionaireList.size();
    }

    @Override
    public String getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mQuestionaireList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View satirView;
        satirView = mInflater.inflate(R.layout.questionnaire_row_design, null);
        TextView textView = (TextView) satirView.findViewById(R.id.text_put_on_sale_menu_1);

        String cook = mQuestionaireList.get(position);
        textView.setText(cook);
        return satirView;
    }
}
