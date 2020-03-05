package com.example.l2595.labc;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private MyService.MyBinder myBinder;
    private ServiceConnection work = new ServiceConnection(){
        @Override
        public void onServiceDisconnected(ComponentName name){

        }
        @Override
        public void  onServiceConnected(ComponentName name, IBinder service){
            myBinder = (MyService.MyBinder)service;
            myBinder.startWork();
            myBinder.getProgress();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startService = (Button)findViewById(R.id.start_service);
        startService.setOnClickListener(this);
        Button stopService = (Button)findViewById(R.id.stop_service);
        stopService.setOnClickListener(this);
        Button bindService = (Button)findViewById(R.id.bind_service);
        bindService.setOnClickListener(this);
        Button unbindService = (Button)findViewById(R.id.unbind_service);
        unbindService.setOnClickListener(this);
        Button startIntentService = (Button)findViewById(R.id.start_intent_service);
        startIntentService.setOnClickListener(this);
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
            case R.id.bind_service:
                Intent bindIntent = new Intent(this,MyService.class);
                bindService(bindIntent,work,BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                Intent unbindService = new Intent(this,MyService.class);
                unbindService(work);
                break;
            case R.id.start_intent_service:
                Log.d("MainActivity","Thread id is "+Thread.currentThread().getId());
                Intent start_intent = new Intent(this,MyIntentService.class);
                startService(start_intent);
                break;
                default:break;
        }
    }



}
