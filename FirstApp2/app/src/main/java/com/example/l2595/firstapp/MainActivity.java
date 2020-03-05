package com.example.l2595.firstapp;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    public static final int UPDATE_VIEW = 1;
    private IntentFilter intentFilter;
    private TextView textView;
//    private Button sendNotice;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VIEW:
                    textView.setText("Nice to meet you~");
                    break;
                    default:break;
            }
        }
    };
    private MyService.DownloadBinder downloadBinder;

//    private NetworkChangeReceiver networkChangeReceiver;
    private List<Fruit> fruitList = new ArrayList<>();
    private Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.com.CONNECTIVITY_CHANGE");
//        networkChangeReceiver = new NetworkChangeReceiver();
//        registerReceiver(networkChangeReceiver,intentFilter);

        if(savedInstanceState != null){
            String tempData = savedInstanceState.getString("data_key");
            Toast.makeText(MainActivity.this,tempData,Toast.LENGTH_SHORT).show();
        }
        initFruit();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        FruitAdapter fruitAdapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(fruitAdapter);

        helper = new Helper(this,"TestAll.db",null,3);
        final Button createDatabase = (Button)findViewById(R.id.btDatabase);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.getWritableDatabase();//返回一个SQLiteDatabase对象
            }
        });

        Button button1 = (Button)findViewById(R.id.bt1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            }
        });
        Button button2 = (Button)findViewById(R.id.bt2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            }
        });
        Button button3 = (Button)findViewById(R.id.bt3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "你是煞笔！";
                Intent  intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtra("extra_data",data);
                startActivityForResult(intent,1);
            }
        });
        Button buttonDia = (Button)findViewById(R.id.btDia);
        buttonDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DialogActivity.class);
                startActivity(intent);
            }
        });
        Button addData = (Button)findViewById(R.id.btAddData);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                contentValues.put("shabi","ZhendeSha");
                contentValues.put("content","今天天气真好");
                db.insert("Category",null,contentValues);
                contentValues.clear();//清空contentValues的值
            }
        });
        Button updateData = (Button)findViewById(R.id.btUpdateData);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("content","天气其实不好");
                db.update("Category",contentValues,"shabi=?",new String[] {"ZhendeSha"});
            }
        });
        Button deleteData = (Button)findViewById(R.id.btDelete);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
//                db.delete("Category","","");
            }
        });
        Button searchButton = (Button)findViewById(R.id.btQuery);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.query("Category",null,null,null,null,null,null);
                if(cursor.moveToFirst()){
                    do{
                        String shabi = cursor.getString(cursor.getColumnIndex("shabi"));
                        String content = cursor.getString(cursor.getColumnIndex("content"));

                        Toast.makeText(MainActivity.this,"Category content is:" + content,Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this,"Category shabi is:" + shabi,Toast.LENGTH_LONG).show();
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });


        textView = (TextView)findViewById(R.id.text);
        Button buttonChange = (Button)findViewById(R.id.change_text);
        buttonChange.setOnClickListener(this);
        Button startService = (Button)findViewById(R.id.start_service);
        Button stopService = (Button)findViewById(R.id.stop_service);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        Button sendNotice = (Button)findViewById(R.id.send_notice);
        sendNotice.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.change_text:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_VIEW;
                        handler.sendMessage(message);
//                        textView.setText("Nice to meet you!");//这是线程不安全的，但是不代表一定会崩溃，但是这样不太好
                    }
                }).start();break;
            case R.id.start_service:
                Intent startIntent = new Intent(this,MyService.class);
                startService(startIntent);
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this,MyService.class);
                stopService(stopIntent);
                break;
            case R.id.send_notice:
                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                NotificationChannel notificationChannel = new NotificationChannel("default","lin",NotificationManager.IMPORTANCE_DEFAULT);
                Notification notification = new NotificationCompat.Builder(this,notificationChannel.getId())
                        .setContentTitle("This is content title")
                        .setContentText("This is content text")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)).build();
                notificationManager.notify(1,notification);
                break;
                default:break;
        }
    }

    private void initFruit(){
        for (int i = 0;i < 20;i++){
            Fruit apple = new Fruit("Apple");
            fruitList.add(apple);
            Fruit banana = new Fruit("Banana");
            fruitList.add(banana);

        }
    }

//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        unregisterReceiver(networkChangeReceiver);
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String tempData = "Something you just typed";
        outState.putString("data_key",tempData);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(MainActivity.this,"MainActivity pause",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStop(){
        super.onStop();
        Toast.makeText(MainActivity.this,"MainActivity stop",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String returnedData = data.getStringExtra("data_return");
                    Toast.makeText(MainActivity.this,returnedData,Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_item:
                Toast.makeText(MainActivity.this,"Add",Toast.LENGTH_SHORT).show();break;
            case R.id.remove_item:
                Toast.makeText(MainActivity.this,"Remove",Toast.LENGTH_SHORT).show();break;
            default:break;
        }
        return true;
    }
}
