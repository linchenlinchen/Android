package com.example.lab_music_player;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class SongMenuActivity extends BaseActivity {
    private List<Song> mySongList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songmenu);
        initSongs();
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.songList_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SongAdapter adapter = new SongAdapter(mySongList,this);
        recyclerView.setAdapter(adapter);
    }
    private void initSongs() {
        mySongList = MusicUtils.getMusicLists(SongMenuActivity.this);
    }
}
