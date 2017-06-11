package com.example.ayzr.nepismis_v01;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.adapters.CommentsAdapter;
import com.example.ayzr.nepismis_v01.adapters.MenusAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenusActivity extends AppCompatActivity {

    GridView morning_grid;
    GridView noon_grid;
    GridView evening_grid;
    Button button_morning;
    Button button_noon;
    Button button_evening;
    final List<String> menus_morning = new ArrayList<String>();
    final List<String> menus_noon = new ArrayList<String>();
    final List<String> menus_evening = new ArrayList<String>();

    MenusAdapter morning_adapter;
    MenusAdapter noon_adapter;
    MenusAdapter evening_adapter;

    boolean morning_visibility = true;
    boolean noon_visibility = true;
    boolean evening_visibility = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        setTitle(R.string.menus_activity_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button_morning = (Button) findViewById(R.id.menus_morning_buttom_down);
        button_noon = (Button) findViewById(R.id.menus_noon_buttom_down);
        button_evening = (Button) findViewById(R.id.menus_evening_buttom_down);



        menus_morning.add(0, "alper");
        menus_morning.add(1, "tiner");
        menus_morning.add(2, "şamp");
        menus_morning.add(3, "kocabas");
        menus_morning.add(4, "izzet");
        menus_morning.add(5, "menu");

        menus_noon.add(0, "alper");
        menus_noon.add(1, "tiner");
        menus_noon.add(2, "şamp");
        menus_noon.add(3, "kocabas");
        menus_noon.add(4, "izzet");
        menus_noon.add(5, "menu");

        menus_evening.add(0, "alper");
        menus_evening.add(1, "tiner");
        menus_evening.add(2, "şamp");
        menus_evening.add(3, "kocabas");
        menus_evening.add(4, "izzet");
        menus_evening.add(5, "menu");

        morning_grid = (GridView) findViewById(R.id.menus_morning_grid);
        morning_adapter = new MenusAdapter(this, menus_morning);
        morning_grid.setAdapter(morning_adapter);

        noon_grid = (GridView) findViewById(R.id.menus_noon_grid);
        noon_adapter = new MenusAdapter(this, menus_noon);
        noon_grid.setAdapter(noon_adapter);

        evening_grid = (GridView) findViewById(R.id.menus_evening_grid);
        evening_adapter = new MenusAdapter(this, menus_evening);
        evening_grid.setAdapter(evening_adapter);

        morning_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (position == menus_morning.size() -1) {
                    menus_morning.remove(position);
                    menus_morning.add(position, "Capare");
                    menus_morning.add(position + 1, "menu");

                    morning_grid.setAdapter(morning_adapter);
                }
            }
        });

        noon_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (position == menus_noon.size() -1) {
                    menus_noon.remove(position);
                    menus_noon.add(position, "Capare");
                    menus_noon.add(position + 1, "menu");

                    noon_grid.setAdapter(noon_adapter);
                }
            }
        });

        evening_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (position == menus_evening.size() -1) {
                    menus_evening.remove(position);
                    menus_evening.add(position, "Capare");
                    menus_evening.add(position + 1, "menu");

                    evening_grid.setAdapter(evening_adapter);
                }
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void buttonClicked(View view) {

        // FragmentManager fragmentManager = getFragmentManager();
        // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // MenusFragment hello = new MenusFragment();
        //  fragmentTransaction.add(R.id.  layout_container, hello, "HELLO");
        // fragmentTransaction.commit();

    }

    public void menus_morning_buttom_down_clicked(View view) {

        if (morning_visibility) {
            morning_grid.setVisibility(View.GONE);
            button_morning.setBackgroundResource(R.mipmap.right);
            morning_visibility = false;
        } else {
            morning_grid.setVisibility(View.VISIBLE);
            button_morning.setBackgroundResource(R.mipmap.down);
            morning_visibility = true;
        }
    }

    public void menus_noon_buttom_down_clicked(View view) {

        if (noon_visibility) {
            noon_grid.setVisibility(View.GONE);
            button_noon.setBackgroundResource(R.mipmap.right);
            noon_visibility = false;
        } else {
            noon_grid.setVisibility(View.VISIBLE);
            button_noon.setBackgroundResource(R.mipmap.down);
            noon_visibility = true;
        }
    }

    public void menus_evening_buttom_down_clicked(View view) {

        if (evening_visibility) {
            evening_grid.setVisibility(View.GONE);
            button_evening.setBackgroundResource(R.mipmap.right);
            evening_visibility = false;
        } else {
            evening_grid.setVisibility(View.VISIBLE);
            button_evening.setBackgroundResource(R.mipmap.down);
            evening_visibility = true;
        }
    }


}
