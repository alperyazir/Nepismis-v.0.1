package com.example.ayzr.nepismis_v01;

import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountDatabase extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "login_database";//database adı

    private static final String TABLE_NAME = "login";
    private static String KULLANICI_ID = "id";
    private static String KULLANICI_MAIL = "mail";
    private static String KULLANICI_SIFRE = "sifre";
    private static String KAYIT_TARIHI = "tarih";


    public AccountDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KULLANICI_ID + " INTEGER PRIMARY KEY,"
                + KULLANICI_MAIL + " TEXT,"
                + KULLANICI_SIFRE + " TEXT,"
                + KAYIT_TARIHI + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }



    public void kullaniciEkle(String mail,String sifre,String tarih) {
        //kullaniciEkle methodu ise adı üstünde Databese veri eklemek için
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KULLANICI_MAIL, mail);
        values.put(KULLANICI_SIFRE, sifre);
        values.put(KAYIT_TARIHI, tarih);

        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }


    public HashMap<String, String> kullaniciDetay(){
        //Bu methodda sadece tek row değerleri alınır.

        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //mesala map.put("x","300"); mesala burda anahtar x değeri 300.

        HashMap<String,String> kisi = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            kisi.put(KULLANICI_MAIL, cursor.getString(1));
            kisi.put(KULLANICI_SIFRE, cursor.getString(2));
            kisi.put(KAYIT_TARIHI, cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return kişi
        return kisi;
    }


    public int getRowCount() { //tabloda kaç satır kayıtlı olduğunu geri döner

        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }


    public void resetTables(){
        // Tüm verileri siler. tabloyu resetler.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}