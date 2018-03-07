package com.example.wille.willing_audio.MessageModel;


import com.example.wille.willing_audio.Model.SongLists;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Message_SongLists {
    public String state;
    public String message;
    public SongLists data;

    public Message_SongLists(){
        state="success";
        message="Operating Normally";
    }

    public Message_SongLists(String state, String message){
        this.state=state;
        this.message=message;
    }

    public Message_SongLists(String state, String message, SongLists data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }
}
