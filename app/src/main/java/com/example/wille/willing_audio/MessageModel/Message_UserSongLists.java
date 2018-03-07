package com.example.wille.willing_audio.MessageModel;


import com.example.wille.willing_audio.Model.UserSongLists;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Message_UserSongLists {
    public String state;
    public String message;
    public UserSongLists data;

    public Message_UserSongLists(){
        state="success";
        message="Operating Normally";
    }

    public Message_UserSongLists(String state, String message){
        this.state=state;
        this.message=message;
    }

    public Message_UserSongLists(String state, String message, UserSongLists data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }
}
