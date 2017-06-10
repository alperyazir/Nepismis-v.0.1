package com.example.ayzr.nepismis_v01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.ayzr.nepismis_v01.adapters.CookListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CookActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final List<String> kisiler=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.cook_activity_name);

  //        getSupportActionBar().setDisplayShowHomeEnabled(true);
  //      getSupportActionBar().setIcon(R.drawable.ne_pismis);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        kisiler.add(0,"alper");
        kisiler.add(1,"tiner");
        kisiler.add(2,"şamp");
        kisiler.add(3,"kocabas");
        kisiler.add(4,"izzet");
        kisiler.add(5,"neşet");

        ListView listView = (ListView) findViewById(R.id.list_order);
        CookListAdapter adapter = new CookListAdapter(this,kisiler);
        listView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
   }

    public void button_cook_order_ready_clicked(View view){
        /// TODO
        /// Add codes what happened when order ready
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_order) {
            // Handle the camera action
        } else if (id == R.id.nav_menu_on_order) {
            startActivity(new Intent(this, OrdersOnSale.class));
        } else if (id == R.id.nav_put_on_sale) {
            startActivity(new Intent(this,PutOnSaleActivity.class));

        } else if (id == R.id.nav_questionnaire) {

        } else if (id == R.id.nav_make_questionnaire) {

        } else if (id == R.id.nav_my_account) {
            startActivity(new Intent(this, ProfileActivity.class));
        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_sign_out) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_menus){
            startActivity(new Intent(this,MenusActivity.class));
        } else if (id == R.id.nav_request_service){
            startActivity(new Intent(this,RequestService.class));
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

