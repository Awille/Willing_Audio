package com.example.wille.willing_audio.MessageModel;


import com.example.wille.willing_audio.Model.SongList;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Message_SongList {
    public String state;
    public String message;
    public SongList data;

    public Message_SongList(){
        state="success";
        message="Operating Normally";
    }

    public Message_SongList(String state, String message){
        this.state=state;
        this.message=message;
    }

    public Message_SongList(String state, String message, SongList data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }
}
