package com.example.l2595.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

//public class MyService extends Service {
//    private MyBinder myBinder = new MyBinder();
//    public MyService() {
//    }
//
//
//    class MyBinder extends Binder{
//        public void start(){
//            Log.d("MyService","start executed");
//        }
//        public int play(){
//            Log.d("MyService","play executed");
//            return 0;
//        }
//    }
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//    @Override
//    public void onCreate(){
//        super.onCreate();
//        Log.d("MyService","onCreate executed");
//    }
//    @Override
//    public int onStartCommand(Intent intent,int flags,int startId){
//        Log.d("MyService","onStartCommand executed");
//        return super.onStartCommand(intent,flags,startId);
//    }
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        Log.d("MyService","onDestroy executed");
//    }
//}
