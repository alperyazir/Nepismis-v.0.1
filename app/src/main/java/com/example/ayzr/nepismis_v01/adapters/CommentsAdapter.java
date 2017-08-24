package com.example.ayzr.nepismis_v01.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ayzr.nepismis_v01.CommentsActivity;
import com.example.ayzr.nepismis_v01.R;

import java.util.List;



public class CommentsAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<CommentsActivity.struct_comments> mCommentsList;
    private boolean is_okey = false;

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
        TextView text_comment = (TextView) satirView.findViewById(R.id.textComment);
        TextView text_comment_name = (TextView) satirView.findViewById(R.id.text_comment_name);
        final Button button_answer  = (Button) satirView.findViewById(R.id.button_comments_answer);
        RatingBar rating = (RatingBar) satirView.findViewById(R.id.rating_comment);
        final TextView text_answer  = (TextView) satirView.findViewById(R.id.text_answer);
        final EditText edit_answer = (EditText) satirView.findViewById(R.id.editText_add_comment);
        //text_answer.setVisibility(View.GONE);
       // button_answer.setEnabled(false);

        CommentsActivity.struct_comments co = mCommentsList.get(position);

        text_comment.setText(co.comment);
        text_comment_name.setText(co.comment_owner_name);
        rating.setRating((float) co.commenr_rating);

     final AlertDialog.Builder builder = new AlertDialog.Builder(satirView.getContext());

     builder.setTitle("Confirm");
     builder.setMessage("Are you sure?");

     builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

         public void onClick(DialogInterface dialog, int which) {

             is_okey = true;
             dialog.dismiss();
             text_answer.setText(edit_answer.getText().toString());
             edit_answer.setText("");
         }
     });

     builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

         @Override
         public void onClick(DialogInterface dialog, int which) {

             is_okey = false;
             dialog.dismiss();
         }
     });

      // edit_answer.addTextChangedListener(new TextWatcher() {
//
      //     public void afterTextChanged(Editable s) {
      //         if(edit_answer.getText().toString() != "")
      //            // button_answer.setEnabled(true);
      //        // else
      //             //button_answer.setEnabled(false);
      //     }
//
      //     public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
      //     public void onTextChanged(CharSequence s, int start, int before, int count) {}
      // });

      button_answer.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

                AlertDialog alert = builder.create();
                  alert.show();
          }
      });

        return satirView;
    }
}
