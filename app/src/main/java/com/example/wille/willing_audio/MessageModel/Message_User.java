package com.example.wille.willing_audio.MessageModel;


import com.example.wille.willing_audio.Model.User;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Message_User {
    public String state;
    public String message;
    public User data;

    public Message_User(){
        state="success";
        message="Operating Normally";
    }

    public Message_User(String state,String message){
        this.state=state;
        this.message=message;
    }

    public Message_User(String state, String message, User data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }
}
