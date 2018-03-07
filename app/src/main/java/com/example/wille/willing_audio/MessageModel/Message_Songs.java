package com.example.wille.willing_audio.MessageModel;


import com.example.wille.willing_audio.Model.Songs;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Message_Songs {
    public String state;
    public String message;
    public Songs data;

    public Message_Songs(){
        state="success";
        message="Operating Normally";
    }

    public Message_Songs(String state, String message){
        this.state=state;
        this.message=message;
    }

    public Message_Songs(String state, String message, Songs data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }
}
