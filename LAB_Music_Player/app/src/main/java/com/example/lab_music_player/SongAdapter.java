package com.example.lab_music_player;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context context;
    private List<Song> mySongList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View songView;
        ImageView singerImage;
        TextView songText;
        public ViewHolder(View view){
            super(view);
            songView = view;
            singerImage = (ImageView)view.findViewById(R.id.singer_image);
            songText = (TextView)view.findViewById(R.id.song_name);
        }
    }

    public SongAdapter(List<Song> mySongList,Context context) {
        this.mySongList = mySongList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song_item,viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.songView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Song song = mySongList.get(position);
                //自定义一个action
                Intent intent = new Intent("com.example.lab_music_player.CHOOSE");
                //创建一个本地广播管理器
                intent.putExtra("address",song.getPath());
                intent.putExtra("position",position);
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
                //发送本地广播
                localBroadcastManager.sendBroadcast(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Song song = mySongList.get(i);
//        viewHolder.singerImage.setImageResource(song.getAlbum_id());
        viewHolder.songText.setText(song.getSong_name());
    }

    @Override
    public int getItemCount() {
        return mySongList.size();
    }
}
