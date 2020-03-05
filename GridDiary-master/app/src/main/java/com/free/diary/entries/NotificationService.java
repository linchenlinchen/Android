package com.free.diary.entries;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;

import com.free.diary.Main2Activity;
import com.free.diary.R;
import com.free.diary.ui.activity.TimeLight;

public class NotificationService extends IntentService {
    private Notification notification;
    private PendingIntent pit;
    private NotificationManager notificationManager;
    private String content = "";
    private int id = 0;
    private static final String TAG = NotificationService.class.getSimpleName();
    //////////
    TimeDatabaseHelper timeDatabaseHelper = new TimeDatabaseHelper(this,"Time.db",null,1);

    public NotificationService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        content = intent.getStringExtra("content");
        id = intent.getIntExtra("id",0);
        Intent intent1 = new Intent(NotificationService.this,TimeLight.class);
        intent1.putExtra("content",content);
        PendingIntent pi = PendingIntent.getActivities(this,0,new Intent[]{intent1},0);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background)//设置小图标
                .setContentTitle("时光日记")//标题
                .setContentText(content)
                .setWhen(System.currentTimeMillis())           //设置通知时间
                .setAutoCancel(false)
                .setContentIntent(pi);
        //设置点击后取消Notification
        // 兼容  API 26，Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
            NotificationChannel notificationChannel = new NotificationChannel("AppTestNotificationId", "AppTestNotificationName", NotificationManager.IMPORTANCE_DEFAULT);
            // 注册通道，注册后除非卸载再安装否则不改变
            notificationManager.createNotificationChannel(notificationChannel);
            mBuilder.setChannelId("AppTestNotificationId");
        }
        notification = mBuilder.build();
        notificationManager.notify(1, notification);
        SQLiteDatabase db = timeDatabaseHelper.getWritableDatabase();
        db.delete("Time","id=?",new String[]{id + ""});

    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        content = intent.getStringExtra("content");
//        id = intent.getIntExtra("id",0);
//        SQLiteDatabase db = timeDatabaseHelper.getWritableDatabase();
//        db.delete("Time.db","id=?",new String[]{id + ""});
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//////        it.putExtra("NotificationService","SUCCESS");
////        pit = PendingIntent.getActivity(this, 0, it, 0);
//
//        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification.Builder mBuilder = new Notification.Builder(this);
//        mBuilder.setSmallIcon(R.drawable.ic_launcher_background)//设置小图标
//                .setContentTitle("时光日记")//标题
//                .setContentText(content)
//                .setWhen(System.currentTimeMillis())           //设置通知时间
//                .setAutoCancel(false);                     //设置点击后取消Notification
//        // 兼容  API 26，Android 8.0
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
//            NotificationChannel notificationChannel = new NotificationChannel("AppTestNotificationId", "AppTestNotificationName", NotificationManager.IMPORTANCE_DEFAULT);
//            // 注册通道，注册后除非卸载再安装否则不改变
//            notificationManager.createNotificationChannel(notificationChannel);
//            mBuilder.setChannelId("AppTestNotificationId");
//        }
//        notification = mBuilder.build();
//        notificationManager.notify(1, notification);
//
//
//    }
}