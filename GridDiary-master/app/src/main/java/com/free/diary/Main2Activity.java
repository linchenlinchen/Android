package com.free.diary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.free.diary.entries.HttpUtil;
import com.free.diary.entries.NotificationService;
import com.free.diary.entries.TimeDatabaseHelper;
import com.free.diary.main.MemoActivity;
import com.free.diary.support.util.ActivityManager;
import com.free.diary.ui.activity.MainActivity;
import com.spark.submitbutton.SubmitButton;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    SubmitButton btDiary, btMemo;
    private static final int TIME_DIFF = 2000;
    private long mExitTime;
    private AlarmManager manager;//实现定时
    private PendingIntent pi;

    //////////
    TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(this,"Time.db",null,1);

    /////bing////
    private ImageView bingPicImg;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btDiary = (SubmitButton) findViewById(R.id.bt_to_diary);
        btMemo = (SubmitButton) findViewById(R.id.bt_to_memo);
        //////bing///////
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        String bingPic = prefs.getString("bing_pic",null);
        if (bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }


        ////////////////////////
        SQLiteDatabase db = timeDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query("Time",null,null,null,null,null,null);
        Calendar cale = null;
        cale = Calendar.getInstance();
        int year,month,day,id=0;
        String content="";
        boolean find = false;
        int now_year = cale.get(Calendar.YEAR);
        int now_month = cale.get(Calendar.MONTH) + 1;
        int now_day = cale.get(Calendar.DATE);
        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"));
                year = cursor.getInt(cursor.getColumnIndex("diary_year"));
                month = cursor.getInt(cursor.getColumnIndex("diary_month"));
                day = cursor.getInt(cursor.getColumnIndex("diary_day"));
                content = cursor.getString(cursor.getColumnIndex("diary_content"));
                System.out.print(year+ " "+month+" "+day);
                if(year<now_year || (year==now_year && month<now_month) || (year==now_year && month==now_month && day<=now_day)){
                    find = true;
                    break;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        if(find){
            manager= (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, NotificationService.class);
            intent.putExtra("content",content);
            intent.putExtra("id",id);
            startService(intent);
//            pi = PendingIntent.getService(this, 0, intent, 0);
//            // 设置AlarmManager启动与时间间隔(低于一分钟会以一分钟计算)
//            manager.setRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.RTC,1000L, pi);
        }

        btDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,MemoActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (System.currentTimeMillis() - mExitTime > TIME_DIFF) {
                    Toast.makeText(Main2Activity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    ActivityManager.removeAllActivity();
                    System.exit(0);
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    ////////bing////////
    private void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendHttpRequest(requestBingPic, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                final String bingPic = response;
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Main2Activity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(Main2Activity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    protected void onRestart() {
        btDiary.setText("Diary");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        btDiary.setText("Diary");
        super.onStop();
    }
}
