package com.example.wille.willing_audio.MessageModel;


import com.example.wille.willing_audio.Model.Song;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Message_Song {
    public String state;
    public String message;
    public Song data;

    public Message_Song(){
        state="success";
        message="Operating Normally";
    }

    public Message_Song(String state, String message){
        this.state=state;
        this.message=message;
    }

    public Message_Song(String state, String message, Song data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }
}
