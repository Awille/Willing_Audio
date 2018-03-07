package com.example.wille.willing_audio.Model;

/**
 * Created by Administrator on 2017/12/31.
 */

public class Comment {
    public long commentId;
    public String account;
    public String content;
    public Long songId;
    public String time;
    public long rCommentId;
    public long like;

    public Comment(){

    }
    public Comment(long commentId,String account,String content,Long songId,String time,long rCommentId,long like){
        this.commentId=commentId;
        this.account=account;
        this.content=content;
        this.songId=songId;
        this.time=time;
        this.rCommentId=rCommentId;
        this.like=like;
    }
}
