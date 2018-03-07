package com.example.wille.willing_audio.Model;

import com.example.wille.willing_audio.MusicService;

import java.util.Random;
import java.util.Stack;

/**
 * Created by Administrator on 2017/12/31.
 */

public class SongList {
    public long listId;
    public String listName;
    public String type;
    public String listAvator;
    public Songs songs;


    public SongList(long listId,String listName,String type,String listAvator,Songs songs){
        this.listId=listId;
        this.listName=listName;
        this.type=type;
        this.listAvator=listAvator;
        this.songs=songs;
        /****************************************/
    }
    /**********************************/
    public SongList() {
        pos=0;
        record=new Stack<>();
    }
    private int pos;
    private Stack<Long> record;

    public int size() {
        return songs.songs.size();
    }
    //添加歌曲到列表中，如果歌曲已经存在则返回false
    public boolean addSong(Song song) {
        System.out.println("adding songName="+song.songName);
        for (int i=0;i<size();++i) {
            if (songs.songs.get(i).songId==song.songId) {
                System.out.println("songId="+song.songId+" have already existed");
                return false;
            }
        }
        songs.songs.add(song);
        System.out.println("adding songId="+song.songId);
        return true;
    }
    public int getPos() {
        return pos;
    }
    public boolean addSongWithSQL(Song song) {
        System.out.println("adding songName="+song.songName);
        for (int i=0;i<size();++i) {
            if (songs.songs.get(i).songId==song.songId) {
                System.out.println("songId="+song.songId+" have already existed");
                return false;
            }
        }
        songs.songs.add(song);
        System.out.println("adding songId="+song.songId);
        return true;
    }
    //在播放列表里面找到songId歌曲的位置
    public int findSong(long songId) {
        for (int i=0;i<size();++i) {
            if (songs.songs.get(i).songId==songId) return i;
        }
        return -1;
    }
    //删除位置i的歌曲
    public void delSong(int i) {
        songs.songs.remove(i);
    }
    //得到位置i的歌曲
    public Song getI(int i) {
        return songs.songs.get(i);
    }
    //播放位置i的歌曲
    public Song playI(int i) {
        record.push(get().songId);
        pos=i;
        return get();
    }
    //返回当前播放的歌曲
    public Song get() {
        return songs.songs.get(pos);
    }
    //下一位置
    private Song posMoveToNext() {
        pos++;
        if (pos>=size()) pos=0;
        return get();
    }
    //上一位置
    private Song posMoveToPre() {
        pos--;
        if (pos<0) pos=size()-1;
        return get();
    }
    //下一首歌
    public Song next(MusicService.MOD mod) {
        switch (mod) {
            case Sequential:
                record.push(get().songId);
                posMoveToNext();
                return get();
            case Random:
                Random random=new Random(System.currentTimeMillis());
                record.push(get().songId);
                pos=random.nextInt(size());
                return get();
            case Single:
                record.push(get().songId);
                posMoveToNext();
                return get();
            case Loop:
                record.push(get().songId);
                posMoveToNext();
                return get();
            default:
                return get();
        }
    }
    //上一首歌
    public Song pre(MusicService.MOD mod) {
        if (record.size()>0) {
            System.out.print("stack:");
            for (int i=0;i<record.size();++i) System.out.print(record.get(i)+" ");
            System.out.println();
            System.out.print("Songs:");
            for (int i=0;i<songs.songs.size();++i) System.out.print(songs.songs.get(i).songId+" ");
            System.out.println();
            long preSongId=record.pop();
            System.out.println("pop songId="+preSongId);
            int preSongPos=this.findSong(preSongId);
            System.out.println("target song is at position "+preSongPos);
            if (preSongPos!=-1) {
                pos=preSongPos;
                return get();
            }
            else return pre(mod);
        }
        System.out.println("stack minsize");
        switch (mod) {
            case Sequential:
                posMoveToPre();
                return get();
            case Random:
                Random random=new Random(System.currentTimeMillis());
                pos=random.nextInt(size());
                return get();
            case Single:
                posMoveToPre();
                return get();
            case Loop:
                posMoveToPre();
                return get();
            default:
                return get();
        }
    }
}
