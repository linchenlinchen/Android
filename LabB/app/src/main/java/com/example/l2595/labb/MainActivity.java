package com.example.l2595.labb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    //处理器
    private Handler LinHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button);
        Button buttonForLoop = (Button)findViewById(R.id.buttonForLoop);
        //第一个按钮与自定义广播知识点有关。通过点击发送广播，被ButtonTouchReceiver接受后现实Toast
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.l2595.labb");
                sendBroadcast(intent);
            }
        });
        //第二个按钮与Looper的使用相关，在获取msg后设置它的obj之后用处理器发送消息，产生Toast
        buttonForLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain();
                msg.obj = "HHHHH,give msg to child thread!";
                LinHandler.sendMessage(msg);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                LinHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        super.handleMessage(msg);
                        Toast.makeText(MainActivity.this,"I finishied Lab2!yeah!haha!" + msg.obj,Toast.LENGTH_LONG).show();
                    }
                };
                Looper.loop();
            }
        }).start();
    }





}
