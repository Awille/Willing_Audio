package com.example.wille.willing_audio.MessageModel;


import com.example.wille.willing_audio.Model.Users;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Message_Users {
    public String state;
    public String message;
    public Users data;

    public Message_Users(){
        state="success";
        message="Operating Normally";
    }

    public Message_Users(String state, String message){
        this.state=state;
        this.message=message;
    }

    public Message_Users(String state, String message, Users data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }
}
