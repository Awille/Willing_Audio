package com.example.wille.willing_audio.Model;

/**
 * Created by Administrator on 2017/12/31.
 */

public class User {
    public String account;
    public String nickname;
    public String password;
    public String avator;
    public String favourite_singer;
    public String favourite_song;
    public String signature;
    public String sex;

    public User(){
        account="";
        nickname="";
        password="";
        avator="newuser.jpg";
    }

    public User(String account,String nickname, String password,String favourite_singer,String favourite_song,String signature,String sex){
        this.account=account;
        this.nickname=nickname;
        this.password=password;
        this.favourite_singer=favourite_singer;
        this.favourite_song=favourite_song;
        this.signature=signature;
        this.sex=sex;
        avator="newuser.jpg";
    }

    public User(String account,String nickname, String password,String avator,String favourite_singer,String favourite_song,String signature,String sex){
        this.account=account;
        this.nickname=nickname;
        this.password=password;
        this.favourite_singer=favourite_singer;
        this.favourite_song=favourite_song;
        this.signature=signature;
        this.sex=sex;
        this.avator=avator;
    }
}
