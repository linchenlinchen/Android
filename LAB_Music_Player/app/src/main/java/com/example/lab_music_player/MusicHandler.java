package com.example.lab_music_player;

import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import static com.example.lab_music_player.BehaviorRepository.*;
import static com.example.lab_music_player.MyService.*;

public class MusicHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case PLAY_OR_PAUSE:
                playOrPause();
                break;
            case LAST_SONG:
                position = position>0 ? position-1 : mySongList.size()-1;
                setPlaySource(mySongList.get(position).getPath());
                playOrPause();
                break;
            case NEXT_SONG:
                position = position<mySongList.size()-1 ? position+1 : 0;
                setPlaySource(mySongList.get(position).getPath());
                playOrPause();
                break;
            case UPDATE_BAR:
                Message message = Message.obtain();
                message.arg1 = duration_time;
                message.arg2 = mediaPlayer.getCurrentPosition();
                Messenger messenger = msg.replyTo;
                try {
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            default:break;
        }
        super.handleMessage(msg);
    }
}
