package com.example.l2595.firstapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Helper extends SQLiteOpenHelper {
    public static final String CREATE_Test1 = "create table test1("
            + "id integer primary key autoincrement,"
            + "title,"
            + "content)";
    public static final String CREATE_CATEGORY = "create table Category(" +
            "id integer primary key autoincrement," +
            "shabi," +
            "content)";

    private Context mContext;


    public Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Test1);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists test1");
        db.execSQL("drop table if exists Category");
        onCreate(db);
    }
}
