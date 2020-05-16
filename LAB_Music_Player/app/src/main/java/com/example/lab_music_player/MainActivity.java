package com.example.lab_music_player;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.security.Permission;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";/*由logt+TAB自动生成。*/
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean isSeekBarChanging;

    List<Song> mySongList;
    int position;
    ImageButton out;
    TextView title;
    TextView author;
    SeekBar seekBar;
    TextView process;
    TextView duration;
    ImageButton last;
    ImageButton resume;
    ImageButton next;

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
        hideActionBar();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                change_song(true);
            }
        });
        out = findViewById(R.id.out);
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        seekBar = findViewById(R.id.seeker_bar);
        process = findViewById(R.id.process);
        duration = findViewById(R.id.duration);
        last = findViewById(R.id.last);
        resume = findViewById(R.id.resume);
        next = findViewById(R.id.next);

        prepareMediaPlayer();

        out.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new MySeekBar());
        last.setOnClickListener(this);
        resume.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    public void prepareMediaPlayer(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            initMediaPlayer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        initMediaPlayer();
                    }else {
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

    public void initMediaPlayer(){
        try{
            mySongList = MusicUtils.getMusicLists(MainActivity.this);
            int length = mySongList.size();
            position = (int)(Math.random()*length);
            Song song = mySongList.get(position);
            Toast.makeText(MainActivity.this,"现在为您播放的是："+song.getPath(),Toast.LENGTH_LONG).show();
            File file = new File(song.getPath());
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            seekBar.setMax(mediaPlayer.getDuration());
            new UpdateSeekBarTask().execute();
//            Intent startIntent = new Intent(this,MyService.class);
//            startService(startIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*销毁时释资源*/
    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        mediaPlayer = null;
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
                change_song(false);
                break;
            case R.id.resume:
                /*继续与暂停*/
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    resume.setImageResource(R.drawable.pause);
                }else {
                    mediaPlayer.pause();
                    resume.setImageResource(R.drawable.resume);
                }
                break;
            case R.id.next:
                change_song(true);
                break;
                default:break;
        }
    }


    public void change_song(Boolean isNext){
        try {
            if(!isNext){
                if (position > 0) {
                    position--;
                }else if(position == 0){
                    position = mySongList.size()-1;
                }
            }else {
                if (position < mySongList.size()-1) {
                    position++;
                }else if(position == mySongList.size()-1){
                    position = 0;
                }
            }
            Song song = mySongList.get(position);

            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(song.getDuration());
            resume.setImageResource(R.drawable.pause);
        }catch (Exception e){
            e.printStackTrace();
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
            isSeekBarChanging = true;
        }
        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class UpdateSeekBarTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                publishProgress(mediaPlayer.getCurrentPosition());
                while (true) {
                    if (mediaPlayer!=null && mediaPlayer.isPlaying()) {/*这个对于mediaPlayer存在的判断很重要！否则切歌时可能会无故暂停*/
                        break;
                    }
                }
            }catch (Exception e){
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            new UpdateSeekBarTask().execute();
        }
    }
}
