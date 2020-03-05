package com.free.diary.support.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.free.diary.R;
import com.free.diary.support.util.ActivityManager;


public class TestCalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_calendar);
        ActivityManager.addActivity(this);
    }
}
