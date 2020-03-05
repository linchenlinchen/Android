package com.free.diary.main;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.free.diary.R;
import com.free.diary.entries.Naming;

public class MemoContentActivity extends AppCompatActivity implements Naming {
    long id;
    String title;
    String content;
    TextView title_view, content_view;
    Button edit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_content);


        final Intent intent = getIntent();

        id = intent.getLongExtra("id",0);
        title = intent.getStringExtra("t");
        content = intent.getStringExtra("c");

        title_view = (TextView) findViewById(R.id.memo_title);
        content_view = (TextView) findViewById(R.id.memo_content);
        edit_button = (Button) findViewById(R.id.memo_edit);

        title_view.setText(title);
        content_view.setText(content);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MemoContentActivity.this,EditActivity.class);
                intent1.putExtra("id",id);
                intent1.putExtra("t",title);
                intent1.putExtra("c",content);
                startActivity(intent1);
            }
        });
    }
}
