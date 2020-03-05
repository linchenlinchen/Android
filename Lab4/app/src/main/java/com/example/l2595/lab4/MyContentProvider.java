//package com.example.l2595.lab4;
//
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.net.Uri;
//
//public class MyContentProvider extends ContentProvider {
//    private DbOpenHelper helper;
//    private SQLiteDatabase db;
//    private static UriMatcher uriMatcher;
//    public static final String AUTHORITY = "com.example.mycontentprovider.wang";
//    public static final int CODE_PERSON = 0;
//    static {
//        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        uriMatcher.addURI(AUTHORITY, "person", CODE_PERSON);
//    }
//
//
//    @Override
//    public boolean onCreate() {
//        helper = DbOpenHelper.getInstance(getContext());
//        db = helper.getWritableDatabase();
//        //在数据库里添加一些数据
//        initData();
//        return true;
//    }
//
//    public void initData() {
//        for (int i = 0; i < 5; i++) {
//            ContentValues values = new ContentValues();
//            values.put("name", "kobe" + (i + 1));
//            values.put("age", 21 + i);
//            db.insert("person", null, values);
//        }
//    }
//
//    @Override
//    public String getType(Uri uri) {
//        return null;
//    }
//
//    public String getTableName(Uri uri) {
//        if (uriMatcher.match(uri) == CODE_PERSON) {
//            return "person";
//        } else {
//            //...
//        }
//        return null;
//    }
//
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection,
//                        String[] selectionArgs, String sortOrder) {
//        String tableName = getTableName(uri);
//        if (tableName == null) {
//            throw new IllegalArgumentException("uri has not been added by urimatcher");
//        }
//        Cursor cursor = db.query(tableName, projection, selection, selectionArgs, null, null, null);
//        return cursor;
//    }
//
//    @Override
//    public Uri insert(Uri uri, ContentValues values) {
//        String tableName = getTableName(uri);
//        if (tableName == null) {
//            throw new IllegalArgumentException("uri has not been added by urimatcher");
//        }
//        db.insert(tableName, null, values);
//
//        //数据库中数据发生改变时，调用
//        getContext().getContentResolver().notifyChange(uri, null);
//        return uri;
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        String tableName = getTableName(uri);
//        if (tableName == null) {
//            throw new IllegalArgumentException("uri has not been added by urimatcher");
//        }
//        int row = db.delete(tableName, selection, selectionArgs);
//        if (row > 0) {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }
//
//        return row;
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String selection,
//                      String[] selectionArgs) {
//        String tableName = getTableName(uri);
//        if (tableName == null) {
//            throw new IllegalArgumentException("uri has not been added by urimatcher");
//        }
//        int row = db.update(tableName, values, selection, selectionArgs);
//        if (row > 0) {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }
//        return row;
//    }
//
//}
////DbOpenHelper代码如下：
//public class DbOpenHelper extends SQLiteOpenHelper {
//
//    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
//                        int version) {
//        super(context, name, factory, version);
//    }
//
//    private static DbOpenHelper helper;
//    public static synchronized DbOpenHelper getInstance(Context context) {
//        if (helper == null) {
//            //创建数据库
//            helper = new DbOpenHelper(context, "my_provider.db", null, 1);
//        }
//        return helper;
//    }
//
//    //创建表
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String sql = "create table person (_id integer primary key autoincrement, name Text, age integer)";
//        db.execSQL(sql);
//    }
//
//    //数据库升级时，回调该方法
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//
//}
