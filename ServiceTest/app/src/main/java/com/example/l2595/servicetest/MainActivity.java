package com.example.l2595.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private MyService myBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startService = (Button)findViewById(R.id.start_service);
        Button stopService = (Button)findViewById(R.id.stop_service);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.start_service:
                Intent startIntent = new Intent(this,MyService.class);
                startService(startIntent);
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this,MyService.class);
                stopService(stopIntent);
                break;
                default:break;
        }
    }

    //用于对进程发送消息的处理
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            TextView textView = (TextView)findViewById(R.id.text1);
            switch (msg.what){
                case 1:
                    textView.append("Button 1 is clicked! Thread 1 is running");
                    break;
                case 0:
                    textView.append("Button 2 is clicked! Thread 2 is running");
                    break;
                    default:break;
            }
        }
    };



//内部的进程类
    public class MyThread extends Thread {
        @Override
        public void run(){
            Message message = new Message();
            message.what = (int)(Math.random() * 10);
            handler.sendMessage(message);
        }
    }

    public class MyService extends Service {
        private MyBinder myBinder = new MyBinder();
        public MyService() {
        }

        public class MyWork {
            public MyWork(){

            }
            //用服务管理子进程1
            public void func1 () {
                MyThread myThread1 = new MyThread();
                myThread1.start();
                Toast.makeText(MainActivity.this, "func1 in MyService using Thread1 with id : " +
                        myThread1.getId() + " is visited", Toast.LENGTH_SHORT);
            }
            //用服务管理子进程2
            public void func2 () {
                MyThread myThread2 = new MyThread();
                myThread2.start();
                Toast.makeText(MainActivity.this, "func1 in MyService using Thread1 with id : " +
                        myThread2.getId() + " is visited", Toast.LENGTH_SHORT);
            }
        }


        class MyBinder extends Binder {
            public void start(){
                Log.d("MyService","start executed");
            }
            public int play(){
                Log.d("MyService","play executed");
                return 0;
            }
        }


        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }
        @Override
        public void onCreate(){
            super.onCreate();
            Log.d("MyService","onCreate executed");
        }
        @Override
        public int onStartCommand(Intent intent,int flags,int startId){
            Log.d("MyService","onStartCommand executed");
            return super.onStartCommand(intent,flags,startId);
        }
        @Override
        public void onDestroy(){
            super.onDestroy();
            Log.d("MyService","onDestroy executed");
        }
    }
}
