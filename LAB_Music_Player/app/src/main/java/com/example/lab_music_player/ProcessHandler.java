//package com.example.lab_music_player;
//
//import android.os.Handler;
//import android.os.Message;
//
//public class ProcessHandler extends Handler {
//    @Override
//    public void handleMessage(Message msg) {
//        super.handleMessage(msg);
//        int duration_time = msg.arg2;
//        int process_time = msg.arg1;
//        process.setText(MusicUtils.formatTime(process_time));
//        duration.setText(MusicUtils.formatTime(duration_time));
//        seekBar.setProgress(process_time);
//        seekBar.setMax(duration_time);
//        return false;
//    }
//}
