package com.example.l2595.firstapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {
    private DownloadBinder mBinder = new DownloadBinder();
    public MyService() {
    }

    @Override
    public void onDestroy() {
        Toast.makeText(MyService.this,"Destroy Service",Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(MyService.this,"Start Service",Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Toast.makeText(MyService.this,"Create Service",Toast.LENGTH_LONG).show();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
            return mBinder;
//        throw new UnsupportedOperationException("Not yet implemented");
    }


    class DownloadBinder extends Binder{
        public void startDownload(){
            Toast.makeText(MyService.this,"startDownload executed",Toast.LENGTH_LONG).show();

        }
        public void getProgress(){
            Toast.makeText(MyService.this,"getProgress executed",Toast.LENGTH_LONG).show();
        }
    }
}
