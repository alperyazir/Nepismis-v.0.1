package com.example.ayzr.nepismis_v01;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by afakan on 19.08.2017.
 */

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME ="sharedpref";
    private static final String KEY_ACCESS_TOKEN ="token";


    private static Context mCntx;
    private static SharedPrefManager mShrd;

    private  SharedPrefManager(Context context)
    {
        mCntx=context;
    }

    public static synchronized  SharedPrefManager getInstance(Context context)
    {
        if(mShrd==null)
        {
            mShrd= new SharedPrefManager(context);
        }
        return mShrd;
    }

    public boolean storeToken(String token)
    {
        SharedPreferences sharedPreferences= mCntx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(KEY_ACCESS_TOKEN,token);
        editor.apply();
        return true;
    }

    public String getToken()
    {
        SharedPreferences sharedPreferences= mCntx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,null);
    }
}
