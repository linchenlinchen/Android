package com.example.l2595.laba;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//内容碎片类
public class NewsContentFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.news_content_frag,container,false);
        return view;
    }

    public void refresh(String newsTitle, String newsContent){
        View visibilityLayout = view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        TextView newsTitleText = (TextView) view.findViewById(R.id.news_title);//获取标题文本对象
        TextView newsContentText = (TextView) view.findViewById(R.id.news_content);//获取内容文本对象
        newsTitleText.setText(newsTitle);// 刷新新闻标题
        newsContentText.setText(newsContent);//刷新新闻内容
    }
}

