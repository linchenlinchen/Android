package com.free.diary.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.free.diary.R;

public class TimeLight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_light);

        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        TextView textView = (TextView)findViewById(R.id.time_light);
        textView.setText(content);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
