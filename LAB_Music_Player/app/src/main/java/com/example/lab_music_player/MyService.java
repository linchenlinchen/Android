package com.example.lab_music_player;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private IntentFilter intentFilter;
    private ResumeReceiver resumeReceiver;
    private NextReceiver nextReceiver;
    private LastReceiver lastReceiver;
    private ChooseReceiver chooseReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private static List<Song> mySongList;
    private static int position;
    private static int duration_time;
    public static MediaPlayer mediaPlayer = null;
    private static String mediaPath;
    public MyService(){
        if(mediaPlayer == null){
            mediaPlayer= new MediaPlayer();
        }
    }

    //TODO :播放或暂停
    public boolean playOrPause(){
        Log.d("MusicService","playOrPause");
        duration_time = mediaPlayer.getDuration();
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            return true;
        }else {
            mediaPlayer.pause();
            return false;
        }
    }

    //TODO :设置播放位置
    public void seekTo(int pos){
        mediaPlayer.seekTo(pos);
    }

    //TODO : 返回正在播放的音频的路径
    public String getMediaPath(){
        return mediaPath;
    }

    //TODO: 停止播放音乐并将播放位置设为0
    public void stop(){
        mediaPlayer.stop();
        try {
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer.seekTo(0);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        unregisterReceiver(resumeReceiver);
        unregisterReceiver(nextReceiver);
        unregisterReceiver(lastReceiver);
        localBroadcastManager.unregisterReceiver(chooseReceiver);
    }

    //TODO :初始化播放设置
    private void initMediaPlayer(){
        try{
            mySongList = MusicUtils.getMusicLists(MyService.this);
            int length = mySongList.size();
            position = (int)(Math.random()*length);
            Song song = mySongList.get(position);
            mediaPath = song.getPath();
            Toast.makeText(MyService.this,"现在为您播放的是："+song.getPath(),Toast.LENGTH_LONG).show();
            File file = new File(mediaPath);
            mediaPlayer.setDataSource(mediaPath);
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO :初始化接收器设置
    private void initReceiver(){
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.lab_music_player.RESUME");
        resumeReceiver = new ResumeReceiver();
        registerReceiver(resumeReceiver,intentFilter);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.lab_music_player.NEXT");
        nextReceiver = new NextReceiver();
        registerReceiver(nextReceiver,intentFilter);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.lab_music_player.LAST");
        lastReceiver = new LastReceiver();
        registerReceiver(lastReceiver,intentFilter);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.lab_music_player.CHOOSE");
        chooseReceiver = new ChooseReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(chooseReceiver,intentFilter);
    }

    //TODO :设置播放的音频路径
    public void setPlaySource(String path){
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            Log.d("MusicService",path);
            mediaPath = path;
            mediaPlayer.prepare();
            duration_time = mediaPlayer.getDuration();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return myBinder;
    }

    @Override
    public void onCreate() {
        initReceiver();
        initMediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
//                change_song(true);
            }
        });
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private IBinder myBinder = new MusicBinder();
    public class MusicBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }

        public int getProcess(){
            return mediaPlayer.getCurrentPosition();
        }

        public int getDuration(){
            return duration_time;
        }

    }

    public class NextReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            position = position<mySongList.size()-1 ? position+1 : 0;
            setPlaySource(mySongList.get(position).getPath());
            playOrPause();
        }
    }
    public class LastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            position = position>0 ? position-1 : mySongList.size()-1;
            setPlaySource(mySongList.get(position).getPath());
            playOrPause();
        }
    }

    public class ResumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            playOrPause();
        }
    }

    public class ChooseReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            position = intent.getIntExtra("position",0);
            setPlaySource(mySongList.get(position).getPath());
            playOrPause();
        }
    }

}
