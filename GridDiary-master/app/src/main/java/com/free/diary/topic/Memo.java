package com.free.diary.topic;

public class Memo {
    private long id;
    private String title;
    private String content;
    private int emergency = 0;

    public Memo(long id, String title, String content, int emergency) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.emergency = emergency;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getEmergency() {
        return emergency;
    }

    public void setEmergency(int emergency) {
        this.emergency = emergency;
    }
}
