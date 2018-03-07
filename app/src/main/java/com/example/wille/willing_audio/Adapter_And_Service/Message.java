package com.example.wille.willing_audio.Adapter_And_Service;


import com.example.wille.willing_audio.MessageModel.Message_User;
import com.example.wille.willing_audio.MessageModel.Message_UserSongLists;
import com.example.wille.willing_audio.MessageModel.Message_Users;
import com.example.wille.willing_audio.Model.SongLists;
import com.example.wille.willing_audio.Model.Songs;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Message {
    public static Message_User message_user;
    public static String base_Uri=new String("http://192.168.199.234:8088/functions/");
    //public static String base_Uri=new String("http://192.168.199.124:58080/AndroidProject/");
    public static Message_UserSongLists message_userSongLists;
    public static SongLists Lists_mycreate;
    public static SongLists Lists_mycollect;
    public static int state;

    public static Message_Users message_users;

    public static SongLists RandomLists;
    public static Songs RandomSongs;
}
