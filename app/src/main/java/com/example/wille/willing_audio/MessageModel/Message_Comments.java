package com.example.wille.willing_audio.MessageModel;


import com.example.wille.willing_audio.Model.Comments;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Message_Comments {
    public String state;
    public String message;
    public Comments data;

    public Message_Comments(){
        state="success";
        message="Operating Normally";
    }

    public Message_Comments(String state, String message){
        this.state=state;
        this.message=message;
    }

    public Message_Comments(String state, String message, Comments data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }
}
