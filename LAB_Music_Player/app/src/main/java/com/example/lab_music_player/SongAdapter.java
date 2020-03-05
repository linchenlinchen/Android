package com.example.lab_music_player;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
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

    public SongAdapter(List<Song> mySongList) {
        this.mySongList = mySongList;
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
                Toast.makeText(v.getContext(), "you clicked view " + song.getSong_name(), Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Song song = mySongList.get(i);
        viewHolder.singerImage.setImageResource(song.getResource());
        viewHolder.songText.setText(song.getSong_name());
    }

    @Override
    public int getItemCount() {
        return mySongList.size();
    }
}
