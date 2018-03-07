package com.example.wille.willing_audio.Model;

/**
 * Created by Administrator on 2017/12/31.
 */

public class UserSongLists {
    public String account;
    public SongLists mycreate;
    public SongLists mycollect;

    public UserSongLists(String account,SongLists mycreate, SongLists mycollect){
        this.account=account;
        this.mycreate=mycreate;
        this.mycollect=mycollect;
    }
}
