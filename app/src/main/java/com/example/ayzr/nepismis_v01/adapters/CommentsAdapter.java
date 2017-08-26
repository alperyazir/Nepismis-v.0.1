package com.example.ayzr.nepismis_v01.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.AccountDatabase;
import com.example.ayzr.nepismis_v01.CommentsActivity;
import com.example.ayzr.nepismis_v01.CookActivity;
import com.example.ayzr.nepismis_v01.HttpCall;
import com.example.ayzr.nepismis_v01.HttpRequest;
import com.example.ayzr.nepismis_v01.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


public class CommentsAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private List<CommentsActivity.struct_comments> mCommentsList;
    private CommentViewHolder holder;


    public CommentsAdapter(Activity activity, List<CommentsActivity.struct_comments> comments) {
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
    public CommentsActivity.struct_comments getItem(int position) {
        return mCommentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         holder = new CommentViewHolder();

       // final View satirView;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.comments_list_row_design, null);

            holder.text_comment = (TextView) convertView.findViewById(R.id.textComment);
            holder.text_comment_name = (TextView) convertView.findViewById(R.id.text_comment_name);
            holder.button_answer = (Button) convertView.findViewById(R.id.button_comments_answer);
            holder.rating = (RatingBar) convertView.findViewById(R.id.rating_comment);
            holder.text_answer = (TextView) convertView.findViewById(R.id.text_answer);
            holder.edit_answer = (EditText) convertView.findViewById(R.id.editText_add_comment);
        }else{
            holder = (CommentViewHolder) convertView.getTag();
        }



        final CommentsActivity.struct_comments co = mCommentsList.get(position);

        holder.text_comment.setText(co.comment);
        holder.text_comment_name.setText(co.comment_owner_name.charAt(0) + " ... " + co.comment_owner_name.charAt(co.comment_owner_name.length() - 1));
        holder.rating.setRating((float) co.commenr_rating);


        if (co.cook_ans.equals("")) {
            holder.edit_answer.setVisibility(View.VISIBLE);
            holder.button_answer.setVisibility(View.VISIBLE);
        } else {
            holder.text_answer.setVisibility(View.VISIBLE);
            if (co.confirm == 0)
                holder.text_answer.setText(co.cook_ans + " \n(Onay Bekleniyor)");
            else
                holder.text_answer.setText(co.cook_ans);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(holder.button_answer.getContext());

        builder.setTitle("Onay");
        builder.setMessage("Emin miyiz?");

        final CommentViewHolder finalHolder = holder;
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                send_comment_answer(co.comment_id, finalHolder.edit_answer.getText().toString());
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final CommentViewHolder finalHolder1 = holder;
        final View finalConvertView = convertView;
        holder.button_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = finalHolder1.edit_answer.getText().toString();
                if (!finalHolder1.edit_answer.getText().toString().isEmpty())
                    builder.show();
                else
                    Snackbar.make(finalConvertView, "Rica ederim boş mesaj ekleme", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        });

        return convertView;
    }

    public void callBack() {
    }

    private void send_comment_answer(int yorum_id, String str) {


        HashMap<String, String> params_db;

        AccountDatabase accountDatabase = new AccountDatabase(holder.button_answer.getContext());
        params_db = accountDatabase.kullaniciDetay();

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("http://nepismis.afakan.net/android/yorumCevap");
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", params_db.get("tarih"));
        params.put("yorum_id", "" + yorum_id);
        params.put("cevap", "" + str);
        httpCall.setParams(params);

        new HttpRequest() {
            @Override
            public List<CookActivity.struct_menu> onResponse(String response) {
                super.onResponse(response);
                try {
                    JSONObject initial = new JSONObject(response);
                    int saved = initial.getInt("save");
                    if (saved == 1) {
                        Toast.makeText(holder.button_answer.getContext(), "Yorum başarıyla eklendi :)", Toast.LENGTH_SHORT).show();
                        callBack();

                    } else {
                        Toast.makeText(holder.button_answer.getContext(), "Yorum eklenemedi :(", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(httpCall);
    }


    class CommentViewHolder {

        TextView text_comment ;
        TextView text_comment_name ;
        Button button_answer;
        RatingBar rating;
        TextView text_answer;
        EditText edit_answer ;

    }
}
