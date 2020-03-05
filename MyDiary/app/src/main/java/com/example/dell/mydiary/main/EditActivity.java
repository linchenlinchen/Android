package com.example.dell.mydiary.main;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dell.mydiary.R;
import com.example.dell.mydiary.entries.MyDatabaseHelper;
import com.example.dell.mydiary.entries.Naming;

public class EditActivity extends AppCompatActivity implements Naming {
    private MyDatabaseHelper helper = new MyDatabaseHelper(this,"People.db",null,1);
    EditText editText1, editText2;
    Button bt_save;
    String title, content;

    //需要被返回的数据
    long re_id;
    boolean re_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final Intent intent = getIntent();
        editText1 = (EditText) findViewById(R.id.memo_edit_title);
        editText2 = (EditText) findViewById(R.id.memo_edit_content);

        final long memo_id = intent.getLongExtra("id",0);
        if (memo_id != MEMO_TEMP_ID){
            title = intent.getStringExtra("t");
            content = intent.getStringExtra("c");

            editText1.setText(title);
            editText2.setText(content);
        }


        //当点击了保存
        bt_save = (Button) findViewById(R.id.memo_save_bt);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(memo_id == MEMO_TEMP_ID) {
                    //如果是新增条目
                    if (editText1.getText().toString().equals("") && editText2.getText().toString().equals("")) {
                    }
                    else {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("memo_title",editText1.getText().toString());
                        contentValues.put("memo_content",editText2.getText().toString());
                        long id =  db.insert("People",null,contentValues);
                        contentValues.clear();

                        re_id = id;
                        re_type = MEMO_NEW;

                    }
                }
                else {
                    //如果是修改条目
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();

                    contentValues.put("memo_title", editText1.getText().toString());
                    contentValues.put("memo_content", editText2.getText().toString());
                    db.update("People", contentValues, "id = ?", new String[]{memo_id + ""});

                    re_id = memo_id;
                    re_type = MEMO_OLD;


                }

                Intent intent_new = new Intent(EditActivity.this,MemoActivity.class);
//                intent_new.putExtra("id",re_id);
//                intent_new.putExtra("new_or_old",re_type);
                startActivity(intent_new);

            }
        });
    }





    public void update(int memo_id){
        //更新数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("memo_title", editText1.getText().toString());
        contentValues.put("memo_content", editText2.getText().toString());
        db.update("People", contentValues, "id = ?", new String[]{memo_id + ""});

        Intent intent1 = new Intent();
        String t = editText1.getText().toString();
        intent1.putExtra("t", t);
        intent1.putExtra("c", editText2.getText().toString());
        setResult(RESULT_OK, intent1);
        finish();
    }

}
