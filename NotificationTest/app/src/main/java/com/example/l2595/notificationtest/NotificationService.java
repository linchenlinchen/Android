package com.example.l2595.notificationtest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

public class NotificationService extends Service {
    private Notification notification;
    private PendingIntent pit;
    private NotificationManager notificationManager;
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent it = new Intent(this, MainActivity.class);
//        it.putExtra("NotificationService","SUCCESS");
        pit = PendingIntent.getActivity(this, 0, it, 0);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder mBuilder=new Notification.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher_background)//设置小图标
                .setContentTitle("预警信息")//标题
                .setContentText("1111")
                .setWhen(System.currentTimeMillis())           //设置通知时间
                .setAutoCancel(false)                           //设置点击后取消Notification
                .setContentIntent(pit);
        // 兼容  API 26，Android 8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // 第三个参数表示通知的重要程度，默认则只在通知栏闪烁一下
            NotificationChannel notificationChannel = new NotificationChannel("AppTestNotificationId", "AppTestNotificationName", NotificationManager.IMPORTANCE_DEFAULT);
            // 注册通道，注册后除非卸载再安装否则不改变
            notificationManager.createNotificationChannel(notificationChannel);
            mBuilder.setChannelId("AppTestNotificationId");
        }
        notification = mBuilder.build();
        notificationManager.notify(1,notification);
    }
}

