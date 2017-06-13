package com.example.ayzr.nepismis_v01;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.example.ayzr.nepismis_v01.adapters.CookListAdapter;
import com.example.ayzr.nepismis_v01.adapters.FragmentDialogAdapter;
import com.example.ayzr.nepismis_v01.adapters.MenusAdapter;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class MenusDialogFragment extends DialogFragment {

    private Button close_button;
    final List<String> menus_morning = new ArrayList<String>();
    private ListView list;
    FragmentDialogAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_menus_dialog_fragment, container,
                false);
        getDialog().setTitle("Menu Seç");
        menus_morning.add(0,"alper");
        menus_morning.add(1,"tiner");
        menus_morning.add(2,"şamp");
        menus_morning.add(3,"kocabas");
        menus_morning.add(4,"izzet");
        menus_morning.add(5,"neşet");

        close_button = (Button) rootView.findViewById(R.id.button_okey);

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ListView listView = (ListView) rootView.findViewById(R.id.list_menu);
        FragmentDialogAdapter adapter = new FragmentDialogAdapter(getActivity(),menus_morning);
        listView.setAdapter(adapter);

        return rootView;
    }


}
