package com.example.wille.willing_audio.MessageModel;

import com.example.wille.willing_audio.Model.Comment;

/**
 * Created by wille on 2018/1/19.
 */

public class Message {
    public String state;
    public String message;

    public Message(){

    }

    public Message(String state, String message){
        this.state=state;
        this.message=message;
    }
}
