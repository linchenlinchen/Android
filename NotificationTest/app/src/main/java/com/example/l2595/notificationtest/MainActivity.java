package com.example.l2595.notificationtest;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private AlarmManager manager;//实现定时
    private PendingIntent pi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationService.class);
        pi = PendingIntent.getService(this, 0, intent, 0);
        // 设置AlarmManager启动与时间间隔(低于一分钟会以一分钟计算)
        manager.setRepeating(AlarmManager.RTC_WAKEUP, AlarmManager.RTC,1000L, pi);
    }
}