package com.example.l2595.lab4;
//17302010021 林晨
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private LinDataBase dbHelper;
    ArrayAdapter<String> adapter;
    List<String> contactsList = new ArrayList<>();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private String data = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new LinDataBase(this, "Book.db", null, 1);
        Button createDatabase = (Button)findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }
        Button button1 = (Button)findViewById(R.id.bt);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                while (contactsList.size() > 0) {
                    String detail = contactsList.remove(contactsList.size() - 1);
                    String name = contactsList.remove(contactsList.size() - 1);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("author", name);
                    contentValues.put("number", detail);
                    db.insert("Book", null, contentValues);
                    contentValues.clear();
                }
                db.close();
            }
        });
        ListView contactsView =  findViewById(R.id.contacts_view);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactsList);
        contactsView.setAdapter(adapter);
        Button button = (Button)findViewById(R.id.bt);
        dbHelper = new LinDataBase(this, "Book.db", null, 2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Book",null,null,null,null,null,null);
                String name = "name:";
                String number = "number: ";
                if(cursor.moveToFirst()){
                    do {
                        name = cursor.getString(cursor.getColumnIndex("author"));
                        number = cursor.getString(cursor.getColumnIndex("number"));
                        contactsList.add("name:"+name+"\nnumber:"+number);
                    }while (cursor.moveToNext());
                    adapter.notifyDataSetChanged();
                }
                cursor.close();
            }
        });
        readContacts();
    }

    private void readContacts(){
        Cursor cursor = null;
        try{
            cursor= getContentResolver().query(ContactsContract.CommonDataKinds.
                    Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext ()) {
                    //获取联系人姓名
                    String displayName = cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)); //获取联系人手机号
                    String number = cursor. getString (cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName);
                    contactsList.add(number);
                }
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e) {
            e.printStackTrace(); }
            finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
