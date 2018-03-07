package com.example.wille.willing_audio.MessageModel;


import com.example.wille.willing_audio.Model.Comment;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Message_Comment {
    public String state;
    public String message;
    public Comment data;

    public Message_Comment(){
        state="success";
        message="Operating Normally";
    }

    public Message_Comment(String state, String message){
        this.state=state;
        this.message=message;
    }

    public Message_Comment(String state, String message, Comment data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }
}
