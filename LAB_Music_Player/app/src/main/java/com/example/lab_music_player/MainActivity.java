package com.example.lab_music_player;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.lab_music_player.MyService.mediaPlayer;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";/*由logt+TAB自动生成。*/
    private IntentFilter intentFilter;
    private ImageReceiver imageReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private MyService.MusicBinder musicBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicBinder = (MyService.MusicBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    ImageButton out;
    TextView title;
    TextView author;
    SeekBar seekBar;
    TextView process;
    TextView duration;
    ImageButton last;
    ImageButton resume;
    ImageButton next;

    /*运用Handler中的handleMessage方法接收service传递的音乐播放进度信息，new Handler.callback放在handler构造函数内防止内存泄露*/
    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int duration_time = msg.arg2;
            int process_time = msg.arg1;
            process.setText(MusicUtils.formatTime(process_time));
            duration.setText(MusicUtils.formatTime(duration_time));
            seekBar.setProgress(process_time);
            seekBar.setMax(duration_time);
            return false;
        }
    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Toast.makeText(this,"You clicked item1",Toast.LENGTH_LONG).show();
                break;
            case R.id.item2:
                Toast.makeText(this,"You clicked item2",Toast.LENGTH_LONG).show();
                break;
                default:break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initReceiver();
        hideActionBar();
        out = findViewById(R.id.out);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        seekBar = findViewById(R.id.seeker_bar);
        process = findViewById(R.id.process);
        duration = findViewById(R.id.duration);
        last = findViewById(R.id.last);
        resume = findViewById(R.id.resume);
        next = findViewById(R.id.next);
        /*确保权限*/
        prepareMediaPlayer();
        /*开启负责播放音乐的MyService*/
        Intent bindIntent = new Intent(this, MyService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE); // 绑定服务
        /*设置点击事件*/
        out.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new MySeekBar());
        last.setOnClickListener(this);
        resume.setOnClickListener(this);
        next.setOnClickListener(this);
        //每隔500毫秒发送音乐进度,也可以放service
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //实例化一个Message对象
                Message msg = Message.obtain();
                //Message对象的arg1参数携带音乐当前播放进度信息，类型是int
                msg.arg1 = musicBinder == null ? 0 : musicBinder.getProcess();
                msg.arg2 = musicBinder == null ? 0 : musicBinder.getDuration();
                //使用MainActivity中的handler发送信息
                handler.sendMessage(msg);
            }
        }, 0, 500);
    }

    public void prepareMediaPlayer(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }break;
            default:break;
        }
    }

    public void hideActionBar(){
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.hide();
        }
    }

    /*销毁时释资源*/
    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        mediaPlayer = null;
        unbindService(serviceConnection);
        localBroadcastManager.registerReceiver(imageReceiver,intentFilter);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.out:
                Intent intent = new Intent(MainActivity.this,SongMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.last:
                resume.setImageResource(R.drawable.pause);
                Intent intent_last = new Intent("com.example.lab_music_player.LAST");
                sendBroadcast(intent_last);
                break;
            case R.id.resume:
                /*继续与暂停*/
                adjustRadioImage();
                Intent intent_resume = new Intent("com.example.lab_music_player.RESUME");
                sendBroadcast(intent_resume);
                break;
            case R.id.next:
                resume.setImageResource(R.drawable.pause);
                Intent intent_next = new Intent("com.example.lab_music_player.NEXT");
                sendBroadcast(intent_next);
                break;
                default:break;
        }
    }

    /*进度条处理*/
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int duration_time = mediaPlayer.getDuration();//获取音乐总时长
            int process_time = mediaPlayer.getCurrentPosition();//获取当前播放的位置
            duration.setText(MusicUtils.formatTime(duration_time));//总时长
            process.setText(MusicUtils.formatTime(process_time));//进度
        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {

        }
        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            MyService.mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    /*调整播放或者暂停图标*/
    public void adjustRadioImage(){
        if(!MyService.mediaPlayer.isPlaying()){
            resume.setImageResource(R.drawable.pause);
        }else {
            resume.setImageResource(R.drawable.resume);
        }
    }

    public void initReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.lab_music_player.CHOOSE");
        imageReceiver = new ImageReceiver();
        localBroadcastManager.registerReceiver(imageReceiver,intentFilter);
    }

    public class ImageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            resume.setImageResource(R.drawable.pause);
        }
    }
}
