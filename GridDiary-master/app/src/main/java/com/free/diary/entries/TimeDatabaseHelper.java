package com.free.diary.entries;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TimeDatabaseHelper extends SQLiteOpenHelper {
    //
    //the name need to be changed
    //
    public static final String CREATE_BOOK = "create table Time ("
            +"id integer primary key autoincrement,"
            +"diary_content text,"
            +"diary_year integer,"
            +"diary_month integer,"
            +"diary_day integer)";

    private Context mContext;

    public TimeDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Time");
        onCreate(db);

    }
}
