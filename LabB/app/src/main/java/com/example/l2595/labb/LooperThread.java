package com.example.l2595.labb;


import android.os.Looper;

import java.util.logging.Handler;

public class LooperThread extends Thread {
    private Handler handler1;
    private Handler handler2;
    private LooperThread(){
        super("LooperThread");
    }
    @Override
    public void run(){
        Looper.prepare();
        Looper looper = Looper.myLooper();
        //Handler1 mWorkHandler = new Handler1(looper);
        looper.loop();
//        handler1 = new Handler();

    }
}
