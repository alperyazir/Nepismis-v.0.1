package com.example.ayzr.nepismis_v01;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuDialogFragment extends DialogFragment {


    private Spinner meal_1;
    private Spinner meal_2;
    private Spinner meal_3;

    private Button button_add_menu;
    private Button button_close;

    private int meal_1_id;
    private int meal_2_id;
    private int meal_3_id;
    private int current_tab_index;


    private ProgressDialog progress;

    List<Integer> meals_params;
    List<String> categories;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_menu_dialog_fragment, container, false);
        getDialog().setTitle("Menu Oluştur");

        Bundle bundle = getArguments();
        current_tab_index = bundle.getInt("current_tab_index", -1);


        meal_1 = (Spinner) rootView.findViewById(R.id.spinner_meal_1);
        meal_2 = (Spinner) rootView.findViewById(R.id.spinner_meal_2);
        meal_3 = (Spinner) rootView.findViewById(R.id.spinner_meal_3);

        button_add_menu = (Button) rootView.findViewById(R.id.button_add_manu);
        button_close = (Button) rootView.findViewById(R.id.button_close_add_menu);

        meals_params = new ArrayList<Integer>();
        categories = new ArrayList<String>();
        categories.clear();
        getMeals();

        button_add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_menu();
                dismiss();
            }
        });


        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        meal_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                meal_1_id= meals_params.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        meal_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                meal_2_id= meals_params.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        meal_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                meal_3_id=  meals_params.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        return rootView;
    }

    public void getMeals(){
        progress = new ProgressDialog(getContext());
        progress.setMessage("Adding menus...");
        progress.setIndeterminate(true);
        progress.show();

        HashMap<String, String> params_db;

        AccountDatabase accountDatabase = new AccountDatabase(getContext());
        params_db = accountDatabase.kullaniciDetay();

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("http://nepismis.afakan.net/android/yemekGuncel");
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", params_db.get("tarih"));
        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public List<CookActivity.struct_menu> onResponse(String response) {
                super.onResponse(response);
                try {
                    progress.dismiss();
                    meals_params.clear();
                    JSONArray initial = new JSONArray(response);
                    for (int i=0; i < initial.length(); i++){
                        JSONObject object = initial.getJSONObject(i);
                        meals_params.add(object.getInt("yemek_id"));
                        categories.add(i,object.getString("yemek_adi"));
                    }
                    create_design();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

        }.execute(httpCall);
    }


    private void create_design(){

        if(categories.size()>0) {
            ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(this.getActivity(),  android.R.layout.simple_spinner_item, categories);
            // Drop down layout style - list view with radio button
            spin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            meal_1.setAdapter(spin_adapter);
            meal_2.setAdapter(spin_adapter);
            meal_3.setAdapter(spin_adapter);
        }
    }

    private void create_menu(){
        HashMap<String, String> params_db;

        AccountDatabase accountDatabase = new AccountDatabase(getContext());
        params_db = accountDatabase.kullaniciDetay();

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("http://nepismis.afakan.net/android/menuOlustur");
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", params_db.get("tarih"));
        params.put("ogun",""+current_tab_index);
        params.put("yemek_1",""+meal_1_id);
        params.put("yemek_2",""+meal_2_id);
        params.put("yemek_3",""+meal_3_id);
        params.put("tarih","2017/08/14");
        httpCall.setParams(params);

        new HttpRequest(){
            @Override
            public List<CookActivity.struct_menu> onResponse(String response) {
                super.onResponse(response);
                try {
                    progress.dismiss();

                    JSONObject initial = new JSONObject(response);
                    int saved = initial.getInt("save");
                    if(saved==1){
                        dismiss();
                        Toast.makeText(button_add_menu.getContext(),"Menu başarıyla eklendi :)",Toast.LENGTH_SHORT).show();
                    }else{
                        dismiss();
                        Toast.makeText(button_add_menu.getContext(),"Menü eklenemedi :(",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

        }.execute(httpCall);
    }





}
