package com.example.ayzr.nepismis_v01;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayzr.nepismis_v01.adapters.CookListAdapter;
import com.example.ayzr.nepismis_v01.adapters.FragmentDialogAdapter;
import com.example.ayzr.nepismis_v01.adapters.MenusAdapter;
import com.example.ayzr.nepismis_v01.adapters.QuestionnarieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static java.lang.StrictMath.toIntExact;

public class MenusDialogFragment extends DialogFragment {

    private Button close_button;
    private Button save_button;
    final List<CookActivity.struct_menu> menus_morning = new ArrayList<>();
    final List<CookActivity.struct_menu> menus_noon    = new ArrayList<>();
    final List<CookActivity.struct_menu> menus_evening = new ArrayList<>();
    private ListView list;
    private TextView text;
    private ProgressDialog progress;
    private List<Long> selected_items;
    private List<Integer> list_selected_menus;
    private int current_tab_index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_menus_dialog_fragment, container,false);
        getDialog().setTitle("Menu Seç");

        Bundle bundle = getArguments();
        current_tab_index = bundle.getInt("current_tab_index", -1);

        text = (TextView) rootView.findViewById(R.id.text_dialog);
        if(current_tab_index == 1) {
            text.setText("Sabah");
        }else if(current_tab_index == 2){
            text.setText("Öğle");
        }else if(current_tab_index == 3){
            text.setText("Akşam");
        }

        close_button = (Button) rootView.findViewById(R.id.button_close);
        save_button = (Button) rootView.findViewById(R.id.button_save);
        list = (ListView) rootView.findViewById(R.id.list_menu);

        selected_items = new ArrayList<Long>();
        list_selected_menus = new ArrayList<Integer>();

        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> parent,View view, int position,long id) {
                            if (!selected_items.contains(id)) {
                                if(selected_items.size() < 3) {
                                    selected_items.add(id);
                                    view.setBackgroundResource(R.color.colorToggleOff);
                                    CookActivity.struct_menu m = (CookActivity.struct_menu) parent.getItemAtPosition(position);
                                    list_selected_menus.add(m.menu_id);
                                }else{
                                    Toast.makeText(getContext(),"3'den fazla seçilemez",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                selected_items.remove(id);
                                view.setBackgroundResource(R.color.colorBackground);
                                CookActivity.struct_menu m = (CookActivity.struct_menu) parent.getItemAtPosition(position);
                                list_selected_menus.remove(m.menu_id);
                            }
                    }
                }
        );


        getMenus(rootView,current_tab_index);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_selected_menus.size() == 3){
                    createSurvey();
            }else {
                      Snackbar.make(rootView, "Select 3 menus please", Snackbar.LENGTH_SHORT)
                              .setAction("Action", null).show();
                }
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    private void createSurvey(){
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Adding menus...");
        progress.setIndeterminate(true);
        progress.show();

        HashMap<String, String> params_db;

        AccountDatabase accountDatabase = new AccountDatabase(getContext());
        params_db = accountDatabase.kullaniciDetay();

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("http://nepismis.afakan.net/android/anketOlustur");
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", params_db.get("tarih"));
        params.put("ogun",""+current_tab_index);
        params.put("menu_1",""+list_selected_menus.get(0));
        params.put("menu_2",""+list_selected_menus.get(1));
        params.put("menu_3",""+list_selected_menus.get(2));
        params.put("tarih","2017/08/14");
        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public List<CookActivity.struct_menu> onResponse(String response) {
                super.onResponse(response);
                try {
                    progress.dismiss();

                    JSONObject initial = new JSONObject(response);
                    boolean saved = initial.getBoolean("save");
                    if(saved){
                        dismiss();
                        Toast.makeText(getContext(),"Saved Survey :)",Toast.LENGTH_LONG).show();
                    }else{
                        dismiss();
                        Toast.makeText(getContext(),"There are already saved Surveys. Sorry :(",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

        }.execute(httpCall);
    }

    private void getMenus(View rootView, final int it){
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Checking menus...");
            progress.setIndeterminate(true);
            progress.show();

            menus_morning.clear();
            menus_noon.clear();
            menus_evening.clear();
            HashMap<String, String> params_db;

            AccountDatabase accountDatabase = new AccountDatabase(rootView.getContext());
            params_db = accountDatabase.kullaniciDetay();

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.GET);
            httpCall.setUrl("http://nepismis.afakan.net/android/menuGuncel");
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", params_db.get("tarih"));
            httpCall.setParams(params);

            new HttpRequest() {
                @Override
                public List<CookActivity.struct_menu> onResponse(String response) {
                    super.onResponse(response);
                    try {
                        progress.dismiss();

                        JSONObject initial = new JSONObject(response);
                        JSONArray morning_array = initial.getJSONArray("sabah");
                        JSONArray noon_array = initial.getJSONArray("ogle");
                        JSONArray evening_array = initial.getJSONArray("aksam");

                        if(it == 1) {
                            for (int i = 0; i < morning_array.length(); i++) {
                                CookActivity.struct_menu m = new CookActivity.struct_menu();
                                JSONObject morning_menus = morning_array.getJSONObject(i);
                                m.menu_id = morning_menus.getInt("menu_id");
                                JSONArray menuy_array = morning_menus.getJSONArray("menuy");

                                for (int j = 0; j < menuy_array.length(); j++) {
                                    JSONObject morning_menuy = menuy_array.getJSONObject(j);
                                    m.meal.add(j, morning_menuy.getString("yemek_adi"));
                                    if (j == 0)
                                        m.order_picture_id = morning_menuy.getInt("yemek_id");
                                }
                                menus_morning.add(i, m);
                            }
                            send_to_dialog(menus_morning);
                        }
                        else if(it == 2) {
                            for (int i = 0; i < noon_array.length(); i++) {
                                CookActivity.struct_menu m = new CookActivity.struct_menu();
                                JSONObject morning_menus = noon_array.getJSONObject(i);
                                JSONArray menuy_array = morning_menus.getJSONArray("menuy");

                                m.menu_id = morning_menus.getInt("menu_id");
                                for (int j = 0; j < menuy_array.length(); j++) {
                                    JSONObject morning_menuy = menuy_array.getJSONObject(j);
                                    m.meal.add(j, morning_menuy.getString("yemek_adi"));
                                    if (j == 0)
                                        m.order_picture_id = morning_menuy.getInt("yemek_id");
                                }
                                menus_noon.add(i, m);
                            }
                            send_to_dialog(menus_noon);
                        }
                        else if(it == 3) {
                            for (int i = 0; i < evening_array.length(); i++) {
                                CookActivity.struct_menu m = new CookActivity.struct_menu();
                                JSONObject morning_menus = evening_array.getJSONObject(i);
                                JSONArray menuy_array = morning_menus.getJSONArray("menuy");

                                m.menu_id = morning_menus.getInt("menu_id");
                                for (int j = 0; j < menuy_array.length(); j++) {
                                    JSONObject morning_menuy = menuy_array.getJSONObject(j);
                                    m.meal.add(j, morning_menuy.getString("yemek_adi"));
                                    if (j == 0)
                                        m.order_picture_id = morning_menuy.getInt("yemek_id");
                                }
                                menus_evening.add(i, m);
                            }
                            send_to_dialog(menus_evening);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(httpCall);


    }

    private void send_to_dialog(List<CookActivity.struct_menu> menu){
        final FragmentDialogAdapter fmAdapter = new FragmentDialogAdapter(getActivity(), menu);
        list.setAdapter(fmAdapter);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

}
