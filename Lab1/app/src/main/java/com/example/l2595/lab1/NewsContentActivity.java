package com.example.l2595.lab1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//关于新闻内容的活动，使得单页模式和双页都能用
public class NewsContentActivity extends AppCompatActivity{

    public static void actionStart(Context context, String newsTitle, String newsContent){
        Intent intent = new Intent(context,NewsContentActivity.class);
        intent.putExtra("news_title",newsTitle);
        intent.putExtra("news_content",newsContent);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        String newsTitle = getIntent().getStringExtra("news_Title");//传入新的新闻标题
        String newsContent = getIntent().getStringExtra("news_Content");//获取传入的新闻内容
        NewsContentFragment newsContentFragment = (NewsContentFragment) getSupportFragmentManager().findFragmentById(R.id.news_content_fragment);
        newsContentFragment.refresh(newsTitle,newsContent);//刷新内容碎片
    }
}
