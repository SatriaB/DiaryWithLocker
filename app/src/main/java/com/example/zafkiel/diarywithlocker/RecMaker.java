package com.example.zafkiel.diarywithlocker;

/**
 * Created by Zafkiel on 1/16/2018.
 */

public class RecMaker {
    private String title;
    private String date;
    private String content;

    public RecMaker(){

    }

    public RecMaker(String title ,String date, String content){
        this.title=title;
        this.date=date;
        this.content=content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
