package com.example.wille.willing_audio.Model;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Song {
    public long songId;
    public String songName;
    public String singer;
    public String type;
    public long popularity;
    public String songAvator;
    public String URL;
    public String lrc;

    public Song(){

    }

    public Song(long songId, String songName, String singer, String type,long popularity,String songAvator,String URL,String lrc){
        this.songId=songId;
        this.songName=songName;
        this.singer=singer;
        this.type=type;
        this.popularity=popularity;
        this.songAvator=songAvator;
        this.URL=URL;
        this.lrc=lrc;
    }

}
