package com.example.lab_music_player;

public class Song {
    private String song_name;
    private int resource;
    /**
     * 歌曲的地址
     */
    private String path;
    /**
     * 歌手
     */
    public String singer;
    /**
     * 歌曲长度
     */
    public int duration;
    /**
     * 歌曲的大小
     */
    public long size;
    /**
    * 歌曲写真
    */
    public String album;
    public long album_id;
    public String url;
    public Song(){

    }
    public Song(String song_name,int resource) {
        this.song_name = song_name;
        this.resource = resource;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
