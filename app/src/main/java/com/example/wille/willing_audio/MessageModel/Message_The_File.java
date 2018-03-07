package com.example.wille.willing_audio.MessageModel;


import com.example.wille.willing_audio.Model.The_File;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Message_The_File {
    public String state;
    public String message;
    public The_File data;

    public Message_The_File(){
        state="success";
        message="Operating Normally";
    }

    public Message_The_File(String state, String message){
        this.state=state;
        this.message=message;
    }

    public Message_The_File(String state, String message, The_File data) {
        this.state = state;
        this.message = message;
        this.data = data;
    }
}
