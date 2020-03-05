package com.example.l2595.servicetest;

import android.content.Context;
import android.os.Message;
import android.widget.Toast;

public class MyThread extends Thread {
    @Override
    public void run(){
        Message message = new Message();
        message.what = (int)(Math.random() * 10);

    }
}
