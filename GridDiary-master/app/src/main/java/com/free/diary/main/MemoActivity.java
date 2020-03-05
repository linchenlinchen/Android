package com.free.diary.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import com.free.diary.R;
import com.free.diary.entries.MyDatabaseHelper;
import com.free.diary.entries.Naming;
import com.free.diary.main.EditActivity;
import com.free.diary.topic.Memo;
import com.spark.submitbutton.SubmitButton;

import java.util.ArrayList;
import java.util.List;


public class MemoActivity extends AppCompatActivity implements View.OnClickListener ,Naming {

    private List<Memo> memoList = new ArrayList<>();
    private MyDatabaseHelper helper = new MyDatabaseHelper(this,"People.db",null,4);
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        init();

        recyclerView = (RecyclerView) findViewById(R.id.memo_title_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MemoAdapter adapter = new MemoAdapter(memoList);
        recyclerView.setAdapter(adapter);
        SubmitButton button = (SubmitButton) findViewById(R.id.add_memo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_edit = new Intent(MemoActivity.this,EditActivity.class);
                intent_to_edit.putExtra("id", MEMO_TEMP_ID);
                startActivity(intent_to_edit);
            }
        });
    }

    private void init(){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("People",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                int memo_id = cursor.getInt(cursor.getColumnIndex("id"));
                String memo_title = cursor.getString(cursor.getColumnIndex("memo_title"));
                String memo_content = cursor.getString(cursor.getColumnIndex("memo_content"));
                int memo_emergency = cursor.getInt(cursor.getColumnIndex("memo_emergency"));
                memoList.add(new Memo(memo_id, memo_title, memo_content,memo_emergency));

            }while (cursor.moveToNext());
            cursor.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case MEMO_EDIT_NUM:
                if (resultCode == RESULT_OK){
                    long id = data.getLongExtra("id",-1);
                    boolean new_or_old = data.getBooleanExtra("new_or_old",true);
                    if(new_or_old == MEMO_NEW){
                        SQLiteDatabase db = helper.getWritableDatabase();
                        Cursor cursor = db.query("People",null,"id=?",new String[]{id + ""},null,null,null);
                        if(cursor.moveToFirst()) {
                            String memo_title = cursor.getString(cursor.getColumnIndex("memo_title"));
                            String memo_content = cursor.getString(cursor.getColumnIndex("memo_content"));
                            int memo_emergency = cursor.getInt(cursor.getColumnIndex("memo_emergency"));
                            memoList.add(new Memo(id,memo_title,memo_content,memo_emergency));
                        }
                    }
                }
                break;
            default:
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        memoList.clear();
        init();
        recyclerView = (RecyclerView) findViewById(R.id.memo_title_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MemoAdapter adapter = new MemoAdapter(memoList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }
    public Memo findMemo(int id){
        for (Memo memo : memoList){
            if(memo.getId() == id){
                return memo;
            }
        }
        return null;
    }


    class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {

        private List<Memo> mMemos;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.memo_item,viewGroup,false);
            final ViewHolder holder = new ViewHolder(view);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    Memo memo = mMemos.get(pos);
                    /**
                     * create a new activity here
                     */
                    Intent intent = new Intent(v.getContext(),MemoContentActivity.class);
                    intent.putExtra("id",memo.getId());
                    intent.putExtra("t",memo.getTitle());
                    intent.putExtra("c",memo.getContent());
                    startActivity(intent);
                }
            });

            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
                    builder.setTitle("提示")
                            .setMessage("您要删除此条备忘录吗")
                            .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int pos = holder.getAdapterPosition();
                                    Memo memo = mMemos.get(pos);
                                    SQLiteDatabase db = helper.getWritableDatabase();
                                    db.delete("People","id=?",new String[]{memo.getId()+""});
                                    onRestart();
                                }
                            }).setNegativeButton("不删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.create().show();
                    return false;
                }
            });


            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Memo memo = mMemos.get(i);
            viewHolder.title.setText(memo.getTitle());
            int red = 255;int blue = 255;int green = 255;
            switch (mMemos.get(i).getEmergency()){
                case 1:red = 255;blue = 59;green = 48;break;
                case 2:red = 171;blue = 139;green = 95;break;
                case 3:red=176;blue=166;green=90;break;
                case 4:red=171;blue=206;green=113;break;
                case 5:red =84;blue=211;green=198;break;
                default:break;
            }
            viewHolder.title.setBackgroundColor(android.graphics.Color.argb(140,red,blue,green));
        }

        @Override
        public int getItemCount() {
            return mMemos.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            View view;
            TextView title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                view = itemView;
                title = (TextView) itemView.findViewById(R.id.memo_title);
            }
        }

        public MemoAdapter(List<Memo> memos){
            mMemos = memos;
        }
    }




}
